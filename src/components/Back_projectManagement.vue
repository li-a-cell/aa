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
        <option value="进行中">进行中</option>
        <option value="未开始">未开始</option>
        <option value="已完成">已完成</option>
      </select>
    </div>

    <!-- 添加项目按钮 -->
    <div class="add-project-button-container">
      <button @click="openAddProjectForm" class="add-project-button">添加新项目</button>
    </div>

    <!-- 添加或编辑项目表单 -->
    <div v-if="showProjectForm" class="form-container">
      <h3>{{ currentProject ? '编辑项目' : '添加新项目' }}</h3>
      <form @submit.prevent="submitForm">
        <div class="form-field">
          <label for="name">项目名称</label>
          <input v-model="formData.name" type="text" placeholder="项目名称" />
          <span class="error">{{ errors.value.name }}</span>
        </div>
        <div class="form-field">
          <label for="managerName">项目经理</label>
          <input v-model="formData.managerName" type="text" placeholder="项目经理姓名" />
          <span class="error">{{ errors.value.managerName }}</span>
        </div>
        <div class="form-field">
          <label for="plannedStartDate">计划开始日期</label>
          <input v-model="formData.plannedStartDate" type="date" placeholder="计划开始日期" />
          <span class="error">{{ errors.value.plannedStartDate }}</span>
        </div>
        <div class="form-field">
          <label for="plannedEndDate">计划结束日期</label>
          <input v-model="formData.plannedEndDate" type="date" placeholder="计划结束日期" />
          <span class="error">{{ errors.value.plannedEndDate }}</span>
        </div>
        <div class="form-field">
          <label for="projectType">项目类型</label>
          <select v-model="formData.projectType">
            <option value="">请选择项目类型</option>
            <option value="房屋建筑">房屋建筑</option>
            <option value="市政工程">市政工程</option>
          </select>
          <span class="error">{{ errors.value.projectType }}</span>
        </div>
        <button type="submit">{{ currentProject ? '确认修改' : '确认添加' }}</button>
        <button @click="closeProjectForm" type="button">取消</button>
      </form>
    </div>

    <!-- 删除确认和上传销项合同 -->
    <div v-if="showDeleteConfirmation" class="delete-confirmation-container">
      <h3>删除确认</h3>
      <p>您确认删除项目：{{ currentProject.name }} 吗？</p>
      <p>项目经理：{{ currentProject.managerName }}</p>
      <p>计划开始日期：{{ currentProject.plannedStartDate }}</p>
      <p>计划结束日期：{{ currentProject.plannedEndDate }}</p>
      <label for="contractFile">请上传销项合同文件：</label>
      <input type="file" @change="handleFileUpload" />
      <button @click="deleteProjectWithFile" type="button">确认删除</button>
      <button @click="closeDeleteConfirmation" type="button">取消</button>
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
          <td>{{ project.name }}</td>
          <td>{{ project.managerName }}</td>
          <td>{{ project.plannedStartDate }}</td>
          <td>{{ project.plannedEndDate }}</td>
          <td>{{ project.locationName }}</td>
          <td>{{ project.contractorName }}</td>
          <td>{{ project.budget }}</td>
          <td>{{ project.status }}</td>
          <td>{{ project.projectType }}</td>
          <td>
            <button @click="editProjectDetails(project)">编辑</button>
            <button @click="confirmDelete(project.id)">删除</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- 分页 -->
    <div class="pagination">
      <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1">上一页</button>
      <span>第 {{ currentPage }} 页</span>
      <button @click="changePage(currentPage + 1)" :disabled="currentPage * pageSize >= filteredProjects.length">下一页</button>
    </div>
  </div>
</template>

<script>
import { ref, computed } from 'vue';
import Swal from 'sweetalert2';
import { useForm } from 'vee-validate';
import * as yup from 'yup';

