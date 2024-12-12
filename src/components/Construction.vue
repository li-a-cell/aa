<template>
  <div class="container">
    <div class="fixed-info">
      <header class="project-header">
        <h1 class="project-title">{{ projectName }}</h1>
        <div class="project-meta" style="display:flex;flex-direction:column;">
          <span>项目类型: {{ projectType }}</span>
        </div>
      </header>
    </div>
    <!-- 添加标题 -->
    <h3 class="table-title">{{ isSubNodePage? '子节点列表' : '项目具体节点(施工阶段）' }}</h3>
    <div class="table-container">
      <el-table :data="currentTableData" style="width: 100%" @row-click="handleRowClick">
        <el-table-column prop="nodeName" label="节点名称"></el-table-column>
        <el-table-column prop="status" label="状态"></el-table-column>
        <el-table-column prop="nodeInfo" label="简要描述"></el-table-column>
        <el-table-column prop="startDate" label="开始时间"></el-table-column>
        <el-table-column prop="endDate" label="预计结束时间"></el-table-column>
      </el-table>
      <button v-if="isSubNodePage === false" @click="openCreateNodeForm" class="common-button create-button">创建节点</button>
      <button v-if="isSubNodePage" @click="openCreateSubNodeForm" class="common-button create-button">创建子节点</button>
    </div>
    <!-- 编辑节点弹窗 -->
    <div v-if="showEditNodeForm" class="modal-overlay">
      <div class="create-node-form-popup">
        <h3>编辑节点</h3>
        <form @submit.prevent="submitEditForm">
          <div class="form-field">
            <label for="editNodeName">节点名称</label>
            <input v-model="editFormData.nodeName" type="text" placeholder="节点名称" @blur="validateField('nodeName')" />
            <span v-if="errors.nodeName" class="error">{{ errors.nodeName }}</span>
          </div>
          <div class="form-field">
            <label for="editStartDate">开始日期</label>
            <input v-model="editFormData.startDate" type="date" @blur="validateField('startDate')" />
            <span v-if="errors.startDate" class="error">{{ errors.startDate }}</span>
          </div>
          <div class="form-field">
            <label for="editEndDate">预计结束日期</label>
            <input v-model="editFormData.endDate" type="date" @blur="validateField('endDate')" />
            <span v-if="errors.endDate" class="error">{{ errors.endDate }}</span>
          </div>
          <div class="form-field">
            <label for="editNodeInfo">简要描述</label>
            <input v-model="editFormData.nodeInfo" type="text" placeholder="输入简要描述" @blur="validateField('nodeInfo')" />
            <span v-if="errors.nodeInfo" class="error">{{ errors.nodeInfo }}</span>
          </div>
          <button type="submit" class="common-button submit-button">确认修改</button>
          <button @click="closeEditNodeForm" type="button" class="common-button cancel-button">取消</button>
        </form>
      </div>
    </div>
    <!-- 创建节点弹窗 -->
    <div v-if="showCreateNodeForm" class="modal-overlay">
      <div class="create-node-form-popup">
        <h3>创建节点</h3>
        <form @submit.prevent="submitCreateForm">
          <div class="form-field">
            <label for="createNodeName">节点名称</label>
            <input v-model="createFormData.nodeName" type="text" placeholder="节点名称" @blur="validateField('nodeName')" />
            <span v-if="errors.nodeName" class="error">{{ errors.nodeName }}</span>
          </div>
          <div class="form-field">
            <label for="createStartDate">开始日期</label>
            <input v-model="createFormData.startDate" type="date" @blur="validateField('startDate')" />
            <span v-if="errors.startDate" class="error">{{ errors.startDate }}</span>
          </div>
          <div class="form-field">
            <label for="createEndDate">预计结束日期</label>
            <input v-model="createFormData.endDate" type="date" @blur="validateField('endDate')" />
            <span v-if="errors.endDate" class="error">{{ errors.endDate }}</span>
          </div>
          <div class="form-field">
            <label for="createNodeInfo">简要描述</label>
            <input v-model="createFormData.nodeInfo" type="text" placeholder="输入简要描述" @blur="validateField('nodeInfo')" />
            <span v-if="errors.nodeInfo" class="error">{{ errors.nodeInfo }}</span>
          </div>
          <button type="submit" class="common-button submit-button">创建</button>
          <button @click="closeCreateNodeForm" type="button" class="common-button cancel-button">取消</button>
        </form>
      </div>
    </div>
    <!-- 创建子节点弹窗 -->
    <div v-if="showCreateSubNodeForm" class="modal-overlay">
      <div class="create-sub-node-form-popup">
        <h3>创建子节点</h3>
        <form @submit.prevent="submitCreateSubNodeForm">
          <div class="form-field">
            <label for="createSubNodeName">子节点名称</label>
            <input v-model="createSubFormData.nodeName" type="text" placeholder="子节点名称" @blur="validateSubNodeField('nodeName')" />
            <span v-if="subNodeErrors.nodeName" class="error">{{ subNodeErrors.nodeName }}</span>
          </div>
          <div class="form-field">
            <label for="createSubStartDate">开始日期</label>
            <input v-model="createSubFormData.startDate" type="date" @blur="validateSubNodeField('startDate')" />
            <span v-if="subNodeErrors.startDate" class="error">{{ subNodeErrors.startDate }}</span>
          </div>
          <div class="form-field">
            <label for="createSubEndDate">预计结束日期</label>
            <input v-model="createSubFormData.endDate" type="date" @blur="validateSubNodeField('endDate')" />
            <span v-if="subNodeErrors.endDate" class="error">{{ subNodeErrors.endDate }}</span>
          </div>
          <div class="form-field">
            <label for="createSubNodeInfo">简要描述</label>
            <input v-model="createSubFormData.nodeInfo" type="text" placeholder="输入简要描述" @blur="validateSubNodeField('nodeInfo')" />
            <span v-if="subNodeErrors.nodeInfo" class="error">{{ subNodeErrors.nodeInfo }}</span>
          </div>
          <button type="submit" class="common-button submit-button">创建</button>
          <button @click="closeCreateSubNodeForm" type="button" class="common-button cancel-button">取消</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import {useStore} from "vuex";
