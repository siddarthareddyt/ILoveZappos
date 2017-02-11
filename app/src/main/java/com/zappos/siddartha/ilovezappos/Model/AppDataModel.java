package com.zappos.siddartha.ilovezappos.Model;

import com.zappos.siddartha.ilovezappos.Service.ProductServiceProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Siddartha on 2/9/2017.
 *
 * Global Datamodel for the app instance
 */

public class AppDataModel {

    private static AppDataModel appDataModel = null;

    public static AppDataModel getAppDataModel(){
        if(appDataModel == null) {
            synchronized (AppDataModel.class) {
                if(appDataModel == null)
                    appDataModel = new AppDataModel();
            }
        }
        return appDataModel;
    }

    //initial data
    private List<Result> initialData = Collections.synchronizedList( new ArrayList<Result>());

    //current displayin product
    private Product currentProduct;

    public List<Result> getInitialData() {
        return initialData;
    }



    public void setInitialData(List<Result> initialData) {
        this.initialData = initialData;
    }

    public int getCartItems() {
        return cartItems;
    }

    public void setCartItems(int cartItems) {
        this.cartItems = cartItems;
    }

    public static void setAppDataModel(AppDataModel appDataModel) {
        AppDataModel.appDataModel = appDataModel;
    }

    private int cartItems;


    public boolean productsListExists(){
        return !initialData.isEmpty();
    }

    public Product getCurrentProduct() {
        return currentProduct;
    }

    public void setCurrentProduct(Product currentProduct) {
        this.currentProduct = currentProduct;
    }

    public void addOneToCart(){
        this.cartItems++;
    }

    public void removeOneCartItems(){
        this.cartItems--;
    }

}