export default {
  name: 'ProjectManagement',
  setup() {
    const projects = ref([
      {
        id: 1,
        name: '项目A',
        managerName: '张三',
        plannedStartDate: '2024-01-01',
        plannedEndDate: '2024-06-01',
        locationName: '北京',
        contractorName: '建筑公司A',
        budget: 1000000,
        status: '进行中',
        projectType: '房屋建筑',
        description: '这是项目A的描述'
      },
      {
        id: 2,
        name: '项目B',
        managerName: '李四',
        plannedStartDate: '2024-02-01',
        plannedEndDate: '2024-08-01',
        locationName: '上海',
        contractorName: '建筑公司B',
        budget: 1500000,
        status: '未开始',
        projectType: '市政工程',
        description: '这是项目B的描述'
      },
    ]);

    const searchTerm = ref('');
    const filterStatus = ref('');
    const currentPage = ref(1);
    const pageSize = ref(5);

    const showProjectForm = ref(false);
    const showDeleteConfirmation = ref(false);
    const currentProject = ref(null);
    const formData = ref({
      name: '',
      managerName: '',
      plannedStartDate: '',
      plannedEndDate: '',
      locationName: '',
      contractorName: '',
      budget: null,
      status: '',
      projectType: '',
      description: '',
    });

    const contractFile = ref(null);

    // 表单验证规则
    const schema = yup.object({
      name: yup.string().required('项目名称是必填项'),
      managerName: yup.string().required('项目经理是必填项'),
      plannedStartDate: yup.date().required('计划开始日期是必填项').typeError('无效的日期'),
      plannedEndDate: yup.date().required('计划结束日期是必填项').typeError('无效的日期'),
      projectType: yup.string().required('项目类型是必填项'),
    });

    // 使用 useForm Hook 进行表单验证
    const {handleSubmit, errors} = useForm({
      validationSchema: schema,
    });

    const submitForm = handleSubmit((values) => {
      if (currentProject.value) {
        updateProject(values);
      } else {
        addProject(values);
      }
    });

    const filteredProjects = computed(() => {
      return projects.value.filter((project) => {
        return (
            (!searchTerm.value || project.name.toLowerCase().includes(searchTerm.value.toLowerCase())) &&
            (!filterStatus.value || project.status === filterStatus.value)
        );
      });
    });

    const paginatedProjects = computed(() => {
      const start = (currentPage.value - 1) * pageSize.value;
      return filteredProjects.value.slice(start, start + pageSize.value);
    });

    const openAddProjectForm = () => {
      currentProject.value = null;
      resetFormData();
      showProjectForm.value = true;
    };

    const closeProjectForm = () => {
      showProjectForm.value = false;
    };

    const editProjectDetails = (project) => {
      currentProject.value = project;
      formData.value = {...project};
      showProjectForm.value = true;
    };

    const addProject = (values) => {
      const newId = projects.value.length ? Math.max(...projects.value.map((p) => p.id)) + 1 : 1;
      projects.value.push({id: newId, ...values});
      Swal.fire('成功', '项目添加成功', 'success');
      closeProjectForm();
    };

    const updateProject = (values) => {
      const index = projects.value.findIndex((p) => p.id === currentProject.value.id);
      if (index !== -1) {
        projects.value[index] = {...values};
        Swal.fire('成功', '项目修改成功', 'success');
        closeProjectForm();
      }
    };

    const confirmDelete = (projectId) => {
      currentProject.value = projects.value.find((project) => project.id === projectId);
      showDeleteConfirmation.value = true;
    };

    const handleFileUpload = (event) => {
      contractFile.value = event.target.files[0];
    };

    const deleteProjectWithFile = () => {
      if (contractFile.value) {
        // Upload file logic here (e.g., send to server)
        console.log('Uploading contract file...', contractFile.value);
      }
      deleteProject(currentProject.value.id);
      closeDeleteConfirmation();
    };

    const deleteProject = (id) => {
      projects.value = projects.value.filter((project) => project.id !== id);
      Swal.fire('删除成功', '项目已删除', 'success');
    };

    const closeDeleteConfirmation = () => {
      showDeleteConfirmation.value = false;
    };

    const changePage = (newPage) => {
      currentPage.value = newPage;
    };

    const resetFormData = () => {
      formData.value = {
        name: '',
        managerName: '',
        plannedStartDate: '',
        plannedEndDate: '',
        locationName: '',
        contractorName: '',
        budget: null,
        status: '',
        projectType: '',
        description: '',
      };
    };

    return {
      projects,
      searchTerm,
      filterStatus,
      currentPage,
      pageSize,
      showProjectForm,
      showDeleteConfirmation,
      currentProject,
      formData,
      filteredProjects,
      paginatedProjects,
      openAddProjectForm,
      closeProjectForm,
      editProjectDetails,
      submitForm,
      addProject,
      updateProject,
      confirmDelete,
      deleteProjectWithFile,
      handleFileUpload,
      deleteProject,
      changePage,
      errors,
    };
  },
};
</script>

