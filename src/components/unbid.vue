<template>
  <div class="bidding-management">
    <h2 class="title">招标管理部门</h2>

    <!-- 招标任务列表 -->
    <div class="task-list card">
      <h3>待招标任务列表</h3>
      <table>
        <thead>
          <tr>
            <th>项目名称</th>
            <th>截至时间</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="project in unbidDtata" :key="project.projectId">
            <td>{{ project.projectName }}</td>
            <td>{{ project.plannedEndDate }}</td>
            <td>{{ project.status }}</td>
            <td>
              <button v-if="project.status === '待招标'" @click="showConfirmDialog(project)" class="btn primary-btn">查看投标人</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 投标人弹窗 -->
    <modal name="bidder-modal" width="50%" height="auto">
      <div class="modal-content">
        <h3>投标人列表</h3>
        <table>
          <thead>
            <tr>
              <th>投标人ID</th>
              <th>投标人名称</th>
              <th>投标时间</th>
              <th>投标价格</th>
              <th>状态</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="bidder in bidderdata" :key="bidder.bidderId">
              <td>{{ bidder.bidderId }}</td>
              <td>{{ bidder.bidderName }}</td>
              <td>{{ bidder.biddingTime }}</td>
              <td>{{ bidder.biddingPrice }}</td>
              <td>{{ bidder.status }}</td>
            </tr>
          </tbody>
        </table>
        <!-- <button @click="closeModal">关闭</button> -->
      </div>
    </modal>
  </div>
</template>

<script>
import { ref, computed, VueElement } from "vue";
import axios from 'axios';
// import {useModel} from 'vue-js-modal'


export default {
  name: "unlogo",
  setup() {
    const unbid = ref(false); 
    const unbidDtata = ref([]);
    const bidderdata = ref([]);
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

    const showConfirmDialog = (project) => {
      const token = localStorage.getItem('jwtToken');
      console.log("待发布项目token", token);
      const projectId = project.projectId;
      console.log("待发布项目id", projectId);

      axios.post('/api/biddingmanagent/getProjectBiddingRecords', { projectId }, {
        headers: { 
          'Token': token,
          'Content-Type': 'application/json'
        }
      })
      .then(response => {
        const data = response.data;
        console.log("待招标管理任务", data);

        // 确保 response.data.data 是一个数组
        if (Array.isArray(data.data[0])) {
          pendingProjectsData.value = data.data[0];
          console.log("接受待发布项目", pendingProjectsData.value);
          console.log("待发布项目数量", pendingProjectsData.value.projectId);
        } 
        else if (typeof data.data[0] === 'object' && data.data !== null) {
          console.log("未招标对象数据为对象");
          // 假设 data 包含一个名为 projects 的数组
          bidderdata.value = data.data.map((item, index) => ({
            bidderId: item.bidderId, // 生成唯一的 id
            bidderName: item.bidderName,
            biddingTime: item.biddingTime,
            plannedEndDate: item.plannedEndDate,
            biddingPrice: item.biddingPrice,
            status: item.status
          }));
          console.log("bidderdata", bidderdata.value);
          // this.$modal.show('bidder-modal'); // 显示弹窗
        } 
        else {
          console.error("返回的数据不是数组", data.data);
        }
      })
      .catch(error => {
        console.error('获取数据失败', error);
        console.log("Selected task ID:", project.project_id);
      });
    };

    const closeModal = () => {
      // this.$modal.hide('bidder-modal'); // 关闭弹窗
    };

    const confirmPublishBidding = () => {
      update(selectedTaskId.value);
      closeModal();
    };

    const update = (taskId) => {
      const task = tasks.value.find((t) => t.id === taskId);
      const project = projects.value.find((p) => p.id === task.projectId);
      if (task && project) {
        task.status = "待招标";
        project.status = "待招标";
      }
    };

    const unbidthing = async () => {
      const token = localStorage.getItem('jwtToken');
      console.log("待发布项目token", token);
      const status = "待招标";
      try {
        const response = await axios.post('/api/project/status', { status }, {
          headers: { 
            'Token': token,
            'Content-Type': 'application/json'
          }
        });
        console.log("待招标项目原始数据", response.data);

        // 确保 response.data.data 是一个数组
        if (Array.isArray(response.data.data[0])) {
          pendingProjectsData.value = response.data.data[0];
          console.log("接受待发布项目", pendingProjectsData.value);
          console.log("待发布项目数量", pendingProjectsData.value.project_id);
        } 
        else if (typeof response.data.data[0] === 'object' && response.data.data !== null) {
          console.log("未招标对象数据为对象");
          // 假设 data 包含一个名为 projects 的数组
          unbidDtata.value = response.data.data.map((item, index) => ({
            projectId: item.projectId, // 生成唯一的 id
            projectName: item.projectName,
            plannedEndDate: item.plannedEndDate,
            budget: item.budget,
            status: item.status
          }));
          console.log("unbidData", unbidDtata.value);
        }
        else {
          console.error("返回的数据不是数组", response.data.data);
        }
      } catch (error) {
        console.error('获取节点数量失败', error);
      }
    };
    unbidthing();

    return {
      tasks,
      projects,
      stats,
      showConfirmDialog,
      closeModal,
      confirmPublishBidding,
      unbidDtata,
      bidderdata
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