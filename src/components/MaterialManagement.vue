<template>
  <div class="material-management"> <!-- 修改类名为更贴合材料管理的语义 -->
    <h2>材料管理</h2>
    <!-- 搜索框与筛选条件 -->
    <div class="search-bar">
      <input
          v-model="searchTerm"
          @input="filterMaterials"
          placeholder="搜索材料（材料名字、材料类型、供应商）"
          class="search-input"
      />
      <select v-model="filterType" @change="filterMaterials" class="select-box">
        <option value="">全部材料类型</option>
        <option value="金属材料">金属材料</option>
        <option value="塑料材料">塑料材料</option>
        <option value="电子元件">电子元件</option>
        <!-- 可根据实际材料类型添加更多选项 -->
      </select>
    </div>
    <!-- 材料入库按钮 -->
    <div class="add-material-button-container">
      <button @click="openAddMaterialForm" class="add-material-button">材料入库</button>
    </div>
    <!-- 添加或编辑材料入库表单 -->
    <div v-if="showMaterialForm" class="modal-overlay">
      <div class="material-form-popup">
        <h3>{{ currentMaterial? '编辑材料入库信息' : '添加材料入库' }}</h3>
        <form @submit.prevent="submitMaterialForm">
          <div class="form-row">
            <div class="form-col">
              <label for="materialName">材料名字</label>
              <input v-model="formData.materialName" type="text" placeholder="材料名字" @blur="validateField('materialName')" />
              <span v-if="errors.materialName" class="error">{{ errors.materialName }}</span>
            </div>
            <div class="form-col">
              <label for="materialType">材料类型</label>
              <select v-model="formData.materialType" @blur="validateField('materialType')">
                <option value="金属材料">金属材料</option>
                <option value="塑料材料">塑料材料</option>
                <option value="电子元件">电子元件</option>
                <!-- 可根据实际材料类型添加更多选项 -->
              </select>
              <span v-if="errors.materialType" class="error">{{ errors.materialType }}</span>
            </div>
          </div>
          <div class="form-row">
            <div class="form-col">
              <label for="quantity">材料数量</label>
              <input v-model="formData.quantity" type="number" placeholder="材料数量" @blur="validateField('quantity')" />
              <span v-if="errors.quantity" class="error">{{ errors.quantity }}</span>
            </div>
            <div class="form-col">
              <label for="supplier">供应商</label>
              <input v-model="formData.supplier" type="text" placeholder="供应商" @blur="validateField('supplier')" />
              <span v-if="errors.supplier" class="error">{{ errors.supplier }}</span>
            </div>
          </div>
          <div class="form-row">
            <div class="form-col">
              <label for="inTime">入库时间</label>
              <input v-model="formData.inTime" type="date" @blur="validateField('inTime')" />
              <span v-if="errors.inTime" class="error">{{ errors.inTime }}</span>
            </div>
            <div class="form-col">
              <label for="price">价格</label>
              <input v-model="formData.price" type="number" placeholder="价格" @blur="validateField('price')" />
              <span v-if="errors.price" class="error">{{ errors.price }}</span>
            </div>
          </div>
          <div class="form-col">
            <label for="remark">备注</label>
            <input v-model="formData.remark" type="text" placeholder="备注" @blur="validateField('remark')" />
            <span v-if="errors.remark" class="error">{{ errors.remark }}</span>
          </div>
          <!-- 根据当前是编辑还是添加状态显示不同按钮文本 -->
          <button type="submit" class="common-button submit-button">{{ currentMaterial? '确认修改' : '确认添加' }}</button>
          <button @click="closeMaterialForm" type="button" class="common-button cancel-button">取消</button>
        </form>
      </div>
    </div>
    <!-- 材料列表展示 -->
    <table class="material-table">
      <thead>
      <tr>
        <th>材料名字</th>
        <th>材料类型</th>
        <th>材料数量</th>
        <th>供应商</th>
        <th>入库时间</th>
        <th>价格</th>
        <th>备注</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(material, index) in paginatedMaterials" :key="material.id">
        <td>{{ material.materialName }}</td>
        <td>{{ material.materialType }}</td>
        <td>{{ material.quantity }}</td>
        <td>{{ material.supplier }}</td>
        <td>{{ material.inTime }}</td>
        <td>{{ material.price }}</td>
        <td>{{ material.remark }}</td>
        <td>
          <button @click="editMaterialDetails(material)" class="common-button edit-button">编辑</button>
        </td>
      </tr>
      </tbody>
    </table>
    <!-- 分页 -->
    <div class="pagination">
      <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1" class="common-button prev-button">上一页</button>
      <span>第 {{ currentPage }} 页</span>
      <button @click="changePage(currentPage + 1)" :disabled="currentPage * pageSize >= filteredMaterials.length" class="common-button next-button">下一页</button>
    </div>
  </div>
