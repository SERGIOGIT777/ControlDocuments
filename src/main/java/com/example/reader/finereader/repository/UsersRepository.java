package com.example.reader.finereader.repository;

import com.example.reader.finereader.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    @Query(value = "select c from Users c where c.login = :login")
    List<Users> findByLogin(@Param(value = "login") String login);

    @Query(value = "select c from Users c where c.person = :person")
    List<Users> findByName(@Param(value = "person") String person);

    @Query(value = "select c from Users c where c.ip = :ip")
    List<Users> findByIp(@Param(value = "ip") String ip);

    @Query(value = "select c from Users c where c.id = :id")
    Users getId(@Param(value = "id") Long id);

    @Modifying
    @Query(value = "update Users c set c.password=:password where c.id=:id")
    void updatePassword(@Param(value = "id") long id,
                        @Param(value = "password") String password);

    @Modifying
    @Query(value = "update Users c set c.login=:login, c.password=:password, c.authority=:authority," +
            "c.person=:person, c.depart=:depart, c.ip=:ip where c.id=:id")
    void updateUsers(@Param(value = "id") long id,
                     @Param(value = "login") String login,
                     @Param(value = "password") String password,
                     @Param(value = "authority") String authority,
                     @Param(value = "person") String person,
                     @Param(value = "depart") String depart,
                     @Param(value = "ip") String ip);
}
