<template>
  <div class="main-content-wrapper">
    <!-- 上边栏 -->
    <div class="top-bar">
      <img src="../assets/shangbianlan.png" alt="Top Bar" class="top-bar-image" />
    </div>

    <div class="main-content">
      <!-- 项目资金图 -->
      <div class="chart-container card">
        <h3 class="chart-title">项目资金图</h3>
        <canvas ref="financeChartRef" class="chart"></canvas>
      </div>

      <!-- 项目完成情况饼状图 -->
      <div class="chart-container card">
        <h3 class="chart-title">项目完成情况饼状图</h3>
        <canvas ref="statusChartRef" class="chart"></canvas>
      </div>

      <!-- 项目进度情况 -->
      <div class="progress-summary card">
        <h3 class="progress-title">项目进度情况</h3>
        <div class="progress-bar">
          <div class="progress-bar-filled" :style="{ width: project.status + '%' }"></div>
        </div>
        <p class="progress-text">{{ project.status }}% 完成</p>
      </div>

      <!-- 通知栏 -->
      <div v-if="notifications.length" class="notification-bar card">
        <h3 class="notification-title">通知</h3>
        <div class="notification-content">
          <div class="message" v-for="(msg, index) in notifications" :key="msg.notification_id">
            <span>{{ msg.message }}</span>
          </div>
        </div>
        <button @click="closeNotification" class="close-button">&times;</button>
      </div>

      <!-- 累计结算金额 -->
      <div class="summary-amount card">
        <h3 class="summary-title">累计结算金额</h3>
        <p class="summary-amount-value">¥ {{ project.budget.toLocaleString() }}</p>
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

