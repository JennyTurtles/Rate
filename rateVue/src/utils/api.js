import axios from 'axios'
import {Message} from 'element-ui';
import router from '../router'
import {mymessage} from '@/utils/mymessage';
import qs from 'qs';

axios.interceptors.response.use(success => {  //
    if (success.status && success.status == 200 && success.data.status == 500) {
        if (success.data.msg === '该数据有关联数据，操作失败!')
            return
        Message.error({message: success.data.msg})
        return;
    }
    if (success.data.msg) {
        // Message.success({message: success.data.msg})
    }
    return success.data;
}, error => { //
    if (error.response.status == 504 || error.response.status == 404) {
        Message.error({message: '服务器被吃了( ╯□╰ )'})
    } else if (error.response.status == 403) {
        Message.error({message: '权限不足，请联系管理员'})
    } else if (error.response.status == 401) {
        mymessage.error({message: error.response.data.msg ? error.response.data.msg : '尚未登录，请登录'})
        router.replace('/');
    } else {
        if (error.response.data.msg) {
            Message.error({message: error.response.data.msg})
        } else {
            Message.error({message: '未知错误!'})
        }
    }
    return;
})

let base = '';

export const postKeyValueRequest = (url, params) => {
    return axios({
        method: 'post',
        url: `${base}${url}`,
        data: params,
        transformRequest: [function (data) {
            let ret = '';
            for (let i in data) {
                ret += encodeURIComponent(i) + '=' + encodeURIComponent(data[i]) + '&'
            }
            return ret;
        }],
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'token': localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).token : ''
        }
    });
}
export const postRequest = (url, params) => {
    return axios({
        method: 'post',
        url: `${base}${url}`,
        data: params,
		//contentType:"applicaiton/json",
		headers: {
		    'Content-Type': 'application/json',
            'token': localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).token : ''
		}
    })
}
//数据转换
export const postRequest1 = (url, params) => {
    return axios({
        method: 'post',
        url: `${base}${url}`,
        data: qs.stringify(params),
		headers: {
            'token': localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).token : ''
		}
    })
}
export const putRequest = (url, params) => {
    return axios({
        method: 'put',
        url: `${base}${url}`,
        data: params,
        headers: {
            'token': localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).token : ''
        }
    })
}
export const getRequest = (url, params) => {
    return axios({
        method: 'get',
        url: `${base}${url}`,
        params: params,
        headers:{
            'token': localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).token : ''
        }
    })
}
export const deleteRequest = (url, params) => {
    return axios({
        method: 'delete',
        url: `${base}${url}`,
        params: params,
        headers:{
            'token': localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).token : ''
        }
    })
}
