package com.ef.model;



public class Result {

    String ipValue;
    String comment;

    public Result(String ipValue, String comment) {
        this.ipValue = ipValue;
        this.comment = comment;
    }

    public String getIpValue() {
        return ipValue;
    }

    public void setIpValue(String ipValue) {
        this.ipValue = ipValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
