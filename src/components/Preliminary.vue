<template>
  <div class="container ">
    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="nodeName" label="节点名称"></el-table-column>
      <el-table-column prop="nodeId" label="节点id"></el-table-column>
      <el-table-column prop="nodeType" label="节点类型"></el-table-column>
      <el-table-column prop="implementerName" label="实施人姓名"></el-table-column>
      <el-table-column prop="implementer" label="实施人员"></el-table-column>
      <el-table-column prop="status" label="状态"></el-table-column>
      <el-table-column prop="startTime" label="开始时间"></el-table-column>
      <el-table-column prop="approvalTime" label="审批时间"></el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableData: [
        {
          nodeName: '模拟节点1',
          nodeId: 'simulate1',
          nodeType: '一级节点',
          implementerName: '李四',
          implementer: '实施人员1',
          status: '已完成',
          startTime: '2023 - 01 - 01 00:00',
          approvalTime: '2023 - 01 - 02 00:00'
        },
        {
          nodeName: '模拟节点2',
          nodeId: 'simulate2',
          nodeType: '二级节点',
          implementerName: '王五',
          implementer: '实施人员2',
          status: '进行中',
          startTime: '2023 - 02 - 01 00:00',
          approvalTime: '未开始'
        }
      ]
    };
  }
};
</script>

<style>
.container  {
  padding: 20px;
}
</style>