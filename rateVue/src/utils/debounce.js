export function debounce(fn,delay){
    let timer = null;
    return function (){
        let that = this
        if(timer) clearTimeout(timer)
        timer = setTimeout(()=>{
            fn.apply(that)
        },delay)
    }
};
