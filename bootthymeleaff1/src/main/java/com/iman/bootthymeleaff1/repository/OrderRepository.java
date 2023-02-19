package com.iman.bootthymeleaff1.repository;

import com.iman.bootthymeleaff1.entity.Order;
import com.iman.bootthymeleaff1.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByUserId(Integer id);

    // new package.....(需與所創建出來的class的欄位對應，不可缺少欄位)
    @Query("select new com.iman.bootthymeleaff1.entity.OrderDetails(o.id ,o.total, o.userId, o.orderDate, t.date, t.price, t.seat, t.memo) from Order o join o.ticketsList t where o.userId = :id")
    List<OrderDetails> findAllDetailsByUserId(Integer id);
}
