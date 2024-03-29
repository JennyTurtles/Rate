import Vue from 'vue'
import App from './App.vue'
import router from './router'
import VueRouter from 'vue-router'
import store from './store'
import Vuex from "vuex";
import * as XLSX from 'xlsx/xlsx.mjs'
import FileSaver from 'file-saver'
import './utils/date.scss';
import VueOfficeDocx from "@vue-office/docx";
import VueOfficePdf from "@vue-office/pdf";
import '@vue-office/docx/lib/index.css'

import {
    Image,
    Transfer,
    Button,
    Input,
    InputNumber,
    Table,
    TableColumn,
    Dialog,
    Card,
    Container,
    Icon,
    Select,
    Form,
    Tag,
    Tree,
    Pagination,
    Badge,
    Loading,
    Message,
    MessageBox,
    Menu,
    Tabs,
    TabPane,
    Breadcrumb,
    Skeleton,
    SkeletonItem,
    BreadcrumbItem,
    Dropdown,
    Steps,
    Step,
    Tooltip,
    Popover,
    Collapse,
    FormItem,
    Checkbox,
    CheckboxGroup,
    Header,
    DropdownMenu,
    DropdownItem,
    Aside,
    Main,
    MenuItem,
    Submenu,
    Option,
    Col,
    Row,
    Upload,
    Radio,
    DatePicker,
    RadioGroup,
    CollapseItem,
    Switch, Popconfirm,Divider,Autocomplete
} from 'element-ui';
Dialog.props.closeOnClickModal.default = false // 点击空白处不关闭弹窗
Vue.directive('focus', {
    inserted(el, binding, vnode) {
        // 聚焦元素
        el.querySelector('input').focus()
    }
})

Vue.prototype.$ELEMENT = { size: 'small', zIndex: 3000 };
Vue.use(Autocomplete)
Vue.use(Divider)
Vue.use(Transfer)
Vue.use(Switch);
Vue.component(Message);
Vue.use(CollapseItem);
Vue.use(Image);
Vue.use(Radio);
Vue.use(Skeleton);
Vue.use(SkeletonItem);
Vue.use(RadioGroup);
Vue.use(CheckboxGroup);
Vue.use(DatePicker);
Vue.use(Upload);
Vue.use(Row);
Vue.use(Col);
Vue.use(Option);
Vue.use(Submenu);
Vue.use(MenuItem);
Vue.use(Header);
Vue.use(DropdownMenu);
Vue.use(DropdownItem);
Vue.use(Aside);
Vue.use(Main);
Vue.use(Checkbox);
Vue.use(FormItem);
Vue.use(Collapse);
Vue.use(Popover);
Vue.use(Menu);
Vue.use(Tabs);
Vue.use(TabPane);
Vue.use(Breadcrumb);
Vue.use(BreadcrumbItem);
Vue.use(Dropdown);
Vue.use(Steps);
Vue.use(Step);
Vue.use(Tooltip);
Vue.use(Tree);
Vue.use(Pagination);
Vue.use(Badge);
Vue.use(Loading);
Vue.use(Button);
Vue.use(Input);
Vue.use(InputNumber);
Vue.use(Table);
Vue.use(TableColumn);
Vue.use(Dialog);
Vue.use(Card);
Vue.use(Container);
Vue.use(Icon);
Vue.use(Select);
Vue.use(Form);
Vue.use(Tag);
Vue.use(Vuex)
Vue.use(Popconfirm)
Vue.use(VueRouter)
Vue.use(VueOfficeDocx)
Vue.use(VueOfficePdf);
// Vue.use(XLSX)
// Vue.use(FileSaver)
Vue.prototype.$message = Message
Vue.prototype.$alert = MessageBox.alert
Vue.prototype.$confirm = MessageBox.confirm
Vue.prototype.$prompt = MessageBox.prompt
// Vue.prototype.$FileSaver = FileSaver;
// Vue.prototype.$XLSX = XLSX;

import qs from 'qs'
import { postRequest1 } from "./utils/api";
import { postRequest } from "./utils/api";
// import {postRequestLogin} from "./utils/api";
import { postKeyValueRequest } from "./utils/api";
import { putRequest } from "./utils/api";
import { deleteRequest } from "./utils/api";
import { getRequest } from "./utils/api";
import { initMenu } from "./utils/menus";
import { initMenu_ex } from "./utils/menus";
import 'font-awesome/css/font-awesome.min.css'
import Directives from './directives/index.js'
import ro from "element-ui/src/locale/lang/ro";
import axios from "axios";
// import {time} from "html2canvas/dist/types/css/types/time";
Vue.use(Directives)
Vue.prototype.postRequest = postRequest;
Vue.prototype.postRequest1 = postRequest1;
// Vue.prototype.postRequestLogin = postRequestLogin;
Vue.prototype.postKeyValueRequest = postKeyValueRequest;
Vue.prototype.putRequest = putRequest;
Vue.prototype.deleteRequest = deleteRequest;
Vue.prototype.getRequest = getRequest;
Vue.prototype.$qs = qs;

Vue.config.productionTip = false
Vue.prototype.$confirm = MessageBox.confirm

