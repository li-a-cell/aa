<template>
  
  <div class="project-overview">
    <!-- Project Header Section -->
    <header class="project-header">
      <h1 class="project-title">项目名称 ({{ projectName }})</h1>
      <div class="project-meta">
        <span>项目类型: {{ projectType }}</span>
        <span>项目经理: {{ projectManager }}</span>
        <span>项目周期: {{ projectCycle }}</span>
      </div>
    </header>

    <!-- Project Summary Section -->
    <section class="project-summary">
      <div class="summary-title">
        综合
        <!-- Node Button -->
        <button @click="goToNodePage" class="node-button">节点</button>
      </div>
      <div class="summary-content">
        <div class="info-item">
          <i class="icon-location"></i>
          项目地址: {{ projectAddress }}
        </div>
        <div class="info-item">
          <i class="icon-department"></i>
          项目所属部门: {{ projectDepartment }}
        </div>
        <div class="info-item">
          <i class="icon-building"></i>
          楼栋名称: {{ buildingName }}
        </div>
        <div class="info-item">
          <i class="icon-code"></i>
          楼栋编号: {{ buildingCode }}
        </div>
        <div class="info-item">
          <i class="icon-clock"></i>
          项目创建时间: {{ creationTime }}
        </div>
        <div class="info-item">
          <i class="icon-user"></i>
          项目创建人: {{ creator }}
        </div>
        <div class="info-item">
          <i class="icon-description"></i>
          项目描述:
          <p>{{ projectDescription }}</p>
        </div>
      </div>
      <div class="project-deadline">
        <i class="icon-calendar"></i>
        项目剩余时间: <span class="days-left">{{ daysLeft }}</span> 天
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios';
import * as jwt_decode from 'jwt-decode';



// State for user login
const account = ref('')
const password = ref('')
const loginButtonText = ref('登录')
const loadingText = ref('正在加载...')
const loading = ref(false)
const error = ref(null)
const users = ref([])

// Project Information
const project_name = ref('xxxxxx')
const projectType = ref('xxxxxx')
const projectManager = ref('xxxxxx')
const projectCycle = ref('xxxxxx')
const projectAddress = ref('xxxxxx')
const projectDepartment = ref('xxxxxx')
const buildingName = ref('xxxxxx')
const buildingCode = ref('xxxxxx')
const creationTime = ref('xxxx-xx-xx xx:xx')
const creator = ref('xxxxxx')
const projectDescription = ref('xxxxxx')
const  ElMessage=ref('')
const daysLeft = ref(62)
const token=ref("")
const handleLogin = () => {
    if (account.value.length === 0 || password.value.length === 0) {
        ElMessage.error('用户名和密码不能为空');
        return;
    }

    const data = {
        account: account.value,
        password: password.value
    };

    // 发送登录请求
    axios.post('http://localhost:8080/auth/login', data)
        .then(response=> {
            console.log('响应数据：', response.data);
            const { code, msg, data } = response.data;

          
                const token= data;
          
                localStorage.setItem('token', token);
                console.log('头部：', token);  
                console.log("sada",localStorage.getItem(token));
          
        })
        .catch(error => {
            console.error('登录失败', error.response ? error.response.data : error);
        });
};
const update = () => {
    const token = localStorage.getItem('token');
    if (token) {
        console.log("不为空", token);
        axios.get(`http://localhost:8080/project/ongoing`, {
            headers: {
                'Token': token
            },
        })
        .then(response => {
            const data = response.data.data;
            console.log("data",data);

            if (!data || Object.keys(data).length === 0) {
                // 处理空数据的情况
                console.log('返回的数据为空');
                // 可以在这里显示提示信息或执行其他操作
                // 例如，更新UI显示提示信息
                error.value = '没有正在进行的项目';
            } else {
                // 处理非空数据的情况
                console.log('受保护的数据：', data);
                // 假设 data 包含一个名为 projects 的数组
                const projects = data.projects || [];
                if (projects.length === 0) {
                    // 处理空数组的情况
                    console.log('没有正在进行的项目');
                    error.value = '没有正在进行的项目';
                } else {
                    // 更新UI显示项目列表
                    users.value = projects;
                }
            }
        })
        .catch(error => {
            console.error('获取数据失败', error);
            error.value = '获取数据失败，请稍后再试';
        });
    }
};
//console.log('受保护的数据：', token);
const router = useRouter()
// Navigate to Node Page
const goToNodePage = () => {
  router.push({ name: 'NodePage' })
}
</script>

<style scoped>
/* Global Layout */
.project-overview {
  width: 1620px;
  height: 580px;
  font-family: 'Arial', sans-serif;
  color: #333;
  background-color: #f9fbfd; /* Light blue background */
  padding: 0px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

/* Project Header Section */
.project-header {
  background-color: #e6f7ff;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.project-title {
  margin: 0;
  font-size: 24px;
  color: #1890ff;
}

.project-meta {
  font-size: 14px;
  color: #555;
  margin-top: 5px;
}

/* Project Summary Section */
.project-summary {
  background-color: #ffffff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 1px 5px rgba(0, 0, 0, 0.1);
}

.summary-title {
  font-size: 20px;
  font-weight: bold;
  color: #1890ff;
  margin-bottom: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* Button Style */
.node-button {
  background-color: #1890ff;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 8px 15px;
  cursor: pointer;
  font-size: 14px;
}

.node-button:hover {
  background-color: #40a9ff;
}

/* Project Content Section */
.summary-content {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.info-item {
  flex: 1 1 45%;
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #666;
}

.info-item i {
  font-size: 16px;
  margin-right: 10px;
  color: #1890ff;
}

/* Project Deadline */
.project-deadline {
  margin-top: 20px;
  font-size: 16px;
  font-weight: bold;
  color: #ff4d4f;
}

.days-left {
  font-size: 18px;
  color: #52c41a;
}

/* Icon Styles (Using FontAwesome or similar libraries) */
.icon-location:before {
  content: "\1F4CD"; /* 📍 */
}

.icon-department:before {
  content: "\1F3E2"; /* 🏢 */
}

.icon-building:before {
  content: "\1F3D7"; /* 🏗️ */
}

.icon-code:before {
  content: "\1F522"; /* 🔢 */
}

.icon-clock:before {
  content: "\23F0"; /* ⏰ */
}

.icon-user:before {
  content: "\1F464"; /* 👤 */
}

.icon-description:before {
  content: "\1F4DD"; /* 📝 */
}

.icon-calendar:before {
  content: "\1F4C5"; /* 📅 */
}
</style>
