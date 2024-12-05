import { createRouter, createWebHistory } from 'vue-router';
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
import NodeDetailPage from '../components/NodeDetailPage.vue';
import component from "element-plus/es/components/tree-select/src/tree-select-option";
import RegulationDetail from '../components/regulationui.vue';
import CreateProject from '../../../frontend/src/components/CreateProject.vue';
import creatprojectui from '@/components/creatprojectui.vue';
import zhaobiaoboard from '@/layouts/zhaobiaoboard.vue';
import zhaobiaomanage from '@/components/zhaobiaoluoji.vue';
import zhaobiaoluoji from '@/components/zhaobiaoluoji.vue';
import unbid from '@/components/unbid.vue';
import unlogo from '@/components/unlogo.vue';
import BidderView from '../components/BidderView.vue';

const routes = [
    // 登录页面使用 LoginLayout
    {
        path: '/',
        component: LoginLayout,
    },
    {
        path: '/bidder-view',
        component: BidderView,
        meta: { requiresAuth: true },
    },
    {
        path: '/zhaobiaoboard',
        name: 'zhaobiaoboard',
        component: zhaobiaoboard,
        children: [
            {
                path: '/zhaobiaomanage',
                name: 'zhaobiaomanage',
                component: zhaobiaomanage,
            },
            {
                path: '/zhaobiaoluoji',
                name: 'zhaobiaoluoji',
                component: zhaobiaoluoji,
            },
            {
                path: '/unbid',
                name: 'unbid',
                component: unbid,
            },
            {
                path: '/unlogo',
                name: 'unlogo',
                component: unlogo,
            }
        ],
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
                path: 'creatprojectui',
                name: 'ProjectForm',
                component: creatprojectui,
            },
            {
                path: 'create-project',
                name: 'CreateProject',
                component: CreateProject,
            },
            {
                path: '/node-detail',
                name: 'NodeDetailPage',
                component: NodeDetailPage,
            },
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
                path: 'details',
                name: 'RegulationDetail',
                component: RegulationDetail,
            },
            {
                path: 'construction',
                name: 'Construction',
                component: Construction,
                meta: {
                    keepAlive: false // 设置为 false 表示不缓存该组件
                },
            },
            {
                path: 'node-page',
                name: 'NodePage',
                component: M_nodePage,
                meta: {
                    keepAlive: false // 设置为 false 表示不缓存该组件
                },
                children: [
                    {
                        path: 'preliminary',
                        name: 'Preliminary',
                        component: Preliminary,
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
