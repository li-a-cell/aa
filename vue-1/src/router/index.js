import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
import ProjectManagement from '../views/ProjectManagement.vue';
// import NodeManagement from '../views/NodeManagement.vue';
 import TaskManagement from '../views/TaskManagement.vue';
import NodePage from '../views/NodePage.vue';
// import Regulations from '../views/Regulations.vue';
// import HistoryProjects from '../views/HistoryProjects.vue';

const routes = [
  { path: '/', name: 'Home', component: Home },
  {
    path: '/projects',
    name: 'ProjectManagement',
    component: ProjectManagement
  },
//   { path: '/nodes', name: 'NodeManagement', component: NodeManagement },
  { path: '/tasks', name: 'TaskManagement', component: TaskManagement },
  { path: '/Node', name: 'NodePage', component: NodePage },
//   { path: '/regulations', name: 'Regulations', component: Regulations },
//   { path: '/history', name: 'HistoryProjects', component: HistoryProjects },
];

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

export default router;
