<template>
  <div class="profile-container">
    <el-card class="profile-card">
      <!-- 头像和信息部分 -->
      <div class="avatar-info-section">
        <div class="avatar-background">
          <el-avatar :src="profilePictureUrl" size="large" class="profile-picture"></el-avatar>
        </div>
        <div class="profile-info">
          <h2 class="profile-name">{{ employee.name }}</h2>
          <h3 class="profile-job">{{ employee.job_type }}</h3>

          <el-descriptions class="profile-details" border column="1">
            <template v-if="!isEditing">
              <el-descriptions-item label="入职日期">{{ formatDate(employee.hire_date) }}</el-descriptions-item>
              <el-descriptions-item label="电话">{{ employee.phone_number }}</el-descriptions-item>
              <el-descriptions-item label="地址">{{ employee.address }}</el-descriptions-item>
              <el-descriptions-item label="年龄">{{ calculateAge(employee.birth_date) }}</el-descriptions-item>
              <el-descriptions-item label="账号">{{ employee.account }}</el-descriptions-item>
            </template>
            <template v-else>
              <el-descriptions-item label="入职日期">{{ formatDate(employee.hire_date) }}</el-descriptions-item>
              <el-descriptions-item label="电话">
                <el-input v-model="employee.phone_number" placeholder="电话"></el-input>
              </el-descriptions-item>
              <el-descriptions-item label="地址">
                <el-input v-model="employee.address" placeholder="地址"></el-input>
              </el-descriptions-item>
              <el-descriptions-item label="年龄">{{ calculateAge(employee.birth_date) }}</el-descriptions-item>
              <el-descriptions-item label="账号">{{ employee.account }}</el-descriptions-item>
            </template>
          </el-descriptions>
        </div>
        <div class="profile-social">
          <el-icon><i class="fab fa-facebook"></i></el-icon>
          <el-icon><i class="fab fa-twitter"></i></el-icon>
          <el-icon><i class="fab fa-linkedin"></i></el-icon>
        </div>
      </div>

      <!-- 修改密码和退出登录按钮部分 -->
      <div class="profile-actions">
        <el-button v-if="!isEditing" type="primary" class="action-button" @click="editProfile">编辑</el-button>
        <el-button v-if="!isEditing" type="primary" class="action-button">修改密码</el-button>
        <el-button v-if="!isEditing" type="danger" class="action-button" @click="logout">退出登录</el-button>

        <el-button v-if="isEditing" type="success" class="action-button" @click="saveChanges">确定</el-button>
        <el-button v-if="isEditing" type="warning" class="action-button" @click="cancelEdit">取消</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "ProfileCard",
  data() {
    return {
      isEditing: false,
      employee: {
        name: "张三",
        job_type: "项目经理",
        hire_date: "2019-04-15",
        phone_number: "+86 123 4567 8910",
        address: "北京市朝阳区某某街道123号",
        birth_date: "1986-05-09",
        account: "zhangsan_123",
        profile_picture: null,
      },
    };
  },
  computed: {
    profilePictureUrl() {
      if (this.employee.profile_picture) {
        return URL.createObjectURL(new Blob([this.employee.profile_picture]));
      } else {
        return new URL('@/assets/logo.png', import.meta.url).href;
      }
    },
  },
  methods: {
    formatDate(date) {
      if (!date) return "N/A";
      const options = { year: "numeric", month: "2-digit", day: "2-digit" };
      return new Date(date).toLocaleDateString("zh-CN", options);
    },
    calculateAge(birthDate) {
      if (!birthDate) return "N/A";
      const birth = new Date(birthDate);
      const today = new Date();
      let age = today.getFullYear() - birth.getFullYear();
      const monthDiff = today.getMonth() - birth.getMonth();
      if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
        age--;
      }
      return age;
    },
    editProfile() {
      this.isEditing = true;
    },
    saveChanges() {
      this.isEditing = false;
      // 在这里可以添加保存更改的逻辑，例如调用API更新信息
    },
    cancelEdit() {
      this.isEditing = false;
      // 在这里可以添加取消编辑的逻辑，例如重置为原来的信息
    },
    logout() {
      // 退出登录逻辑，返回到根界面，并防止通过浏览器返回键回到当前页面
      this.$router.push({ path: '/' }).then(() => {
        window.history.pushState(null, null, location.href);
        window.addEventListener('popstate', function () {
          window.history.pushState(null, null, location.href);
        });
      });
    },
  },
};
</script>

<style scoped>
.profile-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  height: 100vh;
  background-color: #ffffff;
  padding: 20px;
}

.profile-card {
  width: 100%;
  height: 100vh;
  max-width: 600px;
  background-color: #ffffff;
  padding: 20px;
}

.avatar-info-section {
  display: flex;
  flex-direction: column;
  height: 30%;
  align-items: center;
}

.avatar-background {
  background-size: cover;
  background-position: center;
  border-radius: 20%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
}

.profile-picture {
  border: 5px solid #ffffff;
  width: 150px;
  height: 150px;
  border-radius: 50%;
}

.profile-info {
  text-align: center;
  width: 100%;
  height: 70%;
  margin-top: 15px;
}

.profile-details .el-descriptions-item {
  padding: 100px;
}

.profile-social {
  margin-top: 20px;
  text-align: center;
}

.profile-social el-icon {
  margin: 0 10px;
  cursor: pointer;
}

.profile-actions {
  margin-top: 30px;
  text-align: center;
  position: absolute;
  bottom: 20px;
  right: 550px;
}

.action-button {
  margin: 10px;
}

.editable-input {
  margin-bottom: 10px;
  width: 80%;
}
</style>
