<template>
  <div class="login-page">
    <div class="header-title">
      建筑项目管理系统
    </div>
    <div class="login-container" v-if="!phoneLogin">
      <h1 class="login-title">Login</h1>
      <el-form @submit.prevent="handleSubmit" label-width="0">
        <div class="input-group">
          <el-icon class="icon">👤</el-icon>
          <el-input
              v-model="account"
              required
              placeholder="Username"
              class="input-field"
          />
        </div>
        <div v-if="accountError" class="error-message">用户名不能为空</div>
        <div class="input-group">
          <el-icon class="icon">🔒</el-icon>
          <el-input
              :type="showPassword ? 'text' : 'password'"
              v-model="password"
              required
              placeholder="Password"
              class="input-field"
          />
          <el-button class="toggle-password" @click="togglePassword" type="text">{{ showPassword ? '🙈' : '👁️' }}</el-button>
        </div>
        <div v-if="passwordError" class="error-message">密码不能为空</div>
        <el-button type="primary" class="submit-button" @click="handleSubmit">Sign in</el-button>
      </el-form>
      <el-button class="alternate-button" type="text" @click="handlePhoneLogin">Login with Phone Number</el-button>
    </div>

    <div class="login-container" v-else>
      <h1 class="login-title">Phone Number Login</h1>
      <el-form @submit.prevent="handlePhoneSubmit" label-width="0">
        <div class="input-group">
          <el-icon class="icon">📱</el-icon>
          <el-input
              v-model="phoneNumber"
              required
              placeholder="Phone Number"
              class="input-field"
          />
        </div>
        <el-button
            type="primary"
            class="send-code-button"
            :disabled="isCodeSent"
            @click="sendVerificationCode"
        >
          {{ isCodeSent ? `Resend in ${timer}s` : 'Get Verification Code' }}
        </el-button>
        <div class="input-group">
          <el-icon class="icon">🔑</el-icon>
          <el-input
              v-model="verificationCode"
              required
              placeholder="Verification Code"
              class="input-field"
          />
        </div>
        <el-button type="primary" class="submit-button" @click="handlePhoneSubmit">Login</el-button>
      </el-form>
      <el-button class="alternate-button" type="text" @click="handleBackToLogin">Back to Username Login</el-button>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';
import axios from 'axios';


import { useRouter } from 'vue-router';
import { ElButton, ElForm, ElInput, ElIcon, ElMessage } from 'element-plus';
import jwt_decode from 'jwt-decode';

export default {
  name: 'Login',
  components: {
    ElButton,
    ElForm,
    ElInput,
    ElIcon,
  },
  setup() {
    const account = ref('');
    const password = ref('');
    const phoneNumber = ref('');
    const verificationCode = ref('');
    const showPassword = ref(false);
    const phoneLogin = ref(false);
    const isCodeSent = ref(false);
    const timer = ref(60);
    const router = useRouter();
    const accountError = ref(false);
    const passwordError = ref(false);

    const handleSubmit = () => {
      // 检查用户名和密码是否为空
      accountError.value = !account.value;
      passwordError.value = !password.value;
      if (accountError.value || passwordError.value) {
        ElMessage.error('用户名和密码不能为空');
        return;
      }
      // 构造 JSON 格式的数据，包含 User 对象
      const data1 = {
          account: account.value,
          password: password.value
      };
      const token1=ref('');
      // const response1=axios.post('http://localhost:9528/auth/bidderlogin',data1)
      // response1.then((result)=> {
      //    token1.value=result.data.data;
      // })
      // if(token1.value){
      //   router.push('/bidder-view');
      // }
      // 向后端发送用户名和密码
      axios.post('http://localhost:9528/auth/login', data1, {
      })
          .then(response => {
            console.log('响应数据：', response.data); // 打印响应数据以检查结构
            const { code, msg, data } = response.data;
            console.log('响应数据：', response.data.data.type ); // 打印响应数据以检查结构
            if (code === 0) {
              console.log(token1)
              // 登录失败，显示错误信息
              return;
            }
            // 获取JWT令牌，注意 data 是 token 的位置
            const token= data;
            if ((!token)) {
              ElMessage.error('登录失败，未获取到有效的令牌');
              router.push('/bidder-view');
              return;
            }
            try {
              // 解码JWT以获取用户类型
              const decodedToken = jwt_decode(token);
              const { jobType } = decodedToken;
              console.log(decodedToken );
              // 判断用户类型是否为后台管理员
              if (jobType === '项目经理') {
                // 存储JWT到localStorage，方便后续的身份验证
                localStorage.setItem('jwtToken', token);
                console.log(localStorage.getItem('jwtToken'));
                // 跳转到后台管理面板
                router.push('/manadashboard');
              }
              else if(jobType ==='后台管理员'){
                localStorage.setItem('jwtToken', token);
                // 跳转到后台管理面板
                router.push('/admindashboard');
              }
              else if(jobType ==='招标人员'){
                console.log('招标人员');
                localStorage.setItem('jwtToken', token);
                // 跳转到后台管理面板
                router.push({name: 'zhaobiaoboard'});
              }
              else {
                // 如果用户类型不正确，提示无访问权限
                ElMessage.error('您没有访问权限');
              }
            } catch (error) {
              console.error('Invalid token specified', error);
              ElMessage.error('无效的令牌，请联系管理员');
            }
          })

    };


    const handlePhoneSubmit = () => {
      if (!phoneNumber.value) {
        ElMessage.error('Phone number is required');
        return;
      }
      if (!verificationCode.value) {
        ElMessage.error('Verification code is required');
        return;
      }
      if (phoneNumber.value && verificationCode.value) {
        axios.post('http://192.168.1.100:8000/api/verifyCode', {
          phoneNumber: phoneNumber.value,
          verificationCode: verificationCode.value,
        })
            .then(response => {
              console.log('Phone login successful', response.data);
              router.push('/dashboard');
            })
            .catch(error => {
              console.error('Phone login failed', error);
              ElMessage.error('登录失败，请检查验证码');
            });
      }
    };

    const handlePhoneLogin = () => {
      phoneLogin.value = true;
    };

    const handleBackToLogin = () => {
      phoneLogin.value = false;
    };

    const sendVerificationCode = () => {
      if (phoneNumber.value) {
        axios.post('http://192.168.1.100:8000/api/sendVerificationCode', { phoneNumber: phoneNumber.value })
            .then(() => {
              console.log('Verification code sent');
              isCodeSent.value = true;
              startTimer();
            })
            .catch(error => {
              console.error('Failed to send verification code', error);
              ElMessage.error('验证码发送失败');
            });
      }
    };

    const togglePassword = () => {
      showPassword.value = !showPassword.value;
    };

    const startTimer = () => {
      timer.value = 60;
      const countdown = setInterval(() => {
        timer.value -= 1;
        if (timer.value <= 0) {
          clearInterval(countdown);
          isCodeSent.value = false;
        }
      }, 1000);
    };

    return {
      account,
      password,
      phoneNumber,
      verificationCode,
      showPassword,
      phoneLogin,
      isCodeSent,
      timer,
      accountError,
      passwordError,
      handleSubmit,
      handlePhoneSubmit,
      handlePhoneLogin,
      handleBackToLogin,
      sendVerificationCode,
      togglePassword,
    };
  },
};
</script>

