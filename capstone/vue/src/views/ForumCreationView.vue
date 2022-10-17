<template>
  <div class = "new-forum">
  <forum-creation></forum-creation>
  </div>
</template>

<script>
import ForumCreation from '../components/ForumCreation.vue'

export default {
    components: {
        ForumCreation
    }
}
</script>

<style>
.new-forum {
    height: 100vh;
}
</style>