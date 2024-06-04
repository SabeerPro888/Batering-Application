package com.example.barteringapp7;

import java.io.Serializable;
import java.util.List;

public class ViewRequestsInformation implements Serializable {
    public String Receiver_name;
    public String Sender_name ;

    public String RequestedItemName;

    public int Offer_id;
    String RequestInformation;

    Double Rating;

    String ConfirmOfferRequestReceiver;
    String ConfirmOfferRequestSeder;

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }

    String ProfilePic;

    public String getConfirmOfferRequestReceiver() {
        return ConfirmOfferRequestReceiver;
    }

    public void setConfirmOfferRequestReceiver(String confirmOfferRequestReceiver) {
        ConfirmOfferRequestReceiver = confirmOfferRequestReceiver;
    }

    public String getConfirmOfferRequestSeder() {
        return ConfirmOfferRequestSeder;
    }

    public void setConfirmOfferRequestSeder(String confirmOfferRequestSeder) {
        ConfirmOfferRequestSeder = confirmOfferRequestSeder;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return ReceiverId;
    }

    public void setReceiverId(int receiverId) {
        ReceiverId = receiverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int senderId;
    public int ReceiverId;

    public Double getRating() {
        return Rating;
    }

    public void setRating(Double rating) {
        Rating = rating;
    }

    public String getstatus() {
        return status;
    }

    public void setstatus(String offerStatus) {
        this.status = offerStatus;
    }

    String status;

    public String getRequestInformation() {
        return RequestInformation;
    }

    public void setRequestInformation(String requestInformation) {
        RequestInformation = requestInformation;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public int Price;

    public String getReceiver_name() {
        return Receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        Receiver_name = receiver_name;
    }

    public String getSender_name() {
        return Sender_name;
    }

    public void setSender_name(String sender_name) {
        Sender_name = sender_name;
    }

    public String getRequestedItemName() {
        return RequestedItemName;
    }

    public void setRequestedItemName(String requestedItemName) {
        RequestedItemName = requestedItemName;
    }

    public int getOffer_id() {
        return Offer_id;
    }

    public void setOffer_id(int offer_id) {
        Offer_id = offer_id;
    }

    public List<Integer> getOfferedItemsIds() {
        return OfferedItemsIds;
    }

    public void setOfferedItemsIds(List<Integer> offeredItemsIds) {
        OfferedItemsIds = offeredItemsIds;
    }

    public List<Integer> OfferedItemsIds;
}
