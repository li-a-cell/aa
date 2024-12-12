<template>
  <div class="user-management">
    <h2>用户管理</h2>
    <!-- 搜索框与筛选条件 -->
    <div class="search-bar">
      <input
          v-model="searchTerm"
          @input="filterUsers"
          placeholder="搜索用户（姓名、职位类型、部门）"
          class="search-input"
      />
      <select v-model="filterRole" @change="filterUsers" class="select-box">
        <option value="">全部职位类型</option>
        <option value="检查人员">检查人员</option>
        <option value="招标人员">招标人员</option>
        <option value="项目经理">项目经理</option>
        <option value="后台管理员">后台管理员</option>
      </select>
    </div>
    <!-- 添加用户按钮 -->
    <div class="add-user-button-container">
      <button @click="openAddUserForm" class="add-user-button">添加新用户</button>
    </div>
    <!-- 添加或编辑用户表单 -->
    <div v-if="showUserForm" class="modal-overlay">
      <div class="user-form-popup">
        <h3>{{ currentUser ? '编辑用户' : '添加新用户' }}</h3>
        <form @submit.prevent="submitForm">
          <div class="form-row">
            <div class="form-col">
              <label for="name">姓名</label>
              <input v-model="formData.name" type="text" placeholder="用户姓名" @blur="validateField('name')" />
              <span v-if="errors.name" class="error">{{ errors.name }}</span>
            </div>
            <div class="form-col">
              <label for="account">账号</label>
              <input v-model="formData.account" type="text" placeholder="用户账号" @blur="validateField('account')" />
              <span v-if="errors.account" class="error">{{ errors.account }}</span>
            </div>
          </div>
          <div class="form-row">
            <div class="form-col">
              <label for="phone">手机号</label>
              <input v-model="formData.phoneNumber" type="text" placeholder="用户手机号" @blur="validateField('phoneNumber')" />
              <span v-if="errors.phoneNumber" class="error">{{ errors.phoneNumber }}</span>
            </div>
            <div class="form-col">
              <label for="password">密码</label>
              <div class="password-container">
                <input v-model="formData.password" :type="passwordVisible ? 'text' : 'password'" placeholder="用户密码" @blur="validateField('password')" />
                <span v-if="errors.password" class="error">{{ errors.password }}</span>
                <button type="button" @click="togglePasswordVisibility" class="password-toggle-button">{{ passwordVisible ? '隐藏' : '显示' }}</button>
              </div>
            </div>
          </div>
          <div class="form-row">
            <div class="form-col">
              <label for="position">职位类型</label>
              <select v-model="formData.jobType" @blur="validateField('jobType')">
                <option value="检查人员">检查人员</option>
                <option value="招标人员">招标人员</option>
                <option value="项目经理">项目经理</option>
                <option value="后台管理员">后台管理员</option>
              </select>
              <span v-if="errors.jobType" class="error">{{ errors.jobType }}</span>
            </div>
            <div class="form-col">
              <label for="gender">性别</label>
              <select v-model="formData.gender" @blur="validateField('gender')">
                <option value="男">男</option>
                <option value="女">女</option>
              </select>
              <span v-if="errors.gender" class="error">{{ errors.gender }}</span>
            </div>
          </div>
          <div class="form-row">
            <div class="form-col">
              <label for="salary">薪水</label>
              <input v-model="formData.salary" type="number" placeholder="薪水" @blur="validateField('salary')" />
              <span v-if="errors.salary" class="error">{{ errors.salary }}</span>
            </div>
            <div class="form-col">
              <label for="address">家庭住址</label>
              <input v-model="formData.address" type="text" placeholder="家庭住址" @blur="validateField('address')" />
              <span v-if="errors.address" class="error">{{ errors.address }}</span>
            </div>
          </div>
          <div class="form-col">
            <label for="startDate">入职日期</label>
            <input v-model="formData.hireDate" type="date" @blur="validateField('hireDate')" />
            <span v-if="errors.hireDate" class="error">{{ errors.hireDate }}</span>
          </div>
          <div class="form-row">
            <div class="form-col">
              <label for="birthDate">出生年月</label>
              <input v-model="formData.birthDate" type="date" @blur="validateField('birthDate')" />
              <span v-if="errors.birthDate" class="error">{{ errors.birthDate }}</span>
            </div>
          </div>
          <!-- 根据当前是编辑还是添加状态显示不同按钮文本 -->
          <button type="submit" class="common-button submit-button">{{ currentUser ? '确认修改' : '确认添加' }}</button>
          <button @click="closeUserForm" type="button" class="common-button cancel-button">取消</button>
        </form>
      </div>
    </div>
    <!-- 用户列表展示 -->
    <table class="user-table">
      <thead>
      <tr>
        <th>姓名</th>
        <th>账号</th>
        <th>密码</th>
        <th>性别</th>
        <th>职位类型</th>
        <th>手机号</th>
        <th>薪水</th>
        <th>年龄</th>
        <th>家庭住址</th>
        <th>入职日期</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(user, index) in paginatedUsers" :key="user.id">
        <td>{{ user.name }}</td>
        <td>{{ user.account }}</td>
        <td>{{ user.password }}</td>
        <td>{{ user.gender }}</td>
        <td>{{ user.jobType }}</td>
        <td>{{ user.phoneNumber }}</td>
        <td>{{ user.salary }}</td>
        <td>{{ calculateAge(user.birthDate) }}</td>
        <td>{{ user.address }}</td>
        <td>{{ user.hireDate }}</td>
        <td>
          <button @click="editUserDetails(user)" class="common-button edit-button">编辑</button>
          <button @click="confirmDelete(user.employee_id)" class="common-button delete-button">删除</button>
        </td>
      </tr>
      </tbody>
    </table>
    <!-- 分页 -->
    <div class="pagination">
      <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1" class="common-button prev-button">上一页</button>
      <span>第 {{ currentPage }} 页</span>
      <button @click="changePage(currentPage + 1)" :disabled="currentPage * pageSize >= filteredUsers.length" class="common-button next-button">下一页</button>
    </div>
  </div>
