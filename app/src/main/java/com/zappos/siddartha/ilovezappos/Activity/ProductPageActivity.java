package com.zappos.siddartha.ilovezappos.Activity;

import android.animation.Animator;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zappos.siddartha.ilovezappos.Model.AppDataModel;
import com.zappos.siddartha.ilovezappos.Model.Product;
import com.zappos.siddartha.ilovezappos.Model.Products;
import com.zappos.siddartha.ilovezappos.Model.Result;
import com.zappos.siddartha.ilovezappos.Model.SearchResult;
import com.zappos.siddartha.ilovezappos.R;
import com.zappos.siddartha.ilovezappos.Service.ProductServiceProvider;
import com.zappos.siddartha.ilovezappos.databinding.ActivityProductPageBinding;

import java.util.List;

import retrofit2.Response;

public class ProductPageActivity extends AppCompatActivity {

    public static final String CART_STATE = "SAVE_STATE";
    public static final String API_URL = "https://api.zappos.com/";
    private static final String INTENT_DATA = "http://siddartha.com/ilovezappos/";

    private ProgressBar progressBar;
    private ImageView productImage;
    public static boolean isImageLoaded = false;
    private TextView productName;
    private ImageView shopCart;
    private TextView cartnNum;
    private ImageView share;
    private Result result = null;

    private Product product;
    ActivityProductPageBinding productBinding;
    FloatingActionButton fab;
    FloatingActionButton fabAdd;
    FloatingActionButton fabRemove;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    boolean isFabOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_page);

        Toolbar toolbar = (Toolbar)findViewById(R.id.productToolBar);
        setSupportActionBar(toolbar);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //custom action bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            final Drawable back = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
            back.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(back);
        }

        //set cart state
        cartnNum = (TextView)findViewById(R.id.product_count);
        if(savedInstanceState!=null){
            cartnNum.setText(String.valueOf(savedInstanceState.getInt(CART_STATE)));
        }
        else{
            cartnNum.setText(String.valueOf(AppDataModel.getAppDataModel().getCartItems()));
        }

        productName =(TextView)findViewById(R.id.product_name);
        shopCart = (ImageView)findViewById(R.id.shoppingCart);

        progressBar = (ProgressBar)findViewById(R.id.loadingId);
        productImage = (ImageView)findViewById(R.id.product_image);

        //fab icons for cart addition
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fabAdd = (FloatingActionButton)findViewById(R.id.fabAdd);
        fabRemove = (FloatingActionButton)findViewById(R.id.fabRemove);

        //fab animations
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);

        //custom fonts
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/CaviarDreams.ttf");
        productName.setTypeface(typeface);

        overridePendingTransition(R.anim.left_in, R.anim.left_out);

        result = getIntentParams();

        if(result != null){
            productBinding.setResult(result);
            setProductImage(productImage,result.getThumbnailImageUrl());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt(CART_STATE, AppDataModel.getAppDataModel().getCartItems());

        super.onSaveInstanceState(savedInstanceState);
    }

    //on first fab
    public void onFabCartClick(View view){
        animateCartFab();
    }

    //to add or remove
    public void onFabAddRemClick(View view){

        int cartNumber = AppDataModel.getAppDataModel().getCartItems();
        if(view.getTag().toString().equals("addToCart") )
        {
            cartNumber++;
            AppDataModel.getAppDataModel().addOneToCart();
        }

        if(view.getTag().toString().equals("removeFromCart") && cartNumber >0)
        {
            cartNumber--;
            AppDataModel.getAppDataModel().removeOneCartItems();
        }
        ViewPropertyAnimator anim = null;
        if(cartNumber>=0){
            anim = shopCart.animate().scaleX(1.2f).scaleY(1.2f).setDuration(1000);
            cartnNum.setAlpha(0);
            cartnNum.setTranslationY(-1000f);
            cartnNum.setText(String.valueOf(cartNumber));
            cartnNum.setAlpha(1);
            cartnNum.animate().translationYBy(1000f).setDuration(500);
        }
        if(anim!=null){
            anim.setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {

                    shopCart.animate().scaleX(.8f).scaleY(.8f).setDuration(1000);
                    //shopCart.an
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }


    }

    //get intent from either main activity or external acivity
    private Result getIntentParams() {

        Result productResultFromIntent = null;
        Intent intent = getIntent();

        if(intent.getData() != null)
        {
            lookForSharedProduct();
        }
        else
        {
            Bundle bundle = intent.getExtras();
            productResultFromIntent = bundle.getParcelable(MainActivity.EXTRA_PRODUCT);
        }

        return productResultFromIntent;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    public void onShare(View view){

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, INTENT_DATA+result.getProductId());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    //get the shared product for view
    private void lookForSharedProduct(){
        Uri productUri = getIntent().getData();
        Result resultFromOuterAct;

        if( productUri!= null && productUri.toString().contains(INTENT_DATA)){
            String productUriString = productUri.toString();
            final String productId = productUriString.substring(productUriString.lastIndexOf('/')+1);

            ProductServiceProvider.getInstance().setServiceListener(new ProductServiceProvider.ServiceListener() {
                @Override
                public void onResultsSuccess(Response<SearchResult> response) {
                    // TODO remove loading icon

                }

                @Override
                public void onProductsSuccess(Response<Products> response) {
                    List<Product> products = response.body().getProduct();
                    if (products != null && products.size() > 0) {
                        product = response.body().getProduct().get(0);
                        AppDataModel.getAppDataModel().setCurrentProduct(product);

                        productBinding.setProduct(product);
                        setProductImage(productImage, product.getDefaultImageUrl());
                        setProduct(product);
                    }
                }
                @Override
                public void onFailure(String message) {
                    //// TODO handle error screens here
                }
            });

            ProductServiceProvider.getInstance().getProductByID(productId);
        }
    }

    //on back button pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {

                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setProduct(Product product){
        this.product = product;
    }


    //binding for image loading
    @BindingAdapter({"android:src", "app:error"})
    public static void loadImage(ImageView view, String url, Drawable error) {
        Picasso.with(view.getContext())
                .load(url)
                .error(error)
                .into(view);
    }

    @BindingConversion
    public static int convertBooleanToVisibility(boolean visible) {
        return visible ? View.VISIBLE : View.GONE;
    }

    private void setProductImage(final ImageView view, String url){
        Target callBack = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                view.setImageBitmap(bitmap);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                progressBar.setVisibility(View.VISIBLE);
            }
        };

        if(url != null){
            Picasso.with(ProductPageActivity.this)
                    .load(url)
                    .into(callBack);
        }

    }

    //fab animations
    public void animateCartFab(){

        if(isFabOpen){

            fab.startAnimation(rotate_forward);
            fabAdd.startAnimation(fab_close);
            fabRemove.startAnimation(fab_close);
            fabAdd.setClickable(false);
            fabRemove.setClickable(false);
            isFabOpen = false;
            fab.startAnimation(rotate_backward);
        } else {

            fab.startAnimation(rotate_forward);
            fabAdd.startAnimation(fab_open);
            fabRemove.startAnimation(fab_open);
            fabAdd.setClickable(true);
            fabRemove.setClickable(true);
            isFabOpen = true;
            fab.startAnimation(rotate_backward);
        }
    }

}
