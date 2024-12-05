<template>
    <div class="create-project-page">
      <h1>创建项目</h1>
      <button @click="createProject">创建项目</button>
    </div>
  </template>
  
  <script>
  import { defineComponent } from 'vue';
  import { useRouter } from 'vue-router';
  
  export default defineComponent({
    name: 'CreateProject',
    setup() {
      const router = useRouter();
  
      const createProject = () => {
        console.log('创建项目');
        router.push({ name: 'ProjectForm' });
      };
  
      return {
        createProject,
      };
    },
  });
  </script>
  
  <style scoped>
  .create-project-page {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100vh;
    background-color: #f0f4fa;
  }
  
  h1 {
    font-size: 24px;
    margin-bottom: 20px;
  }
  
  button {
    padding: 10px 20px;
    font-size: 16px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  button:hover {
    background-color: #0056b3;
  }
  </style>
  