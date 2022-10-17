import Vue from 'vue'
import Router from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Logout from '../views/Logout.vue'
import Register from '../views/Register.vue'
import store from '../store/index'
//import ListComments from '../components/ListComments.vue'
//ixeateComment.vue'
import CommentDetails from '../components/CommentDetails.vue'
import ForumListView from '../views/ForumListView.vue';
import ForumPostListView from '../views/ForumPostListView.vue'
import ForumPostDetailsView from '../views/ForumPostDetailsView.vue'
import ForumPostCreationView from '../views/ForumPostCreationView.vue'
import ForumCreationView from '../views/ForumCreationView.vue'
import UserForumListView from '../views/UserForumListView.vue'
//import ForumPostDetails from '../components/ForumPostDetails.vue'
// import ForumView from '../views/ForumView'
import ForumControl from '../components/ForumControl.vue'

Vue.use(Router)

/**
 * The Vue Router is used to "direct" the browser to render a specific view component
 * inside of App.vue depending on the URL.
 *
 * It also is used to detect whether or not a route requires the user to have first authenticated.
 * If the user has not yet authenticated (and needs to) they are redirected to /login
 * If they have (or don't need to) they're allowed to go about their way.
 */

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/login",
      name: "login",
      component: Login,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/logout",
      name: "logout",
      component: Logout,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/register",
      name: "register",
      component: Register,
      meta: {
        requiresAuth: false
      }
    },
    // {
    //   path: '/post/:postId/comment',
    //   name: "list-comments",
    //   component: ListComments,
    //   meta: {
    //     requiresAuth: true
    //   }
    // },
    // {
    //   path: '/post/:postId/comment',
    //   name: "create-comment",
    //   component: CreateComment,
    //   meta: {
    //     requiresAuth: true
    //   }
    // },
    {
      path: '/comment/:commentId',
      name: "comment-details",
      component: CommentDetails,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/forums/:forumId',
      name: "forum-posts",
      component: ForumPostListView,
      meta: {
        requiresAuth: false
      }
    },
    {
      path:"/forums",
      name: "forums",
      component: ForumListView,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: '/forums/:forumId/post/:postId',
      name: "post-details",
      component: ForumPostDetailsView,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: '/forums/:forumId/post',
      name: 'new-post-form',
      component: ForumPostCreationView,
      meta: {
        requiresAuth: true
      }
    },
     {
       path: '/create/forums',
       name: 'new-forum',
       component: ForumCreationView,
      meta: {
        requiresAuth: true
       }
     },
     {
       path: '/user/:userId/forums',
       name:'user-forums-list',
       component: UserForumListView,
       meta: {
         requiresAuth: true
       }
     },
     {
      path: '/forums/:forumId/forumcontrol',
      name: "forum-control",
      component: ForumControl,
      meta: {
        requiresAuth: true
      }
    }

  ]
})

router.beforeEach((to, from, next) => {
  // Determine if the route requires Authentication
  const requiresAuth = to.matched.some(x => x.meta.requiresAuth);

  // If it does and they are not logged in, send the user to "/login"
  if (requiresAuth && store.state.token === '') {
    next("/login");
  } else {
    // Else let them go to their next destination
    next();
  }
});

export default router;
