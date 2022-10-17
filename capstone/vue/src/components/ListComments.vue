<template>
  <div>
    <div v-if="isLoading">
      <img
        src="https://slack-imgs.com/?c=1&o1=ro&url=https%3A%2F%2Fmedia1.giphy.com%2Fmedia%2FhuDrF0FD1tc4do9PN8%2Fgiphy.gif%3Fcid%3D6104955e4vr8krt237ezrhk1fnig0pvlkr32uerddw0dre0h%26rid%3Dgiphy.gif%26ct%3Dg"
      />
    </div>
    <div v-else class="comments">
      <div
        class="box"
        v-for="comment in localComments"
        v-bind:key="comment.commentId" v-on:click="viewCommentDetails(comment.commentId)"
      >
      <div class="font">
        @{{ comment.username }}
      </div>
        {{ comment.commentBody }}
      </div>
      <create-comment></create-comment>
    </div>
  </div>
</template>

<script>
import CommentService from "../services/CommentService.js";
import CreateComment from './CreateComment.vue';
import UserService from '../services/UserService.js';


export default {
  components: { CreateComment },
  name: "list-comments",
  data() {
    return {
      message: "Hello World",
      isLoading: true,
      localComments: [],
      currentComment: {
        commentId: null,
        userId: null,
        commentBody: null,
        postId: null,
        dateCreated: null,
        username: null
      }
    };
  },
  methods: {
    getComments() {
      CommentService.listComments(
        this.$route.params.postId
      ).then((response) => {
        console.log("roobie boobsies")
        this.$store.commit("SET_COMMENTS", response.data);
        for (let i = 0; i < this.$store.state.comments.length; i++) {
          UserService.getUsername(this.$store.state.comments[i].userId).then((response) => {
            this.currentComment = this.$store.state.comments[i];
            this.currentComment.username = response.data.username;
            this.localComments.push(this.currentComment);
            this.isLoading = false;
          });
          
        }
        
      });
    },
    deleteComment(id) {
      CommentService.deleteComment(id).then((response) => {
        if (response.status === 200) {
          this.getComments();
        }
      });
    },
    viewCommentDetails(commentId) {
        this.$router.push(`/comment/${commentId}`)
    }
  },
  created() {
    this.getComments();
  },
};
</script>

<style scoped>
.comments {
  margin-top: 20px;
  padding: 20px;
  padding-top: 0;
}
.box {
    background: rgb(115,249,251);
    background: linear-gradient(0deg, rgba(115,249,251,1) 0%, rgba(246,208,125,1) 83%);
    width: 30%;
    transition: box-shadow .3s;
}
.box:hover {
box-shadow: 1px 2px 4px 0px rgba(15, 3, 49, 0.788);
transform: scale(1.03)

}
.comments-wrapper {
  max-height: 250px;
  overflow-y: auto;
  padding-right: 10px;
}
.font {
  font-family: 'DynaPuff', sans-serif;
}


</style>