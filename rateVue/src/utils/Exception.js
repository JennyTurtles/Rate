import Vue from 'vue'
import ExceptionDialog from '../components/ExceptionDialog'

const ExceptionBox = Vue.extend(ExceptionDialog)

ExceptionDialog.install = function (options) {
    if (options === undefined || options === null) {
        options = {
            content: ''
        }
    }else if (typeof options === 'string' || typeof options === 'number') {
        options = {
            content: options
        }
    }
    let instance = new ExceptionBox({
        data: options
    }).$mount()

    document.body.appendChild(instance.$el)

    Vue.nextTick(() => {
        instance.visible = true
    })
}

// 允许直接调用 ExceptionDialog
ExceptionDialog.call = function (options) {
    ExceptionDialog.install(options);
};

export default ExceptionDialog;
