package com.example.reader.finereader.repository;

import com.example.reader.finereader.entities.DeniedWhite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeniedWhiteRepository extends JpaRepository<DeniedWhite, Long> {

    @Query(value = "select c from DeniedWhite c where c.ip=:ip")
    List<DeniedWhite> findByIP(@Param(value = "ip") String ip);

    @Query(value = "select c from DeniedWhite c where c.id = :id")
    DeniedWhite getId(@Param(value = "id") Long id);

    @Modifying
    @Query(value = "update DeniedWhite c set c.ip=:ip, c.person=:person where c.id=:id")
    void updateDenied(@Param(value = "id") long id,
                      @Param(value = "ip") String ip,
                      @Param(value = "person") String person);
}
