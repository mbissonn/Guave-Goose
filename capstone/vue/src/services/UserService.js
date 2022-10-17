import axios from 'axios';

export default {
    getUsername(userId) {
        return axios.get(`/user/${userId}`);
    }
}