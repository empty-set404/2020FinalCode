package com.coder.commont;

import java.util.HashMap;
import java.util.Map;

public class Resp {

    private Boolean success;

    private Integer code;

    private String message;

    private Map<String, Object> data = new HashMap<String, Object>();

    private Resp(){}

    public static Resp ok(){
        Resp resp = new Resp();
        resp.setSuccess(true);
        resp.setCode(ResultCode.SUCCESS);
        resp.setMessage("成功");
        return resp;
    }

    public static Resp error(){
        Resp resp = new Resp();
        resp.setSuccess(false);
        resp.setCode(ResultCode.ERROR);
        resp.setMessage("失败");
        return resp;
    }

    public Resp success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public Resp message(String message){
        this.setMessage(message);
        return this;
    }

    public Resp code(Integer code){
        this.setCode(code);
        return this;
    }

    public Resp data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public Resp data(Map<String, Object> map){
        this.setData(map);
        return this;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}