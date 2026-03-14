<template>
  <div class="register-container">
    <el-card class="register-card" shadow="hover">
      <h1 class="register-title">注册</h1>

      <el-form
        ref="registerFormRef"
        :model="registerForm"
        :rules="registerRules"
        label-width="90px"
        @submit.prevent
      >
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="registerForm.userName" placeholder="8-12位数字+字母" clearable />
        </el-form-item>

        <el-form-item label="密码" prop="userPwd">
          <el-input
            v-model="registerForm.userPwd"
            type="password"
            placeholder="6-12位数字+字母"
            show-password
          />
        </el-form-item>

        <el-form-item label="员工ID" prop="empId">
          <el-input v-model="registerForm.empId" placeholder="请输入员工ID" clearable />
        </el-form-item>

        <div class="button-group">
          <el-button type="primary" size="large" :loading="submitting" @click="handleRegister">
            注册
          </el-button>
          <el-button size="large" @click="goToLogin">返回登录</el-button>
        </div>

        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import http from "../api/http";

export default {
  name: "Register",
  data() {
    return {
      submitting: false,
      errorMessage: "",
      registerForm: {
        userName: "",
        userPwd: "",
        empId: "",
      },
      registerRules: {
        userName: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          {
            pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,12}$/,
            message: "用户名需为8-12位数字+字母",
            trigger: "blur",
          },
        ],
        userPwd: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,12}$/,
            message: "密码需为6-12位数字+字母",
            trigger: "blur",
          },
        ],
        empId: [
          { required: true, message: "请输入员工ID", trigger: "blur" },
          {
            pattern: /^\d+$/,
            message: "员工ID必须为数字",
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    async handleRegister() {
      if (!this.$refs.registerFormRef) return;

      this.errorMessage = "";

      try {
        await this.$refs.registerFormRef.validate();
      } catch {
        return;
      }

      this.submitting = true;
      try {
        const res = await http.put("/regist", {
          userName: this.registerForm.userName,
          userPwd: this.registerForm.userPwd,
          empId: this.registerForm.empId,
        });

        if (res.data?.code === 200) {
          this.$message.success("注册成功，请登录");
          this.$router.push("/login");
        } else {
          const msg = res.data?.message || "注册失败";
          this.errorMessage = msg;
          this.$message.error(msg);
        }
      } catch (error) {
        console.error("注册失败", error);
        this.errorMessage = "注册失败，请稍后再试";
      } finally {
        this.submitting = false;
      }
    },
    goToLogin() {
      this.$router.push("/login");
    },
  },
};
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #74ebd5, #9face6);
}

.register-card {
  width: 420px;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  background-color: #ffffff;
}

.register-title {
  text-align: center;
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 15px;
  margin-top: 20px;
}

.error {
  color: #e74c3c;
  margin-top: 10px;
  text-align: center;
}
</style>
