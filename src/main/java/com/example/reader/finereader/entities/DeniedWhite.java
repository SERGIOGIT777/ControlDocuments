package com.example.reader.finereader.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "denied_white")
@Data
@NoArgsConstructor
public class DeniedWhite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotEmpty(message = "Поле должно быть заполнено")
    @Pattern(regexp = "^([0-1]?\\d?\\d|2[0-4]\\d|25[0-5])(\\.([0-1]?\\d?\\d|2[0-4]\\d|25[0-5])){3}$")
    @Column(name = "ip")
    private String ip;

    @NotEmpty(message = "Поле должно быть заполнено")
    @Size(max = 60, message = "Поле не более 60 символов")
    @Column(name = "person")
    private String person;

}
