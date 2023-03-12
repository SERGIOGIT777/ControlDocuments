package com.example.reader.finereader.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "control")
@Data
@NoArgsConstructor
public class Control {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Поле должно быть заполнено")
    @Column(name = "assignment")
    private String assignment;

    @NotEmpty(message = "Поле должно быть заполнено")
    @Size(min=2, max = 255, message = "Размер поля корреспондент не менее 2 знаков и не более 255")
    @Column(name = "correspondent")
    private String correspondent;

    @NotEmpty(message = "Поле должно быть заполнено")
    @Size(min=2, max = 100, message = "Размер поля регистрационный номер не менее 2 знаков и не более 100")
    @Column(name = "reg_number")
    private String reg_number;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Дата не может быть пустой")
    @Column(name = "dates")
    private LocalDate dates;

    @NotEmpty(message = "Поле должно быть заполнено")
    @Size(min=2, max = 100, message = "Размер поля автор резолюции не менее 2 знаков и не более 100")
    @Column(name = "author_resolution")
    private String author_resolution;

    @Size(min=2, max = 255, message = "Размер поля резолюция не менее 2 знаков и не более 255")
    @Column(name = "resolution")
    private String resolution;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_plane")
    private LocalDate data_plane;

    @Size(min=2, max = 100, message = "Размер поля исполнитель не менее 2 знаков и не более 100")
    @Column(name = "executor")
    private String executor;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_fact")
    private LocalDate data_fact;

    @Size(min=2, max = 100, message = "Размер поля регистрационный номер ответа не менее 2 знаков и не более 100")
    @Column(name = "reg_answer")
    private String reg_answer;

    @Column(name = "answer")
    private String answer;
}
