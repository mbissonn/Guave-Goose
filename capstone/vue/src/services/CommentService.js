
import axios from 'axios';

export default {
    listComments(postId) {
        return axios.get(`/post/${postId}/comment`)
    },

    getAllComments(){
        return axios.get(`/comment`)
    },
    getComment(commentId) {
        return axios.get(`/comment/${commentId}`)
    },

    getCommentByUserId(userId) {
        return axios.get(`user/${userId}/comment`)
    },

    addComment(comment) {
        return axios.post('/comment', comment)
    },

    updateComment(commentId, comment) {
        return axios.put(`/comment/${commentId}`, comment)
    },

    deleteComment(forumId, postId, commentId) {
        return axios.delete(`/forum/${forumId}/post/${postId}/comment/${commentId}`)
    }
}