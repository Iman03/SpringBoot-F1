package com.iman.bootthymeleaff1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDetails {
    //  欄位名稱可自己取
    private Integer orderId;
    private Integer orderTotal;
    private Integer orderUserId;
    private Date orderDate;
    private Date ticketsDate;
    private Integer ticketsPrice;
    private String ticketsSeat;
    private String ticketsMemo;
}
