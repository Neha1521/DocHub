package com.example.bookie;

public class User {

    public String name;
    public int status;

    public User(){

    }

    public User(String name, int status){

        this.name = name;
        this.status = status;
    }

    public String getName(){
        return name;
    }

    public int getStatus(){
        return status;
    }


}
