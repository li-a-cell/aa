<template>
    <div class="bidding-management">
      <h2 class="title">招标管理部门</h2>
  
      <!-- 招标任务列表 -->
      <div class="task-list card">
        <h3>待发布任务列表</h3>
        <table>
          <thead>
            <tr>
              <th>项目ID</th>
              <th>项目名称</th>
              <th>截至时间</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(project, index) in pendingProjectsData" :key="project.projectId">
              <td>{{ project.projectId }}</td>
              <td>{{ project.projectName }}</td>
              <td>{{ project.plannedEndDate }}</td>
              <td>{{ project.status }}</td>
              <td>
                <button v-if="project.status === '待发布'" @click="showConfirmDialog(project)" class="btn primary-btn">发布招标</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </template>
  
  <script>
  import { ref, computed } from "vue";
  import { ElDialog, ElButton } from 'element-plus';
  import axios from 'axios';
  
  export default {
    name: "unlogo",
    components: {
      ElDialog,
      ElButton
    },
    setup() {
      const pendingProjectsData = ref([]);
      const tasks = ref([
        { id: 1, projectId: 1, projectName: "项目A", status: "待发布" },
        { id: 2, projectId: 2, projectName: "项目B", status: "待发布" },
      ]);
      const projects = ref([
        { id: 1, name: "项目A", status: "待发布", contractorId: null },
        { id: 2, name: "项目B", status: "待发布", contractorId: null },
      ]);
  
      const stats = computed(() => {
        const pendingProjects = projects.value.filter((p) => p.status === "待发布").length;
        const biddingTasks = tasks.value.filter((t) => t.status === "待招标").length;
        const ongoingProjects = projects.value.filter((p) => p.status === "施工中").length;
        return { pendingProjects, biddingTasks, ongoingProjects };
      });
  
      const dialogVisible = ref(false);
      const selectedTaskId = ref(null);
  
      const showConfirmDialog = (project) => {
        project.status="已发标";
        console.log("Selected task ID:", project.value);
      };
  
      const closeDialog = () => {
        dialogVisible.value = false;
      };
  
      const confirmPublishBidding = () => {
        update(selectedTaskId.value);
        closeDialog();
      };
  
      const update = (taskId) => {
        const task = tasks.value.find((t) => t.id === taskId);
        const project = projects.value.find((p) => p.id === task.projectId);
        if (task && project) {
          task.status = "待招标";
          project.status = "待招标";
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
relogthing();
      return {
        tasks,
        projects,
        stats,
        showConfirmDialog,
        dialogVisible,
        closeDialog,
        confirmPublishBidding,
        pendingProjectsData
      };
    },
  };
  </script>
  
  <style scoped>
  /* 页面背景颜色优化 */
  .bidding-management {
    background-color: #f4f6f9;
    padding: 20px;
  }
  
  .el-dialog {
    display: block !important; /* 强制显示 */
    z-index: 9999; /* 确保在最上层 */
  }
  
  /* 卡片样式 */
  .card {
    background: rgba(255, 255, 255, 0.85);
    backdrop-filter: blur(8px);
    border-radius: 12px;
    padding: 20px;
    margin-bottom: 20px;
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.08);
    border: 1px solid rgba(0, 0, 0, 0.05);
  }
  
  /* 表格样式 */
  table {
    width: 100%;
    border-collapse: collapse;
    background-color: #ffffff;
    border-radius: 8px;
    overflow: hidden;
  }
  
  th, td {
    text-align: left;
    padding: 12px;
  }
  
  th {
    background: #007bff;
    color: #fff;
  }
  
  td {
    border-bottom: 1px solid #e0e0e0;
  }
  
  /* 按钮样式 */
  .btn {
    padding: 10px 16px;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    font-weight: bold;
    transition: all 0.3s;
  }
  
  .primary-btn {
    background-color: #007bff;
    color: #fff;
  }
  
  .primary-btn:hover {
    opacity: 0.85;
  }
  
  /* 其他常规优化 */
  .title {
    font-weight: bold;
    font-size: 1.8em;
    color: #333;
    margin-bottom: 20px;
  }
  </style>