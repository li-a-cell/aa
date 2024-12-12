<template>
  <div class="material-details-page">
    <div class="detail-container">
      <!-- 材料详细信息展示 -->
      <h2>材料详情</h2>
      <p>材料名称: {{ materialDetails.materialName }}</p>
      <p>材料类型: {{ materialDetails.materialType }}</p>
      <p>数量: {{ materialDetails.currentStockQuantity }}</p>
      <p>备注: {{ materialDetails.remarks }}</p>
      <!-- 更多详情信息展示... -->
    </div>
    <div class="image-container">
      <!-- 图片展示区域 -->
      <img v-if="imageUrl" :src="imageUrl" alt="材料图片" style="max-width: 300px; max-height: 300px;">
      <!-- 上传图片组件 -->
      <el-upload
          class="upload-demo"
          action="/api/uploadMaterialPhoto"
          :headers="{ 'token': token }"
          :on-success="handleUploadSuccess"
        :before-upload="beforeUpload"
        :data="{ materialId: 1 }"
        list-type="picture" >
      <el-button size="small" type="primary">点击上传</el-button>
      </el-upload>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRoute } from "vue-router";
import { ElUpload, ElButton } from 'element-plus';

export default {
  components: {
    ElUpload,
    ElButton
  },
  setup() {
    const materialDetails = ref({});
    const token = localStorage.getItem('jwtToken');
    const materials = ref([]);
    const allMaterialsBackup = ref([]);
    const route = useRoute();
    const imageUrl = ref(''); // 用于存储图片展示的url

    const fetchMaterials = async () => {
      try {
        const response = await axios.get('/api/administrator/getAllMaterial', {
          headers: {
            'token': token
          }
        });
        materials.value = response.data;
        allMaterialsBackup.value = response.data;
        console.log('allMaterialsBackup', allMaterialsBackup);
      } catch (error) {
        console.log('获取材料数据出错：', error.message);
      }
    };

    const handleUploadSuccess = (response, file, fileList) => {
      // 上传成功后，设置图片展示的url，这里假设后端返回的图片地址在response.data.url中，需根据实际调整
      imageUrl.value = response.data.url;
    };

    const beforeUpload = (file) => {
      const isJPEG = file.type === 'image/jpeg';
      const isPNG = file.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPEG &&!isPNG) {
        alert('只能上传 JPG/PNG 格式的图片！');
        return false;
      }
      if (!isLt2M) {
        alert('图片大小不能超过 2MB！');
        return false;
      }
      return true;
    };

    onMounted(() => {
      fetchMaterials();
    });

    return {
      materialDetails,
      imageUrl,
      token,
      handleUploadSuccess,
      beforeUpload
    };
  }
};
</script>

<style scoped>
.material-details-page {
  display: flex;
  padding: 30px;
  width: 100%;
  box-sizing: border-box;
  font-family: 'Arial', sans-serif;
  background-color: #f9fbfd;
}

.detail-container {
  flex: 1;
  margin-right: 30px;
}

.image-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}
</style>