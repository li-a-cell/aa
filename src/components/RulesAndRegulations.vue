<template>
  <div class="regulations-container">
    <div 
      v-for="regulation in regulations" 
      :key="regulation.id" 
      class="regulation-item" 
      @click="handleRowClick(regulation)"
    >
      <div class="regulation-title">{{ regulation.name }}</div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
  name: "RegulationsTable",
  setup() {
    // 使用 ref 来创建响应式的数据
  
    const regulations = ref([
      { id: 1, name: "制度一" },
      { id: 2, name: "制度二" },
      { id: 3, name: "制度三" },
    ]);
    // 使用 useRouter 来获得路由对象
    const router = useRouter();
    const error = ref(null); // 用于存储错误信息
    const users = ref([]); // 用于存储从 API 获取的数据
    const handleLoginregulation = () => {
    const token = localStorage.getItem('jwtToken');
    if (token) {
        console.log("不为空", token);
        axios.get(`/api/manager/getAllRegulations`, {
            headers: {
                'Token': token
            },
        })
        .then(response => {
            const data = response.data.data;
            console.log("data",data);
            if (!data || Object.keys(data).length === 0) {
                // 处理空数据的情况
                console.log('返回的数据为空');
                // 可以在这里显示提示信息或执行其他操作
                // 例如，更新UI显示提示信息
                error.value = '没有正在进行的项目';
            } else {
                // 处理非空数据的情况
                console.log('受保护的数据：', data);
                // 假设 data 包含一个名为 projects 的数组
                const parsedData = data.map((name, index) => ({
          id: index + 1, // 生成唯一的 id
          name: name,
        }));
        regulations.value = parsedData;
            }
        })
        .catch(error => {
            console.error('获取数据失败', error);
            error.value = '获取数据失败，请稍后再试';
        });
    }
};
    handleLoginregulation();

    // 处理点击行的函数
    const handleRowClick = (row) => {
      localStorage.setItem('savedName', row.name);
      router.push({ name: "RegulationDetail", params: { name: row.name, } });
      // 保存数据到 Local Storage
    console.log("After setItem, savedName in localStorage:", localStorage.getItem('savedName'));

// 读取并打印数据
let sane = localStorage.getItem('savedName');
console.log("savedName", sane);
    };

    return {
      regulations,
      handleRowClick,
      error,
      users,
    };
  },
};
</script>

<style scoped>
.regulations-container {
  padding: 20px;
  background-color: #ffffff;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.regulation-item {
  padding: 15px;
  border-bottom: 1px solid #ccc;
  cursor: pointer;
  background-color: #f0f0f0;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.regulation-item:last-child {
  border-bottom: none;
}

.regulation-title {
  font-size: 18px;
  font-weight: bold;
  color: #444;
  margin-bottom: 5px;
}

.regulation-date {
  font-size: 14px;
  color: #777;
  text-align: right; /* 将制定时间文本右对齐 */
}

.regulation-item:hover {
  background-color: #e0e0e0;
  transform: scale(1.02);
}
</style> 