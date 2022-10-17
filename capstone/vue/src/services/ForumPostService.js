import axios from 'axios';


export default {
    getAllPosts(){
        return axios.get('/post')
    },

    getPostsByUser(userId){
        return axios.get(`/user/${userId}/post`)
    },

    getPostsByForum(forumId){
        return axios.get(`/forum/${forumId}/post`)
    },

    getPostById(postId){
        return axios.get(`/post/${postId}`)
    },

    getPostsByKeyWords(keywords){
        return axios.get('post/search', keywords)
    },

    createPostInForum(forumPost){
        return axios.post('/post', forumPost)
    },

    updatePost(forumPost, postId){
        return axios.put(`/post/${postId}`, forumPost)
    },

    deletePost(postId){
        return axios.delete(`/forum/post/${postId}`)
    },

    voteOnPost(postId, value){
        return axios.put(`/post/${postId}/vote?value=${value}`)
    }
}