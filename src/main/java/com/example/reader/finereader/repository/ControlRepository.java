package com.example.reader.finereader.repository;

import com.example.reader.finereader.entities.Control;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ControlRepository extends JpaRepository<Control, Long> {

    @Query("select c from Control c where c.dates_plane<=:dates")
    List<Control> findByStatus(LocalDate dates);

    @Query("select c from Control c where c.id = :id")
    Control getId(@Param(value = "id") Long id);
}
