import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import './styles.css';
import Vuex from 'vuex';
import store from './store';

const app = createApp(App);

// 使用 Element Plus
app.use(ElementPlus);
// 使用 Vuex
app.use(Vuex);
// 使用路由
app.use(router);
// 挂载 store 实例到 Vue 应用
app.use(store);

// 调用 actions 里的方法，尝试在应用启动时恢复之前存储的数据
store.dispatch('restoreDataOnLoad', ['node']).then(() => {
    // 挂载 Vue 实例
    app.mount('#app');
});