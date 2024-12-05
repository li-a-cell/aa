<template>
    <div class="create-project-page">
      <div class="card">
        <h1>创建项目</h1>
        <form @submit.prevent="submitProject">
          <div class="form-row">
            <label>项目名称</label>
            <input v-model="form.projectName" type="text" placeholder="请输入项目名称" required />
          </div>
  
          <div class="form-row">
            <label>计划开始日期</label>
            <input v-model="form.plannedStartDate" type="date" required />
          </div>
  
          <div class="form-row">
            <label>计划结束日期</label>
            <input v-model="form.plannedEndDate" type="date" required />
          </div>
  
          <div class="form-row">
            <label>项目预算</label>
            <input v-model="form.budget" type="number" placeholder="请输入预算金额" required />
          </div>
  
          <div class="form-row">
            <label>项目描述</label>
            <textarea v-model="form.projectDescription" placeholder="请输入项目描述" rows="4"></textarea>
          </div>
  
          <div class="form-row">
            <label>施工地点名称</label>
            <input v-model="form.siteName" type="text" placeholder="请输入施工地点名称" required />
          </div>
  
          <div class="form-row">
            <label>项目状态</label>
            <select v-model="form.status" required>
              <option value="待发布">待发布</option>
              <option value="待招标">待招标</option>
              <option value="施工中">施工中</option>
              <option value="已完成">已完成</option>
            </select>
          </div>
  
          <div class="form-row">
            <label>项目类型</label>
            <select v-model="form.projectType"  required >
              <option value="市政工程">市政工程</option>
              <option value="房屋建筑">房屋建筑</option>
            </select>
          </div>
  
          <div class="form-actions">
            <button type="submit" class="primary-button">提交</button>
            <button type="button" class="secondary-button" @click="resetForm">重置</button>
          </div>
        </form>
      </div>
    </div>
  </template>
  
  <script>
  import { defineComponent } from 'vue';
  import { useRouter } from 'vue-router';
  import { ref } from 'vue';
  import axios from 'axios';
  
  export default {
    name: 'CreateProject',
    setup() {
      const router = useRouter();
  
      const form = ref({
        projectName: '',
        plannedStartDate: '',
        plannedEndDate: '',
        budget: '',
        projectDescription: '',
        siteName: '',
        status: '',
        projectType: '',
      });
  
      const resetForm = () => {
        form.value = {
          projectName: '',
          plannedStartDate: '',
          plannedEndDate: '',
          budget: '',
          projectDescription: '',
          siteName: '',
          status: '',
          projectType: '',
        };
      };
      const token = localStorage.getItem('jwtToken');
      console.log("token", token);
      console.log("form", form.value);
      const submitProject = async () => {
        try {
          form.value.budget = String(form.value.budget);
          const response = await axios.post('http://localhost:9528/manager/createProject', form.value, {
            headers: {
            'Content-Type': 'application/json',
            'token': token
            },
          });
          console.log('创建项目成功！');
          router.push({name: 'Manahome'});

        } catch (error) {
          console.error('创建项目时出错:', error);
          alert('项目创建失败，请稍后再试！');
        }
      };
  
      return {
        form,
        submitProject,
        resetForm,
      };
    },
  };
  </script>
  
  <style scoped>
  /* 背景渐变 */
  .create-project-page {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background: linear-gradient(135deg, #f0f8ff, #e6eefc);
  }
  
  /* 卡片容器 */
  .card {
    backdrop-filter: blur(10px);
    background: rgba(255, 255, 255, 0.8);
    border-radius: 20px; /* 超椭圆 */
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15); /* 非纯黑阴影 */
    padding: 30px;
    width: 400px;
    text-align: center;
  }
  
  /* 标题样式 */
  h1 {
    font-size: 24px;
    color: #333;
    margin-bottom: 20px;
    font-weight: bold;
  }
  
  /* 表单行 */
  .form-row {
    display: flex;
    flex-direction: column;
    margin-bottom: 20px; /* 双倍间距 */
  }
  
  /* 标签 */
  label {
    font-size: 16px;
    margin-bottom: 8px;
    font-weight: bold;
    color: #555;
  }
  
  /* 输入框和选择框 */
  input,
  textarea,
  select {
    font-size: 14px;
    padding: 12px;
    border: none; /* 减少线条 */
    border-radius: 12px;
    background-color: #f7f9fc;
    box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.05); /* 内阴影 */
    outline: none;
    transition: box-shadow 0.3s ease;
  }
  
  input:focus,
  textarea:focus,
  select:focus {
    box-shadow: 0 0 8px rgba(0, 123, 255, 0.3);
  }
  
  /* 按钮层级 */
  .primary-button {
    padding: 10px 20px;
    font-size: 16px;
    font-weight: bold;
    color: white;
    background: linear-gradient(135deg, #007bff, #0056b3);
    border: none;
    border-radius: 12px;
    cursor: pointer;
    transition: background 0.3s ease, transform 0.2s ease;
  }
  
  .primary-button:hover {
    background: linear-gradient(135deg, #0056b3, #003e7e);
    transform: translateY(-2px); /* 提升感 */
  }
  
  .secondary-button {
    padding: 10px 20px;
    font-size: 16px;
    font-weight: bold;
    color: #555;
    background-color: transparent;
    border: 2px solid #ccc;
    border-radius: 12px;
    cursor: pointer;
    margin-top: 10px;
  }
  
  .secondary-button:hover {
    border-color: #888;
  }
  
  /* 间距留白 */
  textarea {
    resize: none;
    line-height: 1.6; /* 提高行高 */
  }
  </style>
  