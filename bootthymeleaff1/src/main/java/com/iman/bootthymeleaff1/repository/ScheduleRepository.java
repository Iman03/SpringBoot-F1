package com.iman.bootthymeleaff1.repository;

import com.iman.bootthymeleaff1.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findAllByDateBefore(Date date);
    List<Schedule> findAllByDateAfter(Date date);
}
