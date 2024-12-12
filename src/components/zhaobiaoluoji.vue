<template>
  <div class="bidding-management">
    <h2 class="title">招标管理部门</h2>
    <!-- 投标人筛选 -->
    <div class="filter card">
      <h3>筛选投标人</h3>
      <select v-model="selectedStatus" class="status-dropdown">
        <option value="白名单">白名单</option>
        <option value="黑名单">黑名单</option>
        <option value="所有">所有</option>
      </select>
    </div>

    <!-- 投标人列表 -->
    <div class="bidder-list card">
      <h3>投标人信息</h3>
      <table>
        <thead>
          <tr>
            <th>投标方名称</th>
            <th>公司名称</th>
            <th>投标状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="bid in filteredBids" :key="bid.bidId">
            <td>{{ bid.name }}</td>
            <td>{{ bid.companyName }}</td>
            <td>{{ bid.status }}</td>
            <td>
              <button @click="toggleStatus(bid)" class="btn secondary-btn">修改状态</button>
              <button @click="deleteBidder(bid)" class="btn danger-btn">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>
<script>
import { ref, computed } from "vue";
import axios from "axios";
import { ElMessage } from "element-plus";

export default {
  name: "zhaobiaomanage",
  setup() {
    // 模拟投标人数据
    const bids = ref([])

    // 当前筛选的投标状态
    const selectedStatus = ref("所有");

    // 计算属性：根据 selectedStatus 过滤投标人
    const filteredBids = computed(() => {
      if (selectedStatus.value === "所有") {
        return bids.value;
      } else {
        return bids.value.filter((bid) => bid.status === selectedStatus.value);
      }
    });

    // 切换投标人状态（白名单/黑名单）
    const toggleStatus = (bid) => {
      bid.status = bid.status === "白名单" ? "黑名单" : "白名单";
    //  console.log(`投标人${bid.id}的状态已切换为${bid.status}`);
    };

    // 数据统计
  const stats = computed(() => {
      const pendingProjects = 0; // 示例中没有相关数据
      const biddingTasks = 0; // 示例中没有相关数据
      const ongoingProjects = 0; // 示例中没有相关数据
      return { pendingProjects, biddingTasks, ongoingProjects };
    });
  const bidder = async () => {
  const token = localStorage.getItem('jwtToken');
  console.log("待发布项目token", token);
  const status = "待发布";
  try {
    const response = await axios.get('/api/biddingmanagent/getAllBidders', {
          headers: { 'Token': token }
        });
    console.log("投标人信息", response.data);
    if (Array.isArray(response.data.data[0])) {
      console.log("接受待发布项目", pendingProjectsData.value);
      console.log("待发布项目数量", pendingProjectsData.value.projectId);
    } 
    else if (typeof response.data.data[0] === 'object' && response.data.data !== null) {
       console.log("未招标对象数据为对象");
               // 假设 data 包含一个名为 projects 的数组
         bids.value= response.data.data.map((item, index) => ({
        name: item.name,
        companyName: item.companyName,
        status: item.status,
        id:item.bidderId,
              }));
        console.log("投标人xinxi", bids.value);
    }
      else {
      console.error("返回的数据不是数组", response.data.data);
    }
  } catch (error) {
    console.error('获取节点数量失败', error);
  }
};
const deleteBidder = async (bid) => {
      const token = localStorage.getItem('jwtToken');
      console.log("删除投标人的id", bid.id);
      try {
       // console.log("删除投标人的id", bid.id);
        await axios.delete(`/api/biddingmanagent/deleteBidder/${bid.id}`, {
          headers: { 'Token': token }
        });
        // 从本地数据中移除投标人
        bids.value = bids.value.filter((b) => b.bidId !== bid.bidId);
        ElMessage.success("投标人删除成功");
      } catch (error) {
        console.error('删除投标人失败', error);
        ElMessage.error("删除投标人失败，请稍后再试");
      }
    };
bidder();
    return {
      bids,
      selectedStatus,
      filteredBids,
      toggleStatus,
      stats,
      deleteBidder,
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

.filter {
  background-color: #f9f9f97f; /* 更柔和的背景颜色 */
  border: 1px solid #e0e0e0; /* 添加边框 */
  border-radius: 8px; /* 圆角边框 */
  box-shadow: 0px 2px 6px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
  padding: 16px; /* 增加内边距 */
  margin-bottom: 20px; 
}

.filter h3 {
  font-size: 1.2em; /* 增大标题字体大小 */
  color: #333; /* 深色文字 */
  margin-bottom: 10px; /* 增加标题和筛选框之间的间距 */
}

.status-dropdown {
  padding: 10px 16px; /* 增加内边距 */
  border: 1px solid #ccc; /* 边框颜色 */
  border-radius: 8px; /* 圆角边框 */
  background-color: #fff; /* 白色背景 */
  color: #333; /* 深色文字 */
  font-size: 1em; /* 字体大小 */
  cursor: pointer; /* 鼠标指针样式 */
  transition: border-color 0.3s, box-shadow 0.3s; /* 平滑过渡效果 */
}

.status-dropdown:focus {
  border-color: #007bff; /* 聚焦时的边框颜色 */
  box-shadow: 0px 0px 8px rgba(0, 123, 255, 0.5); /* 聚焦时的阴影效果 */
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

.secondary-btn {
  background-color: #f0f0f0;
  color: #007bff;
}

.danger-btn {
  background-color: #dc3545;
  color: #fff;
}

.success-btn {
  background-color: #28a745;
  color: #fff;
}

.primary-btn:hover,
.secondary-btn:hover,
.danger-btn:hover,
.success-btn:hover {
  opacity: 0.85;
}

.title {
  font-weight: bold;
  font-size: 1.8em;
  color: #333;
  margin-bottom: 20px;
}
</style>
