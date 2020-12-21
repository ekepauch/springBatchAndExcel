package com.techshard.batch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
//@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "Student")
public class StudentDTO {



    @Id
    @Column (name = "ID", nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column (name = "emailAddress", precision = 10, scale = 4, nullable = false)
    private String emailAddress;

    @NotNull
    @Column (name = "name", nullable = false)
    private String name;

    @NotNull
    @Column (name = "purchasedPackage", nullable = false)
    private String purchasedPackage;


    public StudentDTO() {
    }



    public StudentDTO(@NotNull String emailAddress, @NotNull String name) {
        this.emailAddress = emailAddress;
        this.name = name;
    }


}
