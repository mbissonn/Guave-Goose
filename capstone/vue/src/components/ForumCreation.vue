<template>
    <form class="new-forum" v-on:submit.prevent="createForum()">
        <input type="text" class="input" placeholder="Title" v-model="forum.forumTitle"/>
        <input type="text" class="input" placeholder="Image URL (optional)" v-model="forum.imageSource"/>
        <input type="text" class="textarea" placeholder="Description" v-model="forum.description"/>
        <input type="text" class="input" placeholder="Question 1" v-model="forum.question1">
        <input type="text" class="input" placeholder="Question 2" v-model="forum.question2">
        <input type="text" class="input" placeholder="Question 3" v-model="forum.question3">
        <button type="submit">Create Forum</button>
    </form>
</template>

<script>
import ForumService from '../services/ForumService.js'

export default {
    name: "new-forum",
    data() {
        return {
            forum: {
                forumTitle: '',
                imageSource: '',
                description: '',
                question1: '',
                question2: '',
                question3: ''
            },
            userId: null
        }
    },
    methods: {
        createForum() {
            ForumService.createForum(this.forum).then(() => {
                this.forum = {
                    forumTitle: "",
                    imageSource: "",
                    description: "",
                    question1: "",
                    question2: "",
                    question3: ""
                    
                }
                
            });
            this.$router.push(`/user/${this.userId}/forums`)
            this.$router.go();
        }
    },

    created() {
        this.userId = this.$store.state.user.id;
    }
}
</script>

