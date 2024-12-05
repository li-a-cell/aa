<template>
  <div class="node-detail-container">
    <!-- 整体信息头部区域 -->
    <div class="header-section">
      <!-- 项目名称 -->
      <h2 class="project-name">{{ projectName }}</h2>
      <!-- 项目类型 -->
      <h3 class="project-type">项目类型: <span>{{ projectType }}</span></h3>
      <p class="node-name">节点名称: <span>{{ nodename }}</span></p>
      <!-- 添加开始时间和结束时间到详情信息中 -->
      <p class="start-time">开始时间: <span>{{ nodestartdate }}</span></p>
      <p class="end-time">预计结束时间: <span>{{ nodeenddate }}</span></p>
      <!-- 计算并显示剩余时间 -->
      <p class="remaining-time">{{ calculateRemainingTime() }}</p>
    </div>

    <!-- 详情信息方框 -->
    <div class="detail-info-box">
      <h4 class="detail-info-title">描述</h4>
      <p class="detail-info-content">{{ nodeinfo }}</p>
    </div>

    <!-- 顶部导航 -->
    <el-tabs v-model="activeTab" class="node-tabs">
      <el-tab-pane label="材料详情" name="material">
        <el-table :data="materialData" style="width: 100%">
          <el-table-column prop="materialName" label="材料名称"></el-table-column>
          <el-table-column prop="requiredQuantity" label="所需数量"></el-table-column>
          <el-table-column label="">
            <template #default="scope">
              <div class="button-group">
                <button @click.stop ="showEditMaterial=scope.row">编辑</button>
              </div>
              <!-- 使用v-if控制编辑材料表单显示隐藏 -->
              <div v-if="showEditMaterial===scope.row" class="modal-overlay">
              <div class="edit-material-form-wrapper">
                <div class="modal-content">
                  <el-form :model="editMaterialData">
                    <el-form-item label="数量">
                      <el-input type="number" v-model="editMaterialData.newQuantity"></el-input>
                    </el-form-item>
                  </el-form>
                  <div class="dialog-footer">
                    <el-button @click="hideEditMaterial()">取消</el-button>
                    <el-button type="primary" @click="editMaterial(scope.row)">确认</el-button>
                  </div>
                </div>
              </div>
              </div>
            </template>
            </el-table-column>
        </el-table>
        <div class="button-group" style="float: right; margin-top: 10px;">
          <el-button type="primary" @click="showAddMaterial = true">增加材料</el-button>
        </div>
        <!-- 使用v-if控制增加材料表单显示隐藏 -->
        <div v-if="showAddMaterial" class="modal-overlay">
          <div class="add-material-form-wrapper">
          <div class="modal-content">
            <el-form :model="newMaterial">
              <el-form-item label="材料名称">
                <el-select v-model="newMaterial.materialName" placeholder="请选择材料">
                  <el-option
                      v-for="material in materialOptions"
                      :key="material"
                      :label="material"
                      :value="material"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="数量">
                <el-input type="number" v-model="newMaterial.requiredQuantity"></el-input>
              </el-form-item>
            </el-form>
            <div class="dialog-footer">
              <el-button @click="showAddMaterial = false">取消</el-button>
              <el-button type="primary" @click="addMaterial">确认添加</el-button>
            </div>
          </div>
        </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="设备详情" name="equipment">
        <el-table :data="equipmentData" style="width: 100%">
          <el-table-column prop="equipmentName" label="设备名称"></el-table-column>
          <el-table-column prop="equipmentModel" label="型号"></el-table-column>
          <el-table-column prop="equipmentType" label="设备类型"></el-table-column>
          <el-table-column prop="status" label="设备状态"></el-table-column>
          <el-table-column prop="remarks" label="备注"></el-table-column>
          <el-table-column label="">
            <template #default="scope">
              <div class="button-group">
                <button @click="editEquipment(scope.row)">编辑</button>
              </div>
            </template>
          </el-table-column>
        </el-table>
        <div class="button-group" style="float: right; margin-top: 10px;">
          <el-button type="primary" @click="showAddEquipment = true">增加设备</el-button>
        </div>
        <!-- 使用v-if控制增加设备表单显示隐藏 -->
        <div v-if="showAddEquipment"  class="modal-overlay" >
          <div class="add-equipment-form-wrapper">
          <div class="modal-content">
            <el-form :model="newEquipment">
              <el-form-item label="设备名称">
                <el-input v-model="newEquipment.equipmentName"></el-input>
              </el-form-item>
              <el-form-item label="型号">
                <el-input v-model="newEquipment.equipmentModel"></el-input>
              </el-form-item>
            </el-form>
            <div class="dialog-footer">
              <el-button @click="showAddEquipment = false">取消</el-button>
              <el-button type="primary" @click="addEquipment">确认添加</el-button>
            </div>
          </div>
        </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="检查任务栏" name="checkTasks">
        <el-table :data="checkTasks" style="width: 100%">
          <el-table-column prop="inspectionType" label="任务名称"></el-table-column>
          <el-table-column prop="startDate" label="开始时间"></el-table-column>
          <el-table-column prop="dueDate" label="截止时间"></el-table-column>
          <el-table-column prop="status" label="状态"></el-table-column>
          <el-table-column label="">
            <template #default="scope">
              <div class="button-group">
                <button @click="editTask(scope.row)">编辑</button>
              </div>
            </template>
          </el-table-column>
        </el-table>
        <div class="button-group" style="float: right; margin-top: 10px;">
          <el-button type="primary" @click="showAddTask = true">增加任务</el-button>
        </div>
        <!-- 使用v-if控制增加任务表单显示隐藏 -->
        <div v-if="showAddTask" class="modal-overlay" >
          <div class="add-task-form-wrapper">
          <div class="modal-content">
            <el-form :model="newTask">
              <el-form-item label="任务名称">
                <el-input v-model="newTask.taskName"></el-input>
              </el-form-item>
            </el-form>
            <div class="dialog-footer">
              <el-button @click="showAddTask = false">取消</el-button>
              <el-button type="primary" @click="addTask">确认添加</el-button>
            </div>
          </div>
        </div>
        </div>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import { ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import "element-plus/dist/index.css";

export default {
  setup() {
    const route = useRoute();
    const projectName = ref('');
    const projectType = ref('');
    const activeTab = ref('material');
    const nodename = ref('');
    const nodestartdate = ref('');
    const nodeenddate = ref('');
    const nodeinfo = ref('');
    const nodeId = ref('');
    const materialData = ref([]);
    const equipmentData = ref([]);
    const checkTasks = ref([]);
    // 新增：控制编辑材料表单显示隐藏的变量，初始值设为null，用于区分没有正在编辑的行
    const showEditMaterial = ref(null );
// 新增：用于临时存储编辑材料时的数据
    const editMaterialData = ref({
      materialName: '',
      newQuantity: 0,
    });
    // 新增一个ref用于存储材料名称列表数据（下拉框选项数据）
    const materialOptions = ref([]);
    // 控制增加材料表单显示隐藏的变量
    const showAddMaterial = ref(false);
    const newMaterial = ref({
      materialName: '',
      requiredQuantity: 0,
    });
    // 控制增加设备表单显示隐藏的变量
    const showAddEquipment = ref(false);
    const newEquipment = ref({
      equipmentName: '',
      equipmentModel: '',
    });
    // 控制增加任务表单显示隐藏的变量
    const showAddTask = ref(false);
    const newTask = ref({
      taskName: '',
    });

    // 显示增加材料的表单
    const showAddMaterialDialog = () => {
      showAddMaterial.value = true;
    };

    // 显示增加设备的表单
    const showAddEquipmentDialog = () => {
      showAddEquipment.value = true;
    };

    // 显示增加任务的表单
    const showAddTaskDialog = () => {
      showAddTask.value = true;
    };

    // 从本地存储获取项目名称和项目类型数据
    const userData = JSON.parse(localStorage.getItem('userData'));
    if (userData) {
      projectName.value = userData.projectName;
      projectType.value = userData.projectType;
    }

    // 正确获取路由传递过来的节点数据（通过params.node），并将Proxy对象转换为普通对象
    if (route.params) {
      nodename.value = route.params.nodeName;
      nodestartdate.value = route.params.startDate;
      nodeenddate.value = route.params.endDate;
      nodeinfo.value = route.params.nodeInfo;
      nodeId.value = route.params.nodeId;
    }

    const calculateRemainingTime = () => {
      if (!nodestartdate.value ||!nodeenddate.value) {
        return '未知';
      }
      const start = new Date(nodestartdate.value);
      const end = new Date(nodeenddate.value);
      const now = new Date();
      if (now < start) {
        return '未开始';
      } else if (now > end) {
        return '已结束';
      } else {
        const diff = end - now;
        const days = Math.floor(diff / (1000 * 60 * 60 * 24));
        const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
        return `${days} 天 ${hours} 小时 ${minutes} 分钟`;
      }
    };

    // 获取材料详情数据的函数
    const fetchMaterialData = async () => {
      try {
        const token = localStorage.getItem('jwtToken');
        const nodeId = parseInt(route.params.nodeId, 10);
        if (isNaN(nodeId)) {
          ElMessage.error('无法获取有效的节点ID，请检查路由参数');
          return;
        }
        const response = await axios.get(`http://localhost:9528/manager/node/${nodeId}/materials`, {
          headers: {
            'token': token
          }
        });
        materialData.value = response.data.data;
      } catch (error) {
        ElMessage.error('获取材料详情数据出现网络错误');
      }
    };

    // 新增函数，用于获取材料名称列表数据（下拉框选项数据）
    const fetchMaterialNames = async () => {
      try {
        const token = localStorage.getItem('jwtToken');
        const response = await axios.get('http://localhost:9528/administrator/getAllMaterial', {
          headers: {
            'token': token
          }
        });
        const allMaterials = response.data.map(item => item.materialName); // 获取所有材料名称
        // 过滤掉已存在于materialData中的材料名称
        const uniqueMaterials = allMaterials.filter(material => {
          console.log(material);
          return (materialData.value === null ||
              !materialData.value.some(existingMaterial => existingMaterial.materialName === material));
        });
        materialOptions.value = uniqueMaterials ;
      } catch (error) {
        console.log(error)
        ElMessage.error('获取材料名称列表出现网络错误');
      }
    };

    // 获取设备详情数据的函数
    const fetchEquipmentData = async () => {
      try {
        const token = localStorage.getItem('jwtToken');
        const nodeId = parseInt(route.params.nodeId, 10);
        if (isNaN(nodeId)) {
          ElMessage.error('无法获取有效的节点ID，请检查路由参数');
          return;
        }
        const response = await axios.get(`http://localhost:9528/manager/node/${nodeId}/equipment/details`, {
          headers: {
            'token': token
          }
        });
        equipmentData.value = response.data.data;
      } catch (error) {
        ElMessage.error('获取设备详情数据出现网络错误');
      }
    };

    // 获取检查任务数据的函数
    const fetchCheckTasks = async () => {
      try {
        const token = localStorage.getItem('jwtToken');
        const nodeId = {
          nodeId: route.params.nodeId
        };
        const response = await axios.post('http://localhost:9528/manager/inspectionTasksByNodeId', nodeId, {
          headers: {
            'token': token
          }
        });
        checkTasks.value = response.data.data;
      } catch (error) {
        ElMessage.error('获取检查任务数据出现网络错误',error);
      }
    };

    // 增加材料的函数
    const addMaterial = async () => {
      try {
        const token = localStorage.getItem('jwtToken');
        const reqData = {
          nodeId: String(route.params.nodeId),
          materialName: newMaterial.value.materialName,
          requiredQuantity:newMaterial.value.requiredQuantity,
        };
        const response = await axios.post(`http://localhost:9528/manager/configure`, reqData, {
          headers: {
            'token': token
          }
        });
          await fetchMaterialData();
          ElMessage.success('材料添加成功');
          showAddMaterial.value = false;
          newMaterial.value = { materialName: '', requiredQuantity: 0 };
      } catch (error) {
        ElMessage.error('添加材料出现网络错误');
      }
    };


    // 编辑材料的函数
    // 新增：隐藏编辑材料表单的函数
    const hideEditMaterial = (row) => {
      showEditMaterial.value = false;
      editMaterialData.value = {
        material_name: '',
        new_quantity: 0
      };
    };

// 新增：确认编辑材料的函数，将本行的材料名字，节点id，和输入项（数量）传给后端
    const editMaterial = async (row) => {
      try {
        const token = localStorage.getItem('jwtToken');
        const reqData = {
          nodeId: String(route.params.nodeId),
          materialName: row.materialName,
          newQuantity: editMaterialData.value.newQuantity
        };
        const response = await axios.post('http://localhost:9528/manager/updateMaterialQuantityByName', reqData, {
          headers: {
            'token': token
          }
        });
          await fetchMaterialData();
          ElMessage.success('材料编辑成功');
          hideEditMaterial(row);
      } catch (error) {
        ElMessage.error('编辑材料出现网络错误');
      }
    };

    // 增加设备的函数
    const addEquipment = async () => {
      try {
        const token = localStorage.getItem('jwtToken');
        const nodeId = parseInt(nodeId.value, 10);
        if (isNaN(nodeId)) {
          ElMessage.error('无法获取有效的节点ID，请检查路由参数');
          return;
        }
        const response = await axios.post(`http://localhost:9528/manager/node/${nodeId}/equipment`, newEquipment.value, {
          headers: {
            'token': token
          }
        });
        if (response.data && response.data.success) {
          await fetchEquipmentData();
          ElMessage.success('设备添加成功');
        } else {
          ElMessage.error('设备添加失败');
        }
      } catch (error) {
        ElMessage.error('增加设备出现网络错误');
      }
    };
    // 编辑设备的函数
    const editEquipment = async (row) => {
      const updatedEquipment = {
        id: row.id,
        nodeId: nodeId.value,
        equipmentName: row.equipmentName,
        equipmentModel: row.equipmentModel,
      };
      try {
        const response = await axios.put('后端编辑设备数据的接口地址', updatedEquipment);
        if (response.data && response.data.success) {
          await fetchEquipmentData();
          ElMessage.success('设备编辑成功');
        } else {
          ElMessage.error('设备编辑失败');
        }
      } catch (error) {
        ElMessage.error('编辑设备出现网络错误');
      }
    };

    // 增加任务的函数
    const addTask = async () => {
      try {
        const token = localStorage.getItem('jwtToken');
        const nodeId = parseInt(nodeId.value, 10);
        if (isNaN(nodeId)) {
          ElMessage.error('无法获取有效的节点ID，请检查路由参数');
          return;
        }
        const response = await axios.post(`http://localhost:9528/manager/node/${nodeId}/inspectionTasks`, newTask.value, {
          headers: {
            'token': token
          }
        });
        if (response.data && response.data.success) {
          await fetchCheckTasks();
          ElMessage.success('任务添加成功');
        } else {
          ElMessage.error('任务添加失败');
        }
      } catch (error) {
        ElMessage.error('增加任务出现网络错误');
      }
    };

    // 编辑任务的函数
    const editTask = async (row) => {
      const updatedTask = {
        id: row.id,
        nodeTd: nodeId.value,
        inspectionType: row.inspectionType,
      };
      try {
        const response = await axios.put('后端编辑检查任务数据的接口地址', updatedTask);
        if (response.data && response.data.success) {
          await fetchCheckTasks();
          ElMessage.success('任务编辑成功');
        } else {
          ElMessage.error('任务编辑失败');
        }
      } catch (error) {
        ElMessage.error('编辑任务出现网络错误');
      }
    };

    // 初始加载时调用接口获取数据，新增调用获取材料名称列表数据的函数
    const initData = async () => {
      await fetchMaterialData();
      await fetchEquipmentData();
      await fetchCheckTasks();
      await fetchMaterialNames();
    };
    initData();

    return {
      projectName,
      projectType,
      nodename,
      nodestartdate,
      nodeenddate,
      nodeinfo,
      nodeId,
      newMaterial,
      hideEditMaterial,
      showAddMaterial,
      showAddMaterialDialog,
      activeTab,
      calculateRemainingTime,
      materialData,
      equipmentData,
      checkTasks,
      addMaterial,
      editMaterial,
      showEditMaterial,
      editMaterialData,
      newEquipment,
      showAddEquipment,
      showAddEquipmentDialog,
      addEquipment,
      editEquipment,
      newTask,
      showAddTask,
      showAddTaskDialog,
      addTask,
      editTask,
      materialOptions // 将材料名称列表数据添加到返回值中，以便在模板中使用
    };
  }
};
</script>

<style scoped>
/* 确保整个页面都被占满 */
html, body {
  height: 100%;
  margin: 0;
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
  overflow-x: hidden; /* 隐藏水平滚动条，保持页面简洁，可根据实际情况调整 */
}

.node-detail-container {
  min-height: 100vh; /* 满屏高度 */
  padding: 30px;
  background: linear-gradient(135deg, #f9f9f9, #f9f9f9); /* 渐变背景 */
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  color: #333; /* 修改文字颜色为更常见易读的颜色，可按需调整 */
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  position: relative; /* 添加相对定位，为背景虚化效果的伪元素提供定位上下文 */
}
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 999;
  display: flex;
  justify-content: center;
  align-items: center;
}
.header-section {
  margin-bottom: 20px;
  border-bottom: 3px solid #1890ff;
  padding-bottom: 15px;
}

.project-name {
  font-size: 28px;
  font-weight: bold;
  margin: 0;
  color: #1890ff;
}

.node-name,
.project-type {
  font-size: 18px;
  margin: 8px 0 0;
  color: #1890ff;
}

.start-time,
.end-time {
  color: #1890ff;
  font-size: 16px;
  margin: 6px 0;
}

.remaining-time {
  color: #1890ff;
  font-size: 16px;
  margin: 6px 0;
}

.detail-info-box {
  border: 1px solid #fff;
  padding: 20px;
  margin-top: 30px;
  border-radius: 8px;
  background-color: rgba(255, 255, 255, 0.1);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.detail-info-title {
  font-size: 20px;
  font-weight: 600;
  margin: 0;
  color: #2980b9; /* 修改标题的颜色 */
}

.detail-info-content {
  font-size: 16px;
  line-height: 1.7;
  margin-top: 15px;
  color: #181818;
}

.node-tabs {
  margin-top: 30px;
}

.el-tabs__header {
  border-bottom: none;
  padding: 5px 0;
}

.el-tabs__item {
  font-size: 16px;
  color: #333;
  padding: 10px 20px;
  margin-right: 25px;
  border-radius: 30px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.el-tabs__item:hover {
  color: #3498db;
  background-color: rgba(255, 255, 255, 0.2);
}

.el-tabs__active-bar {
  background-color: #2980b9;
  height: 3px;
}

.el-tabs__item.is-active {
  font-weight: bold;
  color: #2980b9;
}

.button-group {
  display: flex;
  justify-content: space-around;
  margin-top: 10px;
}

.button-group button {
  padding: 5px 10px;
  border: none;
  border-radius: 5px;
  background-color: #1890ff;
  color: #fff;
  cursor: pointer;
}

.button-group button:hover {
  background-color: #3498db;
}

/* 给表格添加最大宽度限制，避免无数据时过度撑开 */
.el-table {
  max-width: 100%; /* 确保不会超出父容器宽度 */
  width: 100%;
  table-layout: fixed; /* 设置表格布局为固定，让列宽更可控 */
}

/* 为表格列设置统一的默认宽度，防止因无内容导致宽度计算异常 */
.el-table th,
.el-table td {
  width: 150px; /* 可根据实际情况调整具体宽度值 */
  max-width: 150px; /* 同时设置最大宽度限制 */
  overflow: hidden; /* 超出宽度部分隐藏内容（如果有），防止撑开 */
  text-overflow: ellipsis; /* 显示省略号表示内容被截断 */
  white-space: nowrap; /* 禁止文本换行 */
}
.edit-material-form-wrapper,
.add-material-form-wrapper,
.add-equipment-form-wrapper,
.add-task-form-wrapper,
.delete-material-dialog-wrapper,
.delete-equipment-dialog-wrapper,
.delete-task-dialog-wrapper {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background-color: #fff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  padding: 20px;
  width: 400px; /* 可以根据实际情况调整宽度 */
  z-index: 1000;
}

/* 为除了操作界面之外的背景添加虚化效果 */
.node-detail-container::before {
  content: "";
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 998; /* 确保在操作界面之下 */
  pointer-events: none; /* 让虚化背景不拦截点击事件 */
  opacity: 0; /* 初始化为透明，稍后通过类名控制显示 */
  transition: opacity 0.3s ease; /* 过渡效果 */
}

/* 当有操作界面显示时，让背景虚化效果生效 */
.node-detail-container.show-overlay::before {
  opacity: 1;
}

.modal-content {
  display: flex;
  flex-direction: column;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.el-button {
  padding: 8px 15px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.el-button:hover {
  opacity: 0.8;
}

.el-button--primary {
  background-color: #1890ff;
  color: #fff;
}

.el-button--primary:hover {
  background-color: #3498db;
}

.el-button--danger {
  background-color: #f56c6c;
  color: #fff;
}

.el-button--danger:hover {
  background-color: #f78989;
}
</style>