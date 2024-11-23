<template>
  <div class="dashboard-container">
    <!-- 侧边栏和内容区域布局 -->
    <!-- 项目概览内容 -->
    <div class="main-content">
      <div class="project-overview">
        <!-- 项目标题部分 -->
        <header class="project-header">
          <h1 class="project-title">{{ currentProject.project_name }}</h1>
          <div class="project-meta">
            <span>项目类型: {{ currentProject.project_type }}</span>
          </div>
        </header>

        <!-- 综合部分 -->
        <section class="project-summary">
          <div class="summary-title">
            综合
            <!-- 节点按钮 -->
            <button @click="goToNodePage" class="node-button">节点</button>
          </div>
          <div class="summary-content">
            <div class="info-item">
              <i class="icon-construction-site"></i>
              施工地: {{ currentProject.site_name }}
            </div>
            <div class="info-item">
              <i class="icon-builder"></i>
              施工方: {{ currentProject.bidder_name }}
            </div>
            <div class="info-item">
              <i class="icon-clock-start"></i>
              开始时间: {{ currentProject.planned_start_date }}
            </div>
            <div class="info-item">
              <i class="icon-clock-end"></i>
              计划结束时间: {{ currentProject.planned_end_date }}
            </div>
            <div class="info-item">
              <i class="icon-budget"></i>
              项目预算: {{ currentProject.budget }}
            </div>
            <div class="info-item">
              <i class="icon-description"></i>
              简要描述:
              <p>{{ currentProject.description }}</p>
            </div>
          </div>
          <div class="project-deadline">
            <i class="icon-calendar"></i>
            项目剩余时间: <span class="days-left">{{ calculateRemainingDays(currentProject.planned_end_date) }}</span> 天
          </div>
        </section>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import router from "@/router/index.js";

export default {
  name: "ProjectManagement",
  setup() {
    const projectData = ref([]);
    const currentProject = ref({});

    // 获取项目数据的函数，发送GET请求到后端接口
    const fetchProjectData = async () => {
      try {
        // 从localStorage获取JWT token
        const token = localStorage.getItem('jwtToken');
        if (!token) {
          console.error('JWT Token is missing!');
          return;
        }

        // 发送GET请求，带上token
        const response = await axios.get('http://localhost:9528/project/ongoing', {
          headers: {
            'token': token  // 请求头包含'token'字段，值为从localStorage获取的JWT token
          },
        });

        // 对后端返回数据结构进行校验，确保是包含数据的数组后再赋值和提取要展示的项目数据
        if (Array.isArray(response.data.data) && response.data.data.length > 0) {
          projectData.value = response.data.data;
          currentProject.value = projectData.value[0];
        } else {
          console.error('后端返回的数据格式不符合预期，没有可用的项目数据');
          alert('暂时无法加载项目数据，请稍后重试或联系管理员');
        }
      } catch (error) {
        if (error.response) {
          console.error('后端错误：', error.response.data);
          if (error.response.data.message === 'NOT_LOGIN') {
            console.error('用户未登录或token无效');
            alert('请先登录系统后再查看项目数据');
          } else {
            alert('后端返回数据出现错误，请联系管理员');
          }
        } else {
          console.error('请求失败：', error.message);
          alert('网络出现问题，请检查网络连接后重试');
        }
      }
    };

    // 在组件挂载时调用接口获取项目数据
    onMounted(() => {
      fetchProjectData();
    });

    // 计算项目剩余天数的函数
    const calculateRemainingDays = (plannedEndDate) => {
      const endDate = new Date(plannedEndDate);
      const currentDate = new Date();
      const timeDiff = endDate.getTime() - currentDate.getTime();
      return Math.ceil(timeDiff / (1000 * 60 * 60 * 24));
    };

    const goToNodePage = () => {
      console.log('节点按钮点击了');
      router.push({ name: 'NodePage' });
    };

    return {
      currentProject,
      goToNodePage,
      calculateRemainingDays
    };
  }
};
</script>

<style scoped>
/* 页面整体布局，去掉滚动条 */
.dashboard-container {
  display: flex;
  height: 100vh;
  overflow: hidden; /* 避免页面整体出现滚动条 */
}

/* 侧边栏容器样式 */
/* 主内容区域，自适应剩余宽度 */
.main-content {
  flex: 1;
  overflow-y: auto; /* 主内容区域可以垂直滚动 */
  padding: 20px;
  box-sizing: border-box;
  background-color: #f5f5f5;
  width: 100%;
}

/* 项目概览样式 */
.project-overview {
  width: 100%; /* 调整为全宽度 */
  max-width: 1600px; /* 限制最大宽度 */
  height: auto; /* 高度自适应内容 */
  font-family: 'Arial', sans-serif;
  color: #333;
  background-color: #f9fbfd;
  padding: 0px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

/* 项目标题部分 */
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

/* 综合部分 */
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

/* 按钮样式 */
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

/* 项目内容部分 */
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

/* 新增的图标样式定义 */
.icon-construction-site:before {
  content: "🏗️";
}

.icon-builder:before {
  content: "👷";
}

.icon-clock-start:before {
  content: "⏱️";
}

.icon-clock-end:before {
  content: "⏲️";
}

.icon-budget:before {
  content: "💰";
}

/* 项目剩余时间 */
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

/* 原有的图标样式保持不变 */
.icon-location:before {
  content: "📍";
}

.icon-department:before {
  content: "🏢";
}

.icon-building:before {
  content: "🏗️";
}

.icon-code:before {
  content: "🔢";
}

.icon-clock:before {
  content: "⏰";
}

.icon-user:before {
  content: "👤";
}

.icon-description:before {
  content: "📝";
}

.icon-calendar:before {
  content: "📅";
}

/* 头像样式 */
.avatar-container {
  position: fixed;
  top: 10px;
  right: 10px;
}

.profile-avatar {
  cursor: pointer;
}
</style>