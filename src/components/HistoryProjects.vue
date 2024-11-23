<template>
  <div class="project-management">
    <h2>项目管理</h2>
    <!-- 搜索框与筛选条件 -->
    <div class="search-bar">
      <input
          type="text"
          v-model="searchTerm"
          @input="filterProjects"
          placeholder="搜索项目（名称、项目类型、施工地、施工方、预算）"
      />
    </div>
    <!-- 项目列表展示 -->
    <div class="project-list">
      <table>
        <thead>
        <tr>
          <th>项目名称</th>
          <th>项目类型</th>
          <th>施工地</th>
          <th>施工方</th>
          <th>预算</th>
          <th>开始时间</th> <!-- 新增开始时间表头 -->
        </tr>
        </thead>
        <tbody>
        <tr v-for="(project, index) in filteredProjects" :key="project.project_id">
          <td>{{ project.project_name }}</td>
          <td>{{ project.project_type }}</td>
          <td>{{ project.site_name }}</td>
          <td>{{ project.bidder_name }}</td>
          <td>{{ project.budget }}</td>
          <td>{{ project.planned_start_date }}</td> <!-- 新增展示开始时间的单元格 -->
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import {ref, computed, onMounted} from 'vue';
import axios from 'axios';

export default {
  name: 'ProjectManagement',
  setup() {
    const searchTerm = ref('');
    const projects = ref([]);  // 用于存储从后端获取到的数据

    // 获取项目数据的函数，发送GET请求到后端接口
    const fetchProjects = async () => {
      try {
        // 从 localStorage 获取 JWT token
        const token = localStorage.getItem('jwtToken');
        console.log('获取到的 token:', token);
        if (!token) {
          console.error('JWT Token is missing!');
          return;  // 如果没有 token，直接返回
        }

        // 发送 GET 请求，带上 token
        const response = await axios.get('http://localhost:9528/project/completed', {
          headers: {
            'token': token  // 请求头包含 'token' 字段，值为从 localStorage 获取的 JWT token
          },
        });

        // 将返回的数据赋值给 projects，这里假设后端返回的数据在 response.data 下，可根据实际调整
        projects.value = response.data.data ;
      } catch (error) {
        if (error.response) {
          // 如果后端返回错误信息
          console.error('后端错误：', error.response.data);
          if (error.response.data.message === 'NOT_LOGIN') {
            console.error('用户未登录或 token 无效');
          }
        } else {
          // 其他错误，例如网络错误
          console.error('请求失败：', error.message);
        }
      }
    };

    // 页面加载时调用获取项目数据的函数
    onMounted(() => {
      fetchProjects();
    });

    const filteredProjects = computed(() => {
      return projects.value.filter((project) => {
        return (!searchTerm.value || project.project_name.toLowerCase().includes(searchTerm.value.toLowerCase()));
      });
    });

    return {
      searchTerm,
      filteredProjects,
      projects
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
