<template>
  <el-dialog :title="`为项目${projectId}投标`" :visible="visible" @close="handleClose">
    <el-form :model="bidForm" :rules="bidRules" ref="bidFormRef">
      <el-form-item label="投标金额" prop="biddingPrice">
        <el-input v-model="bidForm.biddingPrice" type="number" placeholder="请输入投标金额"></el-input>
      </el-form-item>
    </el-form>
    <template slot="footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="confirmBid">确认投标</el-button>
    </template>
  </el-dialog>
</template>

<script>
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';

export default {
  name: 'BidModal',
  props: {
    visible: {
      type: Boolean,
      required: true
    },
    projectId: {
      type: Number,
      required: true
    }
  },
  emits: ['confirm-bid'],
  setup(props, { emit }) {
    const bidForm = reactive({
      biddingPrice: 0
    });
    const bidFormRef = ref(null);
    const bidRules = reactive({
      biddingPrice: [
        { required: true, message: '请输入投标金额', trigger: 'blur' }
      ]
    });

    const handleClose = () => {
      emit('update:visible', false);
    };

    const confirmBid = async () => {
      try {
        await bidFormRef.value.validate();
        emit('confirm-bid', bidForm);
      } catch (error) {
        ElMessage.error('请输入正确的投标金额');
      }
    };

    return {
      bidForm,
      bidFormRef,
      bidRules,
      handleClose,
      confirmBid
    };
  }
};
</script>

<style scoped>
.el-dialog__body {
  padding: 20px;
}

.el-dialog {
  z-index: 9999;
  width: 400px;
  height: auto;
  overflow: visible;
}
</style>