<template>
  <div class="employee-page">
    <el-container>
      <el-aside width="200px" class="menu">
        <el-menu default-active="1" class="el-menu-vertical-demo" @select="handleMenuSelect">
          <el-menu-item index="1">员工信息</el-menu-item>
          <el-menu-item index="2">任务</el-menu-item>
        </el-menu>
      </el-aside>
      <el-main>
        <div class="top-bar">
          <div class="top-bar-left" v-if="activeTab === '1'">
            <!-- 新增员工按钮 -->
            <el-button 
              type="primary" 
              icon="Plus" 
              @click="handleAdd"
              style="margin-bottom: 12px;"
            >
              新增员工
            </el-button>
          </div>
          <div class="top-bar-left" v-else>
            <!-- 新增任务按钮 -->
            <el-button 
              type="primary" 
              icon="Plus" 
              @click="handleAddTask"
              style="margin-bottom: 12px;"
            >
              新增任务
            </el-button>
          </div>
          <div class="top-bar-right">
            <el-dropdown trigger="click" @command="handleUserCommand">
              <span class="user-trigger">
                <!-- 修复：使用 el-icon 包裹图标组件，替代 icon 属性绑定 -->
                <el-avatar :size="36">
                  <el-icon :size="24"><UserFilled /></el-icon>
                </el-avatar>
                <span class="username">{{ userName || '用户' }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="changePwd">修改密码</el-dropdown-item>
                  <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>

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

        <!-- 新增/编辑员工弹窗 -->
        <el-dialog 
          v-model="empDialogVisible" 
          :title="isEdit ? '编辑员工' : '新增员工'" 
          width="500px"
          destroy-on-close
        >
          <el-form ref="empFormRef" :model="empForm" :rules="empRules" label-width="100px">
            <el-form-item label="员工ID" prop="empId" v-if="isEdit">
              <el-input v-model="empForm.empId" disabled placeholder="员工ID" />
            </el-form-item>
            <el-form-item label="姓名" prop="name">
              <el-input v-model="empForm.name" placeholder="请输入员工姓名" clearable />
            </el-form-item>
            <el-form-item label="部门" prop="departmentName">
              <el-input v-model="empForm.departmentName" placeholder="请输入部门" clearable />
            </el-form-item>
            <!-- 工资输入框优化：添加步长、精度限制，支持小数 -->
            <el-form-item label="工资" prop="salary">
              <el-input 
                v-model.number="empForm.salary" 
                type="number" 
                placeholder="请输入工资（支持两位小数）" 
                clearable
                step="0.01" 
                precision="2"
              />
              <div class="form-tip">提示：支持整数或两位小数，如 5000 或 5000.50</div>
            </el-form-item>
            <el-form-item label="经理姓名" prop="managerName">
              <el-input v-model="empForm.managerName" placeholder="请输入经理姓名" clearable />
            </el-form-item>
          </el-form>
          <template #footer>
            <span class="dialog-footer">
              <el-button @click="empDialogVisible = false">取消</el-button>
              <el-button type="primary" :loading="empSubmitting" @click="submitEmpForm">确认</el-button>
            </span>
          </template>
        </el-dialog>

        <!-- 新增/编辑任务弹窗 -->
        <el-dialog 
          v-model="taskDialogVisible" 
          :title="isTaskEdit ? '编辑任务' : '新增任务'" 
          width="500px"
          destroy-on-close
        >
          <el-form ref="taskFormRef" :model="taskForm" :rules="taskRules" label-width="100px">
            <el-form-item label="任务ID" prop="id" v-if="isTaskEdit">
              <el-input v-model="taskForm.id" disabled placeholder="任务ID" />
            </el-form-item>
            <el-form-item label="创建员工ID" prop="createEmpId">
              <el-input 
                v-model="taskForm.createEmpId" 
                type="number" 
                placeholder="请输入创建员工ID" 
                clearable
                :disabled="isTaskEdit"
              />
            </el-form-item>
            <el-form-item label="任务内容" prop="content">
              <el-input 
                v-model="taskForm.content" 
                type="textarea" 
                rows="4"
                placeholder="请输入任务内容" 
                clearable
              />
            </el-form-item>
            <el-form-item label="创建日期" prop="createDate" v-if="isTaskEdit">
              <el-input v-model="taskForm.createDate" disabled placeholder="创建日期" />
            </el-form-item>
          </el-form>
          <template #footer>
            <span class="dialog-footer">
              <el-button @click="taskDialogVisible = false">取消</el-button>
              <el-button type="primary" :loading="taskSubmitting" @click="submitTaskForm">确认</el-button>
            </span>
          </template>
        </el-dialog>

        <div v-if="activeTab === '1'">
          <h2>员工信息</h2>
          <el-table :data="employeeData" style="width: 100%" border>
            <el-table-column prop="empId" label="ID" width="50"></el-table-column>
            <el-table-column prop="name" label="姓名"></el-table-column>
            <el-table-column prop="departmentName" label="部门"></el-table-column>
            <!-- 工资列格式化展示 -->
            <el-table-column label="工资" width="120">
              <template #default="scope">
                {{ formatSalary(scope.row.salary) }}
              </template>
            </el-table-column>
            <el-table-column prop="managerName" label="经理"></el-table-column>
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <!-- 编辑按钮 -->
                <el-button 
                  type="primary" 
                  circle 
                  size="small"
                  @click="handleEdit(scope.row)"
                  title="编辑"
                  style="background: #409eff; border-color: #409eff;"
                >
                  <el-icon color="#fff" size="18"><Edit /></el-icon>
                </el-button>
                <!-- 删除按钮 -->
                <el-button 
                  type="danger" 
                  circle 
                  size="small"
                  @click="handleDelete(scope.row)"
                  title="删除"
                  style="margin-left: 8px;"
                >
                  <el-icon color="#fff" size="18"><Delete /></el-icon>
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div v-else>
          <h2>任务</h2>
          <el-table :data="taskData" style="width: 100%" border>
            <el-table-column prop="id" label="任务ID" width="80"></el-table-column>
            <el-table-column prop="createEmpId" label="创建员工Id" width="100"></el-table-column>
            <el-table-column prop="content" label="任务内容"></el-table-column>
            <el-table-column prop="createDate" label="创建日期"></el-table-column>
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <!-- 编辑任务按钮 -->
                <el-button 
                  type="primary" 
                  circle 
                  size="small"
                  @click="handleTaskEdit(scope.row)"
                  title="编辑"
                  style="background: #409eff; border-color: #409eff;"
                >
                  <el-icon color="#fff" size="18"><Edit /></el-icon>
                </el-button>
                <!-- 删除任务按钮 -->
                <el-button 
                  type="danger" 
                  circle 
                  size="small"
                  @click="handleTaskDelete(scope.row)"
                  title="删除"
                  style="margin-left: 8px;"
                >
                  <el-icon color="#fff" size="18"><Delete /></el-icon>
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import http from "../api/http";
// 导入所需图标
import { UserFilled, Edit, Delete, Plus } from "@element-plus/icons-vue";

export default {
  name: "Employee",
  // 关键修复：在components中注册所有图标组件
  components: {
    UserFilled,
    Edit,
    Delete,
    Plus
  },
  data() {
    return {
      activeTab: "1",
      empId: "",
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
          {
            pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,12}$/,
            message: "用户名需为8-12位数字+字母",
            trigger: "blur",
          },
        ],
        userPwd: [
          { required: true, message: "请输入旧密码", trigger: "blur" },
          {
            pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,12}$/,
            message: "旧密码需为6-12位数字+字母",
            trigger: "blur",
          },
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

      // 员工新增/编辑相关
      empDialogVisible: false, // 员工弹窗显示状态
      isEdit: false, // 是否为编辑状态
      empSubmitting: false, // 提交加载状态
      empForm: { // 员工表单数据
        empId: "",
        name: "",
        departmentName: "",
        salary: "", // 初始值为空，后续通过v-model.number转为数字
        managerName: ""
      },
      empRules: { // 员工表单校验规则（优化工资校验）
        name: [
          { required: true, message: "请输入员工姓名", trigger: "blur" },
          { min: 2, max: 10, message: "姓名长度需在2-10个字符之间", trigger: "blur" }
        ],
        departmentName: [
          { required: true, message: "请输入部门", trigger: "blur" }
        ],
        salary: [
          { required: true, message: "请输入工资", trigger: "blur" },
        ],
        managerName: [
          { required: true, message: "请输入经理姓名", trigger: "blur" }
        ]
      },

      // 任务新增/编辑相关
      taskDialogVisible: false, // 任务弹窗显示状态
      isTaskEdit: false, // 是否为任务编辑状态
      taskSubmitting: false, // 任务提交加载状态
      taskForm: { // 任务表单数据
        id: "",
        createEmpId: "",
        content: "",
        createDate: ""
      },
      taskRules: { // 任务表单校验规则
        createEmpId: [
          { required: true, message: "请输入创建员工ID", trigger: "blur" },
        ],
        content: [
          { required: true, message: "请输入任务内容", trigger: "blur" },
          { min: 5, max: 500, message: "任务内容长度需在5-500个字符之间", trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    // 格式化工资展示（解决识别/显示问题）
    formatSalary(salary) {
      if (salary === null || salary === undefined || salary === "") {
        return "0.00";
      }
      // 转换为数字（处理后端返回的字符串类型工资）
      const num = Number(salary);
      if (isNaN(num)) {
        return "0.00";
      }
      // 保留两位小数，统一格式
      return num.toFixed(2);
    },

    // ===== 员工相关方法 =====
    // 新增员工
    handleAdd() {
      // 重置表单
      this.empForm = {
        empId: "",
        name: "",
        departmentName: "",
        salary: "",
        managerName: ""
      };
      this.isEdit = false;
      this.empDialogVisible = true;
      // 下次DOM更新后清除校验
      this.$nextTick(() => {
        if (this.$refs.empFormRef) {
          this.$refs.empFormRef.clearValidate();
        }
      });
    },
    // 编辑员工（优化工资赋值，处理类型转换）
    handleEdit(row) {
      // 赋值表单数据，工资转为数字（解决字符串类型问题）
      this.empForm = {
        ...row,
        salary: row.salary ? Number(row.salary) : ""
      };
      this.isEdit = true;
      this.empDialogVisible = true;
      // 下次DOM更新后清除校验
      this.$nextTick(() => {
        if (this.$refs.empFormRef) {
          this.$refs.empFormRef.clearValidate();
        }
      });
    },
    // 删除员工
    handleDelete(row) {
      // 二次确认弹窗
      this.$confirm('此操作将永久删除该员工, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        // 调用删除接口
        try {
          await http.delete(`/admin/emp/${row.empId}`);
          this.$message.success('删除成功!');
          // 重新获取数据
          this.fetchEmployeeById();
        } catch (error) {
          this.$message.error('删除失败!');
          console.error(error);
        }
      }).catch(() => {
        this.$message.info('已取消删除');
      });
    },
    // 提交员工表单（新增/编辑，优化工资提交格式）
    async submitEmpForm() {
      if (!this.$refs.empFormRef) return;

      // 表单校验
      try {
        await this.$refs.empFormRef.validate();
      } catch (error) {
        return;
      }

      this.empSubmitting = true;
      try {
        // 处理工资数据：保留两位小数后提交（匹配后端存储规则）
        const submitForm = {
          ...this.empForm,
          salary: this.empForm.salary ? Number(this.empForm.salary).toFixed(2) : this.empForm.salary
        };

        if (this.isEdit) {
          // 编辑员工：调用PUT接口
          await http.put("/admin/emp", submitForm);
          this.$message.success('编辑员工成功!');
        } else {
          // 新增员工：调用POST接口
          await http.post('/admin/emp', submitForm);
          this.$message.success('新增员工成功!');
        }
        // 关闭弹窗并刷新数据
        this.empDialogVisible = false;
        this.fetchEmployeeById();
      } catch (error) {
        this.$message.error(this.isEdit ? '编辑员工失败!' : '新增员工失败!');
        console.error(error);
      } finally {
        this.empSubmitting = false;
      }
    },

    // ===== 任务相关方法 =====
    // 新增任务
    handleAddTask() {
      // 重置任务表单，默认填充当前员工ID
      this.taskForm = {
        id: "",
        createEmpId: this.empId || "", // 默认使用当前登录员工ID
        content: "",
        createDate: ""
      };
      this.isTaskEdit = false;
      this.taskDialogVisible = true;
      // 下次DOM更新后清除校验
      this.$nextTick(() => {
        if (this.$refs.taskFormRef) {
          this.$refs.taskFormRef.clearValidate();
        }
      });
    },
    // 编辑任务
    handleTaskEdit(row) {
      // 赋值任务表单数据
      this.taskForm = { ...row };
      this.isTaskEdit = true;
      this.taskDialogVisible = true;
      // 下次DOM更新后清除校验
      this.$nextTick(() => {
        if (this.$refs.taskFormRef) {
          this.$refs.taskFormRef.clearValidate();
        }
      });
    },
    // 删除任务
    handleTaskDelete(row) {
      // 二次确认弹窗
      this.$confirm('此操作将永久删除该任务, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        // 调用删除接口
        try {
          await http.delete(`/admin/task/${row.id}`);
          this.$message.success('删除任务成功!');
          // 重新获取数据
          this.fetchTaskData();
        } catch (error) {
          this.$message.error('删除任务失败!');
          console.error(error);
        }
      }).catch(() => {
        this.$message.info('已取消删除');
      });
    },
    // 提交任务表单（新增/编辑）
    async submitTaskForm() {
      if (!this.$refs.taskFormRef) return;

      // 表单校验
      try {
        await this.$refs.taskFormRef.validate();
      } catch (error) {
        return;
      }

      this.taskSubmitting = true;
      try {
        if (this.isTaskEdit) {
          // 编辑任务：调用PUT接口
          await http.put("/admin/task", this.taskForm);
          this.$message.success('编辑任务成功!');
        } else {
          // 新增任务：调用POST接口
          await http.post('/admin/task', this.taskForm);
          this.$message.success('新增任务成功!');
        }
        // 关闭弹窗并刷新数据
        this.taskDialogVisible = false;
        this.fetchTaskData();
      } catch (error) {
        this.$message.error(this.isTaskEdit ? '编辑任务失败!' : '新增任务失败!');
        console.error(error);
      } finally {
        this.taskSubmitting = false;
      }
    },

    // ===== 通用方法 =====
    // 处理用户下拉命令
    handleUserCommand(command) {
      if (command === "changePwd") {
        this.openChangePwdDialog();
      } else if (command === "logout") {
        this.logout();
      }
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
    // 处理菜单选择
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
        const response = await http.get("/admin/emp");
        this.employeeData = response.data.data.emps;
      } catch (error) {
        console.error('获取员工信息失败', error);
        this.$message.error('获取员工信息失败');
      }
    },
    // 获取任务信息
    async fetchTaskData() {
      try {
        const response = await http.get(`/admin/task`);
        this.taskData = response.data.data.task;
      } catch (error) {
        console.error('获取任务信息失败', error);
        this.$message.error('获取任务信息失败');
      }
    },
  },
  async mounted() {
    // 从本地存储获取用户信息
    const userInfo = JSON.parse(localStorage.getItem("userInfo"));
    if (userInfo && userInfo.empId) {
      this.empId = userInfo.empId;
      this.userName = userInfo.userName || "";
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
  display: flex;
}

.menu {
  background-color: #f5f5f5;
  border-right: 1px solid #dcdcdc;
}

.el-main {
  padding: 20px;
  overflow-y: auto;
}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.top-bar-right {
  display: flex;
  justify-content: flex-end;
}

.user-trigger {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  user-select: none;
}

.username {
  color: #303133;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

/* 工资输入框提示文字样式 */
.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}
</style>