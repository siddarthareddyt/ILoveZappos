package com.zappos.siddartha.ilovezappos.Service;

import com.zappos.siddartha.ilovezappos.Model.Products;
import com.zappos.siddartha.ilovezappos.Model.Result;
import com.zappos.siddartha.ilovezappos.Model.SearchResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Siddartha on 2/8/2017.
 */

public interface ProductsService {

    //to get products on terms
    @GET("Search")
    Call<SearchResult> getProducts(@Query("term") String term, @Query("key") String key);


    //to get single product based on id
    @GET("Product/{productId}")
    Call<Products> getProductById(@Path("productId") String productId, @Query("key") String key);

}
