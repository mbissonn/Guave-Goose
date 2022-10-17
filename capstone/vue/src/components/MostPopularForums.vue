<template>
  <div class="homePage">
      <link href="https://fonts.googleapis.com/css2?family=DynaPuff&display=swap" rel="stylesheet">
    <div class="pop-posts">
        Today's Most Popular Posts
        <forum-post v-for="post in mostPopularPostsToday" v-bind:key="post.id" v-bind:forumPost="post" />
    </div>
    <div class="recent-forums"> Recent Activity
        <forum v-for="forum in mostRecentPostsForums" v-bind:key="forum.id" v-bind:forum="forum"/> 
    </div>
  </div>
</template>
<script>

//import Forum from '../components/Forum.vue'
import ForumPostService from '../services/ForumPostService'
import CommentService from '../services/CommentService'
import ForumPost from '../components/ForumPost.vue'
import Forum from '../components/Forum.vue'
import ForumService from '../services/ForumService'

export default {
    components:{
        ForumPost,
        Forum
    },
    data(){
        return {
            Posts: [],
            Comments: [],
            Forums: []
        }
    }, 
    computed:{
        mostPopularPostsToday(){
            let popular = this.postsToday;
             return popular.sort(this.popularCompareFunction);
        },

        mostRecentPosts(){
            let recent = this.Posts;
            return recent.sort(this.recentCompareFunction);
        },

        mostRecentPostsIDs(){
            let recent = this.mostRecentPosts;
            return recent.map(post=>{
                return post.forumId;
            })
        },
        mostRecentPostsForums(){
            let forums = this.Forums;
            let recents = forums.filter(forum=>{
                return forum.forumId in this.mostRecentPostsIDs;
            });

            let recentPostIds = this.mostRecentPostsIDs;
            return recents.sort((a,b)=>{
                return recentPostIds.indexOf(a.forumId) - recentPostIds.indexOf(b.forumId);
                });
            
            },
        

        postsToday(){
            return this.Posts.filter(post=>{
                if(post.createDate == this.currentDate){
                    return post;
                }
            })
        },
        currentDate(){
            let today = new Date();
            let month = today.getMonth();

            if((month+1)<10){
                month = "0" + (month+1);
            }
            return today.getFullYear() + "-" + month + "-" + today.getDate();  
        }
    },

    methods:{
        getPosts(){
        ForumPostService.getAllPosts().then(response => {
        this.Posts = response.data;})
        },
         popularCompareFunction(a,b){
            if( (a.upVotes-a.downVotes)>(b.upVotes-b.downVotes)){
                return -1;
            }
            if( (a.upVotes-a.downVotes)<(b.upVotes-b.downVotes)){
                return 1;
            }
             return 0;
        },

        recentCompareFunction(a,b){
            if (a.createDate>b.createDate){
                return -1;
            }
            if(a.createDate<b.createDate){
                return 1;
            }
             return 0;
        },

        getComments(){
            CommentService.getAllComments().then((response) => {
            this.Comments = response.data;
        })
    }, 

        getForums(){
            ForumService.getAllForums().then(response=>{
                this.Forums = response.data;
            })
        },
    },


created(){
        this.getPosts();
        this.getComments();
        this.getForums();
    }

}


</script>

<style>
.pop-posts{
    grid-area: "popPosts";
}

.recent-forums{
    grid-area: "recentForums";
}

.homePage{
    display: grid;
    grid-template-columns: 1fr 1fr;
    grid-template-areas: "popPosts" "recentForums";
    font-family: "DynaPuff", sans-serif
   

}
</style>