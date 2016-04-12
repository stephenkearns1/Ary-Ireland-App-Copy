package ayry.com.ary_app;

/**
 * Created by stephen on 12/04/2016.
 */
public class EventsModel {
    private String title;
    private String place;
    private String address;
    private String phone;
    private String time;

    private int image;

    public EventsModel(String title, String place, String address, String phone, String time, int image) {
        this.title = title;
        this.place = place;
        this.address = address;
        this.phone = phone;
        this.time = time;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}