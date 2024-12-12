<template>
  <div class="main-content-wrapper">
    <div class="main-content">
      <!-- 项目资金图 -->
      <div class="materials-chart card">
        <h3 class="chart-title">项目材料柱状图</h3>
        <canvas ref="materialsChartRef" class="chart"></canvas>
      </div>
      <!-- 项目完成情况饼状图 -->
      <div class="chart-container card chart-full-height">
        <h3 class="chart-title">项目完成情况饼状图</h3>
        <canvas ref="statusChartRef" class="chart"></canvas>
      </div>

      <!-- 项目进度情况 -->
      <div class="progress-summary card">
        <h3 class="progress-title">项目进度情况</h3>
        <div class="progress-bar">
          <div class="progress-bar-filled" :style="{ width: project.status + '%' }"></div>
        </div>
        <p class="progress-text">项目已经完成 {{ project.jindu }}%</p>
      </div>

      <!-- 结算目前项目预算金额 -->
      <div class="summary-amount card" style="grid-column: span 3;">
        <h3 class="summary-title">结算目前项目预算金额</h3>
        <p class="summary-amount-value">&yen; {{ project.budget }}</p>
      </div>

      <!-- 项目日历 -->
      <div class="calendar-container card">
        <h3 class="calendar-title">项目日历</h3>
        <div class="calendar-header">
          <button @click="prevMonth" class="calendar-nav button-primary">&laquo;</button>
          <h3 class="calendar-title">{{ currentMonth }}</h3>
          <button @click="nextMonth" class="calendar-nav button-primary">&raquo;</button>
        </div>

        <!-- Days of the Week -->
        <div class="days-of-week">
          <div class="day-header" v-for="day in daysOfWeek" :key="day">{{ day }}</div>
        </div>

        <!-- Calendar Days -->
        <div class="days">
          <div
            class="day"
            v-for="date in calendarDays"
            :key="date.toISOString()"
            @click="selectDate(date)"
            :class="{
              'highlight': isHighlighted(date),
              'special-date': isSpecialDate(date),
              'today': isToday(date),
              'disabled': date.getMonth() !== currentDate.getMonth()
            }"
          >
            <span>{{ date.getDate() }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import Chart from 'chart.js/auto';
import { useRouter } from 'vue-router';

export default {
  name: 'Home',
  setup() {
    const router = useRouter();
    const materialsChartRef = ref(null);
    const currentDate = ref(new Date());
    const project = ref({
      projectId: null,
      projectName: '',
      status: null, // 代表进度百分比
      managerId: null,
      budget: "",
      jindu: "",
      plannedEndDate: '',
      plannedStartDate: '',
      description: '',
      siteName: "",
      siteId:null,
      materials: [] // 添加 materials 属性
    });
    const notifications = ref([]);
    const financeChartRef = ref(null);
    const statusChartRef = ref(null);
    const ongoingnode= ref(1);
    const completedtNodeCount = ref(1);
    const redotNodeCount = ref(1);
    const update = async () => {
      const token = localStorage.getItem('jwtToken');
      console.log("更新的token", token);
      if (token) {
        try {
          const response = await axios.get('/api/project/onGoing', {
            headers: { 'token': token }
          });
          const data = response.data.data;
          if (!data || data.length === 0) {
            console.log('没有正在进行的项目');
            router.push({ name: 'CreateProject' });
          } else {
            project.value = { ...project.value, ...data[0] };
            console.log('项目数据更新成功');
            console.log(data[0]);
            console.log(project.value.plannedEndDate);
          }
        } catch (error) {
          console.error('获取数据失败', error);
        }
      }
    };
 const material = async () => {
      const token = localStorage.getItem('jwtToken');
      console.log("材料的token", token);
      try {
        const response = await axios.get('/api/manager/incompleteProjects/materials', {
          headers: { 'Token': token }
        });
        const data = response.data.data;
        console.log("材料数据", data[0].materialName);
        project.value.materials = data; // 将材料数据赋值给 project.materials
        console.log("材料数据", project.value.materials);
      } catch (error) {
        console.error('获取数据失败', error);
      }
    };
    const chartData = computed(() => {
  return {
    labels: project.value.materials.map(item => item.materialName),
    datasets: [{
      label: '项目材料数量',
      data: project.value.materials.map(item => item.totalQuantity),
      backgroundColor: '#00BFFF',
    }]
  };
});
const getongoingtNodeCount = async () => {
  const token = localStorage.getItem('jwtToken');
  console.log("节点的token", token);
  const status="施工中";
  try {
    const response = await axios.post('/api/manager/nodes/count',{status} ,{
        headers: { 
          'Token': token,
          'Content-Type': 'application/json'
        }
         }
      );
    console.log("正在进行节点数量", response.data.data);
    ongoingnode.value = response.data.data;
  } catch (error) {
    console.error('获取节点数量失败', error);
  }
};
const getcompletedtNodeCount = async () => {
  const token = localStorage.getItem('jwtToken');
  console.log("节点的token", token);
  const status="已完成";
  try {
    const response = await axios.post('/api/manager/nodes/count',{status} ,{
        headers: { 
          'Token': token,
          'Content-Type': 'application/json'
        }
         }
      );
    console.log("已经完成节点数量", response.data);
    completedtNodeCount.value = response.data.data;
  } catch (error) {
    console.error('获取节点数量失败', error);
  }
};
const getredotNodeCount = async () => {
  const token = localStorage.getItem('jwtToken');
  console.log("节点的token", token);
  const status="未开始";
  try {
    const response = await axios.post('/api/manager/nodes/count',{status} ,{
        headers: { 
          'Token': token,
          'Content-Type': 'application/json'
        }
         }
      );
    console.log("未开始节点数量", response.data);
    redotNodeCount.value = response.data.data;
  } catch (error) {
    console.error('获取节点数量失败', error);
  }
};
const updateProjectStatus = (newStatus) => {
  project.value.status = newStatus;
  project.value.jindu=Math.floor(newStatus);
  console.log("项目状态更新为：", (completedtNodeCount.value /(completedtNodeCount.value+redotNodeCount.value+ongoingnode.value)));
};
updateProjectStatus((completedtNodeCount.value /(completedtNodeCount.value+redotNodeCount.value+ongoingnode.value))*100);
// 使用示例
getongoingtNodeCount ();
      getcompletedtNodeCount ();
      getredotNodeCount ();
      update();
      material();
    const currentMonth = computed(() => {
      return currentDate.value.toLocaleString('default', { month: 'long', year: 'numeric' });
    });

    const daysOfWeek = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

    const calendarDays = computed(() => {
      const year = currentDate.value.getFullYear();
      const month = currentDate.value.getMonth();
      const startOfMonth = new Date(year, month, 1);
      const endOfMonth = new Date(year, month + 1, 0);
      const startDayOfWeek = startOfMonth.getDay();
      const days = [];

      for (let i = startDayOfWeek - 1; i >= 0; i--) {
        days.push(new Date(year, month, 1 - i - 1));
      }

      for (let day = 1; day <= endOfMonth.getDate(); day++) {
        days.push(new Date(year, month, day));
      }

      const remainingDays = 42 - days.length;
      for (let i = 1; i <= remainingDays; i++) {
        days.push(new Date(year, month + 1, i));
      }

      return days;
    });

    const isHighlighted = (date) => {
  const updatedDate = new Date(date);
  updatedDate.setDate(updatedDate.getDate() + 1);
  //console.log('Checking date:', updatedDate.toISOString().split('T')[0]); // 打印日期
  return updatedDate.toISOString().split('T')[0] === project.value.planned_end_date;
};


    const isToday = (date) => {
      const today = new Date();
      return (
        date.getDate() === today.getDate() &&
        date.getMonth() === today.getMonth() &&
        date.getFullYear() === today.getFullYear()
      );
    };
    const isSpecialDate = (date) => Array.isArray(notifications.value) && notifications.value.some((msg) => msg.date === date.toISOString().split('T')[0]);

    const selectDate = (date) => console.log(`Selected date: ${date}`);

    const prevMonth = () => {
      currentDate.value = new Date(currentDate.value.getFullYear(), currentDate.value.getMonth() - 1, 1);
    };

    const nextMonth = () => {
      currentDate.value = new Date(currentDate.value.getFullYear(), currentDate.value.getMonth() + 1, 1);
    };

    const fetchProjectData = async () => {
      try {
        const response = await axios.get('/api/projects/1');
        Object.assign(project.value, response.data);
      } catch (error) {
        console.error('Error fetching project data:', error);
      }
    };

    const fetchNotifications = async () => {
      try {
        const response = await axios.get('/api/notifications');
        notifications.value = response.data;
      } catch (error) {
        console.error('Error fetching notifications:', error);
      }
    };

    const renderMaterialsChart = (ctx) => {
  if (!ctx) return;
  new Chart(ctx, {
    type: 'bar',
    data: chartData.value,
    options: {
      responsive: true,
      scales: {
        y: {
          ticks: {
            callback: (value) =>  value.toLocaleString(),
          },
        },
      },
    },
  });
};

    const renderStatusChart = (ctx) => {
      if (!ctx) return;
      new Chart(ctx, {
        type: 'pie',
        data: {
          labels: ['进行中', '已完成', '暂停'],
          datasets: [
            {
              data: [ongoingnode,completedtNodeCount ,redotNodeCount ],
              backgroundColor: ['#4CAF50', '#00BFFF', '#FF9800'],
            },
          ],
        },
      });
    };

    onMounted(async () => {
      await fetchProjectData();
      await fetchNotifications();

      if (financeChartRef.value) renderFinanceChart(financeChartRef.value);
      if (statusChartRef.value) renderStatusChart(statusChartRef.value);
      if (materialsChartRef.value) renderMaterialsChart(materialsChartRef.value);
    });

    return {
      materialsChartRef,
      currentDate,
      currentMonth,
      daysOfWeek,
      calendarDays,
      prevMonth,
      nextMonth,
      isHighlighted,
      isToday,
      isSpecialDate,
      selectDate,
      project,
      notifications,
      financeChartRef,
      statusChartRef,
    };
  },
};
</script>

<style scoped>
/* 主容器布局 */
.main-content-wrapper {
  display: flex;
  flex-direction: column;
  width: 86vw;
  height: 100vh;
  background-color: #F0F4FA; /* 更柔和的背景色 */
  padding: 0;
  box-sizing: border-box;
  border: 1px solid #E0E6ED; /* 增加边框 */
}

/* 上边栏样式 */
.top-bar {
  width: 100%;
  height: 60px;
  background-color: #005B96; /* 深蓝色调 */
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2); /* 轻微阴影 */
  border-bottom: 1px solid #E0E6ED; /* 增加底部边框 */
}

