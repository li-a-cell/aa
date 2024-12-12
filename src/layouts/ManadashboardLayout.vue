<template>
  <div class="dashboard-layout">
    <!-- 左侧侧边栏 -->
    <div class="sidebar">
      <div class="logo">建筑项目管理系统</div>
      <nav class="nav-menu">
        <ul>
          <li @click="navigateTo('Manahome')" :class="{ active: isActive('Manahome') }">
            <i class="icon fas fa-home"></i><span>主界面</span>
          </li>
          <li @click="navigateTo('ProjectDetails')" :class="{ active: isActive('ProjectDetails') }">
            <i class="icon fas fa-tasks"></i><span>项目详情</span>
          </li>
          <li @click="navigateTo('RulesAndRegulations')" :class="{ active: isActive('RulesAndRegulations') }">
            <i class="icon fas fa-book"></i><span>规章制度</span>
          </li>
          <li @click="navigateTo('HistoryProjects')" :class="{ active: isActive('HistoryProjects') }">
            <i class="icon fas fa-history"></i><span>历史项目</span>
          </li>
        </ul>
      </nav>
    </div>

    <!-- 右侧内容区域 -->
    <div class="content">
      <header class="top-bar">
        <div class="header-title">        </div>
        <div class="avatar-container">
          <el-avatar :src="profilePictureUrl" size="large" class="profile-avatar" @click="goToProfilePage"></el-avatar>
        </div>
      </header>
      <div class="content-view">
        <router-view></router-view> <!-- 渲染子路由内容 -->
      </div>
    </div>
  </div>
</template>

<script>
import router from "@/router/index.js";

export default {
  name: 'Manadashboard',
  data() {
    return {
      employee: {
        name: "张三",
        job_type: "项目经理",
        hire_date: "2019-04-15",
        phone_number: "+86 123 4567 8910",
        address: "北京市朝阳区某某街道123号",
        email: "zhangsan@example.com",
        freelance: true,
        birth_date: "1986-05-09",
        profile_picture: null,
        language: {
          english: 5,
          spanish: 3,
          italian: 1
        },
        skills: [
          { name: "PHP & MySQL", level: 90 },
          { name: "JavaScript", level: 80 },
          { name: "UX Design", level: 80 }
        ]
      }
    };
  },
  computed: {
    profilePictureUrl() {
      if (this.employee.profile_picture) {
        return URL.createObjectURL(new Blob([this.employee.profile_picture]));
      } else {
        return new URL('@/assets/logo.png', import.meta.url).href;
      }
    },
  },
  methods: {
    navigateTo(routeName) {
      this.$router.push({ name: routeName });
    },
    isActive(routeName) {
      return this.$route.name === routeName;
    },
    goToProfilePage() {
      console.log('头像点击了，跳转到个人信息页面');
      router.push('/pro-card'); // 通过路由跳转到个人信息页面
    }
  },
};
</script>

<style scoped>
/* 样式保持不变 */
.dashboard-layout {
  display: flex;
  height: 100vh;
  width: 100vw;
  font-family: Arial, sans-serif;
  background-color: #f5f5f5;
}

/* 左侧侧边栏样式 */
.sidebar {
  width: 240px;
  background-color: #777777;
  border: #33CC33;
  color: #ffffff;
  display: flex;
  flex-direction: column;
  padding: 20px 10px;
  /* 增加阴影效果 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3), 0 2px 4px rgba(0, 0, 0, 0.2);
}


.logo {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 30px;
}

.logo img {
  max-width: 80%;
}

.nav-menu ul {
  list-style: none;
  padding: 0;
}

.nav-menu li {
  display: flex;
  align-items: center;
  padding: 15px;
  margin-bottom: 10px;
  cursor: pointer;
  border-radius: 8px;
  transition: background 0.3s, transform 0.2s;
}

.nav-menu li:hover {
  background-color: #4c5b72;
  transform: translateY(-3px);
}

.nav-menu li.active {
  background-color: #007bff;
}

.nav-menu li.icon {
  margin-right: 10px;
  font-size: 1.2em;
}

.nav-menu li span {
  color: #ffffff;
  font-size: 1.1em;
}

/* 右侧内容区域 */
.content {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #f8f9fa;
}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #ffffff;
  padding: 15px 25px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.header-title {
  font-size: 1.5em;
  font-weight: bold;
  color: #333333;
}


.user-info span {
  margin-right: 10px;
  font-weight: bold;
  color: #333333;
}


.content-view {
  padding: 30px;
  overflow-y: auto;
}
</style>


