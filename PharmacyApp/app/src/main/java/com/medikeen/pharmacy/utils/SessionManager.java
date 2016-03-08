package com.medikeen.pharmacy.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.medikeen.pharmacy.bean.User;

public class SessionManager {

    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    private String TAG = "shareoffer";
    // Context
    Context _context;

    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "LOGGED";

    // All Shared Preferences Keys
    public static final String IS_AUTH = "IsAuth";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String PHARMACY_USER_ID = "address";
    public static final String PHARMACY_USER_FIRST_NAME = "first_name";
    public static final String PHARMACY_USER_LAST_NAME = "last_name";
    public static final String PHARMACY_USER_EMAIL_ADDRESS = "email_address";
    public static final String PHARMACY_USER_IS_ACTIVE = "is_active";
    public static final String PHARMACY_USER_SESSION_ID = "authentication_session_id";

    public static final String PHARMACY_PROFILE_ID = "pharmacy_profile_id";
    public static final String PHARMACY_PROFILE_NAME = "pharmacy_name";
    public static final String PHARMACY_PROFILE_EMAIL_ADDRESS = "pharmacy_email_address";
    public static final String PHARMACY_PROFILE_ADDRESS = "pharmacy_address";
    public static final String PHARMACY_PROFILE_PHONE_NUMBER = "pharmacy_phone_number";
    public static final String PHARMACY_PROFILE_IS_ACTIVE = "is_active";


    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(boolean isLogin, long pharmacyUserId, String pharmacyUserFirstName, String pharmacyUserLastName, String pharmacyUserEmailAddress, String pharmacyUserIsActive, String pharmacyUserSessionId,
                                   long pharmacyProfileId, String pharmacyProfileName, String pharmacyProfileEmailAddress, String pharmacyProfileIsActive, String pharmacyProfileAddress, String pharmacyProfilePhone) {

        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putLong(PHARMACY_USER_ID, pharmacyUserId);
        editor.putString(PHARMACY_USER_FIRST_NAME, pharmacyUserFirstName);
        editor.putString(PHARMACY_USER_LAST_NAME, pharmacyUserLastName);
        editor.putString(PHARMACY_USER_EMAIL_ADDRESS, pharmacyUserEmailAddress);
        editor.putString(PHARMACY_USER_IS_ACTIVE, pharmacyUserIsActive);
        editor.putString(PHARMACY_USER_SESSION_ID, pharmacyUserSessionId);

        editor.putLong(PHARMACY_PROFILE_ID, pharmacyProfileId);
        editor.putString(PHARMACY_PROFILE_NAME, pharmacyProfileName);
        editor.putString(PHARMACY_PROFILE_ADDRESS, pharmacyProfileAddress);
        editor.putString(PHARMACY_PROFILE_EMAIL_ADDRESS, pharmacyProfileEmailAddress);
        editor.putString(PHARMACY_PROFILE_IS_ACTIVE, pharmacyProfileIsActive);
        editor.putString(PHARMACY_PROFILE_PHONE_NUMBER, pharmacyProfilePhone);

        // commit changes
        editor.commit();
    }


    public void checkLogin() {
        // Check login status
        Log.d("this.isLoggedIn()", "isLoggedIn()" + this.isLoggedIn());

        // if(this.isLoggedIn()){
        // user is not logged in redirect him to Login Activity
        //Intent i = new Intent(_context, CategoryListings.class);

        // Closing all the Activities

        //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        //_context.startActivity(i);
        //}
        //else{
        //   Intent i = new Intent(_context, Login.class);
        // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // _context.startActivity(i);
        //}
    }


    public User getUserDetails() {

        long pharmacyUserId = pref.getLong(PHARMACY_USER_ID, -1);
        String pharmacyUserFirstName = pref.getString(PHARMACY_USER_FIRST_NAME, "");
        String pharmacyUserLastName = pref.getString(PHARMACY_USER_LAST_NAME, "");
        String pharmacyUserEmailAddress = pref.getString(PHARMACY_USER_EMAIL_ADDRESS, "");
        String pharmacyUserIsActive = pref.getString(PHARMACY_USER_IS_ACTIVE, "");
        String pharmacyUserSessionId = pref.getString(PHARMACY_USER_SESSION_ID, "");

        long pharmacyProfileId = pref.getLong(PHARMACY_PROFILE_ID, -1);
        String pharmacyProfileName = pref.getString(PHARMACY_PROFILE_NAME, "");
        String pharmacyProfileEmailAddress = pref.getString(PHARMACY_PROFILE_EMAIL_ADDRESS, "");
        String pharmacyProfileIsActive = pref.getString(PHARMACY_PROFILE_IS_ACTIVE, "");
        String pharmacyProfileAddress = pref.getString(PHARMACY_PROFILE_ADDRESS, "");
        String pharmacyProfilePhone = pref.getString(PHARMACY_PROFILE_PHONE_NUMBER, "");

        User user = new User(pharmacyUserId, pharmacyUserFirstName, pharmacyUserLastName, pharmacyUserEmailAddress, pharmacyUserIsActive, pharmacyUserSessionId, pharmacyProfileId, pharmacyProfileName, pharmacyProfileEmailAddress, pharmacyProfileIsActive, pharmacyProfileAddress, pharmacyProfilePhone);

        return user;
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        /*        Intent i = new Intent(_context,LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
		 */
    }

    public boolean isLoggedIn() {
        Log.d(TAG, "" + pref.getBoolean(IS_LOGIN, false));
        return pref.getBoolean(IS_LOGIN, false);
    }


    public void setAuthUSer(boolean authUser) {

        Log.d(TAG, "Sess authe user" + authUser);

        editor.putBoolean(IS_AUTH, authUser);

        editor.commit();
    }

    public boolean isAuthUser() {

        return pref.getBoolean(IS_AUTH, false);

    }


}
