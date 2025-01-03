<template>
  <pagetitle></pagetitle>
  <div class="post-detail-page">
    <div class="go-back">
      <button @click="goBack"><el-icon><ArrowLeftBold /></el-icon>返回</button>
    </div>
    <div class="post">
      <div class="info" @click="gotoHomePage(post.owner)">
        <img :src="post.avatar" class="avatar">
        <div class="text-info">
          <h6>{{ post.ownernickname }}</h6>
          <p>{{ post.time }}</p>
        </div>
      </div>
      <div class="content">
        <h2>{{ post.title }}</h2>
        <p>{{ post.content }}</p>
        <div class="images">
          <img v-for="(image, index) in post.images" :key="index" :src="image" class="image">
        </div>
      </div>
      <div class="num-info">
        <div class="like-num" @click="likeHandle">
          <el-icon :class="this.post.Ilike?'like_true':'like_false'"></el-icon>
          {{ post.likenum }}
        </div>
        <div class="favor-num" @click="favorHandle">
          <el-icon :class="this.post.Ifavor?'favor_true':'favor_false'"></el-icon>
          {{ post.favornum }}
        </div>
        <div class="comment-num">
          <el-icon class="comment_icon"></el-icon>
          {{ post.commentnum }}
        </div>
      </div>
      <div class="write-comment">
        <input type="text" v-model="commenttext" placeholder="写下你的评论">
        <button @click="commentHandle">发布</button>
      </div>
      <div class="comments">
        <comment v-for="_comment in post.comments" :key="_comment.id" :comment="_comment"></comment>
      </div>
    </div>
  </div>
</template>

<script>
import router from "@/router/router.js";
import comment from "@/components/comment.vue";
import pagetitle from "@/components/title.vue";
import axios from "axios";
import Cookies from "js-cookie";
import { useRoute } from "vue-router";
import {ArrowLeftBold, Star} from "@element-plus/icons-vue";

export default {
  name: "PostDetailPage",
  data() {
    return {
      post: {
      },
      commenttext: "",
      myusername:""
    }
  },
  methods: {
    goBack() {
      router.go(-1);
    },
    gotoHomePage(username) {
      router.push(`/homepage/${username}`);
    },
    likeHandle() {
      const request={
        isTargetPost:true,
        postId:this.post.postid,
        reviewId:null,
        username:this.myusername
      }
      if(!this.post.Ilike){
        axios.post("http://47.95.214.215/action/like",request).then(response=>{
          if(response.data.success){
            this.$message.info("点赞成功")
            this.post.Ilike=true
            this.post.likenum++
          }
        })
      }
    },
    favorHandle() {
      const request={
        isTargetPost:true,
        postId:this.post.postid,
        reviewId:null,
        username:this.myusername
      }
      console.log(request)
      if(!this.post.Ifavor){
        axios.post("http://47.95.214.215/action/favor",request).then(response=>{
          if(response.data.success){
            this.$message.info("收藏成功")
            this.post.Ifavor=true
            this.post.favornum++
          }
          else{
            this.$message.info("收藏失败")
          }
        }).catch(error=>{
          this.$message.info("收藏失败")
        })
      }
      else{
        axios.post("http://47.95.214.215/action/cancelFavor",request).then(response=>{
          if(response.data.success){
            this.$message.info("取消收藏成功")
            this.post.Ifavor=false
            this.post.favornum--
          }
          else{
            this.$message.info("取消收藏失败")
          }
        }).catch(error=>{
          this.$message.info("取消收藏失败")
        })
      }
    },
    commentHandle() {
      const request={
        id:543,
        targetPost:this.post.postid,
        username:this.myusername,
        content: this.commenttext,
      }
      console.log(request)
      axios.post("http://47.95.214.215/review/addWithoutImage",request).then(response=>{
        console.log(response.data)
        if(response.data.success===true){
          this.commenttext=""
          this.getPostDetail(this.post.postid)
        }
      })
    },
    getPostDetail(postid){
      axios.get(`http://47.95.214.215/post/query/${postid}`).then(response=>{
        console.log(response.data)
        let _post={
          postid: response.data.post.id,
          owner: response.data.post.owner,
          ownernickname: "",
          avatar: "",
          title: response.data.post.title,
          content: response.data.post.content,
          time: response.data.post.date,
          images: response.data.post.images,
          comments:[],
          likenum: response.data.post.likeNum,
          favornum: 0,
          commentnum: response.data.post.reviews.length,
          Ilike:false,
          Ifavor: false
        }
        let commentsid=response.data.post.reviews
        axios.get(`http://47.95.214.215/userInfo/query/${_post.owner}`).then(response=>{
          _post.ownernickname=response.data.userInfo.nickname
          _post.avatar=response.data.userInfo.avatarUrl.slice(2,-2)
          axios.get(`http://47.95.214.215/post/queryFavorNum/${_post.postid}`).then(response=>{
            if(response.data.success===true){
              _post.favornum=response.data.numFavor
              console.log(response.data)
            }
            const request={
              postId:postid,
              username:this.myusername
            }
            axios.post("http://47.95.214.215/userInfo/LikeOrNotQuery",request).then(response=>{
              console.log(response.data)
              _post.Ilike=response.data.ilike
              _post.Ifavor=response.data.ifavorite
              if(commentsid.length!==0){
                for (const commentid of commentsid) {
                  axios.get(`http://47.95.214.215/review/query/${commentid}`).then(response=>{
                    console.log(response.data)
                    let review={
                      id:response.data.review.id,
                      username:response.data.review.username,
                      nickname:"",
                      avatar:"",
                      time:response.data.review.date,
                      content:response.data.review.content
                    }
                    axios.get(`http://47.95.214.215/userInfo/query/${review.username}`).then(response=>{
                      review.nickname=response.data.userInfo.nickname
                      review.avatar=response.data.userInfo.avatarUrl.slice(2,-2)
                      console.log("review:")
                      console.log(review)
                      _post.comments.push(review)
                      console.log(_post)
                      this.post=_post
                    })
                  })
                }
              }
              else{
                this.post=_post
              }
            }).catch(error=>{
            })
          })
        })
      })
    }
  },
  components: {
    ArrowLeftBold,
    Star,
    pagetitle,
    comment,
  },
  mounted() {
    this.myusername=JSON.parse(Cookies.get("user_info")).username
    const route = useRoute();
    this.getPostDetail(route.params.postid);
  },
};
</script>