.top-bar-image {
  max-height: 100%;
}

/* 主内容布局 */
.main-content {
  display: grid;
  grid-template-columns: 2fr 2fr 1fr;
  grid-template-rows: auto auto auto; /* 增加一行 */
  grid-gap: 15px; /* 加大间距 */
  padding: 20px; /* 增加内边距 */
  background-color: #ffffff; /* 白色背景 */
  border: 1px solid #e0ebedd2; /* 增加边框 */
  border-radius: 12px; /* 圆角 */
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1); /* 更柔和的阴影 */
}
/* 各个卡片样式 */
.card {
  padding: 20px; /* 增加内边距 */
  border-radius: 12px; /* 更大的圆角 */
  background: linear-gradient(to bottom, #FFFFFF, #F0F4FA); /* 渐变背景 */
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1); /* 更柔和的阴影 */
  border: 1px solid #E0E6ED; /* 增加边框 */
}

.chart-container {
  min-height: 200px; /* 调整高度 */
}

.chart {
  max-height: 160px;
  margin: 10px 0;
}

/* 项目进度情况 */
.progress-summary {
  min-height: 120px; /* 调整高度 */
  background-color: #f8f0fa; /* 浅蓝色背景 */
  border: 1px solid #e0edec; /* 增加边框 */
}

