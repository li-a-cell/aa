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

    <!-- 项目阶段 -->
    <section class="project-phase">
      <div class="phase-title">项目当前阶段</div>
      <div class="phase-process">
        <div class="phase-step">
          <i class="icon-clock"></i>
          <span>前期阶段</span>
          <span>xx-xx-xx xx:xx</span>
        </div>
        <div class="phase-step">
          <i class="icon-building"></i>
          <span>施工阶段</span>
          <span>xx-xx-xx xx:xx</span>
        </div>
        <div class="phase-step">
          <i class="icon-code"></i>
          <span>验收阶段</span>
          <span>xx-xx-xx xx:xx</span>
        </div>
        <div class="phase-step">
          <i class="icon-check"></i>
          <span>完成阶段</span>
          <span>xx-xx-xx xx:xx</span>
        </div>
      </div>

      <!-- 添加节点按钮 -->
      <button @click="goToNodePage" class="node-button">查看节点</button>
    </section>

    <!-- 已通过流程 -->
    <section class="project-flow">
      <div class="flow-title">已通过流程</div>
      <div class="flow-list">
        <div class="flow-item" v-for="(item, index) in passedFlow" :key="index">
          <router-link :to="item.link" class="flow-link">
            <span>{{ item.name }}</span>
          </router-link>
          <span>{{ item.time }}</span>
        </div>
      </div>
    </section>

    <!-- 总控计划 -->
    <section class="control-plan">
      <div class="control-title">总控计划</div>
      <div class="control-info">
        <span class="plan-time">xx-xx-xx</span>
        <span class="plan-status">已完成</span>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: "NodePage",
  data() {
    return {
      // 已通过的流程信息
      passedFlow: [
        { name: '审批清单内容', link: '/node/1', time: 'xx分钟' },
        { name: '执行安全检查内容', link: '/node/2', time: 'xx分钟' },
        { name: '安全整改指派', link: '/node/3', time: 'xx分钟' },
        { name: '安全隐患整改', link: '/node/4', time: 'xx分钟' },
        { name: '整改回复', link: '/node/5', time: 'xx分钟' }
      ]
    };
  },
  methods: {
    // 跳转到节点页面
    goToNodePage() {
      this.$router.push({ name: 'NodePage' }); // 通过路由跳转到节点页面
    }
  }
};
</script>

<style scoped>
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
  /* 移除标题部分的边框和阴影 */
  box-shadow: none;
  border: none;
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

/* 项目阶段 */
.project-phase {
  margin-top: 30px;
}

.phase-title {
  font-size: 24px; /* 增加字体大小 */
  font-weight: bold; /* 仍然是粗体 */
  color: #1890ff; /* 蓝色字体 */
  margin-bottom: 20px; /* 增加标题与内容之间的间距 */
  text-align: center; /* 标题居中对齐 */
}

.phase-process {
  display: flex;
  justify-content: space-between;
}

.phase-step {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #555;
  flex: 1;
}

.phase-step i {
  font-size: 18px;
  margin-right: 10px;
  color: #1890ff;
}

.phase-step span {
  display: block;
}

.icon-clock:before {
  content: "⏰"; /* 图标样式 */
}

.icon-building:before {
  content: "🏗️";
}

.icon-code:before {
  content: "🔢";
}

.icon-check:before {
  content: "✅";
}

/* 节点按钮样式 */
.node-button {
  background-color: #1890ff;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 8px 15px;
  cursor: pointer;
  font-size: 14px;
  margin-top: 20px;
}

.node-button:hover {
  background-color: #40a9ff;
}

/* 已通过流程 */
.project-flow {
  background-color: #fff;
  padding: 15px;
  margin-top: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.flow-title {
  font-size: 18px;
  font-weight: bold;
  color: #1890ff;
  margin-bottom: 15px;
}

.flow-list {
  display: flex;
  flex-direction: column;
}

.flow-item {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  color: #555;
  margin: 8px 0;
}

.flow-link {
  color: #1890ff;
  text-decoration: none;
}

.flow-link:hover {
  text-decoration: underline;
}

/* 总控计划 */
.control-plan {
  background-color: #fff;
  padding: 15px;
  margin-top: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.control-title {
  font-size: 18px;
  font-weight: bold;
  color: #1890ff;
  margin-bottom: 15px;
}

.control-info {
  display: flex;
  justify-content: space-between;
}

.plan-time {
  font-size: 14px;
  color: #555;
}

.plan-status {
  font-size: 14px;
  color: #52c41a; /* Green */
  font-weight: bold;
}
</style>
