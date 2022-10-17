<template>
  <div class="comment">
    <link href="https://fonts.googleapis.com/css2?family=DynaPuff&display=swap" rel="stylesheet">
    <div class="box">
      <div class="font has-text-weight-semi-bold">
        @ {{ this.username }}
      </div>
      {{ this.$store.state.activeComment.commentBody }}
    </div>
    <form>
      <input
        type="text"
        v-show="isOriginalPoster || isModerator"
        v-model="activeComment.commentBody"
        placeholder="Edit your comment (HONK)"
      />
      <button
        class="btn btn-submit"
        v-show="isOriginalPoster || isModerator"
        v-on:click.prevent="editComment"
      >
        Edit Comment
      </button>
    </form>
  </div>
</template>

<script>
import CommentService from "../services/CommentService.js";
import ForumService from "../services/ForumService.js";
import UserService from "../services/UserService.js";

export default {
  name: "comment-details",
  data() {
    return {
      isOriginalPoster: false,
      isModerator: false,
      isMember: false,
      activeComment: {
        commentBody: "",
      },
      forumId: -1,
      username: null
    };
  },
  methods: {
    retrieveComment() {
      CommentService.getComment(this.$route.params.commentId).then(
        (response) => {
          this.$store.commit("SET_ACTIVE_COMMENT", response.data);
          if (
            this.$store.state.activeComment.userId == this.$store.state.user.id
          ) {
            this.isOriginalPoster = true;
          }
          const postId = this.$store.state.activeComment.postId;
          ForumService.getForumIdByPostId(postId).then((response) => {
            this.forumId = response.data.forumId;
            if (
              ForumService.getModeratorStatus(this.forumId).then((response) => {
                if (response.data === 2) {
                  this.isModerator = true;
                }
                if (response.data === 0) {
                  this.isMember = true;
                }
              })
            );
          });
          UserService.getUsername(this.$store.state.activeComment.userId).then((response) => {
            this.username = response.data.username;
          })
        }
      );
    },
    editComment() {
      const postId = this.$store.state.activeComment.postId;
      if (this.activeComment.commentBody != "") {
        CommentService.updateComment(
          this.$route.params.commentId,
          this.activeComment
        ).then((response) => {
          if (response.status === 200) {
            this.$router.push(`/forums/${this.forumId}/post/${postId}`);
          }
        });
      } else {
        alert("Comment Body Must Not Be Empty!");
      }
    },
    isPoster() {
      if (
        this.$store.state.user.id === this.$store.state.activeComment.userId
      ) {
        this.isOriginalPoster = true;
      }
    },
  },
  created() {
    this.retrieveComment();

    this.isPoster();
  },
};
</script>

<style scoped>
.comment {
  margin-top: 20px;
  padding: 20px;
  padding-top: 0;
  height: 100vh;
}
.box {
  background: rgb(115, 249, 251);
  background: linear-gradient(
    0deg,
    rgba(115, 249, 251, 1) 0%,
    rgba(246, 208, 125, 1) 83%
  );
  width: 30%;
  transition: box-shadow 0.3s;
}
.font {
  font-family: 'DynaPuff', sans-serif;
}
</style>