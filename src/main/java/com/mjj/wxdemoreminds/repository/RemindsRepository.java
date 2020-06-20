package com.mjj.wxdemoreminds.repository;

import com.mjj.wxdemoreminds.entity.Reminds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RemindsRepository extends JpaRepository<Reminds, Long> {
}
