<template>
  <nav class="sidebar">
    <ul>
      <li class="sidebar-item">
        <router-link
          to="/"
          class="sidebar-link"
          exact-active-class="active-link"
        >
          <i class="icon icon-home"></i>
          <span class="sidebar-text">主页</span>
        </router-link>
      </li>
      <li class="sidebar-item">
        <router-link
          to="/projects"
          class="sidebar-link"
          exact-active-class="active-link"
        >
          <i class="icon icon-project"></i>
          <span class="sidebar-text">项目管理</span>
        </router-link>
      </li>
      <li class="sidebar-item">
        <router-link
          to="/nodes"
          class="sidebar-link"
          exact-active-class="active-link"
        >
          <i class="icon icon-node"></i>
          <span class="sidebar-text">节点管理</span>
        </router-link>
      </li>
      <li class="sidebar-item">
        <router-link
          to="/tasks"
          class="sidebar-link"
          exact-active-class="active-link"
        >
          <i class="icon icon-task"></i>
          <span class="sidebar-text">项目管理</span>
        </router-link>
      </li>
      <li class="sidebar-item">
        <router-link
          to="/regulations"
          class="sidebar-link"
          exact-active-class="active-link"
        >
          <i class="icon icon-regulation"></i>
          <span class="sidebar-text">规章制度</span>
        </router-link>
      </li>
      <li class="sidebar-item">
        <router-link
          to="/history"
          class="sidebar-link"
          exact-active-class="active-link"
        >
          <i class="icon icon-history"></i>
          <span class="sidebar-text">历史项目</span>
        </router-link>
      </li>
    </ul>
  </nav>
</template>

<script>
export default {
  name: 'Sidebar',
};
</script>

<style scoped>
.sidebar {
  width: 200px;
  background-color: #f8f9fa;
  padding: 20px;
}
.sidebar ul {
  list-style-type: none;
  padding: 0;
}
.sidebar-item {
  margin: 15px 0;
  display: flex;
  align-items: center;
  padding-left: 0;
}
.sidebar-link {
  display: flex;
  align-items: center;
  width: 100%;
  padding: 10px 10px;
  text-decoration: none;
  color: #212529;
  border-radius: 4px;
  transition: background-color 0.3s, color 0.3s;
}
.sidebar-link:hover {
  background-color: #e9ecef;
  color: #0056b3;
}
.sidebar-link.active-link {
  background-color: #dee2e6;
  color: #0056b3;
  font-weight: bold;
}
.sidebar-item .icon {
  margin-right: 10px;
  font-size: 20px;
}
.sidebar-text {
  flex-grow: 1;
  text-align: left;
}
.icon-home::before {
  font-size: 24px;
  line-height: 1;
  content: '\1F3E0';
}
.icon-project::before {
  font-size: 24px;
  line-height: 1;
  content: '\1F4C8';
}
.icon-node::before {
  font-size: 39px;
  line-height: 1;
  content: '\2699';
}
.icon-task::before {
  font-size: 24px;
  line-height: 1;
  content: '\270D';
}
.icon-regulation::before {
  font-size: 24px;
  line-height: 1;
  content: '\1F4D3';
}
.icon-history::before {
  font-size: 24px;
  line-height: 1;
  content: '\1F4CA';
}
</style>
