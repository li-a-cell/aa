import { createStore } from 'vuex';
import Vuex from 'vuex';

export default createStore({
  state: {
    projects: [],
    nodes: [],
    tasks: [],
    regulations: [],
  },
  mutations: {
    setProjects(state, projects) {
      state.projects = projects;
    },
    setNodes(state, nodes) {
      state.nodes = nodes;
    },
    setTasks(state, tasks) {
      state.tasks = tasks;
    },
    setRegulations(state, regulations) {
      state.regulations = regulations;
    },
  },
  actions: {
    fetchProjects({ commit }) {
      // 模拟API调用
      const projects = [/* 数据 */];
      commit('setProjects', projects);
    },
    fetchNodes({ commit }) {
      // 模拟API调用
      const nodes = [/* 数据 */];
      commit('setNodes', nodes);
    },
    fetchTasks({ commit }) {
      // 模拟API调用
      const tasks = [/* 数据 */];
      commit('setTasks', tasks);
    },
    fetchRegulations({ commit }) {
      // 模拟API调用
      const regulations = [/* 数据 */];
      commit('setRegulations', regulations);
    },
  },
  getters: {
    allProjects: (state) => state.projects,
    allNodes: (state) => state.nodes,
    allTasks: (state) => state.tasks,
    allRegulations: (state) => state.regulations,
  },
});