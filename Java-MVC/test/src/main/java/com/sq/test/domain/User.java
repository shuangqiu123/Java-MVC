package com.sq.test.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class User {
    @Min(value = 0)
    private int id;

    @NotNull(message = "username cannot be null")
    private String username;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
