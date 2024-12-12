<template>
  <div class="project-management">
    <h2>项目管理</h2>
    <!-- 搜索框与筛选条件 -->
    <div class="search-bar">
      <input
          type="text"
          v-model="searchTerm"
          @input="filterProjects"
          placeholder="搜索项目（名称、经理、施工地、施工方、状态）"
      />
      <select v-model="filterStatus" @change="filterProjects">
        <option value="">全部状态</option>
        <option value="待发布">待发布</option>
        <option value="待招标">待招标</option>
        <option value="施工中">施工中</option>
        <option value="已完成">已完成</option>
      </select>
    </div>

    <!-- 项目列表展示 -->
    <div class="project-list">
      <table>
        <thead>
        <tr>
          <th>项目名称</th>
          <th>项目经理</th>
          <th>计划开始日期</th>
          <th>计划结束日期</th>
          <th>施工地</th>
          <th>施工方</th>
          <th>预算</th>
          <th>状态</th>
          <th>项目类型</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(project, index) in paginatedProjects" :key="project.id">
          <td>{{ project.projectName }}</td>
          <td>{{ project.managerName }}</td>
          <td>{{ project.plannedStartDate }}</td>
          <td>{{ project.plannedEndDate }}</td>
          <td>{{ project.bidderName }}</td>
          <td>{{ project.siteName }}</td>
          <td>{{ project.budget }}</td>
          <td>{{ project.status }}</td>
          <td>{{ project.projectType }}</td>
          <td>
            <button @click="editProjectDetails(project)" class="common-button edit-button">编辑</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- 分页 -->
    <div class="pagination">
      <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1" class="common-button prev-button">上一页</button>
      <span>第 {{ currentPage }} 页</span>
      <button @click="changePage(currentPage + 1)" :disabled="currentPage * pageSize >= filteredProjects.length" class="common-button next-button">下一页</button>
    </div>

    <!-- 编辑项目弹窗 -->
    <div v-if="showEditProjectForm" class="modal-overlay">
      <div class="edit-project-form-popup">
        <h3>编辑项目</h3>
        <form @submit.prevent="submitEditForm">
          <div class="form-field">
            <label for="editProjectName">项目名称</label>
            <input v-model="editFormData.projectName" type="text" placeholder="项目名称" @blur="validateField('projectName')" />
            <span v-if="errors.projectName" class="error">{{ errors.projectName }}</span>
          </div>
          <div class="form-field">
            <label for="editManagerName">项目经理</label>
            <input v-model="editFormData.managerName" type="text" placeholder="项目经理" @blur="validateField('managerName')" />
            <span v-if="errors.managerName" class="error">{{ errors.managerName }}</span>
          </div>
          <div class="form-field">
            <label for="editPlannedStartDate">计划开始日期</label>
            <input v-model="editFormData.plannedStartDate" type="date" @blur="validateField('plannedStartDate')" />
            <span v-if="errors.plannedStartDate" class="error">{{ errors.plannedStartDate }}</span>
          </div>
          <div class="form-field">
            <label for="editPlannedEndDate">计划结束日期</label>
            <input v-model="editFormData.plannedEndDate" type="date" @blur="validateField('plannedEndDate')" />
            <span v-if="errors.plannedEndDate" class="error">{{ errors.plannedEndDate }}</span>
          </div>
          <div class="form-field">
            <label for="editProjectType">项目类型</label>
            <select v-model="editFormData.projectType" @blur="validateField('projectType')">
              <option value="市政工程">市政工程</option>
              <option value="房屋建筑">房屋建筑</option>
            </select>
            <span v-if="errors.projectType" class="error">{{ errors.projectType }}</span>
          </div>
          <div class="form-field">
            <label for="editBudget">预算</label>
            <input v-model="editFormData.budget" type="number" placeholder="预算" @blur="validateField('budget')" />
            <span v-if="errors.budget" class="error">{{ errors.budget }}</span>
          </div>
          <div class="form-field">
            <label for="editStatus">状态</label>
            <select v-model="editFormData.status" @blur="validateField('status')">
              <option value="待发布">待发布</option>
              <option value="待招标">待招标</option>
              <option value="施工中">施工中</option>
              <option value="已完成">已完成</option>
            </select>
            <span v-if="errors.status" class="error">{{ errors.status }}</span>
          </div>
          <button type="submit" class="common-button submit-button">确认修改</button>
          <button @click="closeEditProjectForm" type="button" class="common-button cancel-button">取消</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue';
