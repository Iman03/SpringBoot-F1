package com.iman.bootthymeleaff1.repository;

import com.iman.bootthymeleaff1.entity.Tickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TicketsRepository extends JpaRepository<Tickets, Integer> {

    List<Tickets> findTicketsByDate(Date date);

    Tickets findTicketsBySeat(String seat);
}
