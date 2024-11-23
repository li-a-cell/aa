<template>
  <div id="app">
    <Header />
    <div class="main-content">
      <Sidebar />
      <router-view />
    </div>
    <Footer />
  </div>
</template>

<script>
import Header from './components/Header.vue';
import Sidebar from './components/Sidebar.vue';
//import Footer from './components/Footer.vue';
export default {
  name: 'App',
  components: {
    Header,
    Sidebar,
//    Footer,
  },
};
</script>

<style>
.main-content {
  display: flex;
}
</style>