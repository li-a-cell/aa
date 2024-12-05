<template>
  <el-carousel :interval="3000" type="carousel" height="300px" class="carousel-container" style="background-color: #999999">
    <el-carousel-item>
      <div>这是一个图片1</div>
    </el-carousel-item>
    <el-carousel-item>
      <div>这是一个图片2</div>
    </el-carousel-item>
    <el-carousel-item>
      <div>这是一个图片3</div>
    </el-carousel-item>
  </el-carousel>
</template>

<script>
export default {
  name: 'CarouselComponent'
};
</script>

<style scoped>
.carousel-container {
  width: 100%;
}
</style>