<template>
  <div class="dashboard-layout">
    <!-- 左侧侧边栏 -->
    <div class="sidebar">
      <div class="logo">建筑项目管理系统</div>
      <nav class="nav-menu">
        <ul>
          <li @click="currentView = 'Home'" :class="{ active: currentView === 'Home' }">
            <i class="icon fas fa-home"></i><span>主界面</span>
          </li>
          <li @click="currentView = 'ProjectManagement'" :class="{ active: currentView === 'ProjectManagement' }">
            <i class="icon fas fa-tasks"></i><span>项目管理</span>
          </li>
          <li @click="currentView = 'UserManagement'" :class="{ active: currentView === 'UserManagement' }">
            <i class="icon fas fa-user"></i><span>用户管理</span>
          </li>
          <li @click="currentView = 'Bidder'" :class="{ active: currentView === 'Bidder' }">
            <i class="icon fas fa-bidder"></i><span>投标人管理</span>
          </li>
        </ul>
      </nav>
    </div>

    <!-- 右侧内容区域 -->
    <div class="content">
      <header class="top-bar">
        <div class="header-title">      </div>
        <div class="user-info">
        </div>
      </header>
      <div class="content-view">
        <component :is="currentView" />
      </div>
    </div>
  </div>
</template>

<script>
import ProjectManagement from '../components/Back_projectManagement.vue';
import UserManagement from '../components/UserManagement.vue';
import Home from '../components/Back_home.vue'; // 假设有一个主页组件
import Bidder from '../components/BidderManagement.vue';

export default {
  name: 'Dashboard',
  components: {
    Home,
    ProjectManagement,
    UserManagement,
    Bidder ,
  },
  data() {
    return {
      currentView: 'Home', // 默认显示主页视图
    };
  },
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

.nav-menu li .icon {
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
