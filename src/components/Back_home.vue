<template>
  <div class="dashboard-container">
    <!-- 顶部统计信息 -->
    <div class="top-stats-section">
      <div class="stat-card card stat-background">
        <h2>项目总数</h2>
        <p class="stat-value">{{ projectTotal }}</p>
      </div>
      <div class="stat-card card stat-background">
        <h2>进行中项目数量</h2>
        <p class="stat-value">{{ ongoingProject }}</p>
      </div>
      <div class="stat-card card stat-background">
        <h2>已完成项目数量</h2>
        <p class="stat-value">{{ completedProject }}</p>
      </div>
      <div class="stat-card card stat-background">
        <h2>招标总数</h2>
        <p class="stat-value">{{ biddingTotal }}</p>
      </div>
      <div class="stat-card card stat-background">
        <h2>员工数量</h2>
        <p class="stat-value">{{ employeeNumber }}</p>
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
            <div class="progress" :style="{ width: progress.value*100 + '%' }"></div>
          </div>
          <span>{{ progress.value*100 }}%</span>
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
import Chart from 'chart.js/auto'; // 引入 Chart.js，自动加载相关插件和模块
import axios from 'axios';

export default {
  name: 'AdminDashboard',
  setup() {
    const projectTotal = ref(''); // 项目总数，初始为空，等待从后端获取
    const waitprojecttotal = ref('');
    const Bprojecttotal = ref('');
    const biddingNumbers = []
    const employeeNumbers = []
    const ongoingProject = ref(''); // 进行中项目数量
    const completedProject = ref(''); // 已完成项目数量
    const biddingTotal = ref(''); // 招标总数
    const employeeNumber = ref(''); // 员工数量
    const typenum1 = ref('');
    const typenum2 = ref('');
    const typesum = ref('');
    const amount1 =ref('');
    const amount2 = ref('');
    const amountsum = ref('');
    const allprojectdetail=ref('');
    const num=ref([]);
    const projectProgress =ref([])
    const topFiveMaxValues = ref([]);
    const number1=ref([]);
    // 以下为图表相关数据的响应式定义，初始化为空，等待从后端获取
    const projectTypeData = ref({
      labels: [],
      datasets: []
    });
    const contractAmountData = ref({
      labels: [],
      datasets: []
    });
    const biddingChartData = ref({
      labels: [],
      datasets: []
    });
    const employeeChartData = ref({
      labels: [],
      datasets: []
    });

    const initializeCharts = () => {
      // 项目阶段图表
      const projectPhaseCtx = document.getElementById('projectPhaseChart').getContext('2d');
      const projectPhaseData = {
        labels: ['待发布', '待招标', '施工中', '已完成'],
        datasets: [
          {
            data: [],
            backgroundColor: ['#0055FF', '#33CCFF', '#FF9933', '#33CC33'],
          },
        ],
      };
      // 计算各阶段占比数据，添加错误处理，避免除以0等异常情况
      if (projectTotal.value && projectTotal.value > 0) {
        projectPhaseData.datasets[0].data = [
          (Bprojecttotal.value / projectTotal.value) * 100,
          (waitprojecttotal.value / projectTotal.value) * 100,
          (ongoingProject.value / projectTotal.value) * 100,
          (completedProject.value / projectTotal.value) * 100
        ].map(num => isNaN(num)? 0 : num);
      } else {
        projectPhaseData.datasets[0].data = [0, 0, 0, 0]; // 如果总数为0或无效，默认各阶段占比为0
      }
      new Chart(projectPhaseCtx, {
        type: 'pie',
        data: projectPhaseData,
        options: {
          responsive: true,
          plugins: {
            legend: {
              position: 'right',
            },
          },
        },
      });

      // 项目类型图表
      const projectTypeCtx = document.getElementById('projectTypeChart').getContext('2d');
      const projectTypeData = {
        labels: ['房屋建筑', '市政工程'],
        datasets: [
          {
            data: [],
            backgroundColor: ['#0055FF', '#33CCFF'],
          },
        ],
      };
      if (typesum .value && typesum .value > 0) {
        projectTypeData.datasets[0].data = [
          (typenum1 .value / typesum .value) * 100,
          (typenum2 .value / typesum .value) * 100,
        ].map(num => isNaN(num)? 0 : num);
      } else {
        projectTypeData.datasets[0].data = [0, 0, 0, 0]; // 如果总数为0或无效，默认各阶段占比为0
      }
        new Chart(projectTypeCtx, {
          type: 'pie',
          data: projectTypeData,
          options: {
            responsive: true,
            plugins: {
              legend: {
                position: 'right',
              },
            },
          },
        });
      // 合同金额图表
      const contractAmountCtx = document.getElementById('contractAmountChart').getContext('2d');
      new Chart(contractAmountCtx, {
        type: 'bar',
        data: {
          labels: ['房屋建筑', '市政工程'],
          datasets: [
            {
              data: [amount1.value ,amount2.value],
              backgroundColor: ['#33CCFF', '#33CCFF',]
            }
          ]
        },
        options: {
          responsive: true,
          plugins: {
            legend: {
              position: false,
            },
          },
          scales: {
            x: {
              grid: {
                drawOnChartArea: false, // 不在图表区域内绘制网格线
              }
            },
            y: {
              grid: {
                drawOnChartArea: false, // 不在图表区域内绘制网格线
              }
            },
          },
        }
      });
    };
    const updateCharts = () => {
      // 获取近五个月的时间标签数据
      const months = getRecentFiveMonths();

      // 近五个月项目招投标数量柱状统计图
      const biddingCtx = document.getElementById('biddingChart').getContext('2d');
      const biddingChartData = {
        labels: months, // 设置横坐标标签为获取到的近五个月时间字符串
        datasets: [
          // 这里假设你的柱状图数据集原本的结构，你可根据实际情况调整
          {
            label: '项目招投标数量',
            data:  biddingNumbers, // 这里是示例数据，你需替换为真实的数据，数据长度应与labels长度一致
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1
          }
        ]
      };
      new Chart(biddingCtx, {
        type: 'bar',
        data: biddingChartData,
        options: {
          responsive: true,
          plugins: {
            legend: {
              display: false
            }
          },
          scales: {
            x: {
              grid: {
                drawOnChartArea: false, // 不在图表区域内绘制网格线
              }
            },
            y: {
              grid: {
                drawOnChartArea: false, // 不在图表区域内绘制网格线
              }
            },
          },
        }
      });

      // 近五个月员工总数折线统计图
      const employeeCtx = document.getElementById('employeeChart').getContext('2d');
      const employeeChartData = {
        labels: months, // 设置横坐标标签为获取到的近五个月时间字符串
        datasets: [
          // 这里假设你的折线图数据集原本的结构，你可根据实际情况调整
          {
            label: '员工总数',
            data: employeeNumbers, // 这里是示例数据，你需替换为真实的数据，数据长度应与labels长度一致
            fill: false,
            borderColor: 'rgb(255, 99, 132)',
            tension: 0.1
          }
        ]
      };
      new Chart(employeeCtx, {
        type: 'line',
        data: employeeChartData,
        options: {
          responsive: true,
          plugins: {
            legend: {
              display: false
            }
          },
          scales: {
            x: {
              grid: {
                drawOnChartArea: false, // 不在图表区域内绘制网格线
              }
            },
            y: {
              grid: {
                drawOnChartArea: false, // 不在图表区域内绘制网格线
              }
            },
          },
        }
      });
    };
    const getRecentFiveMonths = () => {
      const months = [];
      const currentDate = new Date();
      const currentYear = currentDate.getFullYear();
      const currentMonth = currentDate.getMonth();

      // 从当前月份往前推四个月（索引从0到3，共四个月）
      for (let i = 1; i < 5; i++) {
        const targetMonth = currentMonth - i;
        const year = currentYear;
        // 如果往前推的月份小于0，说明跨年了，需要调整年份并计算对应的月份
        if (targetMonth < 0) {
          const prevYear = currentYear - 1;
          const prevMonth = 12 + targetMonth;
          months.unshift(`${prevYear}-${(prevMonth + 1).toString().padStart(2, '0')}`);
        } else {
          months.unshift(`${year}-${(targetMonth + 1).toString().padStart(2, '0')}`);
        }
      }
      // 添加当前月份
      months.push(`${currentYear}-${(currentMonth + 1).toString().padStart(2, '0')}`);
      return months;
    };
    const fetchData = async () => {
      try {
        const token = localStorage.getItem('jwtToken');
        if (!token) {
          console.error('JWT Token is missing!');
          return;
        }
        const Data1 = {
          status: "待招标"
        };
        const Data2 = {
          status: "待发布"
        };
        const Data3 = {
          status: "施工中"
        };
        const Data4 = {
          status: "已完成"
        };
        const waitProjectResponse = await axios.post('http://localhost:9528/project/projectNum', Data1, {
          headers: {
            'token': token
          }
        });
        waitprojecttotal.value = waitProjectResponse.data.data;
        const BProjectResponse = await axios.post('http://localhost:9528/project/projectNum', Data2, {
          headers: {
            'token': token
          }
        });
        Bprojecttotal.value = BProjectResponse.data.data;
        // 分别发送请求获取各项顶部统计信息数据
        const ongoingProjectResponse = await axios.post('http://localhost:9528/project/projectNum', Data3, {
          headers: {
            'token': token
          }
        });
        ongoingProject.value = ongoingProjectResponse.data.data;
        const completedProjectResponse = await axios.post('http://localhost:9528/project/projectNum', Data4, {
          headers: {
            'token': token
          }
        });
        completedProject.value = completedProjectResponse.data.data;
        projectTotal.value = waitProjectResponse.data.data + BProjectResponse.data.data + ongoingProjectResponse.data.data + completedProjectResponse.data.data;
        const biddingTotalResponse = await axios.get('http://localhost:9528/administrator/biddingNum', {
          headers: {
            'token': token
          }
        });
        biddingTotal.value = biddingTotalResponse.data.data;

        const employeeNumberResponse = await axios.get('http://localhost:9528/administrator/employee', {
          headers: {
            'token': token
          }
        });
        employeeNumber.value = employeeNumberResponse.data.data;
        // 获取项目类型图表数据
        const type1={
            projectType: "房屋建筑"
        }
        const projectTypeDataResponse1 = await axios.post('http://localhost:9528/project/numByType',type1,{
          headers :{
            'token' : token
          }
        });
        typenum1 .value = projectTypeDataResponse1.data.data;
        const type2={
          projectType: "市政工程"
        }
        const projectTypeDataResponse2 = await axios.post('http://localhost:9528/project/numByType',type2,{
          headers :{
            'token' : token
          }
        });
        typenum2.value = projectTypeDataResponse2.data.data;
        typesum.value =projectTypeDataResponse1.data.data+projectTypeDataResponse2.data.data;
        // 获取合同金额图表数据
        const projectamountDataResponse1 = await axios.post('http://localhost:9528/project/costByType',type1,{
          headers :{
            'token' : token
          }
        });
        amount1.value =projectamountDataResponse1.data.data;
        console.log("amount1",amount1)
        const projectamountDataResponse2 = await axios.post('http://localhost:9528/project/costByType',type2,{
          headers :{
            'token' : token
          }
        });
        amount2.value = projectamountDataResponse2.data.data;
        const allprojectResponse = await axios.get('http://localhost:9528/project/all', {
          headers: {
            'token': token
          }
        });
         allprojectdetail.value = allprojectResponse.data.data;
          for (let i=0;i<allprojectdetail.value.length;i++) {
            try {
              const requestBody1 = {
                  projectId: String(i+1),
                  status:"未开始"
              }; // 根据循环的每个项目id构建请求体
              const response1 = await axios.post('http://localhost:9528/administrator/nodes/count', requestBody1,{
                headers: {
                  'token': token
                }
              });
              let num1=response1.data.data;
              const requestBody2 = {
                projectId: String(i+1),
                status:"施工中"
              }; // 根据循环的每个项目id构建请求体
              const response2 = await axios.post('http://localhost:9528/administrator/nodes/count', requestBody2,{
                headers: {
                  'token': token
                }
              });
              let num2=response2.data.data;
              const requestBody3 = {
                projectId: String(i+1),
                status:"已完成"
              }; // 根据循环的每个项目id构建请求体
              const response3 = await axios.post('http://localhost:9528/administrator/nodes/count', requestBody3,{
                headers: {
                  'token': token
                }
              });
              let num3=response3.data.data ;
              if((num1+num2+num3)==0){
                num[i]=0;
              }
              else{
                num[i]=(num3/(num1+num2+num3));
              }
            } catch (error) {
              console.error(`获取项目 ${i} 详情出错：`, error);
            }
          }
          const number=num
          for(let i=0;i<allprojectdetail.value.length;i++) {
            number1[i]=i;
          }
          const length = allprojectResponse.data.data.length;
        // 简单的冒泡排序，将数组从大到小排序（可以使用更高效的排序算法，如快速排序等，这里为了示例清晰采用冒泡排序思路）
          for (let i = 0; i < length - 1; i++) {
            for (let j = 0; j < length - i - 1; j++) {
              if (number[j] < number[j + 1]) {
              // 交换元素位置
              [number[j], number[j + 1]] = [number[j + 1], number[j]];
              [number1[j], number1[j + 1]] = [number1[j + 1], number1[j]];
              }
            }
         }
        for (let k = 0; k < 5; k++) {
          topFiveMaxValues.value.push({
            value: number[k],
            index: number1[k],
          });
        }
        for (let i = 0; i < 5; i++) {
          const projectName = allprojectdetail.value[number1[i]]?.projectName; // 使用可选链操作符避免出现undefined导致报错
          if (projectName) {
            projectProgress.value[i] = {
              name: projectName,
              value: topFiveMaxValues.value[i].value
            };
          } else {
            console.error(`在索引 ${i} 处获取项目名称失败，值为undefined`);
            // 可以根据业务需求决定如何处理这种情况，比如赋予一个默认名称等
          }
        }
        let currentDate = new Date();
        let currentMonth = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1);
        const currentMonthName = (currentMonth.getMonth() + 1).toString().padStart(2, '0');
        let currentYear = currentMonth.getFullYear();
        for(let i=4;i>=0;i--) {
            if((currentMonthName-i)>0){
             const time1={
                year:String(currentYear),
                month:String(currentMonthName-i),
              }
              const biddingChartDataResponse = await axios.post('http://localhost:9528/administrator/newTender',time1,{
                headers: {
                  'token': token
                }
              });
              biddingNumbers.push(biddingChartDataResponse.data.data)
              // 获取近五个月员工总数折线统计图数据
              const employeeChartDataResponse = await axios.post('http://localhost:9528/administrator/newEmployee',time1,{
                headers: {
                  'token': token
                }
              });
              employeeNumbers.push(employeeChartDataResponse.data.data);
            }
            else{
             const time1={
                year:String (currentYear-1),
                month:String (currentMonthName+12-i),
              }
              const biddingChartDataResponse = await axios.post('http://localhost:9528/administrator/newTender',time1,{
                headers: {
                  'token': token
                }
              });
              biddingNumbers.push(biddingChartDataResponse.data.data)
              // 获取近五个月员工总数折线统计图数据
              const employeeChartDataResponse = await axios.post('http://localhost:9528/administrator/newEmployee',time1,{
                headers: {
                  'token': token
                }
              });
              employeeNumbers.push(employeeChartDataResponse.data.data);
            }

        }
      } catch (error) {
        console.error('获取数据出错：', error);
      }
    };

    onMounted(async () => {
      await fetchData();
      initializeCharts();
      updateCharts();
    });

    return {
      employeeNumbers,
      biddingNumbers,
      typenum1 ,
      typenum2 ,
      amount1 ,
      amount2 ,
      waitprojecttotal,
      Bprojecttotal,
      projectTotal,
      ongoingProject,
      completedProject,
      biddingTotal,
      employeeNumber,
      projectProgress,
      projectTypeData,
      contractAmountData,
      biddingChartData,
      employeeChartData,
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
  background-color: #ffffff;
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