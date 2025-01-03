<template>
  <div class="title">
    <el-row class="navbar">
      <el-col :span="6">
        <div class="logo">
          <el-image class="logopic" :src="logo" fit="cover"></el-image>
          <h3>论坛</h3>
        </div>
      </el-col>
    </el-row>
  </div>
  <div id="userlogin">
    <el-form ref="loginForm" :model="user" label-width="80px">
      <h2>用户登录</h2>
      <el-form-item label="用户名：">
        <el-input v-model="user.username"></el-input>
      </el-form-item>
      <el-form-item label="密码：">
        <el-input type="password" v-model="user.password"></el-input>
      </el-form-item>
      <el-form-item>
        <p>{{ info }}</p>
        <el-button class="custom-button" type="primary" @click="login">登录</el-button>
      </el-form-item>
      <div class="router-link">
        <router-link to="/register">还没有账号？点击注册</router-link>
      </div>
    </el-form>
  </div>
</template>

<script>
import { ElForm, ElFormItem, ElInput, ElButton } from 'element-plus';
import axios from 'axios';
import router from '@/router/router.js';
import Cookies from 'js-cookie';
import LogoPic from "@/assets/logo.png"

export default {
  name: 'LoginPage',
  components: {
    ElForm,
    ElFormItem,
    ElInput,
    ElButton,
  },
  data() {
    return {
      user: {
        username: '',
        password: '',
      },
      info: '',
      userinfo: {
        username: '',
        nickname: '',
        avatar: '',
      },
      logo:LogoPic
    };
  },
  methods: {
    login() {
      axios.post('http://47.95.214.215/userInfo/login', this.user).then((response) => {
        console.log(response.data)
        if (response.data.success===true) {
          this.userinfo.username = response.data.userInfo.username;
          this.userinfo.nickname = response.data.userInfo.nickname;
          this.userinfo.avatar = response.data.userInfo.avatarUrl.slice(2,-2);
          Cookies.set('user_info', JSON.stringify(this.userinfo));
          Cookies.set('isLogin', true);
          router.push('/index');
        }
        else{
          //this.$message.error("登陆失败")
          if(response.data.userInfoResponseFailedReason==="USERNAME_DOES_NOT_EXIST"){
            this.$message.error("用户不存在!");
          }
          else if(response.data.userInfoResponseFailedReason==="WRONG_PASSWORD"){
            this.$message.error("密码错误!");
          }
        }
      });
    },
  },
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
#userlogin {
  width: 400px;
  margin: auto;
  margin-top: 100px;
  padding: 40px;
  border: 1px solid #ccc;
  border-radius: 8px;
  background-color: #f7f7f7;
}

h2 {
  text-align: center;
  margin-bottom: 20px;
}

.custom-button {
  width: 100%;
}

.router-link {
  text-align: center;
  margin-top: 20px;
}
.logopic{
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 15px;
}
</style>