package com.example.reader.finereader.repository;

import com.example.reader.finereader.entities.Control;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ControlRepository extends JpaRepository<Control, Long> {

    @Query("select c from Control c where c.data_plane<=:dates and c.data_fact is null")
    List<Control> findByStatus(LocalDate dates);

    @Query("select c from Control c where c.data_plane<=:dates and c.data_fact is null")
    List<Control> findByStatusError(LocalDate dates);

    @Query("select c from Control c where c.reg_number=:reg_number")
    List<Control> findByNumber(String reg_number);

    @Query("select c from Control c where c.id = :id")
    Control getId(@Param(value = "id") Long id);

    @Modifying
    @Query(value = "update Control c set c.assignment=:assignment, c.correspondent=:correspondent, " +
            "c.reg_number=:reg_number, c.dates=:dates, c.author_resolution=:author_resolution, " +
            "c.resolution=:resolution, c.data_plane=:data_plane, c.executor=:executor, c.data_fact=:data_fact," +
            "c.reg_answer=:reg_answer, c.answer=:answer where c.id=:id")
    void updateDocuments(@Param(value = "id") long id,
                     @Param(value = "assignment") String assignment,
                     @Param(value = "correspondent") String correspondent,
                     @Param(value = "reg_number") String reg_number,
                     @Param(value = "dates") LocalDate dates,
                     @Param(value = "author_resolution") String author_resolution,
                     @Param(value = "resolution") String resolution,
                     @Param(value = "data_plane") LocalDate data_plane,
                     @Param(value = "executor") String executor,
                     @Param(value = "data_fact") LocalDate data_fact,
                     @Param(value = "reg_answer") String reg_answer,
                     @Param(value = "answer") String answer);

    @Modifying
    @Query(value = "update Control c set c.data_fact=:data_fact, c.reg_answer=:reg_answer, " +
            "c.answer=:answer where c.id=:id")
    void executeDocuments(@Param(value = "id") long id,
                         @Param(value = "data_fact") LocalDate data_fact,
                         @Param(value = "reg_answer") String reg_answer,
                         @Param(value = "answer") String answer);
}
