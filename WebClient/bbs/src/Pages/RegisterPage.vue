<template>
  <div class="title">
    <el-row class="navbar">
      <el-col :span="6">
        <div class="logo">
          <el-image src="../assets/logo.png" fit="cover"></el-image>
          <h3>论坛</h3>
        </div>
      </el-col>
    </el-row>
  </div>
  <div id="userregister">
    <el-form ref="registerForm" :model="user" label-width="100px">
      <h2>用户注册</h2>
      <el-form-item label="用户名：">
        <el-input v-model="user.username"></el-input>
      </el-form-item>
      <el-form-item label="密码：">
        <el-input type="password" v-model="user.password"></el-input>
      </el-form-item>
      <el-form-item label="确认密码：">
        <el-input type="password" v-model="passwordconfirm"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button class="custom-button" type="primary" @click="register">注册</el-button>
        <p>{{ info }}</p>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import axios from "axios";
import router from "@/router/router.js";
import { ElForm, ElFormItem, ElInput, ElButton } from 'element-plus';

export default {
  name: "RegisterPage",
  components: {
    ElForm,
    ElFormItem,
    ElInput,
    ElButton,
  },
  data() {
    return {
      user: {
        username: "",
        password: "",
      },
      passwordconfirm: "",
      info: "  ",
    };
  },
  methods: {
    register() {
      if (this.user.password !== this.passwordconfirm) {
        this.$message.error("两次输入密码不一致！")
      }
      else if (this.user.password.length < 6){
        this.$message.error("密码至少6位！")
      }
      else {
        axios.post("http://47.95.214.215/user/add", this.user).then((response) => {
          if(response.data.success){
            alert("注册成功");
            router.push("/login");
          }
          else{
            this.$message.error("用户名已存在!")
          }
        });
        // if (1) {
        //   alert("注册成功");
        //   router.push("/login");
        // }
      }
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

#userregister {
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
  margin-top: 20px;
}
</style>