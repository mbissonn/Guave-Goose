<template>
  <div>
    <link
      href="https://fonts.googleapis.com/css2?family=DynaPuff&display=swap"
      rel="stylesheet"
    />
    <div class="forumPost" v-bind:key="forumPost.postId">
      <div class="card cardStyle">
        <div
          class="container"
          @click="goToPost(forumPost.forumId, forumPost.postId)"
        >
          <div class="forum-name">g/{{ forumName }}</div>
          <h2 class="card-header-title has-text-centered">
            {{ forumPost.postTitle }}
          </h2>
          <div class="card-content has-text-centered">
            <div class="content">{{ forumPost.postBody }}</div>
            <img
              class="postImg"
              :src="forumPost.imagePath"
              alt="Placeholder image"
            />
          </div>
        </div>
        <footer class="card-footer">
          <button @click="voteOnPost(upvote)" class="upvote">
            Honk {{ forumPost.upVotes }}
          </button>
          <button @click="voteOnPost(downvote)" class="downvote">
            Hiss {{ forumPost.downVotes }}
          </button>
          <div class="card-footer-item is-justify=content-center">
            @{{ this.username }}
          </div>
        </footer>
      </div>
    </div>
  </div>
</template>

<script>
import ForumService from "../services/ForumService";
import UserService from "../services/UserService";
import ForumPostService from "../services/ForumPostService";

export default {
  name: "forumPost",
  props: ["forumPost"],

  data() {
    return {
      forumName: "",
      forumId: null,
      username: null,
      upvote: 1,
      downvote: -1,
    };
  },
  methods: {
    goToPost(forumId, postId) {
      this.$router.push(`/forums/${forumId}/post/${postId}`);
    },

    voteOnPost(voteValue) {
      ForumPostService.voteOnPost(this.forumPost.postId, voteValue).then(this.$router.go()
      );
    },

    getForum() {
      ForumService.getForumById(this.forumPost.forumId).then((response) => {
        this.forumName = response.data.forumTitle;
      });
    },

    getUsername() {
      UserService.getUsername(this.forumPost.userId).then((response) => {
        this.username = response.data.username;
      });
    },
  },

  created() {
    this.getForum();
    this.getUsername();
  },
};
</script>

<style scoped>
.cardStyle {
  box-shadow: 1px 2px 4px 0px rgba(15, 3, 49, 0.788);
}

.forumPost,
.card-content {
  font-family: "DynaPuff", sans-serif;
}

.postImg {
  box-shadow: 1px 2px 4px 0px rgba(15, 3, 49, 0.788);
  border: solid black 1px;
  height: 300px;
  width: 500px;
}

.footerBtns {
  padding: 8px;
}

.card {
  padding: 4px;
}

.forumPost {
  padding: 8px;
}

.card-footer {
  align-items: center;
}
</style>