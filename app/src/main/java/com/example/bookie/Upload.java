package com.example.bookie;

public class Upload {

    public String name;
    public String type;
    public String url;

    public Upload(){

    }

    public Upload(String name, String type, String url){

        this.name = name;
        this.type = type;
        this.url = url;
    }

    public String getName(){

        return name;

    }

    public String getType(){
        return type;
    }

    public String getUrl(){

        return url;
    }
}
