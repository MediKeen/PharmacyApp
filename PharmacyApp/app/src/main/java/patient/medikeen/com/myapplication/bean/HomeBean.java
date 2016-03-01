package patient.medikeen.com.myapplication.bean;

/**
 * Created by Varun on 2/21/2016.
 */
public class HomeBean {

    private String orderNumber, orderType, name, address, phone, image;

    public HomeBean(String orderNumber, String orderType, String name, String address, String phone, String image) {
        this.orderNumber = orderNumber;
        this.orderType = orderType;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.image = image;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
