<template>
  <div>
    <pagetitle></pagetitle>
    <div>
      <div class="categories">
        <el-button-group>
          <el-button
              v-for="category in categories"
              :key="category"
              :class="{ active: category === selectedCategory }"
              @click="changeCategory(category)"
          >
            {{ category }}
          </el-button>
        </el-button-group>
      </div>

      <div class="posts">
        <post v-for="post in displayedPosts" :key="post.postid" :post="post" @click="openPostDetail(post.postid)"></post>
      </div>

      <div class="pagination">
        <el-pagination
            :current-page="pagenum"
            :page-count="totalpagenum"
            :pager-count="10"
            @current-change="changePage"
            layout="prev, pager, next, jumper"
        ></el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import pagetitle from "@/components/title.vue";
import post from "@/components/post.vue";
import router from "@/router/router.js";
import { ElButton, ElButtonGroup, ElPagination } from "element-plus";
import axios from "axios";

export default {
  name: "IndexPage",
  components: {
    pagetitle,
    post,
    ElButton,
    ElButtonGroup,
    ElPagination,
  },
  data() {
    return {
      posts: [{
        postid:"",
        owner:"qwe",
        ownernickname: "qwe",
        avatar: '',
        title: "qwe",
        content: "qwe",
        time: "2023-12-28",
        images: ["","","","","","","",""],
        likenum: 0,
        favornum: 0,
        commentnum: 0,
      }
      ],
      pagenum: 1,
      selectedCategory: "分享",
      totalpagenum: 1,
      categories: ["分享", "求助", "吐槽"], // 添加其他分类
    };
  },
  methods: {
    openPostDetail(postid) {
      router.push(`/postdetail/${postid}`);
    },
    changeCategory(category) {
      this.selectedCategory = category;
      this.pagenum = 1; // 切换分类后回到第一页
      this.getPostsFromServer(category);
    },
    changePage(page) {
      console.log(page)
      if (page === 1) {
        this.pagenum = page;
      } else {
        this.pagenum = page;
      }
    },
    parseDateTime(dateTimeString) {
      const pattern = /^(\d{4})年(\d{1,2})月(\d{1,2})日 (\d{1,2}):(\d{2})$/;
      const [, year, month, day, hour, minute] = pattern.exec(dateTimeString);
      const formattedDateTimeString = `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')} ${hour.padStart(2, '0')}:${minute}`;
      return new Date(formattedDateTimeString);
    },
    getPostsFromServer(category) {
      this.posts=[]
      let request=""
      switch (category){
        case "分享":{
          request = "POST_TYPE1"
          break
        }
        case "吐槽":{
          request="POST_TYPE2"
          break
        }
        case "求助":{
          request="POST_TYPE3"
        }
      }
      axios.get(`http://47.95.214.215/samePostTypeList/query/${request}`).then((response) => {
            for (const post of response.data.postList) {
              let _post={
                postid: post.id,
                owner: post.owner,
                ownernickname: "",
                avatar: '',
                title: post.title,
                content: post.content,
                time: post.date,
                images: post.images,
                likenum: post.likeNum,
                favornum: 0,
                commentnum: post.reviews.length,
              }
              axios.get(`http://47.95.214.215/userInfo/query/${_post.owner}`).then(response=>{
                _post.ownernickname=response.data.userInfo.nickname
                _post.avatar=response.data.userInfo.avatarUrl.slice(2,-2)
                axios.get(`http://47.95.214.215/post/queryFavorNum/${_post.postid}`).then(response=>{
                  _post.favornum=response.data.numFavor
                  this.posts.push(_post)
                  this.posts.sort((a,b)=>{
                    const timeA = this.parseDateTime(a.time);
                    const timeB = this.parseDateTime(b.time);
                    console.log(a.time)
                    console.log(timeA)
                    return timeB - timeA;
                  })
                  this.totalpagenum=Math.floor(this.posts.length/10)+1
                  console.log(_post)
                })
              })
            }
          })
          .catch((error) => {
            console.error("Error fetching posts:", error);
          });
    },
  },
  computed: {
    displayedPosts() {
      return this.posts.slice((this.pagenum-1) * 10,this.pagenum * 10);
    },
  },
  mounted() {
    this.changeCategory("分享")
  },
};
</script>

<style scoped>
.categories {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.category {
  cursor: pointer;
  padding: 5px 10px;
  margin-right: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.category.active {
  background-color: #ccc;
}

.posts {
  margin-left: 18%;
  margin-right: 18%;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.page {
  cursor: pointer;
  padding: 5px 10px;
  margin-right: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.page.active {
  background-color: #ccc;
}
</style>