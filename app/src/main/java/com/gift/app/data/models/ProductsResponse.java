package com.gift.app.data.models;

import com.squareup.moshi.Json;

import java.util.List;

public class ProductsResponse {


    @Json(name = "status")
    private Boolean status;
    @Json(name = "msg")
    private String msg;
    @Json(name = "data")
    private List<Product> data;



    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }
}
