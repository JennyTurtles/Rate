import {getRequest} from "./api";
import this_ from '../main'
import router from '../router'
import {log} from "@/utils/sockjs";
import store from '../store'
// export async function initMenu(router, store)  {
export const initMenu = (router, store) => {
// export function initMenu(router, store,to){
    return new Promise((resolve,reject) => {
        if (store.state.routes.length > 0) {
            return;
        }
        let user = JSON.parse(localStorage.getItem('user'))
        // new Promise(resolve => {
        getRequest("/system/config/menu?id="+user.id+"&role="+user.role).then(data => {
            if (data.code == 200) {
                let fmtRoutes = formatRoutes(data.extend.res); //格式化router
                router.addRoutes(fmtRoutes);  //添加到路由
                store.commit('initRoutes', fmtRoutes);  //将数据存到vuex
                if(sessionStorage.getItem('initRoutes') && sessionStorage.getItem('initRoutes_AllSameForm')){
                    sessionStorage.clear('initRoutes')
                    sessionStorage.clear('initRoutes_AllSameForm')
                }
                sessionStorage.setItem('initRoutes',JSON.stringify(fmtRoutes))
                sessionStorage.setItem('initRoutes_AllSameForm',data.extend.res_same)
                store.commit('initRoutesAllSameForm',data.extend.res_same)
                resolve(data.extend.res_same)
            }
        })
    })
}



export const initMenu_ex = (router, store) => {
    if (store.state.routes.length > 0) {
        return;
    }
    getRequest("/system/config/menuex").then(data => {
        if (data) {
            console.log("/system/config/menuex"+data);
            let fmtRoutes = formatRoutes(data); //格式化router
            router.addRoutes(fmtRoutes);  //添加到路由
            store.commit('initRoutes', fmtRoutes);  //将数据存到vuex
            console.log("fmtRoutes:"+fmtRoutes);
            //store.dispatch('connect');//mail好像没用
        }
    })
}
export const formatRoutes = (routes) => {
    let fmRoutes = [];
    routes.forEach(router => {
        let {
            path,
            component,
            name,
            meta,
            iconCls,
            keepalive,
            children,
            enabled
        } = router;
        if (children && children instanceof Array) {
            children = formatRoutes(children);
        }
        let fmRouter = {
            path: path,
            name: name,
            iconCls: iconCls,
            keepalive:keepalive,
            meta: meta,
            children: children,
            enabled:enabled,
            component(resolve) {
                if (component.startsWith("Home")) {
                    require(['../views/' + component + '.vue'], resolve);
                } else if (component.startsWith("Emp")) {
                    require(['../views/teacher/' + component + '.vue'], resolve);
                } else if (component.startsWith("Per")) {
                    require(['../views/admin/' + component + '.vue'], resolve);
                } else if (component.startsWith("Sal")) {
                    require(['../views/admin/' + component + '.vue'], resolve);
                } else if (component.startsWith("E")) {
                    require(['../views/expert/' + component + '.vue'], resolve);
                } else if (component.startsWith("Stu")) {
                    require(['../views/student/' + component + '.vue'], resolve);
                } else if (component.startsWith("Sys")) {
                    require(['../views/admin/' + component + '.vue'], resolve);
                }
            }
        }
        fmRoutes.push(fmRouter);
    })
    return fmRoutes;
}
