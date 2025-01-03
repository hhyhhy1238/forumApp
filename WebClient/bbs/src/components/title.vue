<template>
  <div class="title">
    <el-row class="navbar">
      <el-col :span="6">
        <div class="logo">
          <img class="logopic" src="../../public/logo.png" alt="">
          <h3>论坛</h3>
        </div>
      </el-col>
      <el-col :span="18">
        <div class="login-info">
          <div v-if="isLogin" class="user-info" @click="gotoHomePage(userinfo.username)">
            <el-avatar :src="userinfo.avatar" size="small" shape="circle"></el-avatar>
            <p>{{ userinfo.nickname }}</p>
          </div>
          <router-link v-if="!isLogin" class="login" to="/login"><p>登录/注册</p></router-link>
          <div class="posting-btn">
            <el-button @click="posting" type="primary"><el-icon><CirclePlus /></el-icon> 发布</el-button>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
  <div class="divider"></div>
</template>

<script>
import Cookies from "js-cookie";
import router from "@/router/router.js";
import { ElButton, ElImage, ElRow, ElCol, ElAvatar } from "element-plus";
import {CirclePlus} from "@element-plus/icons-vue";
export default {
  name: "Title",
  components: {
    CirclePlus,
    ElButton,
    ElImage,
    ElRow,
    ElCol,
    ElAvatar
  },
  data() {
    return {
      isLogin: false,
      userinfo: null,
    }
  },
  methods: {
    posting() {
      router.push("/posting");
    },
    gotoHomePage(username) {
      router.push(`/homepage/${username}`);
    }
  },
  mounted() {
    this.isLogin = Cookies.get("isLogin");
    if (this.isLogin) {
      this.userinfo = JSON.parse(Cookies.get("user_info"));
      //console.log(this.userinfo.avatar)
    }
  },
  props:["page"]
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

.login-info {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  margin-left: auto;
  margin-right: 30px;
}
.login{
  align-items: center;
  margin-left: auto;
  margin-right: 30px;
}
.user-info p {
  margin-left: 10px;
}

.posting-btn {
  margin-right: 40px;
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
</style>