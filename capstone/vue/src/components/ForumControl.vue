<template>
    <div>
        <table class="table">
            <th>Username</th>
            <th><abbr title = "(0 = Member, 1 = Moderator Request, 2 = Moderator)"> Member Status</abbr></th>
            <th>Accept</th>
            <th>Deny</th>
            <th>Remove</th>
            <tr v-for="userStatus in userForumStatus" v-bind:key="userStatus.userId">
                <td>{{ userStatus.username }}</td>
                <td> {{ userStatus.forumRoleId }}</td>
                <td class="button" @click="grantModeratorStatus(userStatus.userId)">Accept</td>
                <td></td>
                <td></td>
            </tr>

        </table>
    </div>
</template>

<script>
import ForumService from '../services/ForumService.js'

export default {
    name: "forum-control",
    data() {
        return {
            userForumStatus: [],
            currentUserForumStatus: {
                forumId: null,
                userId: null,
                forumRoleId: null,
                isFavorited: null,
                answer1: null,
                answer2: null,
                answer3: null,
                username: null
            }
        }
    },

    methods: {
        retrieveUserForum() {
            ForumService.getAllUserForumDataByForumId(this.$route.params.forumId
            ).then((response) => {
                this.userForumStatus = response.data;
            })
        },
        grantModeratorStatus(pendingUserId) {
            ForumService.grantModeratorStatus(this.$route.params.forumId, pendingUserId).then((response) => {
                if (response.status === 200) {
                    this.retrieveUserForum();
                } else {
                    alert("Invalid acceptance!")
                }
            })
        }
    },

    created() {
        this.retrieveUserForum();
    }
}
</script>