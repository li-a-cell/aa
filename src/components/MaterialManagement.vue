<template>
  <div class="material-management">
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
    <!-- 材料列表展示 -->
    <table class="material-table">
      <thead>
      <tr>
        <th>材料名字</th>
        <th>材料类型</th>
        <th>总数量</th>
        <th>备注</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(material, index) in paginatedMaterials" :key="material.id">
        <td>{{ material.materialName }}</td>
        <td>{{ material.materialType }}</td>
        <td>{{ material.currentStockQuantity }}</td>
        <td>{{ material.remarks }}</td>
        <td>
          <button @click="goToDetails(material)" class="common-button details-button">详情</button>
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
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
  setup() {
    const searchTerm = ref('');
    const filterType = ref('');
    const currentPage = ref(1);
    const pageSize = ref(5);
    const materials = ref([]);
    const allMaterialsBackup = ref([]);

    // 前端实现筛选逻辑，基于已经获取到的本地数据进行筛选
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

    // 重置筛选条件，恢复到初始获取的所有数据状态
    const resetFilters = () => {
      searchTerm.value = '';
      filterType.value = '';
      materials.value = allMaterialsBackup.value;
    };

    const token = localStorage.getItem('jwtToken');
    // 获取材料数据的函数，通过GET请求获取所有材料数据，只在组件挂载时调用一次
    const fetchMaterials = async () => {
      try {
        const response = await axios.get('/api/administrator/getAllMaterial', {
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
        materials.value = response.data;
        allMaterialsBackup.value = response.data;
      } catch (error) {
        console.log('获取材料数据出错：', error.message);
      }
    };

    // 分页切换
    const changePage = (page) => {
      currentPage.value = page;
    };

    const router = useRouter();

    const goToDetails = (material) => {
      router.push({ name: 'MaterialDetails', params: { materialId: material.materialId } });
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
      changePage,
      resetFilters,
      goToDetails
    };
  }
};
</script>

<style scoped>
/* 材料管理整体区域样式 */
.material-management {
  padding: 30px;
  width: 100%;
  box-sizing: border-box;
  font-family: 'Arial', sans-serif;
  background-color: #f9fbfd;
}

/* 搜索框与筛选条件所在区域的样式 */
.search-bar {
  width: 100%;
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.search-bar input {
  flex: 3;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.search-bar select {
  flex: 1;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
  background-color: #ffffff;
}

/* 材料列表展示区域样式 */
.material-table {
  width: 100%;
  border-collapse: collapse;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: #ffffff;
}

.material-table th {
  background-color: #007bff;
  color: #ffffff;
  text-transform: uppercase;
  padding: 15px;
  text-align: left;
}

.material-table td {
  padding: 15px;
  border: 1px solid #eee;
  text-align: left;
}

.material-table tr:nth-child(even) {
  background-color: #f9f9f9;
}

/* 公共按钮样式 */
.common-button {
  padding: 10px;
  cursor: pointer;
  border: none;
  border-radius: 5px;
  background-color: #007bff;
  color: #ffffff;
  transition: background-color 0.3s ease, transform 0.2s ease;
  margin-right: 10px;
}

.common-button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}

/* 详情按钮样式，可根据实际需求调整 */
.details-button {
  background-color: #28a745; /* 例如设置为绿色 */
}

.details-button:hover {
  background-color: #218838; /* 鼠标悬停颜色变化 */
}

/* 错误提示信息的样式 */
.error {
  color: #ff4d4d;
  font-size: 0.9em;
  margin-top: 5px;
}
</style>