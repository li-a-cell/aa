<template>
  <div class="bidder-management">
    <h2>投标人管理</h2>
    <!-- 搜索框与筛选条件 -->
    <div class="search-bar">
      <input
          type="text"
          v-model="searchTerm"
          @input="filterBidders"
          placeholder="搜索投标人（姓名、公司名称、联系方式、所属类型）"
      />
      <select v-model="filterType" @change="filterBidders">
        <option value="">全部类型</option>
        <option value="白名单">白名单</option>
        <option value="黑名单">黑名单</option>
      </select>
    </div>

    <!-- 添加投标人按钮 -->
    <div class="add-bidder-button-container">
      <button @click="openAddBidderForm" class="add-bidder-button">添加新投标人</button>
    </div>

    <!-- 添加或编辑投标人表单 -->
    <div v-if="showBidderForm" class="form-container">
      <h3>{{ currentBidder ? '编辑投标人' : '添加新投标人' }}</h3>
      <form @submit.prevent="submitForm">
        <div class="form-field">
          <label for="name">投标人姓名</label>
          <input v-model="formData.name" type="text" placeholder="投标人姓名" />
          <span class="error">{{ errors.value.name }}</span>
        </div>
        <div class="form-field">
          <label for="companyName">公司名称</label>
          <input v-model="formData.companyName" type="text" placeholder="公司名称" />
          <span class="error">{{ errors.value.companyName }}</span>
        </div>
        <div class="form-field">
          <label for="contactInfo">联系方式</label>
          <input v-model="formData.contactInfo" type="text" placeholder="联系方式" />
          <span class="error">{{ errors.value.contactInfo }}</span>
        </div>
        <div class="form-field">
          <label for="bidderType">所属类型</label>
          <select v-model="formData.bidderType">
            <option value="">请选择所属类型</option>
            <option value="白名单">白名单</option>
            <option value="黑名单">黑名单</option>
          </select>
          <span class="error">{{ errors.value.bidderType }}</span>
        </div>
        <button type="submit">{{ currentBidder ? '确认修改' : '确认添加' }}</button>
        <button @click="closeBidderForm" type="button">取消</button>
      </form>
    </div>

    <!-- 投标人列表展示 -->
    <div class="bidder-list">
      <table>
        <thead>
        <tr>
          <th>投标人姓名</th>
          <th>公司名称</th>
          <th>联系方式</th>
          <th>所属类型</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(bidder, index) in paginatedBidders" :key="bidder.id">
          <td>{{ bidder.name }}</td>
          <td>{{ bidder.companyName }}</td>
          <td>{{ bidder.contactInfo }}</td>
          <td>{{ bidder.bidderType }}</td>
          <td>
            <button @click="editBidderDetails(bidder)">编辑</button>
            <button @click="confirmDelete(bidder.id)">删除</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- 分页 -->
    <div class="pagination">
      <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1">上一页</button>
      <span>第 {{ currentPage }} 页</span>
      <button @click="changePage(currentPage + 1)" :disabled="currentPage * pageSize >= filteredBidders.length">下一页</button>
    </div>
  </div>
</template>

<script>
import { ref, computed } from 'vue';
import Swal from 'sweetalert2';
import { useForm } from 'vee-validate';
import * as yup from 'yup';

