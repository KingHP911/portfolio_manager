package ru.kinghp.portfolio_manager.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class DBUser {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @NotBlank
    private String login;
    @NotBlank
    private String password;
    private Boolean isAdmin = false;

    @Transient

    private String passwordConfirm;

}
