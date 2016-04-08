package ayry.com.ary_app;

import java.util.ArrayList;

/**
 * Created by Stephen Kearns on 05/03/2016.
 */
public class Shop_items{
    //This would contain the path to the local shop image
     public String img;
     public String title;
     public String desc;
     public int id;
     public String geoLocation;

    public Shop_items(int id, String img, String title, String desc){
        this.id = id;
        this.img = img;
        this.title = title;
        this.desc = desc;
    }

   /* public static ArrayList<Shop_items> getShopItems(){
        ArrayList<Shop_items> shops = new ArrayList<>();

        //populate the arraylist with shops
        shops.add(new Shop_items("R.drawable.android","Asian Food","We understand your taste and needs and so we maintain a great collection of quality exotic food and a variety of Asian food ingredients."));
        shops.add(new Shop_items("R.drawable.android"," Mediterranean Food Market","Stuffed with Asian, Turkish and European groceries, the Mediterranean Food Market on Thomas Street is a good shop to pick up fresh meat of halal standards as well as poultry"));
        shops.add(new Shop_items("R.drawable.android","Asian Food","We understand your taste and needs and so we maintain a great collection of quality exotic food and a variety of Asian food ingredients."));
        shops.add(new Shop_items("R.drawable.android"," Mediterranean Food Market","Stuffed with Asian, Turkish and European groceries, the Mediterranean Food Market on Thomas Street is a good shop to pick up fresh meat of halal standards as well as poultry"));
        shops.add(new Shop_items("R.drawable.android","Asian Food","We understand your taste and needs and so we maintain a great collection of quality exotic food and a variety of Asian food ingredients."));
        shops.add(new Shop_items("R.drawable.android"," Mediterranean Food Market","Stuffed with Asian, Turkish and European groceries, the Mediterranean Food Market on Thomas Street is a good shop to pick up fresh meat of halal standards as well as poultry"));
        shops.add(new Shop_items("R.drawable.android","Asian Food","We understand your taste and needs and so we maintain a great collection of quality exotic food and a variety of Asian food ingredients."));
        shops.add(new Shop_items("R.drawable.android"," Mediterranean Food Market","Stuffed with Asian, Turkish and European groceries, the Mediterranean Food Market on Thomas Street is a good shop to pick up fresh meat of halal standards as well as poultry"));

        return shops;
    } */


    public String getTitle(){
        return title;
    }

    public String getDesc(){
        return desc;
    }

    public String getImg(){
        return img;
    }
}
