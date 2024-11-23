<template>
  <header class="header">
    <div class="header-left">
      <img src="@/assets/logo.png" alt="Logo" class="logo" />
      <h1 class="title">工作台</h1>
    </div>
    <div class="header-right">
      <div class="search-bar">
        <input type="text" placeholder="搜索" class="search-input" />
        <button class="search-button">🔍</button>
      </div>
      <div class="notification">
        <img src="@/assets/notification.png" alt="Notifications" class="notification-icon" />
      </div>
      <div class="user-info">
        <img src="@/assets/avatar.png" alt="Avatar" class="avatar" />
        <div class="dropdown">
          <button class="dropdown-button">用户</button>
          <div class="dropdown-content">
            <button>查看资料</button>
            <button>退出登录</button>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script>
export default {
  name: 'Header',
};
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background-color: #ffffff;
  color: #333;
  border-bottom: 1px solid #e0e0e0;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
}
.header-left {
  display: flex;
  align-items: center;
}
.logo {
  width: 40px;
  height: 40px;
  margin-right: 10px;
}
.title {
  font-size: 24px;
  font-weight: bold;
  color: #007bff;
}
.header-right {
  display: flex;
  align-items: center;
}
.search-bar {
  display: flex;
  align-items: center;
  margin-right: 20px;
}
.search-input {
  border: 1px solid #e0e0e0;
  border-radius: 20px;
  padding: 5px 10px;
  outline: none;
}
.search-button {
  background: none;
  border: none;
  cursor: pointer;
  margin-left: 5px;
}
.notification {
  margin-right: 20px;
}
.notification-icon {
  width: 24px;
  height: 24px;
  cursor: pointer;
}
.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  cursor: pointer;
}
.dropdown {
  position: relative;
  display: inline-block;
}
.dropdown-button {
  background: none;
  border: none;
  color: #333;
  cursor: pointer;
  font-size: 16px;
  margin-left: 10px;
}
.dropdown-content {
  display: none;
  position: absolute;
  right: 0;
  background-color: #f9f9f9;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  padding: 10px;
  z-index: 1;
}
.dropdown:hover .dropdown-content {
  display: block;
}
.dropdown-content button {
  background: none;
  border: none;
  padding: 8px;
  width: 100%;
  text-align: left;
  cursor: pointer;
}
.dropdown-content button:hover {
  background-color: #ddd;
}
</style>