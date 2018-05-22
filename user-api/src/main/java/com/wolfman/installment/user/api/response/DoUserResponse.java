package com.wolfman.installment.user.api.response;

import java.io.Serializable;

/**
 * @ClassName DoUserResponse
 * @Description TODU
 * @Author huhao
 * @Date Create in 2018/5/22 19:48
 * @Version 1.0
 */
public class DoUserResponse implements Serializable {

    private static final long serialVersionUID = -8435748807383450528L;

    private String code;

    private String message;

    private String data;

    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "DoUserResponse{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
