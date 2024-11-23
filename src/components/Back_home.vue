<template>
  <div class="dashboard-container">
    <!-- 顶部统计信息 -->
    <div class="top-stats-section">
      <div v-for="(stat, index) in topStats" :key="index" class="stat-card card stat-background">
        <h2>{{ stat.title }}</h2>
        <p class="stat-value">{{ stat.value }}</p>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="charts-section">
      <div class="chart-card card chart-background">
        <h2>项目所处阶段数量统计图</h2>
        <div class="chart-container">
          <canvas id="projectPhaseChart"></canvas>
        </div>
      </div>
      <div class="chart-card card chart-background">
        <h2>项目类型统计图</h2>
        <div class="chart-container">
          <canvas id="projectTypeChart"></canvas>
        </div>
      </div>
      <div class="chart-card card chart-background">
        <h2>项目类型金额总数统计图</h2>
        <div class="chart-container">
          <canvas id="contractAmountChart"></canvas>
        </div>
      </div>
    </div>

    <!-- 进度条及统计图区域 -->
    <div class="progress-and-stats-section">
      <div class="progress-section card">
        <h2>具体项目进度统计图</h2>
        <div v-for="(progress, index) in projectProgress" :key="index" class="progress-bar-container">
          <label>{{ progress.name }}</label>
          <div class="progress-bar">
            <div class="progress" :style="{ width: progress.value + '%' }"></div>
          </div>
          <span>{{ progress.value }}%</span>
        </div>
      </div>
      <div class="chart-card card chart-background">
        <h2>近五个月发标数量统计图</h2>
        <div class="chart-container">
          <canvas id="biddingChart"></canvas>
        </div>
      </div>
      <div class="chart-card card chart-background">
        <h2>近五个月员工总数折线统计图</h2>
        <div class="chart-container">
          <canvas id="employeeChart"></canvas>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import Chart from 'chart.js/auto';

export default {
  name: 'AdminDashboard',
  setup() {
    const topStats = ref([
      {title: '项目总数', value: '127,825,000'},
      {title: '进行中项目数量', value: '21,100,000'},
      {title: '已完成项目数量', value: '7,340,000'},
      {title: '招标总数', value: '1,870,974'},
      {title: '员工数量', value: '1,266,526'},
    ]);

    const projectProgress = ref([
      {name: '交大园区基础建设', value: 40},
      {name: '农业产业一体化项目', value: 49},
      {name: '商业区改造项目', value: 5},
      {name: '外国语学校扩建', value: 50},
      {name: '交大东大数据工程', value: 0},
    ]);

    const initializeCharts = () => {
      // 项目阶段图表
      const projectPhaseCtx = document.getElementById('projectPhaseChart').getContext('2d');
      new Chart(projectPhaseCtx, {
        type: 'doughnut',
        data: {
          labels: ['开始阶段', '施工阶段', '竣工阶段', '完成阶段'],
          datasets: [
            {
              data: [10, 20, 30, 40],
              backgroundColor: ['#0055FF', '#33CCFF', '#FF9933', '#33CC33'],
            },
          ],
        },
        options: {
          responsive: true,
          plugins: {
            legend: {
              position: 'right', // 修改为显示在图表右方
            },
          },
        },
      });

      // 项目类型图表
      const projectTypeCtx = document.getElementById('projectTypeChart').getContext('2d');
      new Chart(projectTypeCtx, {
        type: 'doughnut',
        data: {
          labels: ['房屋建筑', '市政工程'],
          datasets: [
            {
              data: [15, 25],
              backgroundColor: ['#0055FF', '#33CCFF'],
            },
          ],
        },
        options: {
          responsive: true,
          plugins: {
            legend: {
              position: 'right', // 修改为显示在图表右方
            },
          },
        },
      });

      // 合同金额图表
      const contractAmountCtx = document.getElementById('contractAmountChart').getContext('2d');
      new Chart(contractAmountCtx, {
        type: 'doughnut',
        data: {
          labels: ['房屋建筑', '市政工程'],
          datasets: [
            {
              data: [30, 70],
              backgroundColor: ['#00CC99', '#FFCC00'],
            },
          ],
        },
        options: {
          responsive: true,
          plugins: {
            legend: {
              position: 'right', // 修改为显示在图表右方
            },
          },
        },
      });

      // 近五个月发标数量统计图
      const currentMonth = new Date().getMonth() + 1;
      const months = Array.from({ length: 5 }, (_, i) => `${((currentMonth - 4 + i + 12) % 12) + 1}月`);
      const biddingCtx = document.getElementById('biddingChart').getContext('2d');
      new Chart(biddingCtx, {
        type: 'bar',
        data: {
          labels: months,
          datasets: [
            {
              label: '发标数量',
              data: [5, 10, 15, 20, 25],
              backgroundColor: '#FF9933',
            },
          ],
        },
        options: {
          responsive: true,
          plugins: {
            legend: {
              display: false, // 隐藏条形图图例
            },
          },
          scales: {
            y: {
              beginAtZero: true,
            },
          },
        },
      });

      // 近五个月员工总数折线统计图
      const employeeCtx = document.getElementById('employeeChart').getContext('2d');
      new Chart(employeeCtx, {
        type: 'line',
        data: {
          labels: months,
          datasets: [
            {
              label: '员工总数',
              data: [100, 120, 140, 160, 180],
              borderColor: '#3399FF',
              fill: false,
            },
          ],
        },
        options: {
          responsive: true,
          plugins: {
            legend: {
              display: false, // 隐藏折线图图例
            },
          },
          scales: {
            y: {
              beginAtZero: true,
            },
          },
        },
      });
    };

    onMounted(() => {
      // 初始化图表
      initializeCharts();
    });

    return {
      topStats,
      projectProgress,
    };
  },
};
</script>

<style scoped>
.dashboard-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  padding: 10px;
  background-color: #ffffff;
  font-family: Arial, sans-serif;
  height: 85vh;
  box-sizing: border-box;
  overflow: hidden;
}

.card {
  background-color: #ffffff;
  border-radius: 5px;
  padding: 10px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
}

.stat-background {
  background-color: #cceeff;
}

.top-stats-section {
  grid-column: span 3;
  display: flex;
  justify-content: space-between;
  gap: 5px;
}

.stat-card {
  text-align: center;
  flex: 1;
  padding: 10px;
  border-radius: 5px;
}

.stat-value {
  font-size: 18px;
  font-weight: bold;
  color: #0055FF;
}

.charts-section {
  grid-column: span 3;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
}

.chart-card {
  padding: 10px;
  border-radius: 5px;
}

.chart-background {
  background-color: #ffffff;
}

.chart-container {
  height: 210px; /* 统一三个扇形图的高度 */
  width: 100%;
  display: flex;
  align-items: center; /* 垂直居中 */
}

.progress-and-stats-section {
  grid-column: span 3;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
}

.progress-section {
  background-color: #e6ffe6;
  padding: 10px;
  border-radius: 5px;
}

.progress-bar-container {
  display: flex;
  align-items: center;
  margin-bottom: 5px;
}

.progress-bar {
  flex: 1;
  height: 15px;
  background-color: #e0e0e0;
  border-radius: 5px;
  margin: 0 5px;
  position: relative;
}

.progress {
  height: 100%;
  background-color: #33CC33;
  border-radius: 5px;
}
</style>