import Swal from 'sweetalert2';
import axios from 'axios';

export default {
  name: 'ProjectManagement',
  setup() {
    const searchTerm = ref('');
    const filterStatus = ref('');
    const currentPage = ref(1);
    const pageSize = ref(6);
    const projects = ref([]);
    const totalProjects = ref(0);
    const showEditProjectForm = ref(false);
    const currentProject = ref(null);
    const editFormData = ref({});  // 这里先初始化为空对象，后续在编辑时赋值
    const errors = ref({
      projectName: '',
      managerName: '',
      plannedStartDate: '',
      plannedEndDate: '',
      projectType: '',
      budget: '',
      status: ''
    });

    const validateField = (field) => {
      switch (field) {
        case 'projectName':
          errors.value.projectName = editFormData.value.projectName.trim() === ''? '项目名称是必填项' : '';
          break;
        case 'managerName':
          errors.value.managerName = editFormData.value.managerName.trim() === ''? '项目经理是必填项' : '';
          break;
        case 'planned_start_date':
          errors.value.plannedStartDate = editFormData.value.plannedStartDate === ''? '计划开始日期是必填项' : '';
          break;
        case 'planned_end_date':
          errors.value.plannedEndDate = editFormData.value.plannedEndDate === ''? '计划结束日期是必填项' : '';
          break;
        case 'project_type':
          errors.value.projectType = editFormData.value.projectType.trim() === ''? '项目类型是必填项' : '';
          break;
        case 'budget':
          errors.value.budget = editFormData.value.budget === '' || editFormData.value.budget < 0? '预算是必填项且必须为正数' : '';
          break;
        case 'status':
          errors.value.status = editFormData.value.status === ''? '状态是必填项' : '';
          break;
        default:
          break;
      }
    };

    const validateForm = () => {
      Object.keys(editFormData.value).forEach((field) => validateField(field));
      return!Object.values(errors.value).some((error) => error!== '');
    };

    const token = localStorage.getItem('jwtToken');
    // 获取项目数据的函数，通过GET请求获取所有项目数据
    const fetchProjects = async () => {
      if (!token) {
        console.error('未获取到有效的token，请先登录');
        return;
      }
      try {
        const response = await axios.get('/api/project/all', {
          params: {
            page: currentPage.value,
            pageSize: pageSize.value,
            searchTerm: searchTerm.value,
            filterStatus: filterStatus.value
          },
          headers: {
            'token': token
          }
        });
        if (response.data && response.data.data) {
          projects.value = response.data.data;
          totalProjects.value = response.data.data.length;
        } else {
          console.error('后端返回数据格式不符合预期，请检查接口');
        }
      } catch (error) {
        console.error('获取项目数据出错：', error);
      }
    };

    const filteredProjects = computed(() => {
      return projects.value.filter(project => {
        return (
            (!searchTerm.value || project.projectName.toLowerCase().includes(searchTerm.value.toLowerCase())) &&
            (!filterStatus.value || project.status === filterStatus.value)
        );
      });
    });

    const paginatedProjects = computed(() => {
      const start = (currentPage.value - 1) * pageSize.value;
      return filteredProjects.value.slice(start, start + pageSize.value);
    });

    const filterProjects = () => {
      currentPage.value = 1;
      fetchProjects();
    };

    const changePage = (newPage) => {
      currentPage.value = newPage;
      fetchProjects();
    };

    const editProjectDetails = (project) => {
      showEditProjectForm.value = true;
      if (project) {
        currentProject.value = project;
        editFormData.value = {
          projectName: project.projectName,
          managerName: project.managerName,
          plannedStartDate: project.plannedStartDate,
          plannedEndDate: project.plannedEndDate,
          projectType: project.projectType,
          budget: String (project.budget),
          status: project.status,
          description: project.description,  // 将已有的description赋值过来
          projectId: String(project.projectId)
        };
      } else {
        console.error('传入编辑的项目数据为 undefined，请检查调用处');
      }
    };

    const submitEditForm = async () => {
      if (!validateForm()) {
        console.error('请修正表单中的错误后再提交');
        return;
      }
      if (!token) {
        console.error('未获取到有效的token，请先登录');
        return;
      }
      try {
        const requestData = {
          ...editFormData.value  // 包含了所有编辑后的字段数据，包括projectId和description
        };
        console.log(requestData);
        await axios.post(`/api/administrator/updateProjects`, requestData, {
          headers: {
            'token': token
          }
        });
        Swal.fire({
          icon: 'success',
          title: '编辑成功',
          text: '项目信息已成功修改',
        });
        closeEditProjectForm();
        await fetchProjects();
      } catch (error) {
        console.error('项目编辑出错：', error);
      }
    };

    const closeEditProjectForm = () => {
      showEditProjectForm.value = false;
    };

    onMounted(async () => {
      await fetchProjects();
    });

    return {
      searchTerm,
      filterStatus,
      currentPage,
      pageSize,
      projects,
      totalProjects,
      paginatedProjects,
      filteredProjects,
      filterProjects,
      changePage,
      editProjectDetails,
      showEditProjectForm,
      editFormData,
      errors,
      submitEditForm,
      closeEditProjectForm,
      validateField
    };
  }
};
</script>