</template>

<script>
import {ref, computed, onMounted} from 'vue';
import Swal from 'sweetalert2';
import axios from 'axios';

export default {
  name: 'materialManagement',
  setup() {
    const searchTerm = ref('');
    const filterType = ref('');
    const currentPage = ref(1);
    const pageSize = ref(5);
    const materials = ref([]);
    const showMaterialForm = ref(false);
    const currentMaterial = ref(null);
    const formData = ref({
      materialName: '',
      materialType: '',
      quantity: '',
      supplier: '',
      inTime: '',
      price: '',
      remark: '',
    });
    const errors = ref({
      materialName: '',
      materialType: '',
      quantity: '',
      supplier: '',
      inTime: '',
      price: '',
      remark: '',
    });

    const validateField = (field) => {
      switch (field) {
        case 'materialName':
          errors.value.materialName = formData.value.materialName.trim() === ''? '材料名字是必填项' : '';
          break;
        case 'materialType':
          errors.value.materialType = formData.value.materialType === ''? '材料类型是必填项' : '';
          break;
        case 'quantity':
          errors.value.quantity =
              formData.value.quantity === '' || formData.value.quantity < 0
                  ? '材料数量是必填项且必须为正数'
                  : '';
          break;
        case 'supplier':
          errors.value.supplier = formData.value.supplier.trim() === ''? '供应商是必填项' : '';
          break;
        case 'inTime':
          errors.value.inTime = formData.value.inTime === ''? '入库时间是必填项' : '';
          break;
        case 'price':
          errors.value.price =
              formData.value.price === '' || formData.value.price < 0
                  ? '价格是必填项且必须为正数'
                  : '';
          break;
        case 'remark':
          errors.value.remark = formData.value.remark.trim() === ''? '备注是必填项' : '';
          break;
        default:
          break;
      }
    };

    const validateForm = () => {
      Object.keys(formData.value).forEach((field) => validateField(field));
      return!Object.values(errors.value).some((error) => error!== '');
    };

    const token = localStorage.getItem('jwtToken');
    // 获取材料数据的函数，通过GET请求获取所有材料数据
    const fetchMaterials = async () => {
      try {
        const response = await axios.get('http://localhost:9528/administrator/getAllMaterials', {
          params: {
            page: currentPage.value,
            pageSize: pageSize.value,
            searchTerm: searchTerm.value,
            filterType: filterType.value
          },
          headers: {
            'token': token
          }
        });
        if (response.data && response.data.data) {
          materials.value = response.data.data;
          console.log(materials.value);
        } else {
          Swal.fire('错误', '后端返回数据格式不符合预期，请检查接口', 'error');
        }
      } catch (error) {
        Swal.fire('错误', '获取材料数据出错：' + error.message, 'error');
      }
    };

    const filteredMaterials = computed(() => {
      return materials.value.filter((material) => {
        return (
            (!searchTerm.value || material.materialName.toLowerCase().includes(searchTerm.value.toLowerCase())) &&
            (!filterType.value || material.materialType === filterType.value)
        );
      });
    });

    const paginatedMaterials = computed(() => {
      const startIndex = (currentPage.value - 1) * pageSize.value;
      const endIndex = startIndex + pageSize.value;
      return filteredMaterials.value.slice(startIndex, endIndex);
    });

    // 打开添加材料入库表单
    const openAddMaterialForm = () => {
      showMaterialForm.value = true;
      currentMaterial.value = null;
      formData.value = {
        materialName: '',
        materialType: '',
        quantity: '',
        supplier: '',
        inTime: '',
        price: '',
        remark: '',
      };
      errors.value = {
        materialName: '',
        materialType: '',
        quantity: '',
        supplier: '',
        inTime: '',
        price: '',
        remark: '',
      };
    };

    // 关闭表单
    const closeMaterialForm = () => {
      showMaterialForm.value = false;
    };

    // 提交材料入库表单
    const submitMaterialForm = async () => {
      if (!validateForm()) {
        Swal.fire('错误', '请修正表单中的错误后再提交', 'error');
        return;
      }

      try {
        if (currentMaterial.value) {
          // 编辑材料入库信息，调用更新接口，添加material_id到请求参数中（假设后端接口需要这个字段来确定更新哪条记录，可根据实际调整）
          const updatedMaterial = {...formData.value};
          const {materialId} = currentMaterial.value;
          const response = await axios.put(`http://localhost:9528/administrator/updateMaterial/${materialId}`, updatedMaterial, {
            headers: {
              'token': token
            }
          });
          // 前端更新本地数据
          const index = materials.value.findIndex(material => material.materialId === currentMaterial.value.materialId);
          if (index!== -1) {
            materials.value.splice(index, 1, updatedMaterial);
          }
          Swal.fire('成功', '材料入库信息已更新', 'success');
        } else {
          // 添加材料入库，调用添加接口
          const newMaterial = {...formData.value};
          const response = await axios.post('http://localhost:9528/administrator/addMaterial', newMaterial, {
            headers: {
              'token': token
            }
          });
          materials.value.push(newMaterial);
          Swal.fire('成功', '新的材料已入库', 'success');
        }
      } catch (error) {
        Swal.fire('错误', '提交材料入库表单出错：' + error.message, 'error');
      } finally {
        showMaterialForm.value = false;
        // 重新获取数据刷新列表，可根据业务需求决定是否重置页码等
        await fetchMaterials();
      }
    };

    // 编辑材料入库信息
    const editMaterialDetails = (material) => {
      currentMaterial.value = material;
      formData.value = {...material};
      errors.value = {
        materialName: '',
        materialType: '',
        quantity: '',
        supplier: '',
        inTime: '',
        price: '',
        remark: '',
      };
      showMaterialForm.value = true;
    };

    // 分页切换
    const changePage = (page) => {
      currentPage.value = page;
      fetchMaterials();
    };

    onMounted(async () => {
      await fetchMaterials();
    });

    return {
      searchTerm,
      filterType,
      currentPage,
      pageSize,
      materials,
      filteredMaterials,
      paginatedMaterials,
      showMaterialForm,
      currentMaterial,
      formData,
      errors,
      openAddMaterialForm,
      closeMaterialForm,
      submitMaterialForm,
      editMaterialDetails,
      changePage,
      validateField,
    };
  }
};
</script>

<style scoped>
/* 用户管理相关类名改为材料管理相关类名 */
.user-management {
  /* 修改为 */
  .material-management {
    padding: 30px;
    width: 100%;
    box-sizing: border-box;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    background-color: #f4f6f9;
  }
}
.user-table {
  /* 修改为 */
  .material-table {
    width: 100%;
    border-collapse: collapse;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    background-color: #ffffff;
  }
}
.add-user-button {
  /* 修改为 */
  .add-material-button {
    padding: 15px 30px;
    background: linear-gradient(90deg, #007bff, #0056b3);
    color: #ffffff;
    border: none;
    border-radius: 10px;
    font-weight: bold;
    cursor: pointer;
    box-shadow: 0 5px 10px rgba(0, 0, 0, 0.15);
    transition: background 0.3s ease, transform 0.2s ease;
  }
}
.user-form-popup {
  /* 修改为 */
  .material-form-popup {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 800px;
    max-width: 90%;
    height: auto;
    padding: 40px;
    background: linear-gradient(135deg, #ffffff, #f3f4f6);
    border-radius: 20px;
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
    z-index: 1000;
    display: flex;
    flex-direction: column;
    gap: 20px;
  }
}
</style>