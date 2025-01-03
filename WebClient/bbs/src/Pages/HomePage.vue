<template>
  <pagetitle></pagetitle>
  <div class="homepagecontent">
    <div class="info">
      <!-- 显示个人信息 -->
      <div v-if="!isEditMode" class="user-info">
        <div class="main_info">
          <img :src="userinfo.avatar" alt="Avatar" class="info_avatar"/>
          <!--<img src="@/assets/logo.png" alt="Avatar" class="info_avatar"/>-->
          <div class="main_info_text">
            <div class="main_info_text_name" >
              <h2>{{ userinfo.nickname }}</h2>
              <img src="@/assets/male.png" v-if="userinfo.gender==='男'" alt="" class="gender_info">
              <img src="@/assets/female.png" v-if="userinfo.gender==='女'" alt="" class="gender_info">
            </div>
            <div class="follow_num">
              <div class="following_num">
                <p>关注：{{following.length}}</p>
              </div>
              <div class="followers_num">
                <p>粉丝：{{followers.length}}</p>
              </div>
            </div>
          </div>
          <div v-if="!isMyHomePage" class="follow_btn">
            <el-button  @click="follow" type="primary" :plain="!userinfo.isMyfollowing" >
              {{ userinfo.isMyfollowing ? '已关注' : '关注' }}
            </el-button>
          </div>
          <div v-if="isMyHomePage" class="edit_logout_btn">
            <el-button @click="toggleEditMode" type="primary">编辑个人信息</el-button>
            <el-button @click="logOut" type="danger">退出登录</el-button>
          </div>
        </div>
        <div class="sub_info">
          <p>注册时间：{{userinfo.registerdate}}</p>
          <p>生日：{{ userinfo.birthday }}</p>
          <p>{{ userinfo.signature }}</p>
        </div>



      </div>

      <!-- 编辑模式 -->
      <div v-if="isEditMode" class="edit-mode">
        <h3>编辑个人信息</h3>
        <form>
          <el-form-item label="头像">
            <el-upload
                class="avatar-uploader"
                :auto-upload="false"
                :show-file-list="false"
                :on-change="handleFileUpload"
            >
              <img v-if="loadavatar" :src="changeavatar.url" class="avatar"  alt=""/>
              <el-icon v-else class="avatar-uploader-icon"></el-icon>
            </el-upload>
          </el-form-item>
          <el-form-item label="昵称">
            <el-input v-model="userinfo.nickname" placeholder="输入昵称"></el-input>
          </el-form-item>
          <!-- 其他个人信息字段的输入框 -->
          <el-form-item label="性别">
            <el-input v-model="userinfo.gender" placeholder="输入性别"></el-input>
          </el-form-item>
          <el-form-item label="生日">
            <el-input v-model="userinfo.birthday" placeholder="输入生日"></el-input>
          </el-form-item>
          <el-form-item label="简介">
            <el-input v-model="userinfo.signature" type="textarea" placeholder="输入个人简介"></el-input>
          </el-form-item>
        </form>
        <div>
          <el-button @click="upload" type="success">保存</el-button>
          <el-button @click="toggleEditMode">取消</el-button>
        </div>
      </div>
    </div>

    <!-- 按钮 -->
    <div v-if="isMyHomePage && !isEditMode" class="button-group">
      <el-button @click="showPosts">我的发帖</el-button>
      <el-button @click="showFavorites">我收藏的帖子</el-button>
      <el-button @click="showFollowing">我的关注</el-button>
      <el-button @click="showFollowers">我的粉丝</el-button>
    </div>
    <div v-if="!isMyHomePage" class="button-group">
      <el-button @click="showPosts">TA的发帖</el-button>
      <el-button @click="showFollowing">TA的关注</el-button>
      <el-button @click="showFollowers">TA的粉丝</el-button>
    </div>

    <!-- 内容 -->
    <div v-show="currentTab === 'posts'" class="content">
      <h3>{{ isMyHomePage ? '我的发帖' : 'TA的发帖' }}</h3>
      <post v-for="post in posts" :key="post.id" :post="post" @click="openPostDetail(post.postid)"></post>
    </div>

    <div v-show="currentTab === 'favorites'" class="content">
      <h3>我收藏的帖子</h3>
      <post v-for="post in favorites" :key="post.id" :post="post" @click="openPostDetail(post.postid)"></post>
    </div>

    <div v-show="currentTab === 'following'" class="content">
      <h3>{{ isMyHomePage ? '我的关注' : 'TA的关注' }}</h3>
      <follow v-for="user in following" :key="user.id" :user="user" @click="gotoHomePage(user.username)"></follow>
    </div>

    <div v-show="currentTab === 'followers'" class="content">
      <h3>{{ isMyHomePage ? '我的粉丝' : 'TA的粉丝' }}</h3>
      <follow v-for="user in followers" :key="user.id" :user="user" @click="gotoHomePage(user.username)"></follow>
    </div>
  </div>
