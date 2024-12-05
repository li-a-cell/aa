<template>
  <div class="statistics-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="stat-card">
          <img class="stat-icon" src="../assets/avatar.png" alt="待发布项目数">
          <div class="stat-info">
            <div class="stat-number">{{ daifabu }}</div>
            <div class="stat-label">待发布项目数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card">
          <img class="stat-icon" src="../assets/gailan.png" alt="待招标任务数">
          <div class="stat-info">
            <div class="stat-number">{{ daizhaobiao }}</div>
            <div class="stat-label">待招标任务数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card">
          <img class="stat-icon" src="../assets/logo.png" alt="施工中项目数">
          <div class="stat-info">
            <div class="stat-number">{{ shigongzhong }}</div>
            <div class="stat-label">施工中项目数</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="24">
        <h2>待招标管理任务</h2>
        <el-table :data="unbidDtata" style="width: 100%">
          <el-table-column prop="projectId" label="项目ID" />
          <el-table-column prop="projectName" label="项目名称" />
          <el-table-column prop="plannedEndDate" label="计划结束日期" />
          <el-table-column prop="budget" label="预算" />
         <el-table-column prop="status" label="状态" />
         
        </el-table>
      </el-col>
    </el-row>

    <el-row>
  <el-col :span="24">
    <h2>待发布管理任务</h2>
    <el-table :data="pendingProjectsData" style="width: 100%">
      <el-table-column prop="projectId" label="项目ID" />
      <el-table-column prop="projectName" label="项目名称" />
      <el-table-column prop="plannedEndDate" label="计划结束日期" />
      <el-table-column prop="budget" label="预算" />
      <el-table-column prop="status" label="状态" />
    </el-table>
  </el-col>
</el-row>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElCard, ElRow, ElCol, ElTable, ElTableColumn } from 'element-plus'
import { useRouter } from 'vue-router'
import axios  from 'axios';

// 模拟数据
const unbid = ref([
  { id: 1, name: '项目A', status: '待发布' },
  { id: 2, name: '项目B', status: '待发布' }
])

const biddingTasks = ref([
  { id: 1, name: '项目A', status: '待招标' },
  { id: 2, name: '项目B', status: '待招标' }
])

const ongoingProjects = ref([
  { id: 1, name: '项目A', status: '施工中', constructionPartyId: 101 },
  { id: 2, name: '项目B', status: '施工中', constructionPartyId: 103 }
])