.progress-bar {
  width: 100%;
  height: 24px; /* 增加高度 */
  background: #E0E6ED; /* 浅灰色背景 */
  border-radius: 12px; /* 圆角条 */
  overflow: hidden;
  margin-top: 15px;
}

.progress-bar-filled {
  height: 100%;
  background: #4CAF50; /* 保留绿色 */
  transition: width 0.5s;
}

.progress-text {
  margin-top: 10px;
  font-size: 16px;
  color: #333; /* 深灰色字体 */
}

/* 通知栏 */
.notification-bar {
  min-height: 120px; /* 调整高度 */
  background-color: #FFF8E1; /* 浅黄色背景 */
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); /* 柔和阴影 */
  border: 1px solid #E0E6ED; /* 增加边框 */
}

.notification-title {
  color: #333;
  font-size: 18px;
  font-weight: bold;
}

.notification-content {
  margin-top: 10px;
}

/* 累计结算金额 */
.summary-amount {
  min-height: 120px; /* 调整高度 */
  background: linear-gradient(to bottom, #e0e4fa, #ffffff); /* 渐变浅蓝色 */
  box-shadow: 0 6px 10px rgba(0, 0, 0, 0.1);
  border: 1px solid #E0E6ED; /* 增加边框 */
  color: #00796B; /* 蓝绿色文字 */
  grid-column: span 3; /* 占据三列 */
}

.summary-title {
  font-size: 18px;
  font-weight: bold;
}

.summary-amount-value {
  font-size: 24px;
  font-weight: bold;
}
/* 日历样式 */
.calendar-container {
  grid-column: span 3;
  min-height: 380px; /* 调整高度 */
  text-align: center;
  padding: 20px;
  background: #faf0f000; /* 新的背景颜色，可以根据需要调整 */
  border-radius: 12px;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
  border: 1px solid #E0E6ED; /* 增加边框 */
}


.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.days-of-week,
.days {
  display: flex;
  flex-wrap: wrap;
  gap: 5px; /* 增加间距 */
}

.day-header {
  flex: 1;
  font-weight: bold;
  color: #333;
}

.day {
  flex: 1 0 14%;
  border: 1px solid #ddd;
  border-radius: 6px;
  padding: 10px;
  position: relative;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;
  background-color: #FFFFFF;
}

.day:hover {
  background-color: #E3F2FD; /* 浅蓝色悬停效果 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.day.highlight {
  background-color: rgba(255, 0, 0, 0.6);
  color: white;
  border: none;
}

.day.today {
  background-color: #00d0ff;
  color: white;
  font-weight: bold;
}

.day.special-date {
  background-color: #FFE6E6; /* 红色背景标记特殊日期 */
  color: #D9534F;
  font-weight: bold;
}

.button-primary {
  background: #007BFF;
  border: none;
  font-size: 14px;
  color: #FFF;
  padding: 8px 16px;
  border-radius: 20px;
  cursor: pointer;
  transition: background 0.3s;
}

.button-primary:hover {
  background: #0056b3;
}

.calendar-nav {
  font-size: 16px;
  padding: 6px 12px;
  background: transparent;
  color: #ff00f22c;
  border: 2px solid #ff000000;
  border-radius: 12px;
  cursor: pointer;
  transition: background-color 0.3s, color 0.3s;
}

.calendar-nav:hover {
  background-color: #007BFF;
  color: #FFF;
}
</style>