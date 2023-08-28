export function checkPhone(val) {
    // 检验手机号码格式是否正确
    let reg = /^1[34578]\d{9}$/;
    return reg.test(val);
}

export function checkEmail(val) {
    // 检验邮箱格式是否正确
    let reg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
    return reg.test(val);
}

export function checkIdCard(val) {
    // 检验身份证号码格式是否正确
    let reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
    return reg.test(val);
}

// 校验年份：大于等于1900的整数
export function checkNumber(rule, value, callback) {
    if (value === '') {
        callback(new Error('请输入数字'));
    } else if (!Number.isInteger(Number(value))) {
        callback(new Error('请输入整数'));
    } else if (Number(value) <= 1900) {
        callback(new Error('请输入大于1900的整数'));
    } else {
        callback();
    }
}

export let validateInputPhone = (rule, value, callback) => {
    console.log(value)
    if (!checkPhone(value)) {
        callback(new Error("请输入正确的手机号"));
    } else {
        callback();
    }
};
export let validateInputEmail = (rule, value, callback) => {
    if (!checkEmail(value)) {
        callback(new Error("请输入正确的邮箱"));
    } else {
        callback();
    }
};
export let validateInputIdCard = (rule, value, callback) => {
    if (!checkIdCard(value)) {
        callback(new Error("请输入正确的身份证号码"));
    } else {
        callback();
    }
}




