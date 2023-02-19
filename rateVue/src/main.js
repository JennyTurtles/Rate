import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Vuex from "vuex";
import * as XLSX from 'xlsx/xlsx.mjs'
import FileSaver from 'file-saver'
import {
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
    BreadcrumbItem,
    Dropdown,
    Steps,
    Step,
    Tooltip,
    Popover,
    Collapse,
    FormItem,
    Checkbox,
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
    Switch, Popconfirm
} from 'element-ui';

Vue.directive('focus', {
    inserted(el, binding, vnode) {
        // 聚焦元素
        el.querySelector('input').focus()
    }
})

Vue.prototype.$ELEMENT = { size: 'small', zIndex: 3000 };
Vue.use(Switch);
Vue.use(Message);
Vue.use(CollapseItem);
Vue.use(Radio);
Vue.use(RadioGroup);
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


// //路由跳转之前
router.beforeEach((to, from, next) => {
    if (to.path == '/Admin/Login' || to.path == '/Expert/Login' || to.path == '/' ||
        to.path == '/Student/Login' || to.path == '/Teacher/Login' || to.path == '/Student') {
        if(localStorage.getItem('user') || localStorage.getItem('initRoutes') || localStorage.getItem('initRoutes_AllSameForm')){
            store.commit('initRoutes',[])
            store.commit('INIT_CURRENTHR',{})
            localStorage.clear()
        }
        next()
    }
    //除登录外的其他路径
    else {
        if (localStorage.getItem('user')) {
            if(JSON.parse(localStorage.getItem('user')).role == "3"){
                next()
                return
            }
            var routs = localStorage.getItem('initRoutes_AllSameForm')
            if(routs.indexOf(to.path) == -1){
                Message.warning('无权限！请重新登录')
                next('/')
                return
            }
            next()
        } else {
            next('/?redirect=' + to.path)
        }
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
let vue = new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
export default vue