<style scoped>
/* 样式保持不变 */
/* 用户管理整体区域样式 */
.project-management {
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
.search-bar input {
  flex: 3;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
.search-bar select {
  flex: 1;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
  background-color: #ffffff;
}
/* 添加用户按钮相关样式 */
.add-project-button-container {
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
.edit-project-form-popup {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 600px;
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
.edit-project-form-popup h3 {
  font-size: 24px;
  color: #222;
  text-align: center;
  margin-bottom: 20px;
  font-weight: 600;
  letter-spacing: 1px;
}
.edit-project-form-popup form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}
/* 表单字段样式 */
.form-field {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.form-field label {
  font-weight: 600;
  font-size: 16px;
  color: #444;
  margin-bottom: 5px;
}
.form-field input,
.form-field select {
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
.form-field input:focus,
.form-field select:focus {
  border-color: #007bff;
  box-shadow: 0 4px 8px rgba(0, 123, 255, 0.2);
  background-color: #ffffff;
  outline: none;
}
.form-field input::placeholder {
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
/* 项目列表展示区域样式 */
.project-list table {
  width: 100%;
  border-collapse: collapse;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: #ffffff;
}
.project-list th {
  background-color: #007bff;
  color: #ffffff;
  text-transform: uppercase;
  padding: 15px;
  text-align: left;
}
.project-list td {
  padding: 15px;
  border: 1px solid #eee;
  text-align: left;
}
.project-list tr:nth-child(even) {
  background-color: #f9f9f9;
}
.common-button {
  padding: 10px;
  cursor: pointer;
  border: none;
  border-radius: 5px;
  background-color: #007bff;
  color: #ffffff;
  transition: background-color 0.3s ease, transform 0.2s ease;
  margin-right: 10px;
}
.common-button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}
/* 错误提示信息的样式 */
.error {
  color: #ff4d4d;
  font-size: 0.9em;
  margin-top: 5px;
}
</style>
