<template>
  <div class="employee-page">
    <el-container style="width: 100%; height: 100%;">
      <el-aside width="200px" class="menu">
        <el-menu default-active="1" class="el-menu-vertical-demo" @select="handleMenuSelect">
          <el-menu-item index="1">员工信息</el-menu-item>
          <el-menu-item index="2">任务</el-menu-item>
        </el-menu>
      </el-aside>
      <el-main style="padding: 0; height: 100%; display: flex; flex-direction: column;">
        <!-- 顶部栏：固定在右上角 -->
        <div class="top-bar">
          <!-- 发布任务按钮：仅在任务标签页显示（左侧） -->
          <div class="top-bar-left" v-if="activeTab === '2'">
            <el-button 
              type="primary" 
              icon="Plus" 
              @click="handlePublishTask"
              style="margin: 12px;"
            >
              发布任务
            </el-button>
          </div>
          <!-- 用户头像区域：固定右侧 -->
          <div class="top-bar-right">
            <el-dropdown trigger="click" @command="handleUserCommand">
              <span class="user-trigger">
                <el-avatar :size="36">
                  <el-icon :size="24"><UserFilled /></el-icon>
                </el-avatar>
                <span class="username">{{ userName || '用户' }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="changePwd">修改密码</el-dropdown-item>
                  <el-dropdown-item divided />
                  <el-dropdown-item command="deleteUser" style="color: #f56c6c;">
                    <el-icon style="margin-right: 4px;"><Delete /></el-icon>
                    删除账号
                  </el-dropdown-item>
                  <el-dropdown-item divided />
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>

        <!-- 内容区域：滚动容器 -->
        <div class="content-wrapper">
          <!-- 修改密码弹窗 -->
          <el-dialog v-model="changePwdDialogVisible" title="修改密码" width="420px">
            <el-form ref="changePwdFormRef" :model="changePwdForm" :rules="changePwdRules" label-width="90px">
              <el-form-item label="用户名" prop="userName">
                <el-input v-model="changePwdForm.userName" placeholder="8-12位数字+字母" clearable />
              </el-form-item>
              <el-form-item label="旧密码" prop="userPwd">
                <el-input v-model="changePwdForm.userPwd" type="password" placeholder="6-12位数字+字母" show-password />
              </el-form-item>
              <el-form-item label="新密码" prop="newUserPwd">
                <el-input v-model="changePwdForm.newUserPwd" type="password" placeholder="6-12位数字+字母" show-password />
              </el-form-item>
            </el-form>
            <template #footer>
              <span class="dialog-footer">
                <el-button @click="changePwdDialogVisible = false">取消</el-button>
                <el-button type="primary" :loading="changePwdSubmitting" @click="submitChangePassword">确认修改</el-button>
              </span>
            </template>
          </el-dialog>

          <!-- 发布任务弹窗 -->
          <el-dialog 
            v-model="publishTaskDialogVisible" 
            title="发布任务" 
            width="500px"
            destroy-on-close
          >
            <el-form ref="publishTaskFormRef" :model="publishTaskForm" :rules="publishTaskRules" label-width="100px">
              <el-form-item label="创建员工ID" prop="createEmpId">
                <el-input 
                  v-model="publishTaskForm.createEmpId" 
                  type="number" 
                  placeholder="请输入创建员工ID" 
                  clearable
                  disabled
                />
              </el-form-item>
              <el-form-item label="任务内容" prop="content">
                <el-input 
                  v-model="publishTaskForm.content" 
                  type="textarea" 
                  rows="4"
                  placeholder="请输入任务内容" 
                  clearable
                />
              </el-form-item>
            </el-form>
            <template #footer>
              <span class="dialog-footer">
                <el-button @click="publishTaskDialogVisible = false">取消</el-button>
                <el-button type="primary" :loading="publishTaskSubmitting" @click="submitPublishTask">确认发布</el-button>
              </span>
            </template>
          </el-dialog>

          <!-- 修改任务弹窗 -->
          <el-dialog 
            v-model="editTaskDialogVisible" 
            title="修改任务" 
            width="500px"
            destroy-on-close
          >
            <el-form ref="editTaskFormRef" :model="editTaskForm" :rules="editTaskRules" label-width="100px">
              <el-form-item label="任务ID" prop="id">
                <el-input 
                  v-model="editTaskForm.id" 
                  type="number" 
                  disabled
                />
              </el-form-item>
              <el-form-item label="创建员工ID" prop="createEmpId">
                <el-input 
                  v-model="editTaskForm.createEmpId" 
                  type="number" 
                  disabled
                />
              </el-form-item>
              <el-form-item label="任务内容" prop="content">
                <el-input 
                  v-model="editTaskForm.content" 
                  type="textarea" 
                  rows="4"
                  placeholder="请输入修改后的任务内容" 
                  clearable
                />
              </el-form-item>
            </el-form>
            <template #footer>
              <span class="dialog-footer">
                <el-button @click="editTaskDialogVisible = false">取消</el-button>
                <el-button type="primary" :loading="editTaskSubmitting" @click="submitEditTask">确认修改</el-button>
              </span>
            </template>
          </el-dialog>

          <!-- 员工信息区域 -->
          <div v-if="activeTab === '1'" class="content-inner">
            <h2>员工信息</h2>
            <el-table :data="employeeData" style="width: 100%" border>
              <el-table-column prop="empId" label="ID" width="50"></el-table-column>
              <el-table-column prop="name" label="姓名"></el-table-column>
              <el-table-column prop="departmentName" label="职位"></el-table-column>
              <el-table-column prop="salary" label="工资">
                <template #default="scope">
                  {{ formatSalary(scope.row.salary) }}
                </template>
              </el-table-column>
              <el-table-column prop="managerName" label="经理"></el-table-column>
            </el-table>
          </div>

          <!-- 任务区域 -->
          <div v-else class="content-inner">
            <h2>任务</h2>
            <el-table :data="taskData" style="width: 100%" border>
              <el-table-column prop="id" label="任务ID" width="80"></el-table-column>
              <el-table-column prop="createEmpId" label="创建员工Id" width="100"></el-table-column>
              <el-table-column prop="content" label="任务内容"></el-table-column>
              <el-table-column prop="createDate" label="创建日期"></el-table-column>
              <!-- 操作列：仅创建者可见 -->
              <el-table-column label="操作" width="120">
                <template #default="scope">
                  <template v-if="scope.row.createEmpId === empId">
                    <el-button 
                      type="primary" 
                      size="small" 
                      icon="Edit" 
                      @click="handleEditTask(scope.row)"
                    >
                      修改
                    </el-button>
                    <el-button 
                      type="danger" 
                      size="small" 
                      icon="Delete" 
                      @click="handleDeleteTask(scope.row.id)"
                    >
                      删除
                    </el-button>
                  </template>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import http from "../api/http";
import { UserFilled, Delete, Plus, Edit } from "@element-plus/icons-vue";

export default {
  name: "Employee",
  components: {
    UserFilled,
    Delete,
    Plus,
    Edit
  },
  data() {
    return {
      activeTab: "1",
      empId: "", // 员工ID（用于任务权限控制）
      userId: "", // 用户ID（用于账号删除）
      userName: "",
      employeeData: [],
      taskData: [],

      // 修改密码相关
      changePwdDialogVisible: false,
      changePwdSubmitting: false,
      changePwdForm: {
        userName: "",
        userPwd: "",
        newUserPwd: "",
      },
      changePwdRules: {
        userName: [
          { required: true, message: "请输入用户名", trigger: "blur" },
        ],
        userPwd: [
          { required: true, message: "请输入旧密码", trigger: "blur" },
        ],
        newUserPwd: [
          { required: true, message: "请输入新密码", trigger: "blur" },
          {
            pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,12}$/,
            message: "新密码需为6-12位数字+字母",
            trigger: "blur",
          },
        ],
      },

      // 发布任务相关
      publishTaskDialogVisible: false,
      publishTaskSubmitting: false,
      publishTaskForm: {
        createEmpId: "",
        content: ""
      },
      publishTaskRules: {
        createEmpId: [
          { required: true, message: "创建员工ID不能为空", trigger: "blur" },
          { type: "number", message: "创建员工ID必须为数字", trigger: "blur" }
        ],
        content: [
          { required: true, message: "请输入任务内容", trigger: "blur" },
          { min: 5, max: 500, message: "任务内容长度需在5-500个字符之间", trigger: "blur" }
        ]
      },

      // 修改任务相关
      editTaskDialogVisible: false,
      editTaskSubmitting: false,
      editTaskForm: {
        id: "",
        createEmpId: "",
        content: ""
      },
      editTaskRules: {
        content: [
          { required: true, message: "请输入任务内容", trigger: "blur" },
          { min: 5, max: 500, message: "任务内容长度需在5-500个字符之间", trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    // 格式化工资展示
    formatSalary(salary) {
      if (salary === null || salary === undefined || salary === "") {
        return "0.00";
      }
      const num = Number(salary);
      return isNaN(num) ? "0.00" : num.toFixed(2);
    },

    // 处理用户下拉框命令
    handleUserCommand(command) {
      if (command === "changePwd") {
        this.openChangePwdDialog();
      } else if (command === "logout") {
        this.logout();
      } else if (command === "deleteUser") {
        this.handleDeleteUser();
      }
    },

    // 删除账号（使用userId）
    handleDeleteUser() {
      // 校验userId是否存在
      if (!this.userId) {
        this.$message.error('用户ID不存在，无法删除账号！');
        return;
      }

      this.$confirm(
        '此操作将永久删除当前账号，删除后无法恢复！是否继续？',
        '危险提示',
        {
          confirmButtonText: '确定删除',
          cancelButtonText: '取消',
          type: 'error',
          dangerMode: true
        }
      ).then(async () => {
        try {
          // 调用删除账号接口（使用userId）
          await http.delete(`/user/${this.userId}`);
          this.$message.success('账号删除成功！');
          this.logout();
        } catch (error) {
          this.$message.error('账号删除失败！');
          console.error('删除用户失败：', error);
        }
      }).catch(() => {
        this.$message.info('已取消删除操作');
      });
    },

    // 打开发布任务弹窗
    handlePublishTask() {
      this.publishTaskForm = {
        createEmpId: this.empId || "",
        content: ""
      };
      this.publishTaskDialogVisible = true;
      this.$nextTick(() => {
        if (this.$refs.publishTaskFormRef) {
          this.$refs.publishTaskFormRef.clearValidate();
        }
      });
    },

    // 提交发布任务
    async submitPublishTask() {
      if (!this.$refs.publishTaskFormRef) return;

      try {
        await this.$refs.publishTaskFormRef.validate();
      } catch (error) {
        return;
      }

      this.publishTaskSubmitting = true;
      try {
        const res = await http.post(`/emp/${this.empId}/task`, this.publishTaskForm);
        if (res.data?.code === 200) {
          this.$message.success('任务发布成功！');
          this.publishTaskDialogVisible = false;
          await this.fetchTaskData(); // 刷新任务列表
        } else {
          this.$message.error(res.data?.message || '任务发布失败！');
        }
      } catch (error) {
        this.$message.error('任务发布失败！');
        console.error('发布任务失败：', error);
      } finally {
        this.publishTaskSubmitting = false;
      }
    },

    // 打开修改任务弹窗
    handleEditTask(task) {
      this.editTaskForm = {
        id: task.id,
        createEmpId: task.createEmpId,
        content: task.content
      };
      this.editTaskDialogVisible = true;
      this.$nextTick(() => {
        if (this.$refs.editTaskFormRef) {
          this.$refs.editTaskFormRef.clearValidate();
        }
      });
    },

    // 提交修改任务
    async submitEditTask() {
      if (!this.$refs.editTaskFormRef) return;

      try {
        await this.$refs.editTaskFormRef.validate();
      } catch (error) {
        return;
      }

      this.editTaskSubmitting = true;
      try {
        // 调用修改任务接口
        const res = await http.put(`/task/${this.editTaskForm.id}`, this.editTaskForm);
        if (res.data?.code === 200) {
          this.$message.success('任务修改成功！');
          this.editTaskDialogVisible = false;
          await this.fetchTaskData(); // 刷新任务列表
        } else {
          this.$message.error(res.data?.message || '任务修改失败！');
        }
      } catch (error) {
        this.$message.error('任务修改失败！');
        console.error('修改任务失败：', error);
      } finally {
        this.editTaskSubmitting = false;
      }
    },

    // 删除任务
    handleDeleteTask(taskId) {
      this.$confirm(
        '此操作将永久删除该任务，删除后无法恢复！是否继续？',
        '删除确认',
        {
          confirmButtonText: '确定删除',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(async () => {
        try {
          // 调用删除任务接口
          const res = await http.delete(`/task/${taskId}`);
          if (res.data?.code === 200) {
            this.$message.success('任务删除成功！');
            await this.fetchTaskData(); // 刷新任务列表
          } else {
            this.$message.error(res.data?.message || '任务删除失败！');
          }
        } catch (error) {
          this.$message.error('任务删除失败！');
          console.error('删除任务失败：', error);
        }
      }).catch(() => {
        this.$message.info('已取消删除操作');
      });
    },

    // 打开修改密码弹窗
    openChangePwdDialog() {
      this.changePwdForm.userName = this.userName || "";
      this.changePwdForm.userPwd = "";
      this.changePwdForm.newUserPwd = "";
      this.changePwdDialogVisible = true;

      this.$nextTick(() => {
        if (this.$refs.changePwdFormRef) {
          this.$refs.changePwdFormRef.clearValidate();
        }
      });
    },

    // 提交修改密码
    async submitChangePassword() {
      if (!this.$refs.changePwdFormRef) return;

      try {
        await this.$refs.changePwdFormRef.validate();
      } catch {
        return;
      }

      this.changePwdSubmitting = true;
      try {
        const res = await http.put("/user", {
          userName: this.changePwdForm.userName,
          userPwd: this.changePwdForm.userPwd,
          newUserPwd: this.changePwdForm.newUserPwd,
        });

        if (res.data?.code === 200) {
          this.$message.success("密码修改成功，请重新登录");
          this.changePwdDialogVisible = false;
          this.logout();
        } else {
          this.$message.error(res.data?.message || "修改失败");
        }
      } catch (error) {
        console.error("修改密码失败", error);
        this.$message.error("修改密码失败");
      } finally {
        this.changePwdSubmitting = false;
      }
    },

    // 退出登录
    logout() {
      localStorage.removeItem("userInfo");
      this.$router.push("/login");
    },

    // 切换菜单
    async handleMenuSelect(index) {
      this.activeTab = index;
      if (index === "1") {
        await this.fetchEmployeeById();
      } else if (index === "2") {
        await this.fetchTaskData();
      }
    },

    // 获取员工信息
    async fetchEmployeeById() {
      if (!this.empId) {
        this.$message.error('员工ID未找到，请重新登录');
        return;
      }
      try {
        const response = await http.get(`/emp/${this.empId}`);
        this.employeeData = [response.data.data];
      } catch (error) {
        console.error('获取员工信息失败', error);
        this.$message.error('获取员工信息失败');
      }
    },

    // 获取任务列表
    async fetchTaskData() {
      try {
        const response = await http.get(`/emp/${this.empId}/task`);
        this.taskData = response.data.data.task || [];
      } catch (error) {
        console.error('获取任务信息失败', error);
        this.$message.error('获取任务信息失败');
      }
    },
  },

  async mounted() {
    const userInfo = JSON.parse(localStorage.getItem("userInfo"));
    if (userInfo) {
      // 读取userId（用于账号删除）
      this.userId = userInfo.userId || "";
      // 读取empId（用于任务权限控制）
      this.empId = userInfo.empId || "";
      this.userName = userInfo.userName || "";

      // 校验必要的ID
      if (!this.empId) {
        this.$message.error("员工ID未找到，请重新登录");
        this.$router.push("/login");
        return;
      }

      await this.fetchEmployeeById();
    } else {
      this.$message.error("请先登录");
      this.$router.push("/login");
    }
  },
};
</script>

<style scoped>
.employee-page {
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

/* 侧边栏样式 */
.menu {
  background-color: #f5f5f5;
  border-right: 1px solid #dcdcdc;
  height: 100%;
}

/* 顶部栏：修复布局，确保用户区域固定右侧 */
.top-bar {
  width: 100%;
  display: flex;
  align-items: center; /* 垂直居中 */
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  padding: 0 16px;
  box-sizing: border-box;
  position: relative; /* 增加定位上下文 */
}

/* 发布任务按钮区域：居左 */
.top-bar-left {
  flex: 0 1 auto; /* 自适应宽度，不拉伸 */
}

/* 用户头像区域：强制靠右 */
.top-bar-right {
  display: flex;
  align-items: center;
  padding: 8px 0;
  margin-left: auto; /* 关键：强制靠右 */
  flex: 0 1 auto; /* 自适应宽度，不拉伸 */
}

/* 用户触发区域样式 */
.user-trigger {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  user-select: none;
  padding: 4px 8px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.user-trigger:hover {
  background-color: #f5f5f5;
}

.username {
  color: #303133;
  font-size: 14px;
}

/* 内容容器：滚动区域 */
.content-wrapper {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  box-sizing: border-box;
  height: calc(100% - 60px);
}

.content-inner {
  width: 100%;
}

/* 弹窗底部按钮样式 */
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

/* 按钮间距优化 */
.el-button {
  margin-right: 8px;
}

/* 下拉菜单样式优化 */
:deep(.el-dropdown-menu__item) {
  cursor: pointer;
  font-size: 14px;
}

/* 表格样式优化 */
:deep(.el-table) {
  --el-table-header-text-color: #303133;
  --el-table-row-hover-bg-color: #f8f9fa;
}

/* 操作列按钮样式 */
:deep(.el-table .el-button--small) {
  margin: 0 2px;
}
</style>