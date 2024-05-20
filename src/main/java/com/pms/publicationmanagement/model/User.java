package com.pms.publicationmanagement.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="institution_id")
    private Institution institution;

    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String password;
    private UserType userType;

    public User(Integer id, String firstName, String middleName, String lastName, String email, String password, UserType userType) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.id = id;
        this.userType = userType;
    }

    public User() {
    }
    public String getName() { return String.format("%s %s %s", firstName, middleName, lastName); }


}
