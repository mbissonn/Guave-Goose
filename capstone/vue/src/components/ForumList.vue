<template>
<div  class="forums">
  <forum
    v-for="forum in forumList"
    v-bind:key="forum.forumId"
    v-bind:forum="forum"
  />
  </div>
</template>



<script>

import forumService from '../services/ForumService'
import Forum from '../components/Forum.vue'

export default{
  name:"forum-list",
  components:{
    Forum
  },

  data(){
    return {
      forum: {
        forumId: 0,
        forumTitle: "",
        userId: 0,
        createDate: "",
        imageSource: "",
        description: ""
      },
      forumList: []
    };
  },

  created(){
    forumService.getAllForums().then((response) => {
      this.forumList = response.data;
    })
  }
}

</script>

<style>
.card-content {
  /* background-color: #367588; */
  background-color: #ECE1A2;
  /* background-color: #F89880; */
  /* background-color: #6ca360; */


  color: black;
  border: 10px solid white;
  font-family: "Cinzel", serif;
}

.notification {
  padding-right: 350px;
}

h1{
  font-weight: bold;
}
</style>