import Swal from'sweetalert2'; // 引入SweetAlert库，用于展示提示信息
import router from '@/router';
import { onMounted, onActivated, ref } from 'vue';
import { onBeforeRouteLeave } from 'vue-router';
import store from "@/store.js";

export default {
  name: "SubNodesPage",
  setup() {
    // 用于存储请求得到的父节点数据
    const tableData = ref([]);
    // 用于保存所有父节点数据
    const allParentNodes = ref([]);
    // 当前要显示的数据，初始为父节点数据
    const currentTableData = ref([]);
    const showCreateNodeForm = ref(false);
    const showCreateSubNodeForm = ref(false);
    const createFormData = ref({
      nodeName: '',
      startDate: '',
      endDate: '',
      nodeInfo: '' // 添加简要描述字段
    });
    const createSubFormData = ref({
      nodeName: '',
      startDate: '',
      endDate: '',
      nodeInfo: '' // 添加简要描述字段
    });
    const errors = ref({
      nodeName: '',
      startDate: '',
      endDate: '',
      nodeInfo: '' // 添加简要描述字段的验证错误存储
    });
    const subNodeErrors = ref({
      nodeName: '',
      startDate: '',
      endDate: '',
      nodeInfo: '' // 添加简要描述字段的验证错误存储
    });
    const isSubNodePage = ref(false);  // 标记是否为子节点页面
    // 新增ref变量，用于存储当前点击的父节点的id，初始设为null
    const currentParentNodeId = ref(null);

    // 获取存储的项目数据
    const userData = JSON.parse(localStorage.getItem('userData'));
    const projectName = ref(userData? userData.projectName : '');
    const projectType = ref(userData? userData.projectType : '');
    const token = localStorage.getItem('jwtToken');

    // 请求数据函数，根据传入的状态获取不同的节点信息（这里获取父节点信息）
    const fetchTableData = async (status) => {
      try {
        if (!token) {
          console.error('JWT Token is missing!');
          return;
        }

        // 请求数据
        const requestData = {
          projectId: userData.projectId,
          status: status
        };
        console.log(requestData)
        // 使用 POST 请求发送数据
        const response = await axios.post('/api/manager/topLevelNodes', requestData, {
          headers: {
            'token': token
          }
        });
        // 将返回的数据添加到tableData中，并同时保存到allParentNodes
        tableData.value = [...tableData.value,...response.data.data];
        allParentNodes.value = [...allParentNodes.value,...response.data.data];
        // 初始时将父节点数据设置为当前显示数据
        currentTableData.value = tableData.value;
      } catch (error) {
        if (error.response) {
          console.error('后端错误：', error.response.data);
          alert('获取流程数据时出现错误，请联系管理员');
        } else {
          console.error('请求失败：', error.message);
          alert('网络出现问题，请检查网络连接后重试');
        }
      }
    };

    const openCreateNodeForm = () => {
      showCreateNodeForm.value = true;
    };

    const closeCreateNodeForm = () => {
      showCreateNodeForm.value = false;
    };

    const submitCreateForm = async () => {
      if (!validateForm()) {
        console.error('请修正表单中的错误后再提交');
        return;
      }
      if (!token) {
        console.error('未获取到有效的token，请先登录');
        return;
      }
      // 收集创建父节点的表单数据，类似创建用户时的处理
      const newNode = {
        ...createFormData.value,
        projectId :userData.projectId,
      };
      console.log(newNode);
      try {
        const response = await axios.post('/api/manager/create', newNode, {
          headers: {
            'token': token
          }
        });
        // 将新创建的节点数据添加到列表中（这里简单添加到tableData，可根据实际情况调整）
        Swal.fire('成功', '新节点已创建', 'success');
        router.go(1);
      } catch (error) {
        Swal.fire('错误', '提交表单创建节点出错：' + error.message, 'error');
      } finally {
        closeCreateNodeForm();
        tableData.value=[];
        allParentNodes.value=[];
        await fetchTableData("未开始");
        await fetchTableData("施工中");
        await fetchTableData("已完成");
        // 重新获取数据刷新列表，保持与创建用户逻辑一致，可根据业务需求决定是否重置页码等
      }
    };

    const openCreateSubNodeForm = () => {
      showCreateSubNodeForm.value = true;
    };

    const closeCreateSubNodeForm = () => {
      showCreateSubNodeForm.value = false;
    };

    // 验证子节点表单字段的函数，添加对简要描述的验证
    const validateSubNodeField = (field) => {
      switch (field) {
        case 'nodeName':
          subNodeErrors.value.nodeName = createSubFormData.value.nodeName.trim() === ''? '子节点名称是必填项' : '';
          break;
        case'startDate':
          subNodeErrors.value.startDate = createSubFormData.value.startDate === ''? '开始日期是必填项' : '';
          break;
        case 'endDate':
          subNodeErrors.value.endDate = createSubFormData.value.endDate === ''? '预计结束日期是必填项' : '';
          break;
        case 'nodeInfo':
          subNodeErrors.value.nodeInfo = createSubFormData.value.nodeInfo.trim() === ''? '简要描述是必填项' : '';
          break;
        default:
          break;
      }
    };

    const submitCreateSubNodeForm = async () => {
      if (!token) {
        console.error('未获取到有效的token，请先登录');
        return;
      }
      // 收集创建子节点的表单数据，添加父节点ID相关信息（使用之前记录的currentParentNodeId）
      const newSubNode = {
        ...createSubFormData.value,
        projectId: userData.projectId,
        parentNodeId: currentParentNodeId.value
      };
      console.log(newSubNode);
      try {
        const response = await axios.post('/api/manager/create', newSubNode, {
          headers: {
            'token': token
          }
        });
        // 根据实际情况将新创建的子节点数据添加到合适的列表中，这里假设添加到当前展示的列表（currentTableData）
        Swal.fire('成功', '新子节点已创建', 'success');
        router.go(1);
      } catch (error) {
        Swal.fire('错误', '提交表单创建子节点出错：' + error.message, 'error');
      } finally {
        closeCreateSubNodeForm();
        // 根据当前页面状态决定重新获取数据的方式，保持与创建用户后刷新列表逻辑一致
          await fetchSubNodeData(currentNode.value.parentNodeId);
          await fetchTableData("未开始");
          await fetchTableData("施工中");
          await fetchTableData("已完成");
      }
    };


    const handleRowClick = (row) => {
      // 判断点击的行数据中是否有标识其为子节点的属性（这里假设子节点数据对象中有isSubNode属性，值为true表示是子节点，可根据实际情况调整）
      if (isSubNodePage.value ) {
        const node=row;
        store.commit('setDataAndSave', { key: 'node', data: node });
        // 如果是子节点，进行跳转到节点详情界面的操作，这里使用vue-router的编程式导航进行路由跳转，跳转到NodeDetailPage，并传递子节点id作为参数（假设子节点数据对象中有node_id属性）
        router.push({ name:'NodeDetailPage' });
      } else {
        // 如果不是子节点（即父节点），执行原来展示子节点列表的逻辑
        isSubNodePage.value = true;
        currentTableData.value = [];
        currentParentNodeId.value = row.nodeId;
        fetchSubNodeData(row.nodeId);
      }
    };

    const fetchSubNodeData = async (parentNodeId) => {
      try {
        const token = localStorage.getItem('jwtToken');
        if (!token) {
          console.error('JWT Token is missing!');
          return;
        }

        const response = await axios.get(`/api/manager/childNodes/${parentNodeId}`, {
          headers: {
            'token': token
          }
        });

        if (response.data.data && response.data.data.length > 0) {
          currentTableData.value = response.data.data;
        } else {
          console.log('该节点无子节点');
        }

      } catch (error) {
        if (error.response) {
          console.error('后端错误：', error.response.data);
          alert('获取子节点数据时出现错误，请联系管理员');
        } else {
          console.error('请求失败：', error.message);
          alert('网络出现问题，请检查网络连接后重试');
        }
      }
    };

    // 页面激活时重置相关数据和重新获取数据
    onActivated(() => {
      tableData.value = [];
      allParentNodes.value = [];
      currentTableData.value = [];
      showCreateNodeForm.value = false;
      showCreateSubNodeForm.value = false;
      isSubNodePage.value = false;
      // 重新获取数据
      fetchTableData("未开始");
      fetchTableData("施工中");
      fetchTableData("已完成");
    });

    // 离开页面时记录当前状态（这里可根据需要添加逻辑）
    let leavingShowSubNodes;
    onBeforeRouteLeave((to, from) => {
      leavingShowSubNodes = false;  // 因为不再有子节点显示状态的切换，这里简单设置为false
    });

    // 页面加载时，依次调用三个状态的请求
    onMounted(() => {
      fetchTableData("未开始");
      fetchTableData("施工中");
      fetchTableData("已完成");
    });

    const validateForm = () => {
      Object.keys(createFormData.value).forEach((field) => validateField(field));
      return!Object.values(errors.value).some((error) => error!== '');
    };

    const validateField = (field) => {
      switch (field) {
        case 'nodeName':
          errors.value.nodeName = createFormData.value.nodeName.trim() === ''? '节点名称是必填项' : '';
          break;
        case'startDate':
          errors.value.startDate = createFormData.value.startDate === ''? '开始日期是必填项' : '';
          break;
        case 'endDate':
          errors.value.endDate = createFormData.value.endDate === ''? '预计结束日期是必填项' : '';
          break;
        case 'nodeInfo':
          errors.value.nodeInfo = createFormData.value.nodeInfo.trim() === ''? '简要描述是必填项' : '';
          break;
        default:
          break;
      }
    };

    return {
      tableData,
      allParentNodes,
      currentTableData,
      showCreateNodeForm,
      showCreateSubNodeForm,
      createFormData,
      createSubFormData,
      errors,
      subNodeErrors,
      handleRowClick,
      openCreateNodeForm,
      closeCreateNodeForm,
      submitCreateForm,
      openCreateSubNodeForm,
      closeCreateSubNodeForm,
      submitCreateSubNodeForm,
      projectName,
      projectType,
      isSubNodePage
    };
  }
};
</script>
<style scoped>
.container {
  width: 100%;
  font-family: 'Arial', sans-serif;
  color: #333;
  background-color: #f9fbfd;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

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

.switchable-content {
  margin-top: 20px;
}

.table-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 15px;
}

