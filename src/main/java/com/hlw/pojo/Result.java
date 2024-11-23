package com.hlw.pojo;

import lombok.*;

/**
 * 统一响应结果封装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    //    public Result() {
    //    }
    //    public Result(Integer code, String msg, Object data) {
    //        this.code = code;
    //        this.msg = msg;
    //        this.data = data;
    //    }
    private Integer code ;//1 成功 , 0 失败
    private String msg; //提示信息
    private Object data; //数据 date

    public static Result success(Object data){
        return new Result(1, "success", data);
    }
    public static Result success(Object data,String msg){
        return new Result(1, msg, data);
    }
    public static Result success(){
        return new Result(1, "success", null);
    }
    public static Result error(String msg){
        return new Result(0, msg, null);
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
