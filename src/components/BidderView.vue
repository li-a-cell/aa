<template>
  <div class="bidder-view">
    <!-- 页面头部区域 -->
    <el-header class="header" style="height: 100px">
      <div class="header-left">
        <h1>建筑项目管理系统（投标方）</h1>
      </div>
      <div class="header-right">
        <el-avatar :src="userAvatar" size="40" />
        <span class="username">{{ username }}</span>
      </div>
    </el-header>

    <!-- 轮播图 -->
    <el-carousel :interval="3000" type="carousel" height="300px" class="carousel-container" style="background-color: #999999">
      <el-carousel-item>
        <div>这是一个图片1</div>
      </el-carousel-item>
      <el-carousel-item>
        <div>这是一个图片2</div>
      </el-carousel-item>
      <el-carousel-item>
        <div>这是一个图片3</div>
      </el-carousel-item>
    </el-carousel>

    <!-- 项目容器 -->
    <el-main>
      <div class="project-container">
        <div class="project-list">
          <el-card v-for="project in projects" :key="project.projectId" class="project-card" shadow="hover">
            <div slot="header" class="card-header">
              <span>{{ project.projectName }}</span>
            </div>
            <div class="card-body">
              <p><strong>项目经理:</strong> {{ project.managerName }}</p>
              <p><strong>开始日期:</strong> {{ project.startDate }}</p>
              <p><strong>结束日期:</strong> {{ project.endDate }}</p>
              <p><strong>联系电话:</strong> {{ project.managerPhone }}</p>
              <p><strong>投标截止日期:</strong> {{ project.tenderDeadline }}</p>
              <p><strong>项目类型:</strong> {{ project.projectType }}</p>
            </div>
            <div class="card-footer">
              <el-button type="primary" @click="openBidInput(project.projectId)">投标</el-button>
            </div>
          </el-card>
        </div>
      </div>
    </el-main>

    <!-- 页脚 -->
    <el-footer class="footer" style="background-color: #00aaff; color: white;height: 200px">
      <div class="company-info">
        <p><strong>公司名称：</strong>XX建筑工程有限公司</p>
        <p><strong>公司地址：</strong>XX市XX区XX路XX号</p>
        <p><strong>联系电话：</strong>1234567890</p>
        <p><strong>版权声明：</strong>Copyright © 2024 XX建筑工程有限公司. All rights reserved.</p>
      </div>
    </el-footer>

    <!-- 投标金额输入界面 -->
    <div class="bid-modal" v-if="bidModalVisible">
      <div class="bid-modal-content">
        <!-- 项目详细信息框 -->
        <div class="project-details" v-if="projectDetails">
          <p><strong>项目名称:</strong> {{ projectDetails.projectName }}</p>
          <p><strong>项目经理:</strong> {{ projectDetails.managerName }}</p>
          <p><strong>计划开始日期:</strong> {{ projectDetails.plannedStartDate }}</p>
          <p><strong>计划结束日期:</strong> {{ projectDetails.plannedEndDate }}</p>
          <p><strong>项目地点:</strong> {{ projectDetails.siteName }}</p>
          <p><strong>预算:</strong> {{ projectDetails.budget }}</p>
          <p><strong>项目描述:</strong> {{ projectDetails.description }}</p>
          <p><strong>项目类型:</strong> {{ projectDetails.projectType }}</p>
        </div>

        <h3>为项目{{ currentProjectId }}投标</h3>
        <el-input v-model="biddingPrice" type="number" placeholder="请输入投标金额"></el-input>

        <div class="modal-footer">
          <el-button @click="closeBidInput">取消</el-button>
          <el-button type="primary" @click="confirmBid">确认投标</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import axios from 'axios';

