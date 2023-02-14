package org.sys.rate.config;

public class JsonResult<T> {
    private String code; //返回的状态码
    private String msg; //返回的信息
    private int total; //返回数据的条数
    private int number; //计算得到的数据
    private T data; //数据

    /**
     * 若没有数据返回 默认状态码为200 提示信息为操作成功 数量为0
     */
    public JsonResult(){
        this.code="200";
        this.msg="操作成功";
        this.total=0;
    }

    /**
     * 若没有数据返回 可以人为指定提示信息和状态码 数量为0
     * @param code 状态码
     * @param msg 提示信息
     */
    public JsonResult(String code, String msg){
        this.code=code;
        this.msg=msg;
        this.total=0;
    }

    /**
     * 若没数据返回 可以人为指定提示信息和状态码 数量为0
     * @param code 状态码
     * @param msg 提示信息
     */
    public JsonResult(T data, String code, String msg){
        this.code=code;
        this.msg=msg;
        this.total=0;
        this.data=data;
    }

    /**
     * 若有数据返回时 状态码为200 提示信息为 操作成功 数量为返回值个数和得到计算数据的条数
     * @param data
     * @param total
     */
    public JsonResult(T data, int total, int number){
        this.code="200";
        this.msg="操作成功";
        this.total=total;
        this.number=number;
        this.data=data;
    }

    /**
     * 若有数据返回时 状态码为200 提示信息为 操作成功 数量为返回值个数
     * @param data
     * @param total
     */
    public JsonResult(T data, int total){
        this.code="200";
        this.msg="操作成功";
        this.total=total;
        this.data=data;
    }

    /**
     * 有数据返回 数据个数为人为设置为1 即返回一条数据
     * @param data
     */
    public JsonResult(T data){
        this.code="200";
        this.msg="操作成功";
        this.total=1;
        this.data=data;
    }

    /**
     * 无数据返回 但操作成功 返回的是操作成功的数量
     * @param total
     */
    public JsonResult(int total){
        this.code="200";
        this.msg="操作成功";
        this.total=total;
    }

    /**
     * 无数据返回 状态码和返回信息由自己定义，返回操作数据成功的条数和得到计数数据的条数
     * @param msg
     * @param number
     */
    public JsonResult(String msg, int number){
        this.code="200";
        this.msg=msg;
        this.number=number;
    }

    /**
     * 有数据返回 状态码为200 人为提示信息 数量为数据个数值
     * @param data
     * @param total
     * @param msg
     */
    public JsonResult(T data, int total, String msg){
        this.code="200";
        this.msg=msg;
        this.total=total;
        this.data=data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
