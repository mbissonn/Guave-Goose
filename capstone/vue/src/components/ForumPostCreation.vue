<template>
  <form class="new-post-form" v-on:submit.prevent="savePost()">
    <input
      type="text"
      class="input"
      placeholder="Title"
      v-model="forumPost.postTitle"
    />
    <input
      type="text"
      class="textarea"
      placeholder="Body"
      v-model="forumPost.postBody"
    />
    <input
      type="text"
      class="input"
      placeholder="Image URL"
      v-model="forumPost.imagePath"
    />
    <button type="submit">Save</button>
  </form>
</template>

<script>
import ForumPostService from "../services/ForumPostService.js";

export default {
  name: "new-post-form",
  data() {
    return {
      forumPost: {
        postTitle: "",
        postBody: "",
        imagePath: "",
        forumId:0
      },
    };
  },
  created(){
  this.forumPost.forumId = this.$route.params.forumId; 
  },
  methods: {
    savePost() {
      ForumPostService.createPostInForum(this.forumPost).then(() => {
        // this.forumPost = {
        //   postTitle: "",
        //   postBody: "",
        //   imagePath: ""
        // };
        this.$router.push(`/forums/${this.forumPost.forumId}`)
      });
    },
  },
};
</script>
