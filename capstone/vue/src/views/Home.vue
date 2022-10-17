<template>
  <div class="home">
    <most-popular-forums/>
  </div>
</template>

<script>
import MostPopularForums from '../components/MostPopularForums.vue';

export default {
  components: { MostPopularForums },
  name: "home"
};
</script>

