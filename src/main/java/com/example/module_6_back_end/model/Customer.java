package com.example.module_6_back_end.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends Person {
    private String company;
    private Boolean isDisabled;

    @Override
    public String toString() {
        return "Customer{" +
                "company='" + company + '\'' +
                "} " + super.toString();
    }
}
