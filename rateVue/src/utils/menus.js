import {getRequest} from "./api";

export const initMenu = (router, store) => {
    if (store.state.routes.length > 0) {
        return;
    }
    // console.log(store.state.currentHr);
    let user = JSON.parse(localStorage.getItem('user'))
    getRequest("/system/config/menu?id="+user.id+"&role="+user.role).then(data => {
        if (data) {
            let fmtRoutes = formatRoutes(data); //格式化router
            router.addRoutes(fmtRoutes);  //添加到路由
            localStorage.setItem('initRoutes',JSON.stringify(fmtRoutes))
            // localStorage.setItem('initRoutes_allmenus',JSON.stringify(res))
            store.commit('initRoutes', fmtRoutes);  //将数据存到vuex

        }
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
