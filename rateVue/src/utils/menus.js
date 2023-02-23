import {getRequest} from "./api";
import this_ from '../main'
import router from '../router'
import store from '../store/index'
import {log} from "@/utils/sockjs";

export const initMenu = new Promise((resolve, reject) => {
    if (store.state.routes.length > 0) {
        reject()
    }
    let user = JSON.parse(localStorage.getItem('user'))
    getRequest("/system/config/menu?id="+user.id+"&role="+user.role).then(data => {
        if (data) {
            let fmtRoutes = formatRoutes(data); //格式化router
            router.addRoutes(fmtRoutes);  //添加到路由
            store.commit('initRoutes', fmtRoutes);  //将数据存到vuex
            if(sessionStorage.getItem('initRoutes')){
                sessionStorage.clear('initRoutes')
            }
            sessionStorage.setItem('initRoutes',JSON.stringify(fmtRoutes))

            var data = JSON.parse(JSON.stringify(fmtRoutes))
            let res = ['/home']
            data.forEach(item=>{
                if(item.children.length){
                    item.children.forEach(item1=>{
                        if(!item1.children){
                            res.push(
                                item1.path
                            );
                        }else{
                            item1.children.forEach(item2=>{
                                if(!item2.children){
                                    res.push(
                                        item2.path
                                    );
                                }
                            })
                        }
                    })
                }
            })
            sessionStorage.setItem('initRoutes_AllSameForm',res)
            store.commit('initRoutesAllSameForm',res)
            resolve(data)
        }
    })
})
            // this_.$router.replace('/home').then(
            //     resolve =>{}
            // ).catch(
            //     err => {
            //         console.log(err)
            //     });



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