</template>

<script>
import {ref, computed, onMounted} from 'vue';
import Swal from 'sweetalert2';
import axios from 'axios';

export default {
  name: 'UserManagement',
  setup() {
    const searchTerm = ref('');
    const filterRole = ref('');
    const currentPage = ref(1);
    const pageSize = ref(5);
    const users = ref([]);
    const showUserForm = ref(false);
    const currentUser = ref(null);
    const passwordVisible = ref(false);
    const formData = ref({
      name: '',
      account: '',
      phoneNumber: '',
      password: '',
      jobType: '',
      gender: '',
      salary: '',
      birthDate: '',
      hireDate: '',
      address: '',
    });
    const errors = ref({
      name: '',
      account: '',
      phoneNumber: '',
      password: '',
      jobType: '',
      gender: '',
      salary: '',
      birthDate: '',
      hireDate: '',
      address: '',
    });

    const validateField = (field) => {
      switch (field) {
        case 'name':
          errors.value.name = formData.value.name.trim() === '' ? '姓名是必填项' : '';
          break;
        case 'account':
          errors.value.account = formData.value.account.trim() === '' ? '账号是必填项' : '';
          break;
        case 'phoneNumber':
          errors.value.phoneNumber =
              !/^\d{11}$/.test(formData.value.phoneNumber)
                  ? '手机号格式不正确，必须为11位数字'
                  : '';
          break;
        case 'password':
          errors.value.password = formData.value.password.trim() === '' ? '密码是必填项' : '';
          break;
        case 'jobType':
          errors.value.jobType = formData.value.jobType === '' ? '职位类型是必填项' : '';
          break;
        case 'gender':
          errors.value.gender = formData.value.gender === '' ? '性别是必填项' : '';
          break;
        case 'salary':
          errors.value.salary =
              formData.value.salary === '' || formData.value.salary < 0
                  ? '薪水是必填项且必须为正数'
                  : '';
          break;
        case 'address':
          errors.value.address = formData.value.address.trim() === '' ? '地址是必填项' : '';
          break;
        case 'hireDate':
          errors.value.hireDate = formData.value.hireDate === '' ? '入职日期是必填项' : '';
          break;
        case 'birthDate':
          errors.value.birthDate = formData.value.birthDate === '' ? '出生年月是必填项' : '';
          break;
        default:
          break;
      }
    };

    const validateForm = () => {
      Object.keys(formData.value).forEach((field) => validateField(field));
      return !Object.values(errors.value).some((error) => error !== '');
    };

    const token = localStorage.getItem('jwtToken');
    // 获取用户数据的函数，通过GET请求获取所有用户数据
    const fetchUsers = async () => {
      try {
        const response = await axios.get('/api/administrator/employees', {
          headers: {
            'token': token
          }
        });
        if (response.data && response.data.data) {
          users.value = response.data.data;
          console.log(users.value);
        } else {
          Swal.fire('错误', '后端返回数据格式不符合预期，请检查接口', 'error');
        }
      } catch (error) {
        Swal.fire('错误', '获取用户数据出错：' + error.message, 'error');
      }
    };

    const filteredUsers = computed(() => {
      return users.value.filter((user) => {
        return (
            (!searchTerm.value || user.name.toLowerCase().includes(searchTerm.value.toLowerCase())) &&
            (!filterRole.value || user.jobType === filterRole.value)
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
        phoneNumber: '',
        password: '',
        jobType: '',
        gender: '',
        salary: '',
        birthDate: '',
        hireDate: '',
        address: '',
      };
      errors.value = {
        name: '',
        account: '',
        phoneNumber: '',
        password: '',
        jobType: '',
        gender: '',
        salary: '',
        birthDate: '',
        hireDate: '',
        address: '',
      };
      console.log(formData.value.phone_number );
    };

    // 关闭表单
    const closeUserForm = () => {
      showUserForm.value = false;
    };

    // 切换密码可见性
    const togglePasswordVisibility = () => {
      passwordVisible.value = !passwordVisible.value;
    };

    // 提交表单
    const submitForm = async () => {
      if (!validateForm()) {
        Swal.fire('错误', '请修正表单中的错误后再提交', 'error');
        return;
      }

      try {
        if (currentUser.value) {
          // 编辑用户，调用更新接口，添加employee_id到请求参数中
          const updatedUser = {...formData.value};
          const {employeeId} = currentUser.value;
          console.log(currentUser.value);
          const response = await axios.put(`/api/administrator/updateemployee/${employeeId}`, updatedUser, {
            headers: {
              'token': token
            }
          });
          // 前端更新本地数据
          const index = users.value.findIndex(user => user.employeeId === currentUser.value.employeeId);
          if (index !== -1) {
            users.value.splice(index, 1, updatedUser);
          }
          Swal.fire('成功', '用户信息已更新', 'success');

        } else {
          // 添加用户，调用添加接口
          const newUser = {...formData.value};
          console.log(newUser);
          const response = await axios.post('/api/administrator/createemployee', newUser, {
            headers: {
              'token': token
            }
          });
          users.value.push(newUser);
          Swal.fire('成功', '新用户已添加', 'success');
        }
      } catch (error) {
        Swal.fire('错误', '提交表单出错：' + error.message, 'error');
      } finally {
        showUserForm.value = false;
        // 重新获取数据刷新列表，可根据业务需求决定是否重置页码等
        await fetchUsers();
      }
    };

    // 编辑用户
    const editUserDetails = (user) => {
      currentUser.value = user;
      formData.value = {...user};
      errors.value = {
        name: '',
        account: '',
        phoneNumber: '',
        password: '',
        jobType: '',
        gender: '',
        salary: '',
        birthDate: '',
        hireDate: '',
        address: '',
      };
      showUserForm.value = true;
    };

    // 删除用户
    const confirmDelete = (employeeId) => {
      Swal.fire({
        title: '确认删除?',
        text: '删除后将无法恢复!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '删除',
        cancelButtonText: '取消',
      }).then((result) => {
        if (result.isConfirmed) {
          // 这里使用 fetch 发起 DELETE 请求
          fetch(`/api/administrator/deleteemployee/${parseInt(employeeId)}`, {
            method: 'DELETE',
            headers: {
              'Content-Type': 'application/json',
              'token': token
            },
          })
              .then(response => {
                if (!response.ok) {
                  throw new Error('删除失败，请稍后再试');
                }
                return response.json();
              })
              .then(() => {
                // 前端删除本地数据
                users.value = users.value.filter(user => user.employeeId !== employeeId);
                Swal.fire('已删除', '用户已被删除', 'success');
                // 重新获取数据刷新列表
                fetchUsers();
              })
              .catch((error) => {
                Swal.fire('删除失败', error.message, 'error');
              });
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
      fetchUsers();
    };

    onMounted(async () => {
      await fetchUsers();
    });

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
      passwordVisible,
      formData,
      errors,
      openAddUserForm,
      closeUserForm,
      togglePasswordVisibility,
      submitForm,
      editUserDetails,
      confirmDelete,
      calculateAge,
      changePage,
      validateField,
    };
  }
};
</script>

<style scoped>
/* 样式保持不变 */
/* 用户管理整体区域样式 */
.user-management {
  padding: 30px;
  width: 100%;
  box-sizing: border-box;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #f4f6f9;
}
/* 搜索框与筛选条件所在区域的样式 */
.search-bar {
  width: 100%;
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}
.search-input {
  flex: 3;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.select-box {
  flex: 1;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
  background-color: #ffffff;
}
/* 添加用户按钮相关样式 */
.add-user-button-container {
  margin-bottom: 20px;
  text-align: right;
}
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 999;
  display: flex;
  justify-content: center;
  align-items: center;
}
.user-form-popup {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 800px;
  max-width: 90%;
  height: auto;
  padding: 40px;
  background: linear-gradient(135deg, #ffffff, #f3f4f6);
  border-radius: 20px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
  z-index: 1000;
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.user-form-popup h3 {
  font-size: 24px;
  color: #222;
  text-align: center;
  margin-bottom: 20px;
  font-weight: 600;
  letter-spacing: 1px;
}
.user-form-popup form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}
/* 表单行样式 */
.form-row {
  display: flex;
  justify-content: space-between;
  gap: 20px;
}
/* 表单列样式 */
.form-col {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.form-col label {
  font-weight: 600;
  font-size: 16px;
  color: #444;
  margin-bottom: 5px;
}
.form-col input,
.form-col select {
  width: 100%;
  padding: 12px 16px;
  border-radius: 12px;
  border: 1px solid #ccc;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.1);
  font-size: 14px;
  color: #333;
  background-color: #f8f8f8;
  transition: border-color 0.3s ease, box-shadow 0.3s ease, background-color 0.3s ease;
}
.form-col input:focus,
.form-col select:focus {
  border-color: #007bff;
  box-shadow: 0 4px 8px rgba(0, 123, 255, 0.2);
  background-color: #ffffff;
  outline: none;
}
.form-col input::placeholder {
  color: #aaa;
  font-style: italic;
}
.submit-button {
  background: linear-gradient(90deg, #007bff, #0056b3);
  color: #ffffff;
  border: none;
  border-radius: 10px;
  padding: 15px 30px;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
  transition: background 0.3s ease, transform 0.2s ease;
}
.submit-button:hover {
  background: linear-gradient(90deg, #0056b3, #003f7f);
  transform: translateY(-3px);
}
.cancel-button {
  background: linear-gradient(90deg, #e0e0e0, #bbbbbb);
  color: #333;
  border: none;
  border-radius: 10px;
  padding: 15px 30px;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
  transition: background 0.3s ease, transform 0.2s ease;
}
.cancel-button:hover {
  background: linear-gradient(90deg, #bbbbbb, #999999);
  transform: translateY(-3px);
}
.user-form-popup__buttons {
  display: flex;
  justify-content: space-between;
  gap: 10px;
}
.add-user-button {
  padding: 15px 30px;
  background: linear-gradient(90deg, #007bff, #0056b3);
  color: #ffffff;
  border: none;
  border-radius: 10px;
  font-weight: bold;
  cursor: pointer;
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.15);
  transition: background 0.3s ease, transform 0.2s ease;
}
.add-user-button:hover {
  background: linear-gradient(90deg, #0056b3, #003f7f);
  transform: translateY(-2px);
}
/* 用户列表表格的整体样式 */
.user-table {
  width: 100%;
  border-collapse: collapse;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: #ffffff;
}
.user-table th {
  background-color: #007bff;
  color: #ffffff;
  text-transform: uppercase;
  padding: 15px;
  text-align: left;
}
.user-table td {
  padding: 15px;
  border: 1px solid #eee;
  text-align: left;
}
.user-table tr:nth-child(even) {
  background-color: #f9f9f9;
}
.user-table td button {
  padding: 10px;
  cursor: pointer;
  border: none;
  border-radius: 5px;
  background-color: #007bff;
  color: #ffffff;
  transition: background-color 0.3s ease, transform 0.2s ease;
  margin-right: 10px;
}
.user-table td button:last-child {
  margin-right: 0;
}
.edit-button {
  background-color: #007bff;
  color: #ffffff;
  border: none;
  border-radius: 5px;
  padding: 10px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}
.edit-button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}
.delete-button {
  background-color: #ff4d4d;
  color: #ffffff;
  border: none;
  border-radius: 5px;
  padding: 10px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}
.delete-button:hover {
  background-color: #cc0000;
  transform: translateY(-2px);
}
/* 分页区域的样式 */
.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
  gap: 15px;
}
.prev-button {
  padding: 10px 15px;
  background-color: #007bff;
  color: #ffffff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}
.prev-button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}
.next-button {
  padding: 10px 15px;
  background-color: #007bff;
  color: #ffffff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}
.next-button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}
/* 错误提示信息的样式 */
.error {
  color: #ff4d4d;
  font-size: 0.9em;
  margin-top: 5px;
}
.password-container {
  position: relative;
}
.password-toggle-button {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #007bff;
  cursor: pointer;
}
.password-toggle-button:hover {
  color: #0056b3;
}
</style>
