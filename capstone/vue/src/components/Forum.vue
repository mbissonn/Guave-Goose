<template>
  <div>
    <div class="forum" v-bind:key="forum.forumId">
      <div class="card">
        <div class="container" @click="goToForum(forum.forumId)">
          <div class="notification">
            <div class="card-content">
              <div class="media">
                <div class="media-left">
                  <figure class="image is-128x128">
                    <img class="forumImg" :src="forum.imageSource" alt="Placeholder image" />
                  </figure>
                </div>
                <div class="media-content font">
                  <h1 class="forum-name">g/{{ forum.forumTitle }}</h1>
                  <!-- <a href="#"><p class="title is-4">e/cat</p></a> -->
                  <p class="subtitle is-6">
                    Date Created: {{ forum.createDate }}
                  </p>
                </div>
              </div>
              <div class="content font">
                {{ forum.description }}
              </div>
              <div class="centerBtn">
                <button class="button is-danger is-outlined">Favorite</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "forum",
  props: ["forum"],

  methods: {
    goToForum(forumId) {
      this.$router.push(`/forums/${forumId}`);
    },
  },
};
</script>

<style>
.container {
  cursor: pointer;
  background-color: transparent;
  
}

.card, .media, .content, .notification {
  background-color: transparent;
}

.forum {
  padding-top: 4px;
}

.card {
  box-shadow: 1px 2px 4px 0px rgba(15, 3, 49, 0.788);
  padding: 8px;
}



.card-content:hover {
  box-shadow: 1px 2px 4px 0px rgba(15, 3, 49, 0.788);
  transform: scale(1.03);
}

.forumImg {
  border: solid black 1px;
}

.font {
  font-family: "DynaPuff", sans-serif;
}

.centerBtn {
  text-align: center;
}
</style>  