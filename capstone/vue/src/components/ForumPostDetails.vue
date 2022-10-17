<template>
   <forum-post id ="forumPost"
   v-bind:key="forumPost.postId" 
   v-bind:forumPost="forumPost">
   </forum-post> 
</template>

<script>
import ForumPostService from "../services/ForumPostService.js"
import ForumPost from '../components/ForumPost.vue'

export default {
    data(){
        return {
            post : null
        }
    },
    name: "forumPost-details",
    props: ["forumPost"],
    components: {
        ForumPost
    },
    
    methods: {
        getForumPostDetails(){
            ForumPostService.getPostById(
                this.$route.params.forumId, this.$route.params.postId
            ).then((response) => {
                // eslint-disable-next-line vue/no-mutating-props
                this.forumPost = response.data;
            });
        }
    },
    created(){
        this.getForumPostDetails();
    }
}
</script>

<style> 
#forumPost {
    padding: 4px;
    align-items: center;
    

}
</style>
