package com.socialinfotech.socialchat.domain.chat;

/**
 * Created by pankaj on 05/04/16.
 */
public class conversation {

    String table_name;
    String user_name;
    String last_msg;
    String status;
    int read_number;


    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getLast_msg() {
        return last_msg;
    }

    public void setLast_msg(String last_msg) {
        this.last_msg = last_msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRead_number() {
        return read_number;
    }

    public void setRead_number(int read_number) {
        this.read_number = read_number;
    }
}
