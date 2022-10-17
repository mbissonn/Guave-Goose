import axios from "axios";

const options = {
    method: 'GET',
    url: 'https://random-username-generate.p.rapidapi.com/',
    params: { locale: 'en_US', minAge: '18', maxAge: '50' },
    headers: {
        'X-RapidAPI-Key': '7cabe9042emshed9e4a22c9864ebp1f540cjsnec8e39488784',
        'X-RapidAPI-Host': 'random-username-generate.p.rapidapi.com'
    }
};

export default {

    getUsername() {
        return axios.request(options)
    }
}