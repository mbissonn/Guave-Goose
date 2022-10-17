<template>
    <div class = "spaghetti">
    <forum-post-details/>
    <list-comments/>
    </div>
</template>

<script>
import ForumPostDetails from '../components/ForumPostDetails.vue'
import ListComments from '../components/ListComments.vue'

export default {
    components: {
        ForumPostDetails,
        ListComments
    }
}
</script>

<style>

.spaghetti {
    height: 100vh;
}

</style>
