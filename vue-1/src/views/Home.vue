<template>
  <div class="main-content-wrapper">
    <!-- 上边栏 -->
    <div class="top-bar">
      <img src="../assert/上边栏.png" alt="Top Bar" class="top-bar-image" />
    </div>

    <div class="main-content">
      <div class="main-left">
        <!-- 项目资金图 -->
        <div class="chart-container card">
          <h3 class="chart-title">项目资金图</h3>
          <canvas ref="financeChartRef" class="chart"></canvas>
        </div>
      </div>

      <div class="main-center">
        <!-- 项目进度情况 -->
        <div class="progress-block card">
          <h3 class="progress-title">项目进度情况</h3>
          <div class="progress-bar">
            <div class="progress-bar-filled" :style="{ width: progress + '%' }"></div>
          </div>
          <p class="progress-text">{{ progress }}% 完成</p>
        </div>
      </div>

      <div class="main-right">
        <!-- 项目完成情况饼状图 -->
        <div class="chart-block card">
          <h3 class="chart-title">项目完成情况饼状图</h3>
          <canvas ref="statusChartRef" class="chart"></canvas>
        </div>
      </div>
    </div>

    <div class="notification-summary">
      <!-- 通知栏 -->
      <div v-if="showNotification" class="notification-bar card">
        <h3 class="notification-title">通知</h3>
        <div class="notification-content">
          <div class="message" v-for="(msg, index) in messages.slice(0, 5)" :key="index">
            <span>{{ msg }}</span>
          </div>
        </div>
        <button @click="closeNotification" class="close-button">&times;</button>
      </div>

      <!-- 累计结算金额 -->
      <div class="summary-amount card glass-card">
        <h3 class="summary-title">累计结算金额</h3>
        <p class="summary-amount-value">¥ 123,456,789</p>
      </div>
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
            'today': isToday(date),
            'disabled': date.getMonth() !== currentDate.getMonth()
          }"
        >
          <span>{{ date.getDate() }}</span>
        </div>
      </div>
    </div>

    <!-- 右下角功能按钮 -->
  </div>
</template>

<script>
import { ref, onMounted, computed, watch } from 'vue';
import Chart from 'chart.js/auto';
import axios from 'axios';

