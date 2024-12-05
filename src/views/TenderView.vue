<template>
  <div id="tender-view">
    <Header />
    <div class="container">
      <Sidebar />
      <MainContent />
    </div>
  </div>
</template>

<script>
import Header from '../components/tendercom/Header.vue';
import Sidebar from '../components/tendercom/Sidebar.vue';
import MainContent from '../components/tendercom/MainContent.vue';

export default {
  name: 'TenderView',
  components: {
    Header,
    Sidebar,
    MainContent,
  },
};
</script>

<style scoped>
.container {
  display: flex;
}
</style>
