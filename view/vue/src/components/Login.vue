<template>
  <div class="login-container">
    <el-card class="login-card" shadow="hover">
      <h1 class="login-title">欢迎登录</h1>
      <el-form @submit.prevent="handleLogin" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="username" placeholder="请输入用户名" clearable></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="password" type="password" placeholder="请输入密码" show-password></el-input>
        </el-form-item>
        <div class="button-group">
          <el-button type="primary" size="large" @click="handleLogin">登录</el-button>
          <el-button type="primary" size="large" @click="goToRegister">注册</el-button>
        </div>
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import http from "../api/http";

export default {
  name: 'Login',
  data() {
    return {
      username: '',
      password: '',
      errorMessage: '',
    };
  },
  methods: {
    async handleLogin() {
      try {
        const response = await http.post("/login", {
          userName: this.username,
          userPwd: this.password,
        });
        if (response.data.code === 200) {
          // 保存用户信息到 localStorage
          localStorage.setItem('userInfo', JSON.stringify(response.data.data));
          // 登录成功，跳转到员工信息页面
          if(response.data.data.userType=="emp"){
            this.$router.push('/employee');
          }else if(response.data.data.userType=="admin"){
            this.$router.push('/admin');
          }
        } else {
          this.errorMessage = response.data.message || '登录失败，请检查用户名和密码';
        }
      } catch (error) {
        this.errorMessage = '登录失败，请稍后再试';
      }
    },
    goToRegister() {
      this.$router.push('/register');
    },
  },
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #74ebd5, #9face6);
}

.login-card {
  width: 400px;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  background-color: #ffffff;
}

.login-title {
  text-align: center;
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 15px; /* 调整按钮之间的间距 */
  margin-top: 20px;
}

.login-container button {
  font-size: 16px; /* 增大按钮字体 */
  padding: 10px 20px; /* 增大按钮尺寸 */
}

.error {
  color: #e74c3c;
  margin-top: 10px;
  text-align: center;
}
</style>