.sub-table-title {
  font-size: 16px;
  font-weight: bold;
  margin-top: 20px;
  margin-bottom: 10px;
}

.table-container {
  display: flex;
  flex-direction: column;
  position: relative;
}

.common-button {
  padding: 10px;
  cursor: pointer;
  border: none;
  border-radius: 5px;
  background-color: #007bff;
  color: #ffffff;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.common-button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}

.create-button {
  align-self: flex-end;
  margin-top: 10px;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1050;
  display: flex;
  justify-content: center;
  align-items: center;
}

.edit-node-form-popup h3 {
  font-size: 24px;
  color: #222;
  text-align: center;
  margin-bottom: 20px;
  font-weight: 600;
  letter-spacing: 1px;
}

.edit-node-form-popup form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.form-field {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-field label {
  font-weight: 600;
  font-size: 16px;
  color: #444;
  margin-bottom: 5px;
}

.form-field input,
.form-field select {
  width: 100%;
  padding: 12px 16px;
  border-radius: 12px;
  border: 1px solid #ccc;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.1);
  font-size: 14px;
  color: #333;
  background-color: #f8f8f8;
  transition: border-color 0.3s ease, box-shadow 0.3s ease, background-color 0.3s ease;
}

.form-field input:focus,
.form-field select:focus {
  border-color: #007bff;
  box-shadow: 0 4px 8px rgba(0, 123, 255, 0.2);
  background-color: #ffffff;
  outline: none;
}

.form-field input::placeholder {
  color: #aaa;
  font-style: italic;
}

.submit-button {
  background: linear-gradient(90deg, #007bff, #0056b3);
  color: #ffffff;
  border: none;
  border-radius: 10px;
  padding: 15px 30px;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
  transition: background 0.3s ease, transform 0.2s ease;
}

.submit-button:hover {
  background: linear-gradient(90deg, #0056b3, #003f7f);
  transform: translateY(-3px);
}

.cancel-button {
  background: linear-gradient(90deg, #e0e0e0, #bbbbbb);
  color: #333;
  border: none;
  border-radius: 10px;
  padding: 15px 30px;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
  transition: background 0.3s ease, transform 0.2s ease;
}

.cancel-button:hover {
  background: linear-gradient(90deg, #bbbbbb, #999999);
  transform: translateY(-3px);
}

.create-node-form-popup {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 600px;
  max-width: 90%;
  height: auto;
  padding: 40px;
  background-color: #ffffff;
  border-radius: 20px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
  z-index: 1051;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.create-node-form-popup h3 {
  font-size: 24px;
  color: #222;
  text-align: center;
  margin-bottom: 20px;
  font-weight: 600;
  letter-spacing: 1px;
}

.create-node-form-popup form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.el-table {
  width: 100%;
  border-collapse: collapse;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: #ffffff;
}

.el-table th {
  background-color: #007bff;
  color: #ffffff;
  text-transform: uppercase;
  padding: 15px;
  text-align: left;
}

.el-table td {
  padding: 15px;
  border: 1px solid #eee;
  text-align: left;
}

.el-table tr:nth-child(even) {
  background-color: #f9f9f9;
}
</style>