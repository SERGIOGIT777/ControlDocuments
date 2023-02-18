package com.example.reader.finereader.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "people")
@Data
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Поле должно быть заполнено")
    @Size(min=2, message = "Поле имя не менее 2 знаков")
    @Column(name = "login")
    private String login;

    @NotEmpty(message = "Поле должно быть заполнено")
    @Size(min=2, message = "Пароль от 2 знаков")
    @Column(name = "password")
    private String password;

    @NotEmpty(message = "Поле должно быть заполнено")
    @Transient
    @Size(min=2, message = "Пароль от 2 знаков")
    @Column(name = "confirm_pass")
    private String confirm_password;

    @Column(name = "authority")
    private String authority;
    @NotEmpty(message = "Поле должно быть заполнено")
    @Size(max = 60, message = "Поле не более 60 символов")
    @Column(name = "person")
    private String person;

    @Column(name = "stars")
    private String stars;

    @Column(name = "depart")
    private String depart;

    @Column(name = "number")
    private String number;

    @NotEmpty(message = "Поле должно быть заполнено")
    @Pattern(regexp = "^([0-1]?\\d?\\d|2[0-4]\\d|25[0-5])(\\.([0-1]?\\d?\\d|2[0-4]\\d|25[0-5])){3}$")
    @Column(name = "ip")
    private String ip;
}