const originalPush = VueRouter.prototype.push
const originalReplace = VueRouter.prototype.replace
//重写push和replace方法，传递回调函数，警告消失
// push
VueRouter.prototype.push = function push(location, onResolve, onReject) {
    if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
    return originalPush.call(this, location).catch(err => err)
}
// replace
VueRouter.prototype.replace = function push(location, onResolve, onReject) {
    if (onResolve || onReject) return originalReplace.call(this, location, onResolve, onReject)
    return originalReplace.call(this, location).catch(err => err)
}
// //路由跳转之前
// var timer
router.beforeEach((to, from, next) => {
    // console.log(to.path)
    if (to.path == '/Admin/Login' || to.path == '/Expert/Login' || to.path == '/' ||
        to.path == '/Student/Login' || to.path == '/Teacher/Login' || to.path == '/Student') {
        if (localStorage.getItem('user') || sessionStorage.getItem('initRoutes') || sessionStorage.getItem('initRoutes_AllSameForm')) {
            store.commit('initRoutes', [])
            store.commit('initRoutesAllSameForm', [])
            store.commit('INIT_CURRENTHR', {})
            localStorage.clear('initRoutes')
            sessionStorage.clear('initRoutes_AllSameForm')
        }
        next()
    } else if (to.path == '/Student/Register' || to.path == '/Teacher/Register' || to.path == '/ResetPassword') {
        next()
    }
    else if (to.path == '/teacher/tperact/InformationDetails') {
        next()
    }
    //除登录外的其他路径
    else {
        if (localStorage.getItem('user')) {
            if ((from.path == ' /Admin/Login' || from.path == '/Expert/Login' || from.path == '/' ||
                from.path == '/Student/Login' || from.path == '/Teacher/Login' || from.path == '/Student') && to.path == '/home') {
                next()
                return;
            }
            if (to.path == '/admin/PersonalCenter' || to.path == '/teacher/PersonalCenter' || to.path == '/student/PersonalCenter' || to.path == '/superAdmin/PersonalCenter') {
                next()
                return;
            }
            if (JSON.parse(localStorage.getItem('user')).role == "3") {
                next()
                return
            }
            if (JSON.parse(localStorage.getItem('user')).role.indexOf("3") >= 0) {
                next()
                return
            }
            if (to.path == '/pending/message') { //待办消息菜单
                next()
                return
            }
            initMenu(router, store).then((data) => {
                if (data.indexOf(to.path) == -1) {
                    next('/')
                    return
                } else {
                    next()
                }
            })
            if (sessionStorage.getItem('initRoutes_AllSameForm').indexOf(to.path) >= 0) {
                next()
            } else {
                Message.warning('无权限！请重新登录')
                next('/')
                return
            }
        }
        else if (localStorage.getItem('teacher')) {
            // next('/?redirect=' + to.path)
            next()
        } else {
            next('/')
        }
    }
})
Vue.filter('fileNameFilter', function (data) {
    if (data == null || data == '') {
        return '无证明材料'
    } else {
        var arr = data.split('/')
        return arr.reverse()[0].split('#$%')[2]
    }
})
// 注册一个全局过滤器(将得到的数据ms转为时间(年月日))
Vue.filter('dataFormat', function (originVal) {
    const dt = new Date(originVal);
    // 年
    const year = dt.getFullYear();
    // 转换出来的month默认是从0开始的,设置显示为两位数，不足的就用0来填充
    // 月
    const month = (dt.getMonth() + 1 + '').padStart(2, '0');
    // 日
    const day = (dt.getDate() + '').padStart(2, '0');
    // 时
    const hour = (dt.getHours() + '').padStart(2, '0');
    // 分
    const minutes = (dt.getMinutes() + '').padStart(2, '0');
    // 秒
    const seconds = (dt.getSeconds() + '').padStart(2, '0');
    // 拼接时间数据
    return `${year}-${month}-${day} ${hour}:${minutes}:${seconds}`;
});
Vue.prototype.dateFormatFunc = function (originVal){
    const dt = new Date(originVal);
    // 年
    const year = dt.getFullYear();
    // 转换出来的month默认是从0开始的,设置显示为两位数，不足的就用0来填充
    // 月
    const month = (dt.getMonth() + 1 + '').padStart(2, '0');
    // 日
    const day = (dt.getDate() + '').padStart(2, '0');
    // 时
    const hour = (dt.getHours() + '').padStart(2, '0');
    // 分
    const minutes = (dt.getMinutes() + '').padStart(2, '0');
    // 秒
    const seconds = (dt.getSeconds() + '').padStart(2, '0');
    // 拼接时间数据
    return `${year}-${month}-${day} ${hour}:${minutes}:${seconds}`;
}
Vue.prototype.initTutor = function (user){
    if(!user.teacherName) {
        this.getRequest('/student/basic/getTutorInfo?id=' + user.id).then(response => {
            if(response.obj) {
                user.teacherName =  response.obj;
            }
        })
    }
}
Vue.prototype.previewFileMethod = function (data){ //预览证明材料
    return axios({
        url: '/achievements/basic/downloadByUrl',
        method: 'post',
        responseType: 'blob',
        data: qs.stringify({url: data.url}),
        headers: {
            'token': JSON.parse(localStorage.getItem('user')).token ? JSON.parse(localStorage.getItem('user')).token : ''
        }
    }).then(response => {
        let url = window.URL.createObjectURL(new Blob([response]));
        return url;
    });
};
Vue.prototype.downloadFileMethod = function (data){ //预览证明材料
    var fileName = data.url.split('/').reverse()[0]
    axios({
        url: '/achievements/basic/downloadByUrl',
        method: 'post',
        responseType: 'blob',
        data: qs.stringify({url: data.url}),
        headers: {
            'token': JSON.parse(localStorage.getItem('user')).token ? JSON.parse(localStorage.getItem('user')).token : ''
        }
    }).then(response => {
        let url = window.URL.createObjectURL(new Blob([response]));
        const link = document.createElement('a');
        if (url.startsWith('http:')) {
            url = url.replace('http:', 'https:');
        }
        link.href = url;
        link.setAttribute('download', fileName);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    });
};

let vue = new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
export default vue
