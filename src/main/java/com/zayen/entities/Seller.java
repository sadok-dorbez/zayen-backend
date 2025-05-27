package com.zayen.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zayen.enumeration.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Seller extends User{

    private String phoneNumber;
    private int age;
    private String nationalIdentityCard;
    @JsonIgnore
    @OneToMany(mappedBy = "seller")
    private List<Item> items;


    public Seller(String firstname, String lastname, String email, String password,
                     Role role, String phoneNumber, int age, String nationalIdentityCard) {
        super(firstname, lastname, email, password, role);
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.nationalIdentityCard = nationalIdentityCard;

    }
}