export default {
  name: 'BidderManagement',
  setup() {
    const bidders = ref([
      {
        id: 1,
        name: '王五',
        companyName: '建筑公司C',
        contactInfo: '13800138000',
        bidderType: '白名单',
        description: '这是投标人王五的描述'
      },
      {
        id: 2,
        name: '赵六',
        companyName: '建筑公司D',
        contactInfo: '13800138001',
        bidderType: '黑名单',
        description: '这是投标人赵六的描述'
      },
    ]);

    const searchTerm = ref('');
    const filterType = ref('');
    const currentPage = ref(1);
    const pageSize = ref(5);

    const showBidderForm = ref(false);
    const currentBidder = ref(null);
    const formData = ref({
      name: '',
      companyName: '',
      contactInfo: '',
      bidderType: '',
      description: '',
    });

    // 表单验证规则
    const schema = yup.object({
      name: yup.string().required('投标人姓名是必填项'),
      companyName: yup.string().required('公司名称是必填项'),
      contactInfo: yup.string().required('联系方式是必填项'),
      bidderType: yup.string().required('所属类型是必填项'),
    });

    // 使用 useForm Hook 进行表单验证
    const { handleSubmit, errors } = useForm({
      validationSchema: schema,
    });

    const submitForm = handleSubmit((values) => {
      if (currentBidder.value) {
        updateBidder(values);
      } else {
        addBidder(values);
      }
    });

    const filteredBidders = computed(() => {
      return bidders.value.filter((bidder) => {
        return (
            (!searchTerm.value || bidder.name.toLowerCase().includes(searchTerm.value.toLowerCase())) &&
            (!filterType.value || bidder.bidderType === filterType.value)
        );
      });
    });

    const paginatedBidders = computed(() => {
      const start = (currentPage.value - 1) * pageSize.value;
      return filteredBidders.value.slice(start, start + pageSize.value);
    });

    const openAddBidderForm = () => {
      currentBidder.value = null;
      resetFormData();
      showBidderForm.value = true;
    };

    const closeBidderForm = () => {
      showBidderForm.value = false;
    };

    const editBidderDetails = (bidder) => {
      currentBidder.value = bidder;
      formData.value = { ...bidder };
      showBidderForm.value = true;
    };

    const addBidder = (values) => {
      const newId = bidders.value.length ? Math.max(...bidders.value.map((p) => p.id)) + 1 : 1;
      bidders.value.push({ id: newId, ...values });
      Swal.fire('成功', '投标人添加成功', 'success');
      closeBidderForm();
    };

    const updateBidder = (values) => {
      const index = bidders.value.findIndex((p) => p.id === currentBidder.value.id);
      if (index !== -1) {
        bidders.value[index] = { ...values };
        Swal.fire('成功', '投标人修改成功', 'success');
        closeBidderForm();
      }
    };

    const confirmDelete = (bidderId) => {
      currentBidder.value = bidders.value.find((bidder) => bidder.id === bidderId);
      Swal.fire({
        title: '确认删除？',
        text: `您确认删除投标人：${currentBidder.value.name} 吗？`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '确认删除',
        cancelButtonText: '取消'
      }).then((result) => {
        if (result.isConfirmed) {
          deleteBidder(bidderId);
        }
      });
    };

    const deleteBidder = (id) => {
      bidders.value = bidders.value.filter((bidder) => bidder.id !== id);
      Swal.fire('删除成功', '投标人已删除', 'success');
    };

    const changePage = (newPage) => {
      currentPage.value = newPage;
    };

    const resetFormData = () => {
      formData.value = {
        name: '',
        companyName: '',
        contactInfo: '',
        bidderType: '',
        description: '',
      };
    };

    return {
      bidders,
      searchTerm,
      filterType,
      currentPage,
      pageSize,
      showBidderForm,
      currentBidder,
      formData,
      filteredBidders,
      paginatedBidders,
      openAddBidderForm,
      closeBidderForm,
      editBidderDetails,
      submitForm,
      addBidder,
      updateBidder,
      confirmDelete,
      deleteBidder,
      changePage,
      errors,
    };
  },
};
</script>

<style scoped>
.bidder-management {
  padding: 30px;
  width: 100%;
  box-sizing: border-box;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #f4f6f9;
}

.search-bar {
  width: 100%;
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}
.bidder-list td button {
  padding: 10px;
  cursor: pointer;
  border: none;
  border-radius: 5px;
  background-color: #007bff;
  color: #ffffff;
  transition: background-color 0.3s ease, transform 0.2s ease;
  margin-right: 10px;
}

.bidder-list td button:last-child {
  margin-right: 0;
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

.add-bidder-button-container {
  margin-bottom: 20px;
  text-align: right;
}

.add-bidder-button {
  padding: 10px 20px;
  background-color: #007bff;
  color: #ffffff;
  border: none;
  border-radius: 5px;
  font-weight: bold;
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.add-bidder-button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}

.bidder-list table {
  width: 100%;
  border-collapse: collapse;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: #ffffff;
}

.bidder-list th {
  background-color: #007bff;
  color: #ffffff;
  text-transform: uppercase;
  padding: 15px;
  text-align: left;
}

.bidder-list td {
  padding: 15px;
  border: 1px solid #eee;
  text-align: left;
}

.bidder-list tr:nth-child(even) {
  background-color: #f9f9f9;
}

.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
  gap: 15px;
}

.pagination button {
  padding: 10px 15px;
  background-color: #007bff;
  color: #ffffff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.pagination button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}

button {
  padding: 10px;
  cursor: pointer;
  border: none;
  border-radius: 5px;
  background-color: #007bff;
  color: #ffffff;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

button:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}

.error {
  color: #ff4d4d;
  font-size: 0.9em;
  margin-top: 5px;
}

.form-container {
  margin-bottom: 20px;
  padding: 20px;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  background-color: #ffffff;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
}

.form-field {
  margin-bottom: 15px;
}

.form-field label {
  font-weight: bold;
  margin-bottom: 5px;
  display: block;
}

.form-field input {
  width: 100%;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.05);
}
</style>