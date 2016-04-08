package ayry.com.ary_app;

import java.util.ArrayList;

/**
 * Created by Stephen Kearns on 29/03/2016.
 */
public class ObjectRequestHolder {

    static User userApp;
    static Shop_items shop;

    static ArrayList<Shop_items> shopList;
    public ObjectRequestHolder(){
        shopList = new ArrayList<>();

    }

    public void setUserApp(User userApp) {
        this.userApp = userApp;
    }

    public void setShop(Shop_items shop) {
        this.shop = shop;
    }


    public void addShop(Shop_items shop){
         shopList.add(shop);
    }

    public ArrayList<Shop_items> getShopList(){
        return shopList;
    }


    public User getUserApp() {
        return userApp;
    }

    public Shop_items getShop() {
        return shop;
    }
}
