<template>
  <div>
    <link href="https://fonts.googleapis.com/css2?family=DynaPuff&display=swap" rel="stylesheet">
    <div>
        <h4  class = "fontHeader">My Forums</h4>
        <h5  class = "fontSidebar" v-for="forum in ForumMember" @click="goToForumPage(forum.forumId)" v-bind:key="forum.forumId">{{forum.forumTitle}}</h5>
        <h2 class = "fontSidebar" @click="goToMyForums">Create Forum</h2>


    </div>


  </div>
</template>

<script>
import forumService from '../services/ForumService'

export default {
    data(){
        return{
        name: 'side-bar-forum-list',
        Favorites: [],
        ForumMember: [],
        ModeratorForums: [],
        userId: null
        }
    },
  
methods:{

    goToForumPage(id){
      this.$router.push(`/forums/${id}`)
    },

    getFavorites(){
        forumService.getFavorites().then(response => {
        this.Favorites = response.data;})
     },

     getModeratorForums(){
       forumService.getModeratorForums().then(response=>{
         this.ModeratorForums = response.data;
       })
     },
    
    getMemberForums(){
      forumService.getMemberForums().then((response)=>{
        this.ForumMember = response.data;
      })
    },

    goToMyForums() {
      this.$router.push(`/user/${this.userId}/forums`)
    }

    },
    created(){
        this.userId = this.$store.state.user.id;
        this.getFavorites();
        this.getMemberForums();
        this.getModeratorForums();
    },

    update(){
        this.userId = this.$store.state.user.userId;
        this.getFavorites();
        this.getMemberForums();
        this.getModeratorForums();
    }
}
</script>

<style>

.fontHeader {
  font-family: "DynaPuff", sans-serif;
  text-align: center;
  font-size: 35px;
  font-weight: bold;
}

.fontSidebar{
  font-family: "DynaPuff", sans-serif;
  text-align: center;
  font-size: 35px;
  cursor: pointer;
}

.fontSidebar:hover {
  font-weight: bold;
  box-shadow: 1px 2px 4px 0px rgba(15, 3, 49, 0.788);
  transform: scale(1.03);
}

</style>