package com.mjj.wxdemoreminds.repository;

import com.mjj.wxdemoreminds.entity.Reminds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RemindsRepository extends JpaRepository<Reminds, Long> {

    List<Reminds> findRemindsByOpenIdOrderByIdDesc(@Param("openId") String openId);

    List<Reminds> findRemindsByValidTrue();

    void deleteById(@Param("id")Long id);

}
