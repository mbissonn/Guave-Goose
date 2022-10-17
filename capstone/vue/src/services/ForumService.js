import axios from "axios";



export default {

    getAllForums(){
        return axios.get('/forum');
    },

    getForumById(forumId){
        return axios.get(`/forum/${forumId}`)
    },

    getForumsByUserId(userId){
        return axios.get(`/user/${userId}/forum`)
    },

    getForumFromKeywords(keywords){
        return axios.get('/forum/search', keywords)
    },
    
    getMemberForums(){
        return axios.get('/user/member')
    },

    getFavoriteForums(){
        return axios.get('/user/favorite')
    },

    createForum(Forum){
        return axios.post('/forum', Forum)
    },

    updateForum(forumId, Forum){
        return axios.put(`/forum/${forumId}`, Forum)
    },

    deleteForum(forumId){
        axios.delete(`/forum/${forumId}`)
    },

    userJoinForum(forumId){
        axios.post(`/user/forum/${forumId}`)
    },

    requestModeratorStatus(forumId){
        axios.put(`/user/forum/${forumId}`)
    },

    grantModeratorStatus(userId, forumId){
        axios.put(`/user/forum/${forumId}/moderate`, userId)
    },

    setFavoriteForum(forumId){
        axios.put(`/user/forum/${forumId}/favorite`)
    },

    answerQuestion(userId, forumId, questionId, answer){
        return axios.put(`/user/forum/${forumId}/question/${questionId}`, userId, answer)
    },
    getForumIdByPostId(postId) {
        return axios.get(`forum/post/${postId}`)
    },

    getModeratorStatus(forumId) {
        return axios.get(`forum/${forumId}/user`)
    },

    getFavorites(){
        return axios.get(`/user/favorite`)
    },

    getAllUserForumDataByForumId(forumId) {
        return axios.get(`/userstatus/forum/${forumId}`)
    },
   
    getAnswers(forumId, userId){
        return axios.get(`/user/${userId}/forum/${forumId}/answer`)
    }
}