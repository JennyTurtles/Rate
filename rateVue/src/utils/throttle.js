export function throttle(fn, delay) {
    let timer = null;
    return function () {
        if(!timer) {
            let that = this
            timer = setTimeout(() => {
                fn.apply(that, arguments)
                timer = null;
            }, delay)
        }
    }
}