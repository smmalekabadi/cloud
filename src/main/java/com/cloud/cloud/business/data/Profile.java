package com.cloud.cloud.business.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "profile",schema = "cloud")
@Getter
@Setter
//@AllArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true)
    @NonNull
    private String email;
    private String phoneNo;
    private String nationalCode;
    private String address;
    private String postalCode;
    @Transient
    private String password;

    public Profile() {
    }

    public Profile(String email, String phoneNo, String nationalCode, String address, String postalCode, String password) {
        this.email = email;
        this.phoneNo = phoneNo;
        this.nationalCode = nationalCode;
        this.address = address;
        this.postalCode = postalCode;
        this.password = password;
    }
}
