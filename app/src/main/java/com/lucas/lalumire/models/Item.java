package com.lucas.lalumire.models;

import android.net.Uri;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Item {
    public String ListingID;
    public String Title;
    public String sellerName;
    public String sellerUID;
    public Uri sellerImageURL;
    public int Likes;
    public LocalDateTime ListedTime;
    public float Price;
    public float Rating;
    public String Description;
    public String TransactionInformation;
    public String ProcurementInformation;
    public String Category;
    public int Stock;
    public ArrayList<String> images;
    public boolean isAdvert;
    public boolean isLiked;

    public String getListingID() {
        return ListingID;
    }

    public void setListingID(String listingID) {
        ListingID = listingID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerUID() {
        return sellerUID;
    }

    public void setSellerUID(String sellerUID) {
        this.sellerUID = sellerUID;
    }

    public Uri getSellerImageURL() {
        return sellerImageURL;
    }

    public void setSellerImageURL(Uri sellerImageURL) {
        this.sellerImageURL = sellerImageURL;
    }

    public int getLikes() {
        return Likes;
    }

    public void setLikes(int likes) {
        Likes = likes;
    }

    public LocalDateTime getListedTime() {
        return ListedTime;
    }

    public void setListedTime(LocalDateTime listedTime) {
        ListedTime = listedTime;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public float getRating() {
        return Rating;
    }

    public void setRating(float rating) {
        Rating = rating;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTransactionInformation() {
        return TransactionInformation;
    }

    public void setTransactionInformation(String transactionInformation) {
        TransactionInformation = transactionInformation;
    }

    public String getProcurementInformation() {
        return ProcurementInformation;
    }

    public void setProcurementInformation(String procurementInformation) {
        ProcurementInformation = procurementInformation;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int stock) {
        Stock = stock;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public boolean isAdvert() {
        return isAdvert;
    }

    public void setAdvert(boolean advert) {
        isAdvert = advert;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public Item(String listingID, String title, String sellerName, String sellerUID, Uri sellerImageURL, int likes, LocalDateTime listedTime, float price, float rating, String description, String transactionInformation, String procurementInformation, String category, int stock, ArrayList<String> images, boolean isAdvert, boolean isLiked) {
        ListingID = listingID;
        Title = title;
        this.sellerName = sellerName;
        this.sellerUID = sellerUID;
        this.sellerImageURL = sellerImageURL;
        Likes = likes;
        ListedTime = listedTime;
        Price = price;
        Rating = rating;
        Description = description;
        TransactionInformation = transactionInformation;
        ProcurementInformation = procurementInformation;
        Category = category;
        Stock = stock;
        this.images = images;
        this.isAdvert = isAdvert;
        this.isLiked = isLiked;

    }
}