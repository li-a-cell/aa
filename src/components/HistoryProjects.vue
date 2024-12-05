<template>
  <div class="project-management">
    <h2>历史项目</h2>
    <!-- 搜索框与筛选条件 -->
    <div class="search-bar">
      <select v-model="searchOption" @change="filterProjects">
        <option value="projectName">项目名称</option>
        <option value="projectType">项目类型</option>
        <option value="siteName">施工地</option>
        <option value="bidderName">施工方</option>
        <option value="budget">预算</option>
      </select>
      <input
          type="text"
          v-model="searchTerm"
          @input="filterProjects"
          :placeholder="`搜索${searchOption}`"
          class="search-input"
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
          <th>开始时间</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(project, index) in paginatedProjects" :key="project.projectId">
          <td>{{ project.projectName }}</td>
          <td>{{ project.projectType }}</td>
          <td>{{ project.siteName }}</td>
          <td>{{ project.bidderName }}</td>
          <td>{{ project.budget }}</td>
          <td>{{ project.plannedStartDate }}</td>
        </tr>
        <tr v-if="paginatedProjects.length < itemsPerPage">
          <td v-for="i in 6" :key="i">&nbsp;</td>
        </tr>
        </tbody>
      </table>
    </div>
    <!-- 分页按钮 -->
    <div class="pagination fixed-pagination">
      <button @click="prevPage" :disabled="currentPage === 1">上一页</button>
      <span>第 {{ currentPage }} 页 / 共 {{ totalPages }} 页</span>
      <input type="number" v-model.number="inputPage" min="1" :max="totalPages" @change="goToPage" class="page-input" />
      <button @click="nextPage" :disabled="currentPage === totalPages">下一页</button>
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
    const searchOption = ref('projectName'); // 新增搜索选项
    const projects = ref([]);  // 用于存储从后端获取到的数据
    const currentPage = ref(1); // 当前页码
    const inputPage = ref(1); // 用户输入的页码
    const itemsPerPage = 6; // 每页显示的数据条数

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
        projects.value = response.data.data;
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

    // 计算分页后的项目列表
    const paginatedProjects = computed(() => {
      const start = (currentPage.value - 1) * itemsPerPage;
      const end = start + itemsPerPage;
      return filteredProjects.value.slice(start, end);
    });

    // 计算总页数
    const totalPages = computed(() => {
      return Math.ceil(filteredProjects.value.length / itemsPerPage);
    });

    // 切换到上一页
    const prevPage = () => {
      if (currentPage.value > 1) {
        currentPage.value--;
        inputPage.value = currentPage.value;
      }
    };

    // 切换到下一页
    const nextPage = () => {
      if (currentPage.value < totalPages.value) {
        currentPage.value++;
        inputPage.value = currentPage.value;
      }
    };

    // 跳转到指定页
    const goToPage = () => {
      if (inputPage.value >= 1 && inputPage.value <= totalPages.value) {
        currentPage.value = inputPage.value;
      }
    };

    const filteredProjects = computed(() => {
      return projects.value.filter((project) => {
        return (!searchTerm.value || project[searchOption.value].toLowerCase().includes(searchTerm.value.toLowerCase()));
      });
    });

    return {
      searchTerm,
      searchOption,
      filteredProjects,
      paginatedProjects,
      currentPage,
      inputPage,
      totalPages,
      prevPage,
      nextPage,
      goToPage,
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
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #f4f6f9;
}

.search-bar {
  width: 100%;
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.search-bar .search-input {
  flex-grow: 2;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
}

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

.pagination {
  margin-top: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 15px;
  position: sticky;
  bottom: 0;
  background-color: #f4f6f9;
  padding: 10px 0;
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

.page-input {
  width: 50px;
  padding: 5px;
  text-align: center;
  border-radius: 5px;
  border: 1px solid #ddd;
}
</style>
