
import Vuex from 'vuex';
const store = new Vuex.Store({
    state: {
        // 这里可以添加各种需要管理的状态数据，初始值按实际需求设置
    },
    mutations: {
        // 通用的设置数据并保存到本地存储的 mutation
        setDataAndSave(state, { key, data }) {
            state[key] = data;
            localStorage.setItem(key, JSON.stringify(data));
        },
        // 通用的从本地存储恢复数据到 state 的 mutation
        restoreData(state, key) {
            const storedData = localStorage.getItem(key);
            if (storedData) {
                state[key] = JSON.parse(storedData);
            }
        }
    },
    actions: {
        // 异步的通用数据恢复 action，接收要恢复数据对应的 key 数组
        async restoreDataOnLoad({ commit }, keys) {
            keys.forEach(key => {
                commit('restoreData', key);
            });
        }
    }
});

export default store;