const projectBids = ref([
  { projectId: 1, bidderId: 101, bidAmount: 100000 },
  { projectId: 1, bidderId: 102, bidAmount: 120000 },
  { projectId: 2, bidderId: 103, bidAmount: 150000 }
])
const pendingProjectsData = ref([]);
const unbidDtata = ref([]);
const daifabu = ref(0);
const shigongzhong = ref(0);
const daizhaobiao = ref(0);
const reloggnum  = async () => {
  const token = localStorage.getItem('jwtToken');
  console.log("待发布token", token);
  const status="待发布";
  try {
    const response = await axios.post('http://localhost:9528/project/projectnum',{status} ,{
        headers: { 
          'Token': token,
          'Content-Type': 'application/json'
        }
         }
      );
    console.log("待发布数量", response.data.data);
    daifabu.value = response.data.data;
  } catch (error) {
    console.error('获取节点数量失败', error);
  }
};    
const relogthing = async () => {
  const token = localStorage.getItem('jwtToken');
  console.log("待发布项目token", token);
  const status = "待发布";
  try {
    const response = await axios.post('http://localhost:9528/project/status', { status }, {
      headers: { 
        'Token': token,
        'Content-Type': 'application/json'
      }
    });
    console.log("待发布项目原始数据", response.data);

    // 确保 response.data.data 是一个数组
    if (Array.isArray(response.data.data[0])) {
      pendingProjectsData.value = response.data.data[0];
      console.log("接受待发布项目", pendingProjectsData.value);
      console.log("待发布项目数量", pendingProjectsData.value.projectId);
    } 
    else if (typeof response.data.data[0] === 'object' && response.data.data !== null) {
       console.log("未招标对象数据为对象");
               // 假设 data 包含一个名为 projects 的数组
         pendingProjectsData.value= response.data.data.map((item, index) => ({
         projectId: index + 1, // 生成唯一的 i, // 生成唯一的 id
         projectName: item.projectName,
         plannedEndDate:item.plannedEndDate,
         budget: item.budget,
         status: item.status
        }));
        console.log("更爱后发布项目", pendingProjectsData.value);
    }
      else {
      console.error("返回的数据不是数组", response.data.data);
    }
  } catch (error) {
    console.error('获取节点数量失败', error);
  }
};
const undothing = async () => {
  const token = localStorage.getItem('jwtToken');
  console.log("待招标项目token", token);
  const status = "待招标";
  try {
    const response = await axios.post('http://localhost:9528/project/status', { status }, {
      headers: { 
        'Token': token,
        'Content-Type': 'application/json'
      }
    });
    console.log("待招标项目原始数据", response.data);

    // 确保 response.data.data 是一个数组
    if (Array.isArray(response.data.data[0])) {
      unbidDtata.value = response.data.data[0];
      console.log("接受待发布项目", pendingProjectsData.value);
      console.log("待发布项目数量", pendingProjectsData.value.project_id);
    } 
    else if (typeof response.data.data[0] === 'object' && response.data.data !== null) {
                 // 假设 data 包含一个名为 projects 的数组
        unbidDtata.value= response.data.data.map((item, index) => ({
         projectId: index + 1, // 生成唯一的 i, // 生成唯一的 id
         projectName: item.projectName,
         plannedEndDate:item.plannedEndDate,
         budget: item.budget,
         status: item.status
        }));
        
    }
      else {
      console.error("返回的数据不是数组", response.data.data);
    }
  } catch (error) {
    console.error('获取节点数量失败', error);
  }
};
const ongoingnum = async () => {
  const token = localStorage.getItem('jwtToken');
  console.log("施工中token", token);
  const status="施工中";
  try {
    const response = await axios.post('http://localhost:9528/project/projectnum',{status} ,{
        headers: { 
          'Token': token,
          'Content-Type': 'application/json'
        }
         }
      );
    console.log("正在进行项目数量", response.data.data);
    shigongzhong.value = response.data.data;
  } catch (error) {
    console.error('获取节点数量失败', error);
  }
};
const biddingnum= async () => {
  const token = localStorage.getItem('jwtToken');
  console.log("带招标token", token);
  const status="待招标";
  try {
    const response = await axios.post('http://localhost:9528/project/projectnum',{status} ,{
        headers: { 
          'Token': token,
          'Content-Type': 'application/json'
        }
         }
      );
    console.log("带招标数量", response.data.data);
   daizhaobiao.value = response.data.data;
  } catch (error) {
    console.error('获取节点数量失败', error);
  }
};
const unlog = async () => {
      const token = localStorage.getItem('jwtToken');
      //console.log("待发布项目列表token", token);
      if (token) {
        try {
          const response = await axios.get('http://localhost:9528/biddingmanagent/getbiddingrecord', {
            headers: { 'Token': token,
               'Content-Type': 'application/json'
             }
          });
          const data = response.data;
          console.log("待招标管理任务", data);
        } catch (error) {
          console.error('获取数据失败', error);
        }
      }
    };
    ongoingnum();
    biddingnum();
    reloggnum();
    unlog();
    relogthing();
    undothing();
</script>
<style scoped>
.statistics-container {
  padding: 20px;
  background: linear-gradient(135deg, #f0f0f0, #e0e0e0);
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  background: rgba(240, 248, 255, 0.8); /* 浅蓝色底色 */
  border-radius: 10px;
  border: 2px solid rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  margin-bottom: 20px;
}

.stat-icon {
  width: 40px;
  height: 40px;
  margin-right: 20px;
  border: 2px solid #409EFF;
  border-radius: 50%;
}

.stat-info {
  text-align: left;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 16px;
  color: #606266;
  line-height: 1.5;
}

/* 其他样式优化 */
.stat-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
  transform: translateY(-2px);
  transition: all 0.3s ease;
}

.stat-card .stat-icon {
  transition: transform 0.3s ease;
}

.stat-card:hover .stat-icon {
  transform: scale(1.1);
}

.stat-card .stat-number {
  font-family: 'Arial', sans-serif;
}

.stat-card .stat-label {
  font-family: 'Arial', sans-serif;
}

/* 表格样式 */
.el-table {
  margin-top: 20px;
}

.el-table th {
  background-color: #f0f0f0;
  color: #333;
}

.el-table td {
  text-align: center;
}
</style>