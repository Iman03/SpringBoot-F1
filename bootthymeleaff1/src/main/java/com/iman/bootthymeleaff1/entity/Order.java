package com.iman.bootthymeleaff1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tbl_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;
    private Integer total;
    private Date orderDate;

    @OneToMany(targetEntity = Tickets.class, cascade = CascadeType.ALL)
    // name => 對應的table要join on的欄位 / referencedColumnName => 要join on的欄位
    @JoinColumn(name = "orderId", referencedColumnName = "id")
    private List<Tickets> ticketsList;
}
