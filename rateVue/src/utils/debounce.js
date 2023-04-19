export function debounce(fn,delay){
    let timer = null;
    return function (){
        let that = this
        let args = arguments
        if(timer) clearTimeout(timer)
        timer = setTimeout(()=>{
            fn.apply(that,args)
        },delay)
    }
};