<style scoped>
.project-management {
  padding: 30px;
  width: 100%;
  box-sizing: border-box;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; /* 更现代的字体 */
  background-color: #f4f6f9; /* 背景色稍微浅灰，更加柔和 */
}

.search-bar {
  width: 100%;
  display: flex;
  gap: 15px; /* 增加搜索框和选择框的间距 */
  margin-bottom: 20px; /* 增加底部的间距 */
}
.project-list td button {
  padding: 10px;
  cursor: pointer;
  border: none; /* 移除默认边框 */
  border-radius: 5px;
  background-color: #007bff; /* 蓝色背景，统一样式 */
  color: #ffffff;
  transition: background-color 0.3s ease, transform 0.2s ease;
  margin-right: 10px; /* 为按钮之间添加间距 */
}

.project-list td button:last-child {
  margin-right: 0; /* 确保最后一个按钮没有右边距 */
}

.search-bar input {
  flex: 3;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 添加阴影让输入框有立体感 */
}

.search-bar select {
  flex: 1;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
  background-color: #ffffff; /* 保持选择框背景颜色 */
}

.add-project-button-container {
  margin-bottom: 20px;
  text-align: right;
}

.add-project-button {
  padding: 10px 20px;
  background-color: #007bff; /* 蓝色按钮，更加鲜明 */
  color: #ffffff;
  border: none;
  border-radius: 5px;
  font-weight: bold;
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: background-color 0.3s ease, transform 0.2s ease; /* 添加按钮颜色和缩放过渡效果 */
}

.add-project-button:hover {
  background-color: #0056b3; /* 鼠标悬停时颜色加深 */
  transform: translateY(-2px); /* 鼠标悬停时向上移动 */
}

.delete-confirmation-container {
  margin-bottom: 20px;
  padding: 20px;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  background-color: #ffffff;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05); /* 增加轻微阴影让其更加突出 */
}

.delete-confirmation-container button {
  padding: 10px 20px;
  margin-right: 15px;
  font-size: 1em;
  cursor: pointer;
  border-radius: 5px;
  background-color: #ff4d4d; /* 红色按钮，强调删除操作 */
  color: #ffffff;
  border: none;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.delete-confirmation-container button:hover {
  background-color: #cc0000; /* 鼠标悬停时颜色加深 */
  transform: translateY(-2px);
}

.project-list table {
  width: 100%;
  border-collapse: collapse;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 为表格增加轻微阴影，使其更有立体感 */
  background-color: #ffffff;
}

.project-list th {
  background-color: #007bff; /* 表头背景色与添加项目按钮保持一致 */
  color: #ffffff;
  text-transform: uppercase; /* 表头内容大写 */
  padding: 15px;
  text-align: left;
}

.project-list td {
  padding: 15px;
  border: 1px solid #eee; /* 边框颜色稍浅，更柔和 */
  text-align: left;
}

.project-list tr:nth-child(even) {
  background-color: #f9f9f9; /* 为偶数行添加背景色，提升可读性 */
}

.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
  gap: 15px;
}

.pagination button {
  padding: 10px 15px;
  background-color: #007bff;
  color: #ffffff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.pagination button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}

button {
  padding: 10px;
  cursor: pointer;
  border: none; /* 移除默认边框 */
  border-radius: 5px;
  background-color: #007bff; /* 蓝色背景，统一样式 */
  color: #ffffff;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

button:hover {
  background-color: #0056b3; /* 鼠标悬停时颜色加深 */
  transform: translateY(-2px); /* 鼠标悬停时按钮向上移动 */
}

.error {
  color: #ff4d4d; /* 改为更鲜明的红色 */
  font-size: 0.9em;
  margin-top: 5px; /* 与输入框保持间距 */
}

.form-container {
  margin-bottom: 20px;
  padding: 20px;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  background-color: #ffffff;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05); /* 添加轻微的阴影 */
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
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.05); /* 内阴影使输入框更有立体感 */
}

</style>
