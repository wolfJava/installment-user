package com.wolfman.installment.user.api.request;

import java.io.Serializable;

/**
 * @ClassName DoUserRequest
 * @Description TODU
 * @Author huhao
 * @Date Create in 2018/5/22 19:39
 * @Version 1.0
 */
public class DoUserRequest implements Serializable {

    private static final long serialVersionUID = 2138125312896875544L;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "DoUserRequest{" +
                "id=" + id +
                '}';
    }

}
