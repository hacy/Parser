package com.ef.model;



import java.util.Date;


public class LogData {
    Date logDate;
    String ipValue;
    Integer status;
    String uAgent;
    String request;

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getIpValue() {
        return ipValue;
    }

    public void setIpValue(String ipValue) {
        this.ipValue = ipValue;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getuAgent() {
        return uAgent;
    }

    public void setuAgent(String uAgent) {
        this.uAgent = uAgent;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public LogData(){

    }
}
