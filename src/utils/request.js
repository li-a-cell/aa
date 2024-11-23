import axios from 'axios';

const request=axios.create({
    baseURL:'http://localhost:9528',
});

request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json ';
    let user=JSON.parse(localStorage.getItem("Token"))
    config.headers['token']=user.token
    return config;
},
);
request.interceptors.response.use(
    response => {
        let res = response.data;
        if(typeof res === 'object'){
            res=res ? JSON.parse(res):res;
        }
        else if(typeof res === 'string'){
            res=res ? JSON.parse(res):res;
        }
        return res;
    }
)