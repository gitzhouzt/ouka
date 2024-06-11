<template>
  <n-carousel show-arrow autoplay>
    <img v-for="i in 7" :key="i" class="carousel-img" :src="`${envConfig.static}/images/ad/ad_${i}.webp`" />
  </n-carousel>
</template>

<script setup lang="ts">
import { getEnvConfig } from '~/.env-config';

const envConfig = getEnvConfig(import.meta.env);
</script>
<style scoped>
.carousel-img {
  width: 100%;
  height: 240px;
  object-fit: cover;
}
</style>
