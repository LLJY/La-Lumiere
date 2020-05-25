package com.lucas.lalumire.models;
//this helps activity to determine where to proceed,e.g. if sign up success, go back to login.
public enum LoginActivityStatus {
    STATUS_LOGIN_SUCCESS,
    STATUS_SIGN_UP_SUCCESS,
    STATUS_INVALID_CREDENTIALS,
    STATUS_BAD_EMAIL,
    STATUS_LOADING,
    STATUS_ACCOUNT_EXISTS,
    STATUS_WEAK_PASSWORD,
    STATUS_INVALID_USER,
    STATUS_ERR
}
