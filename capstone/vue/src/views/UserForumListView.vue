<template>
    <div>
        <button @click="goToNewForumForm()">Create New Forum</button>
        <user-forum-list></user-forum-list>
    </div>
</template>

<script>
import userForumList from '../components/UserForumList.vue'

export default {
    components: {
        userForumList
    },
    methods: {
    goToNewForumForm(){
      this.$router.push('/create/forums')
    }
  }
}
</script>
