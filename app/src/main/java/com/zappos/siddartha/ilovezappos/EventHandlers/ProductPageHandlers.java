package com.zappos.siddartha.ilovezappos.EventHandlers;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.zappos.siddartha.ilovezappos.R;

/**
 * Created by Siddartha on 2/10/2017.
 */


//not used
public class ProductPageHandlers {

    public void onClickSizeButton(View view){

        PopupMenu dropDownMenu = new PopupMenu(view.getContext(), (Button)view);
        dropDownMenu.getMenuInflater().inflate(R.menu.size_menu_dropdown, dropDownMenu.getMenu());
        ((Button) view).setText("Select Size");
        dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return true;
            }
        });
        dropDownMenu.show();
    }
}
