<template>
    <div class = "forumPostList">
        <button @click="goToNewPostForm(forumPost)">Create New Post</button>
        <forum-post
        v-for="forumPost in forumPostList"
        v-bind:key="forumPost.postId"
        v-bind:forumPost="forumPost"
        >
        </forum-post>
    </div>
</template>

<script>
import forumPostService from '../services/ForumPostService'
import ForumPost from '../components/ForumPost.vue'

export default {
    name: "forumPost-list",
    components: {
        ForumPost
    },
    data() {
        return {
            forumPosts: {
                postId: 0,
                postTitle: "",
                postBody: "",
                imagePath: "",
                upVotes: 0,
                downVotes: 0,
                userId: 0,
                
            },
            forumId: null,
            forumPostList: []
        };
    },
    methods: {
        getForumPosts(){
            forumPostService.getPostsByForum(
                this.$route.params.forumId
            ).then((response) => {
                this.forumPostList = response.data;
            });
        },
        goToNewPostForm(){
            this.$router.push(`/forums/${this.forumId}/post`)
        }
    },
    created(){
        this.getForumPosts();
        this.forumId = this.$route.params.forumId;
    }
}

</script>

<style scoped>
    .forumPostList {
        height: 100vh;
    }
</style>