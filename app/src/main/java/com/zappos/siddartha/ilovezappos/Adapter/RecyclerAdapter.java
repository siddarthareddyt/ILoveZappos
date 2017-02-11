package com.zappos.siddartha.ilovezappos.Adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zappos.siddartha.ilovezappos.Activity.MainActivity;
import com.zappos.siddartha.ilovezappos.Activity.ProductPageActivity;
import com.zappos.siddartha.ilovezappos.Model.Result;
import com.zappos.siddartha.ilovezappos.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Siddartha on 2/9/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ProductHolder> {

    private List<Result> products;
    private Context context;

    public RecyclerAdapter(Context context, List<Result> products) {
        this.context = context;
        this.products = products;
    }

    public static class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String PRODUCT_KEY = "PRODUCT";

        private ImageView product_thumb;
        private TextView product_name;
        private TextView product_desc;
        private TextView product_dis;
        private TextView product_price;
        private Result product;

        public ProductHolder(View view) {
            super(view);
            product_thumb = (ImageView) view.findViewById(R.id.product_thumb);
            product_name = (TextView) view.findViewById(R.id.product_name);
            product_desc = (TextView) view.findViewById(R.id.product_desc);
            product_dis = (TextView)view.findViewById(R.id.product_dis);
            product_price = (TextView)view.findViewById(R.id.product_price);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Context context = itemView.getContext();
            Intent productPageIntent = new Intent(context, ProductPageActivity.class);
            productPageIntent.putExtra(MainActivity.EXTRA_PRODUCT, product);

            View sharedView = product_thumb;
            String transitionName = context.getResources().getString(R.string.shared_tranName);
            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation((MainActivity)context, sharedView, transitionName);
            context.startActivity(productPageIntent, transitionActivityOptions.toBundle());
        }


        public void bindProduct(Result product) {
            this.product = product;
            Picasso.with(product_thumb.getContext()).load(product.getThumbnailImageUrl()).into(product_thumb);
            product_name.setText(product.getProductName());
            product_desc.setText(product.getOriginalPrice());
            product_dis.setText(product.getPercentOff());

        }
    }

    @Override
    public RecyclerAdapter.ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new ProductHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ProductHolder holder, int position) {
        Result productItem = products.get(position);
        holder.bindProduct(productItem);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
