import { createRouter, createWebHistory } from 'vue-router';
import Login from '../components/Login.vue';
import DashboardLayout from '../layouts/ManadashboardLayout.vue';
import LoginLayout from '../components/Login.vue';
import Admindashboard from "../layouts/Admindashboard.vue";
import M_nodePage from '../views/Manager_node.vue';
import Manahome from '../components/Mana_home.vue';
import ProjectDetails from '../components/Mana_projectManagement.vue';
import RulesAndRegulations from '../components/RulesAndRegulations.vue';
import HistoryProjects from '../components/HistoryProjects.vue';
import ProfileCard from '../components/ProfileCard.vue';
import Preliminary from '@/components/Preliminary.vue';
import Construction from '@/components/Construction.vue';
import Acceptance from '@/components/Acceptance.vue';
import Completion from '@/components/Completion.vue';

const routes = [
    // 登录页面使用 LoginLayout
    {
        path: '/',
        component: LoginLayout,
    },
    {
        path: '/admindashboard',
        component: Admindashboard ,
    },
    {
        path: '/pro-card',
        component: ProfileCard,
    },
    // 侧边栏布局使用 DashboardLayout
    {
        path: '/manadashboard',
        component: DashboardLayout,
        children: [
            {
                path: '',
                name: 'Manahome',
                component: Manahome,
            },
            {
                path: 'project-details',
                name: 'ProjectDetails',
                component: ProjectDetails,
            },
            {
                path: 'rules-and-regulations',
                name: 'RulesAndRegulations',
                component: RulesAndRegulations,
            },
            {
                path: 'history-projects',
                name: 'HistoryProjects',
                component: HistoryProjects,
            },
            {
                path: 'node-page',
                name: 'NodePage',
                component: M_nodePage,
                children: [
                    {
                        path: 'preliminary',
                        name: 'Preliminary',
                        component: Preliminary,
                    },
                    {
                        path: 'construction',
                        name: 'Construction',
                        component: Construction,
                    },
                    {
                        path: 'acceptance',
                        name: 'Acceptance',
                        component: Acceptance,
                    },
                    {
                        path: 'completion',
                        name: 'Completion',
                        component: Completion,
                    },
                ]
            },
        ],
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