export default {
  name: 'BidderView',
  data() {
    return {
      userAvatar: 'https://example.com/path/to/avatar.jpg',
      username: '用户名',
      projects: [],
      bidModalVisible: false,
      currentProjectId: null,
      biddingPrice: 0,
      bidderId: null,
      projectDetails: null, // 用于存储点击投标后获取的项目详细信息
      hasBidBefore: false // 用于标记是否已经对当前项目投过标
    };
  },
  mounted() {
    this.fetchProjects();
    const storedBidderId = localStorage.getItem('bidderId');
    if (storedBidderId) {
      this.bidderId = storedBidderId;
      console.log('已从本地存储中获取投标方ID：', this.bidderId);
    }
  },
  methods: {
    async fetchProjects() {
      try {
        const response = await axios.get('/bidders/tender-project-view/all');
        if (response.status === 200) {
          this.projects = response.data;
        }
      } catch (error) {
        console.error('获取项目信息失败', error);
      }
    },
    openBidInput(projectId) {
      this.currentProjectId = projectId;
      this.bidModalVisible = true;

      // 点击投标按钮后获取项目详细信息
      this.fetchProjectDetails(projectId);
    },
    closeBidInput() {
      this.bidModalVisible = false;
    },
    async confirmBid() {
      const biddingTime = new Date();
      // 检查投标金额是否超过项目预算
      if (this.biddingPrice <= this.projectDetails.budget) {
        ElMessage.error('投标金额必须超过项目预算，请重新输入');
        return;
      }

      // 检查是否已经对该项目投过标
      if (this.hasBidBefore) {
        ElMessage.error('您已经对该项目投过标，不能再次投标');
        return;
      }

      try {
        const response = await axios.post('/bidders/bid', {
          projectId: this.currentProjectId,
          bidderId: this.bidderId,
          biddingPrice: this.biddingPrice,
          biddingTime: biddingTime
        });

        if (response.status === 200) {
          ElMessage.success('投标成功');
          this.closeBidInput();
          this.fetchProjects();
          // 标记已经对该项目投过标
          this.hasBidBefore = true;
        } else {
          ElMessage.error('投标失败，请稍后再试');
        }
      } catch (error) {
        ElMessage.error('投标失败，请稍后再试');
      }
    },
    async fetchProjectDetails(projectId) {
      try {
        const response = await axios.get(`/bidders/project/${projectId}`);
        if (response.status === 200) {
          this.projectDetails = response.data;
        }
      } catch (error) {
        console.error('获取项目详细信息失败', error);
        ElMessage.error('获取项目详细信息失败，请稍后再试');
      }
    }
  }
};
</script>

<style scoped>
/* 页面头部的样式定义 */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: #00aaff;
  color: white;
}

.header-left h1 {
  margin: 0;
}

.header-right {
  display: flex;
  align-items: center;
}

.username {
  margin-left: 10px;
  color: white;
}

/* 项目容器的样式定义，用于包裹项目列表 */
.project-container {
  padding: 0 200px;
}

/* 项目列表的样式定义，用于排列项目卡片 */
.project-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

/* 项目卡片的样式定义 */
.project-card {
  width: calc(33.33% - 20px);
  margin-bottom: 20px;
  min-height: 300px;
  max-height: 380px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  position: relative;
  box-sizing: border-box;
}

/* 项目卡片头部的样式定义 */
.card-header {
  font-weight: bold;
}

/* 项目卡片主体内容的样式 */
.card-body p {
  margin: 5px 0;
  font-size: 14px;
}

/* 投标金额输入界面样式 */
.bid-modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 400px;
  background-color: rgba(0, 0, 0, 0);
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 10px;
}

.bid-modal-content {
  background: white;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  width: 100%;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

/* 项目详细信息框样式 */
.project-details {
  margin-bottom: 20px;
  border-bottom: 1px solid #ccc;
  padding-bottom: 20px;
}

/* 页脚（底栏）的样式定义 */
.footer {
  background-color: #00aaff;
  padding: 10px;
  text-align: center;
}

.footer.company-info p {
  margin: 0;
  line-height: 2.5;
  color: white;
}

/* 轮播图的样式定义 */
.el-carousel {
  margin: 20px 0;
}

.el-carousel-item {
  text-align: center;
}

.el-carousel-item div {
  font-size: 24px;
  color: white;
}
</style>
