<template>
    <div class="home">
      <h2>项目进展总览</h2>
      <!-- 项目进展信息展示 -->
    </div>
  </template>
  
  <script>
  export default {
    name: 'Home',
    computed: {
      projects() {
        return this.$store.getters.allProjects;
      },
    },
    created() {
      this.$store.dispatch('fetchProjects');
    },
  };
  </script>
  
  <style scoped>
  .home {
    flex: 1;
    padding: 20px;
  }
  </style>
  