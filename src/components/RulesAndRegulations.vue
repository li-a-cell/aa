<template>
  <div class="regulations-container">
    <div v-for="regulation in regulations" :key="regulation.id" class="regulation-item" @click="handleRowClick(regulation)">
      <div class="regulation-title">{{ regulation.name }}</div>
      <div class="regulation-date">制定时间：{{ regulation.createdAt }}</div>
    </div>
  </div>
</template>

<script>
export default {
  name: "RegulationsTable",
  data() {
    return {
      regulations: [
        { id: 1, name: "制度一", createdAt: "2023-01-15" },
        { id: 2, name: "制度二", createdAt: "2023-02-10" },
        { id: 3, name: "制度三", createdAt: "2023-03-05" },
      ],
    };
  },
  methods: {
    handleRowClick(row) {
      this.$router.push({ name: "RegulationDetail", params: { id: row.id } });
    },
  },
};
</script>

<style scoped>
.regulations-container {
  padding: 20px;
  background-color: #ffffff;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.regulation-item {
  padding: 15px;
  border-bottom: 1px solid #ccc;
  cursor: pointer;
  background-color: #f0f0f0;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.regulation-item:last-child {
  border-bottom: none;
}

.regulation-title {
  font-size: 18px;
  font-weight: bold;
  color: #444;
  margin-bottom: 5px;
}

.regulation-date {
  font-size: 14px;
  color: #777;
  text-align: right; /* 将制定时间文本右对齐 */
}

.regulation-item:hover {
  background-color: #e0e0e0;
  transform: scale(1.02);
}
</style>