<style scoped>
.post-detail-page {
  padding-left: 18%;
  padding-right: 18%;
}

.go-back {
  margin-bottom: 20px;
}

.post {
  border: 1px solid #ccc;
  padding: 5%;
  padding-top: 10px;
  padding-bottom: 10px;
  margin-bottom: 20px;
}

.info {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  cursor: pointer;
}

.avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.3);
  margin-right: 10px;
}

.text-info {
  display: flex;
  flex-direction: column;
}
.text-info h6 {
  font-size: 16px;
  font-weight: bold;
  margin-top: 5px;
  margin-bottom: 5px;
}

.text-info p {
  font-size: 12px;
  color: #999;
  margin: 0;
}
.content {
  margin-bottom: 10px;
}

h2 {
  font-size: 24px;
  margin-bottom: 10px;
}

.images {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 10px;
}

.image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  margin-right: 10px;
  margin-bottom: 10px;
}

.num-info {
  display: flex;
  align-items: center;
}

.num-info div {
  display: flex;
  align-items: center;
  justify-items: center;
  margin-left: auto;
  margin-right: auto;
  font-size: 14px;
  color: #999;
}

.comment-num {
  margin-right: 0;
}

.write-comment {
  display: flex;
  align-items: center;
  margin: 10px;
}

input[type="text"] {
  flex: 1;
  height: 30px;
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 3px;
  margin-right: 10px;
}

button {
  padding: 5px 10px;
  background-color: #337ab7;
  color: #fff;
  border: none;
  border-radius: 3px;
  cursor: pointer;
}

.comments {
  margin-top: 20px;
}

.like_false{
  width: 18px;
  height: 18px;
  background-image: url("@/assets/heart.png");
  margin-right: 20px;
}
.like_true{
  width: 18px;
  height: 18px;
  background-image: url("@/assets/heart_full.png");
  margin-right: 20px;
}
.favor_false{
  width: 18px;
  height: 18px;
  background-image: url("@/assets/star.png");
  margin-right: 20px;
}
.favor_true{
  width: 18px;
  height: 18px;
  background-image: url("@/assets/star_full.png");
  margin-right: 20px;
}
.comment_icon{
  width: 18px;
  height: 18px;
  background-image: url("@/assets/comment.png");
  margin-right: 20px;
}
</style>