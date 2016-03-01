package patient.medikeen.com.myapplication.bean;

/**
 * Created by Varun on 2/27/2016.
 */
public class PrescriptionBean {

    private String resource_id, resource_type, person_id, recepient_name, recepient_address, recepient_number, offer_type, is_image_uploaded, is_valid, is_email_sent, created_date, updated_date, pharmacy_profile_id, order_status, cost, rejection_code, rejection_details;

    public PrescriptionBean(String resource_id, String resource_type, String person_id, String recepient_name, String recepient_address, String recepient_number, String offer_type, String is_image_uploaded, String is_valid, String is_email_sent, String created_date, String updated_date, String pharmacy_profile_id, String order_status, String cost, String rejection_code, String rejection_details) {
        this.resource_id = resource_id;
        this.resource_type = resource_type;
        this.person_id = person_id;
        this.recepient_name = recepient_name;
        this.recepient_address = recepient_address;
        this.recepient_number = recepient_number;
        this.offer_type = offer_type;
        this.is_image_uploaded = is_image_uploaded;
        this.is_valid = is_valid;
        this.is_email_sent = is_email_sent;
        this.created_date = created_date;
        this.updated_date = updated_date;
        this.pharmacy_profile_id = pharmacy_profile_id;
        this.order_status = order_status;
        this.cost = cost;
        this.rejection_code = rejection_code;
        this.rejection_details = rejection_details;
    }

    public String getResource_id() {
        return resource_id;
    }

    public void setResource_id(String resource_id) {
        this.resource_id = resource_id;
    }

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getRecepient_name() {
        return recepient_name;
    }

    public void setRecepient_name(String recepient_name) {
        this.recepient_name = recepient_name;
    }

    public String getRecepient_address() {
        return recepient_address;
    }

    public void setRecepient_address(String recepient_address) {
        this.recepient_address = recepient_address;
    }

    public String getRecepient_number() {
        return recepient_number;
    }

    public void setRecepient_number(String recepient_number) {
        this.recepient_number = recepient_number;
    }

    public String getOffer_type() {
        return offer_type;
    }

    public void setOffer_type(String offer_type) {
        this.offer_type = offer_type;
    }

    public String getIs_image_uploaded() {
        return is_image_uploaded;
    }

    public void setIs_image_uploaded(String is_image_uploaded) {
        this.is_image_uploaded = is_image_uploaded;
    }

    public String getIs_valid() {
        return is_valid;
    }

    public void setIs_valid(String is_valid) {
        this.is_valid = is_valid;
    }

    public String getIs_email_sent() {
        return is_email_sent;
    }

    public void setIs_email_sent(String is_email_sent) {
        this.is_email_sent = is_email_sent;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String getPharmacy_profile_id() {
        return pharmacy_profile_id;
    }

    public void setPharmacy_profile_id(String pharmacy_profile_id) {
        this.pharmacy_profile_id = pharmacy_profile_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getRejection_code() {
        return rejection_code;
    }

    public void setRejection_code(String rejection_code) {
        this.rejection_code = rejection_code;
    }

    public String getRejection_details() {
        return rejection_details;
    }

    public void setRejection_details(String rejection_details) {
        this.rejection_details = rejection_details;
    }
}
