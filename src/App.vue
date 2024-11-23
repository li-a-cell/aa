<template>
  <div class="app-container">
    <!-- 添加左上角的标题 -->
    <!-- 使用 <router-view> 来显示当前路由匹配的组件 -->
    <router-view />
  </div>
</template>

<script>

export default {
  name: 'App',
};
</script>

<style>
/* 添加通用样式 */
body {
  font-family: Arial, sans-serif;
  background-color: #f5f5f5;
  margin: 0;
  padding: 0;
  background-attachment: fixed; /* 背景固定，不跟随页面滚动 */
}

.app-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: 100vh;
  justify-content: center;
}

</style>