<style scoped>
/* 新增的样式：根容器背景 */
.login-page {
  height: 100vh; /* 确保背景覆盖整个页面 */
  width: 100vw;
  background-image: url('/login.jpg'); /* 替换为你的图片路径 */
  background-size: cover; /* 确保背景图片覆盖整个页面 */
  background-position: center; /* 居中显示背景图片 */
  background-repeat: no-repeat; /* 阻止背景重复 */
}

/* 之前的登录容器样式 */
.login-container {
  position: absolute; /* 使用绝对定位 */
  top: 50%; /* 让容器垂直中心对齐 */
  right: 100px; /* 距离页面右边 100px */
  transform: translateY(-50%); /* 通过 transform 使其垂直居中 */

  display: flex;
  flex-direction: column;
  align-items: center;
  max-width: 500px;
  padding: 40px;
  background-color: rgba(255, 255, 255, 0);
  border-radius: 15px;
  box-shadow: 0px 8px 20px rgba(0, 0, 0, 0.2);
}

.header-title {
  position: absolute; /* 使用绝对定位 */
  top: 20px; /* 距离顶部 20px */
  left: 30px; /* 距离左侧 30px */
  font-size: 60px; /* 字体大小 */
  font-weight: bold; /* 加粗文字 */
  color: #ffffff; /* 白色文字，确保在背景上清晰可见 */
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.7), /* 原始阴影 */ 4px 4px 6px rgba(0, 0, 0, 0.5), /* 第二层阴影，增加深度 */ 6px 6px 8px rgba(0, 0, 0, 0.3); /* 第三层阴影，更模糊 */
}

/* 样式调整 */
.input-group {
  display: flex;
  align-items: center;
  background-color: #f3f3f3;
  border-radius: 8px;
  margin: 15px 0;
  padding: 15px 20px;
  width: 100%;
  box-shadow: inset 0px 1px 4px rgba(0, 0, 0, 0.1);
}

.icon {
  font-size: 1.5em;
  color: #555555;
  margin-right: 15px;
}

.input-field {
  flex: 1;
}

.submit-button {
  margin-top: 30px;
  padding: 15px 25px;
  width: 100%;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1.2em;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;
}

.submit-button:hover {
  background-color: #0056b3;
  transform: translateY(-3px);
}

.alternate-button {
  margin-top: 20px;
  padding: 10px 20px;
  width: 100%;
  background-color: #6c757d;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1.1em;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;
}

.alternate-button:hover {
  background-color: #5a6268;
  transform: translateY(-3px);
}

.send-code-button {
  margin: 15px 0;
  padding: 10px 20px;
  width: 100%;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1.1em;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;
}

.send-code-button:hover {
  background-color: #218838;
  transform: translateY(-3px);
}

.error-message {
  color: #ff4d4f;
  font-size: 0.9em;
  margin-top: -10px;
  margin-bottom: 10px;
}
</style>