export default {
  name: 'Home',
  setup() {
    const currentDate = ref(new Date());
    const user_name= ref('manager001');
    const user_ps= ref('password123');
    const account = ref('')
const password = ref('')
const loginButtonText = ref('登录')
const date_begin = ref('')
const date_end = ref('')
const error = ref(null)
const users = ref([])

// Project Information
const project_name = ref('xxxxxx')
const projectType = ref('xxxxxx')
const projectManager = ref('xxxxxx')
const projectCycle = ref('xxxxxx')
const projectAddress = ref('xxxxxx')
const projectDepartment = ref('xxxxxx')
const buildingName = ref('xxxxxx')
const buildingCode = ref('xxxxxx')
const creationTime = ref('xxxx-xx-xx xx:xx')
const creator = ref('xxxxxx')
const projectDescription = ref('xxxxxx')
const  ElMessage=ref('')
const daysLeft = ref(62)
const token=ref("")
const project = ref({
      project_id: null,
      project_name: '',
      status: 60, // 代表进度百分比
      manager_id: null,
      budget: 0,
      start_date: '',
      end_date: '',
      description: '',
    });
    const notifications = ref([]);
    const financeChartRef = ref(null);
    const statusChartRef = ref(null);
    const handleLogin = () => {
    const data = {
        account: user_name.value,
        password: user_ps.value
    };
console.log("data",data);
    // 发送登录请求
    axios.post('http://localhost:8080/auth/login', data)
        .then(response=> {
            console.log('响应数据：', response.data);
            const { code, msg, data } = response.data;

          
                const token= data;
          
                localStorage.setItem('token', token);
                console.log('头部：', token);  
                console.log("sada",localStorage.getItem(token));
          
        })
        .catch(error => {
            console.error('登录失败', error.response ? error.response.data : error);
        });
};
const update = () => {
    const token = localStorage.getItem('token');
    if (token) {
        console.log("不为空", token);
        axios.get(`http://localhost:8080/project/ongoing`, {
            headers: {
                'Token': token
            },
        })
        .then(response => {
            const data = response.data.data;
            console.log("data",data[0].budget);

            if (!data || Object.keys(data).length === 0) {
                // 处理空数据的情况
                console.log('返回的数据为空');
                // 可以在这里显示提示信息或执行其他操作
                // 例如，更新UI显示提示信息
                error.value = '没有正在进行的项目';
            } else {
                // 处理非空数据的情况
                console.log('受保护的数据：', data.da);
                // 假设 data 包含一个名为 projects 的数组
                const projects = data.projects || [];
                if (projects.length === 0) {
                    // 处理空数组的情况
                    console.log('没有正在进行的项目');
                    error.value = '没有正在进行的项目';
                } else {
                    // 更新UI显示项目列表
                    users.value = projects;
                }
            }
        })
        .catch(error => {
            console.error('获取数据失败', error);
            error.value = '获取数据失败，请稍后再试';
        });
    }
};
 // 使用 onMounted 钩子函数在组件加载或页面刷新时执行 update 函数
 onMounted(() => {
      handleLogin();
      update();
    });

    // 获取当前月份
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
        const date = new Date(year, month, 1 - i - 1);
        days.push(date);
      }

      for (let day = 1; day <= endOfMonth.getDate(); day++) {
        days.push(new Date(year, month, day));
      }

      const remainingDays = 42 - days.length;
      for (let i = 1; i <= remainingDays; i++) {
        const date = new Date(year, month + 1, i);
        days.push(date);
      }

      return days;
    });

    // 日期相关函数
    const isHighlighted = (date) => date.toISOString().split('T')[0] === project.value.start_date;
    const isToday = (date) => {
      const today = new Date();
      return (
        date.getDate() === today.getDate() &&
        date.getMonth() === today.getMonth() &&
        date.getFullYear() === today.getFullYear()
      );
    };
    const isSpecialDate = (date) => notifications.value.some((msg) => msg.date === date.toISOString().split('T')[0]);

    const selectDate = (date) => console.log(`Selected date: ${date}`);

    const prevMonth = () => {
      currentDate.value = new Date(currentDate.value.getFullYear(), currentDate.value.getMonth() - 1, 1);
    };

    const nextMonth = () => {
      currentDate.value = new Date(currentDate.value.getFullYear(), currentDate.value.getMonth() + 1, 1);
    };

    // API 数据获取
    const fetchProjectData = async () => {
      try {
        const response = await axios.get('/api/projects/1'); // 替换为真实 API
        Object.assign(project.value, response.data);
      } catch (error) {
        console.error('Error fetching project data:', error);
      }
    };

    const fetchNotifications = async () => {
      try {
        const response = await axios.get('/api/notifications'); // 替换为真实 API
        notifications.value = response.data;
      } catch (error) {
        console.error('Error fetching notifications:', error);
      }
    };

    // 图表渲染
    const renderFinanceChart = (ctx) => {
      if (!ctx) return;
      new Chart(ctx, {
        type: 'bar',
        data: {
          labels: ['项目A', '项目B', '项目C', '项目D', '项目E'],
          datasets: [
            {
              label: '项目自有资金',
              data: [24900000, 52748000, 2852000, 47245989, 31000000],
              backgroundColor: '#00BFFF',
            },
          ],
        },
        options: {
          responsive: true,
          scales: {
            y: {
              ticks: {
                callback: (value) => '¥' + value.toLocaleString(),
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
              data: [60, 30, 10],
              backgroundColor: ['#4CAF50', '#00BFFF', '#FF9800'],
            },
          ],
        },
      });
    };

    const closeNotification = () => {
      notifications.value = [];
    };

    onMounted(async () => {
      await fetchProjectData();
      await fetchNotifications();

      if (financeChartRef.value) renderFinanceChart(financeChartRef.value);
      if (statusChartRef.value) renderStatusChart(statusChartRef.value);
    });

    return {
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
      closeNotification,
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
  width: 100vw;
  height: 100vh;
  background-color: #F0F4FA; /* 更柔和的背景色 */
  padding: 0;
  box-sizing: border-box;
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
}

.top-bar-image {
  max-height: 100%;
}

/* 主内容布局 */
.main-content {
  display: grid;
  grid-template-columns: 2fr 2fr 1fr;
  grid-template-rows: auto auto;
  grid-gap: 15px; /* 加大间距 */
  padding: 20px; /* 增加内边距 */
}

/* 各个卡片样式 */
.card {
  padding: 20px; /* 增加内边距 */
  border-radius: 12px; /* 更大的圆角 */
  background: linear-gradient(to bottom, #FFFFFF, #F0F4FA); /* 渐变背景 */
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1); /* 更柔和的阴影 */
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
.additional-module {
  min-height: 120px;
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  padding: 15px;
  gap: 10px;
}

.module-item {
  display: flex;
  align-items: center;
  gap: 10px;
  border-bottom: 1px solid #ddd;
  padding: 10px 0;
}

.module-item:last-child {
  border-bottom: none;
}

.module-icon {
  flex-shrink: 0;
  width: 40px;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.icon-image {
  max-width: 100%;
  max-height: 100%;
}

.module-info {
  flex-grow: 1;
}

.module-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.module-subtitle {
  font-size: 12px;
  color: #777;
}

.module-action {
  flex-shrink: 0;
}

.icon-button {
  background: none;
  border: none;
  font-size: 20px;
  color: #007BFF;
  cursor: pointer;
  transition: color 0.3s;
}

.icon-button:hover {
  color: #0056b3;
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
  background: linear-gradient(to bottom, #E0F7FA, #FFFFFF); /* 渐变浅蓝色 */
  box-shadow: 0 6px 10px rgba(0, 0, 0, 0.1);
  color: #00796B; /* 蓝绿色文字 */
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
  background: #FFFFFF;
  border-radius: 12px;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
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
  background-color: rgba(0, 191, 255, 0.6);
  color: white;
  border: none;
}

.day.today {
  background-color: #00BFFF;
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
  color: #007BFF;
  border: 2px solid #007BFF;
  border-radius: 12px;
  cursor: pointer;
  transition: background-color 0.3s, color 0.3s;
}

.calendar-nav:hover {
  background-color: #007BFF;
  color: #FFF;
}
</style>