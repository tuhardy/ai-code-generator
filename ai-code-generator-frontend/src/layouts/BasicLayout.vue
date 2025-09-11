<template>
  <a-layout class="basic-layout">
    <global-header />
    <a-layout-content>
      <router-view />
    </a-layout-content>
    <global-footer />
  </a-layout>
</template>

<script setup>
import GlobalHeader from '@/components/GlobalHeader.vue'
import GlobalFooter from '@/components/GlobalFooter.vue'
</script>

<style scoped>
.basic-layout {
  min-height: 100vh;
}
</style>