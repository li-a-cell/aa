<template>
    <div class="regulation-detail-container">
      <h1 class="regulation-title">{{ regulation.name }}</h1>
      <div class="regulation-content">
        <p>{{ regulation.content }}</p>
      </div>
      <button @click="goBack">返回</button>
    </div>
  </template>
  
  <script>
  import { ref, onMounted } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import axios from 'axios';
  
  export default {
    name: "RegulationDetail",
    setup() {
      const router = useRouter();
      const route = useRoute(); // 定义 route 变量
      const regulation = ref({
        id: null,
        name: '',
        content: ''
      });
  
      // 获取规章制度详情
      const postRegulationDetail = async () => {
  try {
    console.log("name", name);
    const token = localStorage.getItem('jwtToken');
    const rname = localStorage.getItem('savedName');
    console.log("token", token);
    if (token) {
      const response = await axios.post(
        `/api/manager/getRegulations`,
        JSON.stringify({
          regulationName: rname
        }),
        {
          headers: {
            'Content-Type': 'application/json',
            'Token': token
          }
        }
      );
      const data = response.data.data;
      console.log(data);
      regulation.value = {
        id: data.regulationId,
        name: data.regulationName,
        content: data.regulationContent
      }; 
      console.log('POST 响应数据:', data);
    }
  } catch (error) {
    console.error('POST 请求失败', error);
  }
};
  
      // 在组件挂载后调用 postRegulationDetail
      onMounted(() => {
        postRegulationDetail();
      });
  
      // 返回上一页
      const goBack = () => {
        router.go(-1);
      };
  
      return {
        regulation,
        goBack
      };
    }
  };
  </script>
  
  <style scoped>
  .regulation-detail-container {
    padding: 20px;
    background-color: #ffffff;
    border: 1px solid #ccc;
    border-radius: 5px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    max-width: 800px;
    margin: 0 auto;
  }
  
  .regulation-title {
    font-size: 24px;
    font-weight: bold;
    color: #333;
    margin-bottom: 20px;
  }
  
  .regulation-content {
    font-size: 16px;
    color: #555;
    line-height: 1.6;
    margin-bottom: 20px;
  }
  
  button {
    background-color: #007bff;
    color: #fff;
    border: none;
    padding: 10px 20px;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }
  
  button:hover {
    background-color: #0056b3;
  }
  </style>