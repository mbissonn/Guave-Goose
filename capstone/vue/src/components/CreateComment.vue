<template>
  <form>
    <div>
      <input
        type="text"
        v-model="newComment.commentBody"
        placeholder="Honk a new comment!"
      />
      <button class="btn btn-submit" v-on:click.prevent="saveComment">
        Submit Comment
      </button>
      <button class="btn" v-on:click="cancel">Cancel</button>
    </div>
  </form>
</template>

<script>
import CommentService from "../services/CommentService.js";

export default {
  name: "create-comment",
  data() {
    return {
      newComment: {
        //userId: null,
        commentBody: "",
        postId: null,
      },
    };
  },
  methods: {
    saveComment() {
      //this.newComment.userId = this.$store.state.user.id;
      if (this.newComment.commentBody != "") {
        CommentService.addComment(this.newComment).then((response) => {
          if (response.status === 201) {
            this.$router.go();
            this.newComment = {
              //userId: null,
              commentBody: "",
              postId: null,
            };
          }
        }).catch((error) => {
            console.log("This is from the error")
            if (error.response.status == 404) {
              alert("You must be a member of this forum to post a comment!");
            } else if (error.response.status == 401) {
              alert("You must be logged in to post a comment!")
            }
          });
      } else {
        alert("Comment Body Must Not Be Empty");
      }
      console.log("This is from immediately after");
    },
    cancel() {
      const postId = this.$route.params.postId;
      this.$router.push({ path: `/post/${postId}/comment` });
    },
  },

  created() {
    this.newComment.postId = this.$route.params.postId;
  },
};
</script>