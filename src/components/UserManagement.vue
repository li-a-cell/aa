<template>
  <div class="user-management">
    <h2>用户管理</h2>

    <!-- 搜索框与筛选条件 -->
    <div class="search-bar">
      <el-input
          v-model="searchTerm"
          @input="filterUsers"
          placeholder="搜索用户（姓名、职位类型、部门）"
          clearable
          style="flex: 3;"
      />
      <el-select v-model="filterRole" @change="filterUsers" style="flex: 1;">
        <el-option label="全部职位类型" value=""></el-option>
        <el-option label="检查人员" value="检查人员"></el-option>
        <el-option label="招标人员" value="招标人员"></el-option>
        <el-option label="项目经理" value="项目经理"></el-option>
        <el-option label="后台管理员" value="后台管理员"></el-option>
      </el-select>
    </div>

    <!-- 添加用户按钮 -->
    <div class="add-user-button-container">
      <el-button @click="openAddUserForm" type="primary">添加新用户</el-button>
    </div>

    <!-- 添加或编辑用户表单 -->
    <el-dialog :visible.sync="showUserForm" :title="currentUser ? '编辑用户' : '添加新用户'">
      <el-form :model="formData" :rules="formRules" ref="form" label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="formData.name" placeholder="用户姓名" />
        </el-form-item>
        <el-form-item label="账号" prop="account">
          <el-input v-model="formData.account" placeholder="用户账号" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="formData.phone" placeholder="用户手机号" />
        </el-form-item>
        <el-form-item label="职位类型" prop="position">
          <el-select v-model="formData.position" placeholder="请选择职位类型">
            <el-option label="检查人员" value="检查人员"></el-option>
            <el-option label="招标人员" value="招标人员"></el-option>
            <el-option label="项目经理" value="项目经理"></el-option>
            <el-option label="后台管理员" value="后台管理员"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="formData.gender" placeholder="请选择性别">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="薪水" prop="salary">
          <el-input v-model="formData.salary" type="number" placeholder="薪水" />
        </el-form-item>
        <el-form-item label="入职日期" prop="startDate">
          <el-date-picker v-model="formData.startDate" type="date" placeholder="入职日期" />
        </el-form-item>
        <el-form-item label="出生年月" prop="birthDate">
          <el-date-picker v-model="formData.birthDate" type="date" placeholder="出生年月" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeUserForm">取消</el-button>
        <el-button type="primary" @click="submitForm">{{ currentUser ? '确认修改' : '确认添加' }}</el-button>
      </span>
    </el-dialog>

    <!-- 用户列表展示 -->
    <el-table :data="paginatedUsers" style="width: 100%" border>
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="account" label="账号" />
      <el-table-column prop="password" label="密码" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="position" label="职位类型" />
      <el-table-column prop="gender" label="性别" />
      <el-table-column prop="salary" label="薪水" />
      <el-table-column :label="'年龄'" :formatter="(row) => calculateAge(row.birthDate)" />
      <el-table-column prop="startDate" label="入职日期" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button @click="editUserDetails(scope.row)" size="small" type="primary">编辑</el-button>
          <el-button @click="confirmDelete(scope.row.id)" size="small" type="danger">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
        :current-page="currentPage"
        :page-size="pageSize"
        :total="filteredUsers.length"
        @current-change="changePage"
        layout="prev, pager, next"
    />
  </div>
</template>

<script>
import { ref, computed } from 'vue';
import Swal from 'sweetalert2';
import { useForm } from 'vee-validate';
import * as yup from 'yup';
import { ElInput, ElSelect, ElOption, ElButton, ElForm, ElFormItem, ElDatePicker, ElDialog, ElTable, ElTableColumn, ElPagination } from 'element-plus';

