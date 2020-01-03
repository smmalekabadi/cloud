package com.cloud.cloud.business.data;

import lombok.Data;

@Data
public class ProfileSimple {
    public ProfileSimple(String email, String password) {
        this.email = email;
        this.password = password;
    }

    private String email;
    private String password;

    @Override
    public String toString() {
        return "{" +
                "\"email=\"\"" + email + '\'' +
                ", \"password=\"\"" + password + '\"' +
                "}";
    }
}
