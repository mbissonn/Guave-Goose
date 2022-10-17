<template>
    <div class = "userForumList">
        <forum 
        v-for="forum in userForumList"
        v-bind:key="forum.forumId"
        v-bind:forum="forum"
        />
    </div>
</template>

<script>
import Forum from '../components/Forum.vue'
import ForumService from '../services/ForumService.js'

export default {
    components:{
        Forum
    },

    data() {
        return {
            forum: {
                forumId: 0,
                forumTitle: "",
                userId: 0,
                createDate: "",
                imageSource: "",
                description: ""
            },
            userForumList: []
        };
    },

    created(){
        ForumService.getForumsByUserId(this.$route.params.userId).then((response) => {
            this.userForumList = response.data;
        })
    }
}
</script>

<style>

.userForumList {
    height: 100vh;
}

</style>