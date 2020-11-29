package com.example.athenaeum;

import com.google.type.LatLng;

import java.util.ArrayList;

public class Request {
    //    private ArrayList<User> requestSenders = new ArrayList<User>();
    private Book RequestedBook;
    private User Owner;
    private User Requester;
    private LatLng location;

    public Request(){}

    public Request(User requester, Book book){
        this.Requester = requester;
        this.RequestedBook = book;
        boolean duplicate = false;

//        for(int j=0; j < book.getRequesters().size(); j++){
//            if (book.getRequesters().get(j).getProfile().getEmail().equals(requester.getProfile().getEmail())){
//                duplicate = true;
//            }
//        }
//
//        if( duplicate){
//            return;
//        }
//        else {
//            this.RequestedBook.addRequesters(requester);
////            System.out.println("Added to requesters list i think");
//        }
    }

    public User getRequester(){
        return this.Requester;
    }

    public Book getRequestedBook(){
        return this.RequestedBook;
    }


}
