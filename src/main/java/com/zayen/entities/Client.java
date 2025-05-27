package com.zayen.entities;

import com.zayen.enumeration.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class Client extends User {
    private String phoneNumber;

    public Client(String firstname, String lastname, String email, String password,
                 Role role, String phoneNumber) {
        super(firstname, lastname, email, password, role);
        this.phoneNumber = phoneNumber;

    }

}
