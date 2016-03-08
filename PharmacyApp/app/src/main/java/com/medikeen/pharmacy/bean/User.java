package com.medikeen.pharmacy.bean;

/**
 * Created by Varun on 2/27/2016.
 */
public class User {

    private long pharmacyUserId;
    private String pharmacyUserFirstName;
    private String pharmacyUserLastName;
    private String pharmacyUserEmailAddress;
    private String pharmacyUserIsActive;
    private String pharmacyUserSessionId;

    private long pharmacyProfileId;
    private String pharmacyProfileName;
    private String pharmacyProfileEmailAddress;
    private String pharmacyProfileIsActive;
    private String pharmacyProfileAddress;
    private String pharmacyProfilePhone;

    public User(long pharmacyUserId, String pharmacyUserFirstName, String pharmacyUserLastName, String pharmacyUserEmailAddress, String pharmacyUserIsActive, String pharmacyUserSessionId, long pharmacyProfileId, String pharmacyProfileName, String pharmacyProfileEmailAddress, String pharmacyProfileIsActive, String pharmacyProfileAddress, String pharmacyProfilePhone) {
        this.pharmacyUserId = pharmacyUserId;
        this.pharmacyUserFirstName = pharmacyUserFirstName;
        this.pharmacyUserLastName = pharmacyUserLastName;
        this.pharmacyUserEmailAddress = pharmacyUserEmailAddress;
        this.pharmacyUserIsActive = pharmacyUserIsActive;
        this.pharmacyUserSessionId = pharmacyUserSessionId;
        this.pharmacyProfileId = pharmacyProfileId;
        this.pharmacyProfileName = pharmacyProfileName;
        this.pharmacyProfileEmailAddress = pharmacyProfileEmailAddress;
        this.pharmacyProfileIsActive = pharmacyProfileIsActive;
        this.pharmacyProfileAddress = pharmacyProfileAddress;
        this.pharmacyProfilePhone = pharmacyProfilePhone;
    }

    public long getPharmacyUserId() {
        return pharmacyUserId;
    }

    public void setPharmacyUserId(long pharmacyUserId) {
        this.pharmacyUserId = pharmacyUserId;
    }

    public String getPharmacyUserFirstName() {
        return pharmacyUserFirstName;
    }

    public void setPharmacyUserFirstName(String pharmacyUserFirstName) {
        this.pharmacyUserFirstName = pharmacyUserFirstName;
    }

    public String getPharmacyUserLastName() {
        return pharmacyUserLastName;
    }

    public void setPharmacyUserLastName(String pharmacyUserLastName) {
        this.pharmacyUserLastName = pharmacyUserLastName;
    }

    public String getPharmacyUserEmailAddress() {
        return pharmacyUserEmailAddress;
    }

    public void setPharmacyUserEmailAddress(String pharmacyUserEmailAddress) {
        this.pharmacyUserEmailAddress = pharmacyUserEmailAddress;
    }

    public String getPharmacyUserIsActive() {
        return pharmacyUserIsActive;
    }

    public void setPharmacyUserIsActive(String pharmacyUserIsActive) {
        this.pharmacyUserIsActive = pharmacyUserIsActive;
    }

    public String getPharmacyUserSessionId() {
        return pharmacyUserSessionId;
    }

    public void setPharmacyUserSessionId(String pharmacyUserSessionId) {
        this.pharmacyUserSessionId = pharmacyUserSessionId;
    }

    public long getPharmacyProfileId() {
        return pharmacyProfileId;
    }

    public void setPharmacyProfileId(long pharmacyProfileId) {
        this.pharmacyProfileId = pharmacyProfileId;
    }

    public String getPharmacyProfileName() {
        return pharmacyProfileName;
    }

    public void setPharmacyProfileName(String pharmacyProfileName) {
        this.pharmacyProfileName = pharmacyProfileName;
    }

    public String getPharmacyProfileEmailAddress() {
        return pharmacyProfileEmailAddress;
    }

    public void setPharmacyProfileEmailAddress(String pharmacyProfileEmailAddress) {
        this.pharmacyProfileEmailAddress = pharmacyProfileEmailAddress;
    }

    public String getPharmacyProfileIsActive() {
        return pharmacyProfileIsActive;
    }

    public void setPharmacyProfileIsActive(String pharmacyProfileIsActive) {
        this.pharmacyProfileIsActive = pharmacyProfileIsActive;
    }

    public String getPharmacyProfileAddress() {
        return pharmacyProfileAddress;
    }

    public void setPharmacyProfileAddress(String pharmacyProfileAddress) {
        this.pharmacyProfileAddress = pharmacyProfileAddress;
    }

    public String getPharmacyProfilePhone() {
        return pharmacyProfilePhone;
    }

    public void setPharmacyProfilePhone(String pharmacyProfilePhone) {
        this.pharmacyProfilePhone = pharmacyProfilePhone;
    }
}
