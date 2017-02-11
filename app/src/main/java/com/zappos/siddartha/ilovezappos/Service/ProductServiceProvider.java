package com.zappos.siddartha.ilovezappos.Service;

import android.app.Activity;
import android.app.DownloadManager;

import com.zappos.siddartha.ilovezappos.Model.Product;
import com.zappos.siddartha.ilovezappos.Model.Products;
import com.zappos.siddartha.ilovezappos.Model.SearchResult;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Siddartha on 2/8/2017.
 */

public class ProductServiceProvider {

    public static final String API_KEY = "b743e26728e16b81da139182bb2094357c31d331";
    public static final String API_URL = "https://api.zappos.com/";

    private ServiceListener serviceListener;

    private Retrofit retrofit;

    private static volatile ProductServiceProvider instance = null;


    private ProductServiceProvider() {
        retrofit  = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ProductServiceProvider getInstance(){
        if(instance == null) {
            synchronized (ProductServiceProvider.class) {
                if(instance == null)
                    instance = new ProductServiceProvider();
            }
        }
        return instance;
    }

    public void setServiceListener(ServiceListener serviceListener) {
        this.serviceListener = serviceListener;
    }


    //provider for set of products
    public void searchProducts(String searchTerm){

        ProductsService productsService = retrofit.create(ProductsService.class);
        Call<SearchResult> productsSericeCall = productsService.getProducts(searchTerm, API_KEY);

        Request req =  productsSericeCall.request();

        productsSericeCall.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if(response.isSuccessful()){
                    serviceListener.onResultsSuccess(response);
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
            }
        });

    }


    //provider for product on id
    public void getProductByID(String productId){

        ProductsService productsService = retrofit.create(ProductsService.class);
        Call<Products> productsSericeCall = productsService.getProductById(productId, API_KEY);

        Request req =  productsSericeCall.request();
        productsSericeCall.enqueue(new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                if(response.isSuccessful()){
                    serviceListener.onProductsSuccess(response);
                }
            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {
            }
        });
    }

    public interface ServiceListener {
        public void onResultsSuccess(Response<SearchResult> response);
        public void onProductsSuccess(Response<Products> response);
        public void onFailure(String message);
    }
}
