package ayry.com.ary_app;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Stephen Kearns on 05/03/2016.
 */


public class ShoplistAdapter extends RecyclerView.Adapter<ShoplistAdapter.ViewHolder>{

    private ArrayList<Shop_items> myShopItems;



    //attach onClickListener for added functionality
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView shopImage;
        public TextView shopTitle;
        public TextView shopDesc;

        public ViewHolder(View itemView) {
            super(itemView);

            shopImage = (ImageView) itemView.findViewById(R.id.shop_img);
            shopTitle = (TextView) itemView.findViewById(R.id.shop_title);
            shopDesc = (TextView) itemView.findViewById(R.id.shop_description);

        }

    }


    public void clear() {
        if(myShopItems != null && !myShopItems.isEmpty())
            myShopItems.clear();
        notifyDataSetChanged();
    }


    //Constructor to retrive data from shop_items
    public ShoplistAdapter(ArrayList<Shop_items> shopItmes){
        myShopItems = shopItmes;
    }


    public void addAll(ArrayList<Shop_items> shopItems){
        myShopItems.addAll(shopItems);
        notifyDataSetChanged();

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  {

        // Context context = parent.getContext();
        //  LayoutInflater inflater = LayoutInflater.from(context);


        //inflate the custom shop list row
        //View shopListView = inflater.inflate(R.layout.shoplist_row, parent,false);
        View shopListView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoplist_row, parent, false);


        //LinearLayoutManager is used here, this will layout the elements in a similair fashion

        //Retrun a new holder instance
        ViewHolder viewHolder = new ViewHolder(shopListView);
        return viewHolder;



    }


    @Override
    public void onBindViewHolder(ShoplistAdapter.ViewHolder viewHolder,  int position) {
        //retrive the data model based on the position
        Shop_items shop = myShopItems.get(position);

        //int drawableId = getgetResources.getIdentifier(drawablename, "drawable", getPackageName());
        //set the item views with content from the data model
        ImageView shopImg = viewHolder.shopImage;
        TextView shopTitleTV = viewHolder.shopTitle;
        TextView shopDescTV = viewHolder.shopDesc;

        //set the views with the data
        shopImg.setImageResource(R.drawable.android);
        shopTitleTV.setText(shop.getTitle());
        shopDescTV.setText(shop.getDesc());
    }

    @Override
    public int getItemCount() {

        return myShopItems.size();

    }


}



/* Working list using arrayAdapter


public class ShoplistAdapter extends ArrayAdapter<Shop_items> {
    private static String TAG = "checkVariable";
    public ShoplistAdapter(Context context, ArrayList<Shop_items> shops){
        super(context,0,shops);
    }

    public View getView(int postion, View conView, ViewGroup parent){
         //retrive the data from the array of shops
        Shop_items shop = getItem(postion);

        Log.i(TAG, "Shop" + shop.getTitle() + shop.getDesc());


        //check if an existing view is being used if not infalte if not

        conView = LayoutInflater.from(getContext()).inflate(R.layout.shoplist_row, parent, false);

        //assign views to populate data with to holding variables
        TextView shopTitle = (TextView) conView.findViewById(R.id.shop_title);
        TextView shopDesc = (TextView) conView.findViewById(R.id.shop_description);

        //Set the views with the shop data
        shopTitle.setText(shop.getTitle());
        shopDesc.setText(shop.getDesc());

        return conView;
    }
}

*/