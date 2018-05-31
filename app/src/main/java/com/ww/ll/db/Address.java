package com.ww.ll.db;

import org.litepal.crud.DataSupport;

/**
 *
 * @author Ww
 * @date 2018/5/27 0027
 */

public class Address extends DataSupport {
    private String address;

    private int id;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