</template>

<script>
import pagetitle from "@/components/title.vue"
import post from "@/components/post.vue"
import follow from "@/components/follow.vue";
import Cookies from "js-cookie"
import {useRoute} from "vue-router"
import axios from "axios"
import {ElButton, ElForm, ElFormItem, ElInput} from "element-plus"
import router from "@/router/router.js";

export default {
  name: "HomePage",
  components: {
    ElButton,
    ElForm,
    ElFormItem,
    ElInput,
    pagetitle,
    post,
    follow
  },
  data() {
    return {
      isMyHomePage: false,
      isEditMode: false,
      currentTab: 'posts', // 默认显示我的发帖
      userinfo: {
        username: "",
        avatar:[],
        nickname: 'John Doe123123',
        gender: "",
        birthday: '1990-01-01',
        registerdate: "2023-12-19",
        signature: "个性签名",
        isMyfollowing: false
      },
      posts: [], // 我的发帖列表
      favorites: [], // 我收藏的帖子列表
      following: [], // 我关注的用户列表
      followers: [], // 我的粉丝列表
      followers_id: [],
      following_id:[],
      myPosts_id:[],
      myCollections_id:[],
      changeavatar:{},
      loadavatar:false
    }
  },
  methods: {
    follow() {
      const myinfo = JSON.parse(Cookies.get("user_info"))
      const request = {
        concernedId:this.userinfo.username,
        username:myinfo.username
      }
      if (this.userinfo.isMyfollowing) {
        axios.post("http://47.95.214.215/Concerned/cancelconcerned",request).then(
            this.$message.info("取消关注成功")
        )
      }
      else {
        axios.post("http://47.95.214.215/Concerned/concerned",request).then(
            this.$message.info("关注成功")
        )
      }
      this.userinfo.isMyfollowing = !this.userinfo.isMyfollowing
    },
    showPosts() {
      this.currentTab = 'posts';
    },
    showFavorites() {
      this.currentTab = 'favorites';
    },
    showFollowing() {
      this.currentTab = 'following';
    },
    showFollowers() {
      this.currentTab = 'followers';
    },
    toggleEditMode() {
      this.isEditMode = !this.isEditMode;
    },
    logOut(){
      Cookies.set('isLogin', false)
      Cookies.set('user_info',null)
      router.push("/index")
    },
    handleFileUpload(file,fileList){
      const isIMAGE = (file.raw.type === 'image/jpeg' || file.raw.type === 'image/png'|| file.raw.type === 'image/gif')
      const isLt1M = file.size / 1024 / 1024 < 1

      if (!isIMAGE) {
        this.$message.error('上传文件只能是图片格式!')
        fileList.pop()
        return
      }
      if (!isLt1M) {
        this.$message.error('上传文件大小不能超过 1MB!')
        fileList.pop()
        return
      }
      this.changeavatar={
        url: URL.createObjectURL(file.raw),
        file: file.raw
      }
      this.loadavatar=true
    },
    upload() {
      const formData = new FormData();
      formData.append("username", this.userinfo.username);
      formData.append("nickname", this.userinfo.nickname);
      formData.append("avatar", this.changeavatar.file);
      formData.append("birthday", this.userinfo.birthday);
      formData.append("gender", this.userinfo.gender);
      formData.append("signature", this.userinfo.signature);
      axios.post("http://47.95.214.215/UserDetails/update", formData).then(response => {
        if (response.data.success) {
          this.$message.info("个人信息修改成功")
          this.toggleEditMode()
        }
      })
    },
    openPostDetail(postid) {
      router.push(`/postdetail/${postid}`);
    },
    gotoHomePage(username){
      router.push(`/homepage/${username}`)
    },
    parseDateTime(dateTimeString) {
      const pattern = /^(\d{4})年(\d{1,2})月(\d{1,2})日 (\d{1,2}):(\d{2})$/;
      const [, year, month, day, hour, minute] = pattern.exec(dateTimeString);
      const formattedDateTimeString = `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')} ${hour.padStart(2, '0')}:${minute}`;
      return new Date(formattedDateTimeString);
    },
    getHomePageInfo(username){
      console.log(username)
      const myinfo = JSON.parse(Cookies.get("user_info"))
      this.isMyHomePage = username === myinfo.username;
      this.userinfo.username=username
      this.posts=[]// 我的发帖列表
      this.favorites=[]// 我收藏的帖子列表
      this.following=[]// 我关注的用户列表
      this.followers=[]// 我的粉丝列表
      axios.get(`http://47.95.214.215/UserDetails/query/${this.userinfo.username}`).then(response=>{
        this.userinfo.gender=response.data.gender
        this.userinfo.birthday=response.data.birthday
        this.userinfo.registerdate=response.data.registerTime
        this.userinfo.signature=response.data.signature
      })
      axios.get(`http://47.95.214.215/userInfo/query/${this.userinfo.username}`).then(response=>{
        console.log(response.data.userInfo.avatarUrl)
        if(response.data.success===true){
          this.userinfo.nickname=response.data.userInfo.nickname
          this.userinfo.avatar=response.data.userInfo.avatarUrl.slice(2,-2)
          this.myPosts_id=response.data.userInfo.myPosts
          console.log(this.myPosts_id)
          this.myCollections_id=response.data.userInfo.myCollections
        }
        if(!this.isMyHomePage){
          let request={
            username:myinfo.username,
            targetusername:username
          }
          axios.post(`http://47.95.214.215/userInfo/FavorOrNot`,request).then(response=>{
            console.log(response.data)
            if(response.data.ifavor===true){
              this.userinfo.isMyfollowing=true
            }

          })
        }
        for (const postId of this.myPosts_id) {
          axios.get(`http://47.95.214.215/post/query/${postId}`).then(response=>{
            const post={
              postid:response.data.post.id,
              owner:response.data.post.owner,
              ownernickname: "",
              avatar: "",
              title:response.data.post.title,
              content:response.data.post.content,
              time: response.data.post.date,
              images: response.data.post.images,
              likenum: response.data.post.likeNum,
              favornum: 0,
              commentnum:response.data.post.reviews.length,
            }
            axios.get(`http://47.95.214.215/userInfo/query/${post.owner}`).then(response=>{
              post.avatar=response.data.userInfo.avatarUrl.slice(2,-2)
              post.ownernickname=response.data.userInfo.nickname
              this.posts.push(post)
              this.posts.sort((a,b)=>{
                const timeA = this.parseDateTime(a.time);
                const timeB = this.parseDateTime(b.time);
                return timeB - timeA;
              })
            })

          })
        }
        if(this.isMyHomePage){
          for (const postId of this.myCollections_id) {
            axios.get(`http://47.95.214.215/post/query/${postId}`).then(response=>{
              const post={
                postid:response.data.post.id,
                owner:response.data.post.owner,
                ownernickname: "",
                avatar: "",
                title:response.data.post.title,
                content:response.data.post.content,
                time: response.data.post.date,
                images: response.data.post.images,
                likenum: response.data.post.likeNum,
                favornum: 0,
                commentnum:response.data.post.reviews.length,
              }
              axios.get(`http://47.95.214.215/userInfo/query/${post.owner}`).then(response=>{
                post.avatar=response.data.userInfo.avatarUrl.slice(2,-2)
                post.ownernickname=response.data.userInfo.nickname
                this.favorites.push(post)
              })

            })
          }
        }
      })
      axios.get(`http://47.95.214.215/FansAndConcerned/Toget/${username}`).then(response=>{
        console.log(response.data)
        this.followers_id=response.data.friendship.myFans===null?[]:response.data.friendship.myFans
        this.following_id=response.data.friendship.myConcerned===null?[]:response.data.friendship.myConcerned
        for (const username of this.followers_id) {
          axios.get(`http://47.95.214.215/userInfo/query/${username}`).then(response=>{
            const user={
              username:response.data.userInfo.username,
              nickname: response.data.userInfo.nickname,
              avatar:response.data.userInfo.avatarUrl.slice(2,-2)
            }
            this.followers.push(user)
          })
        }
        for (const username of this.following_id) {
          axios.get(`http://47.95.214.215/userInfo/query/${username}`).then(response=>{
            console.log(response.data)
            const user={
              username:response.data.userInfo.username,
              nickname: response.data.userInfo.nickname,
              avatar:response.data.userInfo.avatarUrl.slice(2,-2)
            }
            this.following.push(user)
          })
        }
      })
    }
  },
  mounted() {
    const route = useRoute()

    this.getHomePageInfo(route.params.username)
  },
  beforeRouteUpdate(to, from, next) {
    // 判断当前路由的参数是否与之前不同
    if (to.params.username !== from.params.username) {
      console.log(to.params.username)
      this.getHomePageInfo(to.params.username)
      this.showPosts()
      next();
    } else {
      this.showPosts()
      next();
    }
  },
};
</script>

