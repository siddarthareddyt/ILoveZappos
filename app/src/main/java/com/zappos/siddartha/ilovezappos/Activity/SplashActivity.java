package com.zappos.siddartha.ilovezappos.Activity;

import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.zappos.siddartha.ilovezappos.Model.AppDataModel;
import com.zappos.siddartha.ilovezappos.Model.Products;
import com.zappos.siddartha.ilovezappos.Model.SearchResult;
import com.zappos.siddartha.ilovezappos.R;
import com.zappos.siddartha.ilovezappos.Service.ProductServiceProvider;

import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    public final static String EXTRA_INITIAL_PRODUCTS = "com.zappos.siddartha.ilovezappos.INITIAL_PRODUCTS";

    private Animation splashAnimation;
    private ImageView splashImage;
    private ImageView loveIconImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashImage = (ImageView)findViewById(R.id.splashLogoImage);
        loveIconImage = (ImageView)findViewById(R.id.loveImageId);

        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        if (savedInstanceState == null) {
            splashAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_image_animation);
            splashImage.startAnimation(splashAnimation);

            splashAnimation = AnimationUtils.loadAnimation(this, R.anim.love_icon_animation);
            loveIconImage.startAnimation(splashAnimation);
        }


        loadInitialProducts();


    }


    //get initial products for display
    private void loadInitialProducts(){
        final Intent intent = new Intent(this, MainActivity.class);

        ProductServiceProvider.getInstance().setServiceListener(new ProductServiceProvider.ServiceListener() {
            @Override
            public void onResultsSuccess(Response<SearchResult> response) {
                if(response.body().getResults() != null){
                    AppDataModel.getAppDataModel().setInitialData(response.body().getResults());
                    intent.putExtra(EXTRA_INITIAL_PRODUCTS, true);
                }

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        startActivity(intent);
                        finish();
                    }
                }, 1000);
            }

            @Override
            public void onProductsSuccess(Response<Products> response) {

            }

            @Override
            public void onFailure(String message) {
                intent.putExtra(EXTRA_INITIAL_PRODUCTS, false);
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        startActivity(intent);
                        finish();
                    }
                }, 1000);
            }
        });

        ProductServiceProvider.getInstance().searchProducts("");
    }
}
