<template>
  <div class="project-overview">
    <!-- 项目标题部分 -->
    <header class="project-header">
      <h1 class="project-title">项目名称 (xxxxxx)</h1>
      <div class="project-meta">
        <span>项目类型: xxx</span>
        <span>项目经理: xxx</span>
        <span>项目周期: xxx</span>
      </div>
    </header>

    <!-- 综合部分 -->
    <section class="project-summary">
      <div class="summary-title">
        综合
        <!-- 节点按钮 -->
        <button @click="goToNodePage" class="node-button">节点</button>
      </div>
      <div class="summary-content">
        <div class="info-item">
          <i class="icon-location"></i>
          项目地址: xxxxxx
        </div>
        <div class="info-item">
          <i class="icon-department"></i>
          项目所属部门: xxxxxx
        </div>
        <div class="info-item">
          <i class="icon-building"></i>
          楼栋名称: xxxxxx
        </div>
        <div class="info-item">
          <i class="icon-code"></i>
          楼栋编号: xxxxxx
        </div>
        <div class="info-item">
          <i class="icon-clock"></i>
          项目创建时间: xxxx-xx-xx xx:xx
        </div>
        <div class="info-item">
          <i class="icon-user"></i>
          项目创建人: xxx
        </div>
        <div class="info-item">
          <i class="icon-description"></i>
          项目描述:
          <p>xxxxxx</p>
        </div>
      </div>
      <div class="project-deadline">
        <i class="icon-calendar"></i>
        项目剩余时间: <span class="days-left">62</span> 天
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: "TaskManagement",
  methods: {
    // 跳转到节点页面
    goToNodePage() {
      this.$router.push({ name: 'NodePage' }); // 通过路由跳转到节点页面
    }
  }
};
</script>

<style scoped>
/* 全局布局 */
.project-overview {
  width: 1620px;
  height: 580px;
  font-family: 'Arial', sans-serif;
  color: #333;
  background-color: #f9fbfd; /* 浅蓝背景 */
  padding: 0px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

/* 项目标题部分 */
.project-header {
  background-color: #e6f7ff;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.project-title {
  margin: 0;
  font-size: 24px;
  color: #1890ff;
}

.project-meta {
  font-size: 14px;
  color: #555;
  margin-top: 5px;
}
/* 综合部分 */
.project-summary {
  background-color: #ffffff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 1px 5px rgba(0, 0, 0, 0.1);
}

.summary-title {
  font-size: 20px;
  font-weight: bold;
  color: #1890ff;
  margin-bottom: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 按钮样式 */
.node-button {
  background-color: #1890ff;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 8px 15px;
  cursor: pointer;
  font-size: 14px;
}

.node-button:hover {
  background-color: #40a9ff;
}

/* 项目内容部分 */
.summary-content {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.info-item {
  flex: 1 1 45%;
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #666;
}

.info-item i {
  font-size: 16px;
  margin-right: 10px;
  color: #1890ff;
}

/* 项目剩余时间 */
.project-deadline {
  margin-top: 20px;
  font-size: 16px;
  font-weight: bold;
  color: #ff4d4f;
}

.days-left {
  font-size: 18px;
  color: #52c41a;
}

/* 图标样式 (可用 FontAwesome 或类似库) */
.icon-location:before {
  content: "📍";
}

.icon-department:before {
  content: "🏢";
}

.icon-building:before {
  content: "🏗️";
}

.icon-code:before {
  content: "🔢";
}

.icon-clock:before {
  content: "⏰";
}

.icon-user:before {
  content: "👤";
}

.icon-description:before {
  content: "📝";
}

.icon-calendar:before {
  content: "📅";
}
</style>
