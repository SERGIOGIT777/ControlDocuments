package com.example.reader.finereader.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "denied_black")
@Data
@NoArgsConstructor
public class DeniedBlack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dates")
    private String dates;

    @Column(name = "ip")
    private String ip;
}
