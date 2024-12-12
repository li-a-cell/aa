<template>
  <div class="dashboard-layout">
    <!-- 左侧侧边栏 -->
    <div class="sidebar">
      <div class="logo">建筑项目管理系统</div>
      <nav class="nav-menu">
        <ul>
          <li @click="navigateTo('Home')" :class="{ active: isActive('Home') }">
            <i class="icon fas fa-home"></i><span>主界面</span>
          </li>
          <li @click="navigateTo('ProjectManagement')" :class="{ active: isActive('ProjectManagement') }">
            <i class="icon fas fa-tasks"></i><span>项目管理</span>
          </li>
          <li @click="navigateTo('UserManagement')" :class="{ active: isActive('UserManagement') }">
            <i class="icon fas fa-user"></i><span>用户管理</span>
          </li>
          <li @click="navigateTo('MaterialManagement')" :class="{ active: isActive('MaterialManagement') }">
            <i class="icon fas fa-user"></i><span>材料管理</span>
          </li>
        </ul>
      </nav>
    </div>

    <!-- 右侧内容区域 -->
    <div class="content">
      <header class="top-bar">
        <div class="header-title">建筑项目管理系统</div>
        <div class="user-info">
          <!-- 用户信息显示可以自定义 -->
        </div>
      </header>
      <div class="content-view">
        <router-view></router-view> <!-- 使用router-view来渲染对应路由组件 -->
      </div>
    </div>
  </div>
</template>

<script>
import router from "@/router/index.js";  // 假设你的路由实例是从这里引入，需根据实际情况调整路径
import ProjectManagement from '../components/Back_projectManagement.vue';
import UserManagement from '../components/UserManagement.vue';
import Home from '../components/Back_home.vue';
import MaterialManagement from '../components/MaterialManagement.vue';

export default {
  name: 'Dashboard',
  components: {
    Home,
    ProjectManagement,
    UserManagement,
    MaterialManagement,
  },
  data() {
    return {
      currentView: 'Home'
    };
  },
  methods: {
    navigateTo(routeName) {
      this.$router.push({ name: routeName });
    },
    isActive(routeName) {
      return this.$route.name === routeName;
    }
  },
  mounted() {
    const firstEnter = localStorage.getItem('firstEnter');
    if (firstEnter === null) {
      // 如果是首次进入，跳转到主界面
      this.$router.push({ name: 'Home' });
      localStorage.setItem('firstEnter', 'false');
    }
  }
};
</script>

<style scoped>
/* 主体布局 */
.dashboard-layout {
  display: flex;
  height: 100vh;
  width: 100vw;
  font-family: Arial, sans-serif;
  background-color: #f5f5f5;
}

/* 左侧侧边栏样式 */
.sidebar {
  width: 240px; /* 固定宽度 */
  background-color: #34495e;
  color: #fff;
  display: flex;
  flex-direction: column;
  padding: 20px;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
}

.logo {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 30px;
  color: #ecf0f1;
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
  background-color: #16a085;
  transform: translateY(-3px);
}

.nav-menu li.active {
  background-color: #2980b9;
}

.nav-menu li.icon {
  margin-right: 10px;
  font-size: 1.2em;
}

.nav-menu li span {
  color: #fff;
  font-size: 1.1em;
}

/* 右侧内容区域 */
.content {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #ecf0f1;
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
  font-size: 1.8em;
  font-weight: bold;
  color: #2c3e50;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-info span {
  margin-right: 10px;
  font-weight: bold;
  color: #333333;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.content-view {
  padding: 30px;
  overflow-y: auto;
}
</style>