<style scoped>
.homepagecontent{
  min-width: 800px;
  margin-left: 18%;
  margin-right: 18%;
}
.info{
  margin-top: 50px;
  border: 1px solid #ccc;
  border-radius: 1%;
}
.user-info {
  display: flex;
  flex-direction: column;
  //align-items: center;
  margin-bottom: 20px;
  margin-left: 50px;
  margin-right: 50px;
}
.info_avatar{
  height: 100px;
  width: 100px;
}
.main_info{
  display: flex;
  margin-top: 20px;
}
.main_info_text_name{
  display: flex;
  align-items: center;
  margin-left: 20px;
}
.gender_info{
  margin-left: 10px;
  height: 16px;
  width: 16px;
}
.follow_num{
  display: flex;
  margin-left: 20px;
}
.following_num{
  display: flex;
  align-items: center;
  height: 20px;
  padding-left: 10px;
  padding-right: 10px;
  border-left: 1px solid #ccc;
  border-right: 1px solid #ccc;
}
.followers_num{
  display: flex;
  align-items: center;
  height: 20px;
  padding-left: 10px;
  padding-right: 10px;
  border-right: 1px solid #ccc;
}
.follow_btn{
  display: flex;
  align-items: center;
  margin-left: auto;
  margin-right: 20px;
}
.edit_logout_btn{
  display: flex;
  align-items: center;
  margin-left: auto;
}
.sub_info{
  margin-top: 20px;
  margin-left: 120px;
}
.sub_info p{
  font-size: 10px;
  margin-top: 5px;
  margin-bottom: 5px;
  color: #8c939d;
}
.avatar {
  width: 200px;
  height: 200px;
  border-radius: 50%;
}
.edit-mode {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
}

.avatar-uploader {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}

.button-group {
  display: flex;
  justify-content: center;
  margin-top: 40px;
  margin-bottom: 20px;
}

.content {
  display: flex;
  flex-direction: column;
}
.content h3{
  align-self: center;
}
</style>