package com.example.reader.finereader.repository;

import com.example.reader.finereader.entities.DeniedBlack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeniedBlackRepository extends JpaRepository<DeniedBlack, Long> {

    @Query(value = "select c from DeniedBlack c where c.ip=:ip")
    List<DeniedBlack> findByIP(@Param(value = "ip") String ip);
}
