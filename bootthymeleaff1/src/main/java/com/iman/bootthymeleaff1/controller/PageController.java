package com.iman.bootthymeleaff1.controller;

import com.iman.bootthymeleaff1.entity.Order;
import com.iman.bootthymeleaff1.entity.OrderDetails;
import com.iman.bootthymeleaff1.entity.User;
import com.iman.bootthymeleaff1.repository.OrderRepository;
import com.iman.bootthymeleaff1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class PageController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;

    // get home page
    @GetMapping({"/","/home"})
    public ModelAndView getHomePage(){
        ModelAndView mav = new ModelAndView("home");
        return mav;
    }


    // get login page
    @GetMapping({"/login"})
    public ModelAndView getLoginPage(){
        ModelAndView mav = new ModelAndView("login");
        User user = new User();
        mav.addObject("user", user);
        return mav;
    }

    @GetMapping({"/login/{id}"})
    public ModelAndView getMemberPage(@PathVariable Integer id){
        ModelAndView mav = new ModelAndView("member");
        User user = new User();
        if (id != null){
            if (userRepository.findById(id).isPresent()){
                user = userRepository.findById(id).get();
            }
        }
        mav.addObject("user", user);
        return mav;
    }

    // get signUp page
    @GetMapping("/signUp")
    public ModelAndView signUp(){
        ModelAndView mav = new ModelAndView("signUp");
        User newUser = new User();
        mav.addObject("user", newUser);
        return mav;
    }

    // get member page and orders
    @GetMapping("/signIn/{id}")
    public ModelAndView member(@PathVariable Integer id){
        ModelAndView mav = new ModelAndView("member");
        User user = userRepository.findById(id).get();
        mav.addObject("userInfo", user);

        List<OrderDetails> orderDetailsList = orderRepository.findAllDetailsByUserId(id);
        mav.addObject("orderDetails", orderDetailsList);
        return mav;
    }
}
