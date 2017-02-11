package com.zappos.siddartha.ilovezappos.Model;

import java.util.List;

/**
 * Created by Siddartha on 2/10/2017.
 */

public class Products {

    private List<Product> product = null;
    private String statusCode;

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

}
