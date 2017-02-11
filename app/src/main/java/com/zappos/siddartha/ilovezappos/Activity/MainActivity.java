package com.zappos.siddartha.ilovezappos.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zappos.siddartha.ilovezappos.Adapter.RecyclerAdapter;
import com.zappos.siddartha.ilovezappos.Model.AppDataModel;
import com.zappos.siddartha.ilovezappos.Model.Products;
import com.zappos.siddartha.ilovezappos.Model.Result;
import com.zappos.siddartha.ilovezappos.Model.SearchResult;
import com.zappos.siddartha.ilovezappos.R;
import com.zappos.siddartha.ilovezappos.Service.ProductServiceProvider;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_PRODUCT = "com.zappos.siddartha.ilovezappos.PRODUCT";

    public final static String CART_STATE = "CART_ITEMS";


    private EditText searchText;
    private String searchQuery = null;

    private RecyclerView recyclerView;
    private RecyclerAdapter productsRecyclerAdapter;

    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;

    private List<Result> productList;

    private int cartNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //custom toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.zapToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        searchText = (EditText)findViewById(R.id.searchQuery);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(linearLayoutManager);

        if(savedInstanceState != null){
            //saved cart number
            cartNumber = savedInstanceState.getInt(CART_STATE);
        }
        else {
            cartNumber =AppDataModel.getAppDataModel().getCartItems();
        }

        TextView cart_count = (TextView)findViewById(R.id.cart_count);
        cart_count.setText(String.valueOf(cartNumber));

        //hide keyboard on startup
        hideSoftKeyboard();

        overridePendingTransition(R.anim.right_in, R.anim.right_out);

        boolean status = getIntent().getBooleanExtra(SplashActivity.EXTRA_INITIAL_PRODUCTS, false);

        //setup initial data
        productList = AppDataModel.getAppDataModel().getInitialData();
        productsRecyclerAdapter = new RecyclerAdapter(this, productList);
        recyclerView.setAdapter(productsRecyclerAdapter);
        setRecyclerViewScrollListener();
        setRecyclerViewItemTouchListener();

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt(CART_STATE, AppDataModel.getAppDataModel().getCartItems());

        super.onSaveInstanceState(savedInstanceState);
    }


    //on search query fired
    public void onSearch(View view){
        searchQuery = searchText.getText().toString();
        final Intent intent = new Intent(this, ProductPageActivity.class);
        //get single instance of the service provider
        ProductServiceProvider.getInstance().setServiceListener(new ProductServiceProvider.ServiceListener() {
            @Override
            public void onResultsSuccess(Response<SearchResult> response) {
                // TODO remove loading icon
                List<Result> results = response.body().getResults();
                if(results != null && results.size() > 0){
                    intent.putExtra(EXTRA_PRODUCT, response.body().getResults().get(0));
                    startActivity(intent);
                }
            }

            @Override
            public void onProductsSuccess(Response<Products> response) {

            }

            @Override
            public void onFailure(String message) {

            }
        });

        ProductServiceProvider.getInstance().searchProducts(searchQuery);
    }

    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void setRecyclerViewScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
            }
        });
    }

    //recycler touch listerner
    private void setRecyclerViewItemTouchListener() {
        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {

                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                int position = viewHolder.getAdapterPosition();
                productList.remove(position);
                recyclerView.getAdapter().notifyItemRemoved(position);
            }
        };


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