export default {
  name: 'UserManagement',
  components: {
    ElInput,
    ElSelect,
    ElOption,
    ElButton,
    ElForm,
    ElFormItem,
    ElDatePicker,
    ElDialog,
    ElTable,
    ElTableColumn,
    ElPagination,
  },
  setup() {
    const users = ref([
      {
        id: 1,
        name: '张三',
        account: '123456',
        password: '123456',
        phone: '12345678901',
        position: '项目经理',
        gender: '男',
        salary: 12000,
        birthDate: '1985-06-15',
        startDate: '2024-01-01',
      },
      {
        id: 2,
        name: '李四',
        account: '12345',
        password: '123456',
        phone: '19876543210',
        position: '招标人员',
        gender: '女',
        salary: 10000,
        birthDate: '1990-09-10',
        startDate: '2024-02-01',
      },
    ]);

    const searchTerm = ref('');
    const filterRole = ref('');
    const currentPage = ref(1);
    const pageSize = ref(5);

    const showUserForm = ref(false);
    const currentUser = ref(null);
    const formData = ref({
      name: '',
      account: '',
      phone: '',
      position: '',
      gender: '',
      salary: '',
      birthDate: '',
      startDate: '',
    });

    // 表单验证规则
    const formRules = {
      name: [{ required: true, message: '姓名是必填项', trigger: 'blur' }],
      account: [{ required: true, message: '账号是必填项', trigger: 'blur' }],
      phone: [
        { required: true, message: '手机号是必填项', trigger: 'blur' },
        { pattern: /^\d{11}$/, message: '手机号格式不正确', trigger: 'blur' },
      ],
      position: [{ required: true, message: '职位类型是必填项', trigger: 'change' }],
      gender: [{ required: true, message: '性别是必填项', trigger: 'change' }],
      salary: [{ required: true, message: '薪水是必填项', trigger: 'blur' }, { type: 'number', min: 0, message: '薪水必须为正数', trigger: 'blur' }],
      startDate: [{ required: true, message: '入职日期是必填项', trigger: 'change' }],
      birthDate: [{ required: true, message: '出生年月是必填项', trigger: 'change' }],
    };

    const filteredUsers = computed(() => {
      return users.value.filter((user) => {
        return (
            (!searchTerm.value || user.name.toLowerCase().includes(searchTerm.value.toLowerCase())) &&
            (!filterRole.value || user.position === filterRole.value)
        );
      });
    });

    const paginatedUsers = computed(() => {
      const startIndex = (currentPage.value - 1) * pageSize.value;
      const endIndex = startIndex + pageSize.value;
      return filteredUsers.value.slice(startIndex, endIndex);
    });

    // 打开添加用户表单
    const openAddUserForm = () => {
      showUserForm.value = true;
      currentUser.value = null;
      formData.value = {
        name: '',
        account: '',
        phone: '',
        position: '',
        gender: '',
        salary: '',
        birthDate: '',
        startDate: '',
      };
    };

    // 关闭表单
    const closeUserForm = () => {
      showUserForm.value = false;
    };

    // 提交表单
    const submitForm = () => {
      if (currentUser.value) {
        // 编辑用户
        Object.assign(currentUser.value, formData.value);
        Swal.fire('成功', '用户信息已更新', 'success');
      } else {
        // 添加用户
        const newUser = { ...formData.value, id: Date.now() };
        users.value.push(newUser);
        Swal.fire('成功', '新用户已添加', 'success');
      }
      showUserForm.value = false;
    };

    // 编辑用户
    const editUserDetails = (user) => {
      currentUser.value = user;
      formData.value = { ...user };
      showUserForm.value = true;
    };

    // 删除用户
    const confirmDelete = (userId) => {
      Swal.fire({
        title: '确认删除?',
        text: '删除后将无法恢复!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '删除',
        cancelButtonText: '取消',
      }).then((result) => {
        if (result.isConfirmed) {
          users.value = users.value.filter(user => user.id !== userId);
          Swal.fire('已删除', '用户已被删除', 'success');
        }
      });
    };

    // 计算年龄
    const calculateAge = (birthDate) => {
      const birthYear = new Date(birthDate).getFullYear();
      const currentYear = new Date().getFullYear();
      return currentYear - birthYear;
    };

    // 分页切换
    const changePage = (page) => {
      currentPage.value = page;
    };

    return {
      searchTerm,
      filterRole,
      currentPage,
      pageSize,
      users,
      filteredUsers,
      paginatedUsers,
      showUserForm,
      currentUser,
      formData,
      formRules,
      openAddUserForm,
      closeUserForm,
      submitForm,
      editUserDetails,
      confirmDelete,
      calculateAge,
      changePage,
    };
  },
};
</script>

<style scoped>
.user-management {
  padding: 30px;
  width: 100%;
  box-sizing: border-box;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #f4f6f9;
}

.search-bar {
  width: 100%;
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.add-user-button-container {
  margin-bottom: 20px;
  text-align: right;
}

.add-user-button-container .el-button {
  padding: 10px 20px;
  background-color: #007bff;
  color: #ffffff;
  border: none;
  border-radius: 5px;
  font-weight: bold;
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.add-user-button-container .el-button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}

.el-dialog__footer {
  display: flex;
  justify-content: flex-end;
}

.el-table .el-table__body-wrapper {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: #ffffff;
}

.el-table th {
  background-color: #007bff;
  color: #ffffff;
  text-transform: uppercase;
  padding: 15px;
  text-align: left;
}

.el-table td {
  padding: 15px;
  border: 1px solid #eee;
  text-align: left;
}

.el-table tr:nth-child(even) {
  background-color: #f9f9f9;
}

.el-pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
  gap: 15px;
}

.el-pagination .el-button {
  padding: 10px 15px;
  background-color: #007bff;
  color: #ffffff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.el-pagination .el-button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}

button {
  padding: 10px;
  cursor: pointer;
  border: none;
  border-radius: 5px;
  background-color: #007bff;
  color: #ffffff;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}

.error {
  color: #ff4d4d;
  font-size: 0.9em;
  margin-top: 5px;
}

.form-container {
  margin-bottom: 20px;
  padding: 20px;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  background-color: #ffffff;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
}

.form-field {
  margin-bottom: 15px;
}

.form-field label {
  font-weight: bold;
  margin-bottom: 5px;
  display: block;
}

.form-field input {
  width: 100%;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.05);
}
</style>
