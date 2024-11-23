<template>
  <div class="project-overview">

    <!-- 固定项目标题和元数据部分 -->
    <div class="fixed-info">
      <header class="project-header">
        <h1 class="project-title">{{ currentProject.project_name }}</h1>
        <div class="project-meta" style="display: flex; flex-direction: column;">
          <span>项目类型: {{ currentProject.project_type }}</span>
        </div>
      </header>
    </div>

    <!-- 可切换内容部分 -->
    <div v-if="showSwitchableContent" class="switchable-content">
      <!-- 项目阶段部分 -->
      <section class="project-phase">
        <div class="phase-title">项目阶段</div>
        <div class="phase-process">
          <div class="phase-step" @click="goToPhase('Preliminary')">
            <i class="icon-clock"></i>
            <span class="phase-name">前期阶段</span>
          </div>
          <div class="phase-step" @click="goToPhase('Construction')">
            <i class="icon-building"></i>
            <span class="phase-name">施工阶段</span>
          </div>
          <div class="phase-step" @click="goToPhase('Acceptance')">
            <i class="icon-code"></i>
            <span class="phase-name">验收阶段</span>
          </div>
          <div class="phase-step" @click="goToPhase('Completion')">
            <i class="icon-check"></i>
            <span class="phase-name">完成阶段</span>
          </div>
        </div>
      </section>

      <!-- 已通过流程部分 -->
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
    </div>

    <!-- 用于展示不同阶段的内容（通过路由视图） -->
    <router-view></router-view>

  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import router from "@/router/index.js";

export default {
  name: "ProjectOverview",
  watch: {
    // 监听路由的变化
    $route(to) {
      // 如果路由名称属于阶段页面则隐藏可切换内容
      this.showSwitchableContent = !['Preliminary', 'Construction', 'Acceptance', 'Completion'].includes(to.name);
    }
  },
  setup() {
    const currentProject = ref({});
    const projectData = ref([]);
    const showSwitchableContent = ref(true);

    // 获取项目数据的函数，发送GET请求到后端接口
    const fetchProjectData = async () => {
      try {
        // 从localStorage获取JWT token
        const token = localStorage.getItem('jwtToken');
        if (!token) {
          console.error('JWT Token is missing!');
          return;
        }

        // 发送GET请求，带上token
        const response = await axios.get('http://localhost:9528/project/ongoing', {
          headers: {
            'token': token  // 请求头包含'token'字段，值为从localStorage获取的JWT token
          },
        });

        // 对后端返回数据结构进行校验，确保是包含项目数据的对象后再赋值on
        if (Array.isArray(response.data.data) && response.data.data.length > 0) {
          console.log(response.data.data);
          projectData.value = response.data.data;
          currentProject.value = projectData.value[0];
        } else {
          console.error('后端返回的数据格式不符合预期，没有可用的项目数据');
          alert('暂时无法加载项目数据，请稍后重试或联系管理员');
        }
      } catch (error) {
        if (error.response) {
          console.error('后端错误：', error.response.data);
          if (error.response.data.message === 'NOT_LOGIN') {
            console.error('用户未登录或token无效');
            alert('请先登录系统后再查看项目数据');
          } else {
            alert('后端返回数据出现错误，请联系管理员');
          }
        } else {
          console.error('请求失败：', error.message);
          alert('网络出现问题，请检查网络连接后重试');
        }
      }
    };

    // 在组件挂载时调用接口获取项目数据
    onMounted(() => {
      fetchProjectData();
    });

    // 跳转到项目阶段页面
    const goToPhase = (phase) => {
      router.push({ name: phase });
    };

    return {
      projectData ,
      currentProject,
      showSwitchableContent,
      goToPhase
    };
  }
};
</script>

<style scoped>
.project-overview {
  width: 100%;
  font-family: 'Arial', sans-serif;
  color: #333;
  background-color: #f9fbfd;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

/* 固定信息部分样式 */
.fixed-info {
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

/* 可切换内容部分样式 */
.switchable-content {
  margin-top: 20px;
}

.phase-title {
  font-size: 28px;
  font-weight: bold;
  color: #1890ff;
  margin-bottom: 20px;
  text-align: center;
}

.phase-process {
  display: flex;
  justify-content: space-between;
}

.phase-step {
  display: flex;
  align-items: center;
  font-size: 18px;
  color: #555;
  flex: 1;
  cursor: pointer;
  transition: transform 0.3s;
}

.phase-step:hover {
  transform: scale(1.05);
}

.phase-step i {
  font-size: 24px;
  margin-right: 10px;
  color: #1890ff;
}

.phase-name {
  font-weight: bold;
  font-size: 20px;
  color: #1890ff;
}
.phase-name:hover {
  color: #0056b3; /* 鼠标悬停时颜色变化 */
}
/* 已通过流程部分样式 */
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
</style>