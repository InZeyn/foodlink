package com.example.inzeyn.foodlink.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inzeyn.foodlink.Interfaces.ILoadMenuItem;
import com.example.inzeyn.foodlink.Models.MenuItem;
import com.example.inzeyn.foodlink.R;

import java.util.List;

class LoadingViewHolder extends RecyclerView.ViewHolder{

    public ProgressBar progressBar;

    public LoadingViewHolder(View menuItemView){
        super(menuItemView);
        progressBar = menuItemView.findViewById(R.id.menuProgressBar);
    }

}

class MenuItemViewHolder extends RecyclerView.ViewHolder {

    public TextView title, price;
    public Button makeToast;
    public MenuItemViewHolder(View menuItemView) {
        super(menuItemView);
        makeToast = menuItemView.findViewById(R.id.menuFinalBtn);
        title = menuItemView.findViewById(R.id.itemTitle);
        price = menuItemView.findViewById(R.id.itemPrice);
    }
}
public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM=0,VIEW_TYPE_LOADING=1;
    ILoadMenuItem loadMenuItem;
    boolean isLoading;
    Activity activity;
    List<MenuItem> menuItems;
    int visibleThreshold=5;
    int lastVisibleMenuItem, totalMenuItemCount;

    public MenuAdapter(RecyclerView recyclerView,Activity activity, List<MenuItem> menuItems) {
        this.activity = activity;
        this.menuItems = menuItems;

        final LinearLayoutManager linearLayoutManager =(LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalMenuItemCount = linearLayoutManager.getItemCount();
                lastVisibleMenuItem = linearLayoutManager.findLastVisibleItemPosition();
                if(!isLoading && totalMenuItemCount <= (lastVisibleMenuItem+visibleThreshold)) {
                    if(loadMenuItem != null) {
                        loadMenuItem.onLoadMenuITem();
                        isLoading = true;

                    }
                }

            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return menuItems.get(position) == null ? VIEW_TYPE_LOADING:VIEW_TYPE_ITEM;
    }

    public void setLoadMenuItem(ILoadMenuItem loadMenuItem) {
        this.loadMenuItem = loadMenuItem;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity)
                    .inflate(R.layout.menu_item_layout,parent,false);
            return new MenuItemViewHolder(view);
        }else if(viewType ==VIEW_TYPE_LOADING){
            View view = LayoutInflater.from(activity)
                    .inflate(R.layout.menu_item_loading,parent,false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof MenuItemViewHolder ) {
            MenuItem menuItem = menuItems.get(position);
            MenuItemViewHolder viewHolder = (MenuItemViewHolder) holder;
            viewHolder.title.setText(menuItems.get(position).getName());
            viewHolder.price.setText("$"+(String.valueOf(menuItems.get(position).getPrice())));
            viewHolder.makeToast.setText("+");
            viewHolder.makeToast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Added" , Toast.LENGTH_SHORT).show();
                }
            });
        }else if(holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

}

