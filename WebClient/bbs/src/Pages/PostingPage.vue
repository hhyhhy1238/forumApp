<template>
  <div class="title">
    <el-row class="navbar">
      <el-col :span="6">
        <div class="logo">
          <el-image class="logopic" :src="logo" fit="cover"></el-image>
          <h3>论坛</h3>
        </div>
      </el-col>
      <el-col :span="18">
        <div class="login-info">
          <div class="user-info" @click="gotoHomePage(userinfo.username)">
            <el-avatar :src="userinfo.avatar" size="small" shape="circle"></el-avatar>
            <p>{{ userinfo.nickname }}</p>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
  <div class="divider"></div>
  <div class="posting">
    <h1>发布帖子</h1>
      <div>
        <el-form-item label="标题">
          <el-input v-model="post.title"></el-input>
        </el-form-item>
      </div>
      <div>
        <el-form-item label="正文">
          <el-input type="textarea" :autosize="{minRows:6}" v-model="post.content" class="content"></el-input>
        </el-form-item>
      </div>
      <div>
        <el-form-item label="分类">
          <el-radio-group v-model="post.category">
            <el-radio v-for="category in categories" :key="category" :label="category">{{ category }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </div>
      <div>
        <el-form-item label="图片">
          <el-upload
              class="image-upload"
              :auto-upload="false"
              accept="image/jpeg,image/png"
              :limit=9
              :on-exceed="handleExceed"
              :on-change="handleFileUpload"
              :on-remove="handleRemove"
              :file-list="post.images"
              list-type="picture-card"
          >
            <i class="el-icon-plus add-icon" style="font-style: normal;">+</i>
          </el-upload>
        </el-form-item>
      </div>
      <div>
        <el-form-item>
          <el-button class="posting_btn" type="primary" @click="submitPost">发布</el-button>
        </el-form-item>
      </div>
  </div>
</template>

<script>
import axios from 'axios';
import Cookies from "js-cookie";
import router from "@/router/router.js";
import LogoPic from "@/assets/logo.png"
export default {
  name: "PostingPage",
  data() {
    return {
      post: {
        username:'',
        title: '',
        content: '',
        category: '',
        images: []
      },
      userinfo: {},
      categories: ["求助","分享","吐槽"],
      logo:LogoPic
    }
  },
  methods: {
    handleRemove(file,fileList) {
      this.post.images=fileList.map((item) => {
        return {
          url: item.url,
          file: item.raw
        }
      })
      console.log(this.post.images)
    },
    handleFileUpload(file,fileList){
      const isIMAGE = (file.raw.type === 'image/jpeg' || file.raw.type === 'image/png')
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
      let image = {
        url:URL.createObjectURL(file.raw),
        file:file.raw
      }
      // let reader = new FileReader()
      // reader.readAsDataURL(file.raw)
      // reader.onload = function(e){
      //   image.file=this.result
      // }
      this.post.images.push(image)
      console.log(this.post.images)
    },
    handleExceed(){
      this.$message({
        type:'warning',
        message:'超出最大上传文件数量的限制！'
      })
    },
    submitPost() {
      if (this.post.title===""){
        this.$message.error("标题不能为空")
        return
      }
      let type=""
      switch (this.post.category){
        case "分享":{
          type = "POST_TYPE1"
          break
        }
        case "吐槽":{
          type="POST_TYPE2"
          break
        }
        case "求助":{
          type="POST_TYPE3"
        }
      }
      const  formData=new FormData()
      formData.append("id", "45");
      formData.append("owner", this.userinfo.username);
      formData.append("title", this.post.title);
      formData.append("content", this.post.content);
      formData.append("type", type);

      for (const image of this.post.images) {
        formData.append("images", image.file);
      }
      const request={
        id:"45",
        owner:this.userinfo.username,
        title:this.post.title,
        content:this.post.content,
        images:[],
        type:type,
      }
      for (const image of this.post.images) {
        request.images.push(image.file)
      }
      console.log(request)
      axios.post("http://47.95.214.215/post/add",formData)
          .then(response => {
            console.log(response.data)
           if(response.data.success===true){
             alert('帖子发布成功')
             router.push("/index")
           }
          })
          .catch(error => {
            // 处理发布失败的错误
            this.$message.error('帖子发布失败')
          });
    },
    gotoHomePage(username) {
      router.push(`/homepage/${username}`);
    }
  },
  mounted() {
    this.userinfo = JSON.parse(Cookies.get("user_info"));
  }
};
</script>

<style scoped>
.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  display: flex;
  align-items: center;
  margin-left: 20px;
}

.logo h3 {
  margin-left: 10px;
}

.posting{
  margin-top: 100px;
  margin-left: 18%;
  margin-right: 18%;
  min-width: 800px;
  padding: 40px;
  border: 1px solid #ccc;
  border-radius: 8px;
  background-color: #f7f7f7;
}
.content{
  min-height: 100px;
  resize: vertical;
}
.login-info {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  margin-left: auto;
  margin-right: 130px;
}

.user-info p {
  margin-left: 10px;
}

.divider {
  height: 1px;
  background-color: #ccc;
  margin: 10px 0;
}
.logopic{
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 15px;
}
.posting_btn{
  margin-left: auto;
  margin-right: 20px;
}
</style>