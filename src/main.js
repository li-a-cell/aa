import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import ElementPlus from 'element-plus';
import cors from 'cors';
import 'element-plus/dist/index.css';
import './styles.css'; // 现有的样式文件

const app = createApp(App);

// 使用 Element Plus
app.use(ElementPlus);

// 使用路由
app.use(router);
app.use(cors)
// 挂载 Vue 实例
app.mount('#app');