export default {
  name: 'Home',
  setup() {
    const currentDate = ref(new Date());
    const progress = ref(80);
    const markedDates = ref(['2024-06-01', '2024-06-10', '2024-11-15']);
    const showNotification = ref(true);
    const messages = ref([
      '消息一：系统更新',
      '消息二：检查项目进度',
      '消息三：新增项目A',
      '消息四：项目B已完成',
      '消息五：请检查系统日志',
    ]);

    // 图表引用
    const financeChartRef = ref(null);
    const statusChartRef = ref(null);

    const currentMonth = computed(() => {
      return currentDate.value.toLocaleString('default', { month: 'long', year: 'numeric' });
    });

    const daysOfWeek = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

    const calendarDays = computed(() => {
      const startOfMonth = new Date(
        currentDate.value.getFullYear(),
        currentDate.value.getMonth(),
        1
      );
      const endOfMonth = new Date(
        currentDate.value.getFullYear(),
        currentDate.value.getMonth() + 1,
        0
      );
      const startDay = startOfMonth.getDay();
      const endDay = endOfMonth.getDay();

      const days = [];

      // 填充上个月的日期
      for (let i = 0; i < startDay; i++) {
        const date = new Date(startOfMonth);
        date.setDate(startOfMonth.getDate() - startDay + i);
        days.push(date);
      }

      // 填充本月的日期
      for (let i = 1; i <= endOfMonth.getDate(); i++) {
        days.push(new Date(currentDate.value.getFullYear(), currentDate.value.getMonth(), i));
      }

      // 填充下个月的日期
      for (let i = 1; i <= 6 - endDay; i++) {
        const date = new Date(endOfMonth);
        date.setDate(endOfMonth.getDate() + i);
        days.push(date);
      }

      return days;
    });

    const isHighlighted = (date) => {
      return markedDates.value.includes(date.toISOString().split('T')[0]);
    };

    const isToday = (date) => {
      const today = new Date();
      return (
        date.getDate() === today.getDate() &&
        date.getMonth() === today.getMonth() &&
        date.getFullYear() === today.getFullYear()
      );
    };

    const selectDate = (date) => {
      console.log(`Selected date: ${date}`);
    };

    const prevMonth = () => {
      currentDate.value = new Date(currentDate.value.getFullYear(), currentDate.value.getMonth() - 1, 1);
    };

    const nextMonth = () => {
      currentDate.value = new Date(currentDate.value.getFullYear(), currentDate.value.getMonth() + 1, 1);
    };

    const closeNotification = () => {
      showNotification.value = false;
    };

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

    onMounted(() => {
      if (financeChartRef.value) {
        renderFinanceChart(financeChartRef.value);
      }
      if (statusChartRef.value) {
        renderStatusChart(statusChartRef.value);
      }
    });

    watch(currentDate, () => {
      console.log('Current date changed:', currentDate.value);
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
      selectDate,
      closeNotification,
      progress,
      showNotification,
      messages,
      financeChartRef,
      statusChartRef,
    };
  },
};
</script>

<style scoped>
.main-content-wrapper {
  display: flex;
  flex-direction: column;
  width: calc(100vw - 250px);
  height: 100vh;
  overflow: hidden;
  background-color: #F0F4FF;
}

.top-bar {
  width: 100%;
  height: 150px;
  background-color: #E6F7FF;
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.top-bar-image {
  position: relative;
  max-height: 100%;
  max-width: 90%;
}

.main-content {
  flex-grow: 1;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-gap: 20px;
  padding: 20px;
  background-color: #ffffff;
  border-radius: 15px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin-left: 250px;
}

.card {
  padding: 20px;
  border-radius: 15px;
  background: #FFFFFF;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.08);
  margin-bottom: 20px;
}

.glass-card {
  background: rgba(255, 255, 255, 0.25);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.avatar-icon {
  border-radius: 50%;
  border: 2px solid #D1E9FF;
}

.button-primary {
  background-color: #0077B6;
  color: #ffffff;
  padding: 10px 15px;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.button-primary:hover {
  background-color: #005F8C;
}

.main-left,
.main-center,
.main-right {
  padding: 15px;
  background: #ffffff;
  border-radius: 15px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.notification-summary {
  display: grid;
  grid-template-columns: 2fr 1fr;
  grid-gap: 20px;
  padding: 20px;
}

.notification-bar {
  background-color: #FFF7E6;
  padding: 15px;
  text-align: center;
  color: #333;
  font-weight: bold;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: relative;
}

.notification-title {
  margin-bottom: 10px;
}

.notification-content {
  overflow-y: auto;
  max-height: 150px;
}

.message {
  margin-bottom: 10px;
  padding: 10px;
  background-color: #ffffff;
  border-radius: 8px;
  border: 1px solid #ddd;
}

.close-button {
  background: none;
  border: none;
  font-size: 20px;
  color: #333;
  cursor: pointer;
  position: absolute;
  top: 10px;
  right: 10px;
}

.summary-amount {
  padding: 15px;
  background: #D1E9FF;
  border-radius: 15px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  text-align: center;
  color: #003366;
}

.summary-title {
  margin-bottom: 10px;
  font-weight: bold;
  color: #003366;
}

.summary-amount-value {
  font-size: 28px;
  font-weight: bold;
  color: #0077B6;
}

.chart {
  max-height: 300px;
  margin: auto;
}

.progress-bar {
  background: #F0F0F0;
  height: 25px;
  border-radius: 12px;
  overflow: hidden;
}

.progress-bar-filled {
  background: linear-gradient(90deg, #4CAF50, #85e085);
  height: 100%;
  transition: width 0.5s;
}

.calendar-container {
  background-color: #ffffff;
  padding: 20px;
  border-radius: 15px;
  text-align: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  margin: 20px 0;
}

.days-of-week {
  display: flex;
  justify-content: space-between;
  font-weight: bold;
  color: #333;
}

.days {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.day {
  width: 13%;
  padding: 10px;
  text-align: center;
  cursor: pointer;
  border-radius: 8px;
  font-size: 14px;
  transition: background-color 0.3s;
}

.day.highlight {
  background-color: rgba(0, 191, 255, 0.7);
  color: white;
  font-weight: bold;
}

.day.today {
  background-color: #00BFFF;
  color: white;
  font-weight: bold;
}

.calendar-nav {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #00BFFF;
  transition: color 0.3s;
}

.calendar-nav:hover {
  color: #0077b3;
}

.bottom-right-feature {
  position: fixed;
  bottom: 20px;
  right: 20px;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  background: #ffffff;
  padding: 10px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.feature-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.feature-icon {
  width: 40px;
  height: 40px;
  margin-right: 10px;
}

.feature-text {
  text-align: right;
}

.feature-title {
  font-size: 16px;
  font-weight: bold;
  margin: 0;
}

.feature-description {
  font-size: 12px;
  color: #666;
  margin: 0;
}
</style>