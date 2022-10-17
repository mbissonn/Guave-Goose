<template>
  <div id="app">
    <div id="nav" class="navbar" role="navigation">
      <!-- <div class="navbar-brand">
        <button class="side-bar-button" @click="openClose()">SIDE BAR</button>
      </div> -->
      <div>
        <img class="logo" src="../assets/cover.png"/>
      </div>
      <div class="navbar-end">
      <div v-if="$store.state.token != ''" >You are logged in as  {{userName}} </div>
      <div v-else>You are not logged in.</div>
      <router-link v-bind:to="{ name: 'home' }"> Home | </router-link>&nbsp;
      <router-link v-bind:to="{ name: 'logout' }" v-if="$store.state.token != ''"> Logout | </router-link>&nbsp;
      <router-link v-bind:to="{ name: 'login' }" v-else>&nbsp;Login | </router-link>&nbsp;
      <router-link v-bind:to="{name: 'forums'}">Browse Forums</router-link>
      </div>
    </div>
    <div class="page">
      <side-bar class="w3-side-bar w3-bar-block" />
      <div class="main-page">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script>
import SideBar from "./components/SideBar.vue";


export default {
  data() {
    return {
      forum: [],
      isOpen: false,
      userName: ""
    };
  },
  components: {
    SideBar,
  },
  methods: {
    openClose() {
      if (this.isOpen) {
        this.isOpen = false;
      } else {
        this.isOpen = true;
      }
    },
  },

  created(){
    this.userName = this.$store.state.user.username;
  },
  updated(){
    this.userName = this.$store.state.user.username;
  }
};
</script>

<style scoped>
#app{
background: rgb(248,152,128);
background: linear-gradient(0deg, rgba(248,152,128,1) 0%, rgba(236,225,162,1) 50%, rgba(248,152,128,1) 100%);
height: 100%;
}
.header {
  text-align: center;
  align-items: center;
  justify-content: center;
}
.side-bar {
  display: inline;
  width: 25px;
  display: flex;
  position: fixed;
  flex-direction: column;
  justify-content: center;
  border-right: 5px;
  grid-area: "side";
}

.side-bar-button:hover {
  box-shadow: 1px 2px 4px 0px rgba(15, 3, 49, 0.788);
  transform: scale(1.03);
}
.main-page {
  grid-area: "content";
}

.page {
  display: grid;
  grid-template-columns: 1fr 5fr;
  grid-template-areas: "side" "content";
}

.navbar {
  background: rgb(248,152,128);
  background: linear-gradient(0deg, rgba(248,152,128,1) 0%, rgba(236,225,162,1) 100%);
  border-bottom: 12px solid;
  border-color: #367588;
  padding: 4px;
}

.navbar-brand {
  flex: 2;
}


.logo {
  width: 300px;
  height: auto;
  border: 0;
  align-items: center;
}

.navbar-end {
  flex: 2;
}

.logo {
  width:300px;
  height: auto;
  border:0;
}
</style>