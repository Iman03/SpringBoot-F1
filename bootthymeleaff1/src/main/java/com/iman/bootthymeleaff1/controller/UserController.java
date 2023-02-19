package com.iman.bootthymeleaff1.controller;

import com.iman.bootthymeleaff1.entity.OrderDetails;
import com.iman.bootthymeleaff1.entity.User;
import com.iman.bootthymeleaff1.repository.OrderRepository;
import com.iman.bootthymeleaff1.repository.UserRepository;
import com.iman.bootthymeleaff1.token.JwtToken;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;

    // login page, press Login button
    @PostMapping("/signIn")
    public ModelAndView getUser(@ModelAttribute User loginUser){
        JwtToken jwtToken = new JwtToken();
        String token = jwtToken.generateToken(loginUser.getEmail()); // 取得token

        ModelAndView mav = new ModelAndView();
        mav.addObject("token", token);
        User checkLoginUser = userRepository.findUserByEmail(loginUser.getEmail());
        if (checkLoginUser == null){
            // set null-user to loginUser, to show No such account!
            loginUser.setName("null-user");
            mav.addObject("loginUser", loginUser);
            mav.setViewName("login");
        } else {
            // use MD5 生成hash password
            String hashedPassword = DigestUtils.md5DigestAsHex(loginUser.getPassword().getBytes());
            if ((loginUser.getEmail().equals(checkLoginUser.getEmail())) && (hashedPassword.equals(checkLoginUser.getPassword()))){
                mav.addObject("userInfo", checkLoginUser);
                // get order details for this member
                List<OrderDetails> orderDetailsList = orderRepository.findAllDetailsByUserId(checkLoginUser.getId());
                mav.addObject("orderDetails", orderDetailsList);
                mav.setViewName("member");
            } else {
                mav.addObject("checkLoginUser", checkLoginUser);
                mav.setViewName("login");
            }
        }
        return mav;
    }

    // sign in to order
    @PostMapping("/signInForOrder/{orderInfo}")
    public ModelAndView signInForOrder(@ModelAttribute User loginUser, @PathVariable String orderInfo){
        User checkLoginUser = userRepository.findUserByEmail(loginUser.getEmail());
        ModelAndView mav = new ModelAndView("loginForTickets");
        // get order total price
        Integer total = Integer.parseInt(orderInfo.substring(orderInfo.indexOf("total=")+6));
        // get order seats
        String seat = orderInfo.subSequence((orderInfo.indexOf("seat=")+5), orderInfo.indexOf("&")).toString();
        mav.addObject("seat", seat);
        mav.addObject("total", total);

        if (checkLoginUser == null){
            // set null-user to loginUser, to show No such account!
            loginUser.setName("null-user");
            mav.addObject("loginUser", loginUser);
        } else {
            // use MD5 生成hash password
            String hashedPassword = DigestUtils.md5DigestAsHex(loginUser.getPassword().getBytes());
            if ((loginUser.getEmail().equals(checkLoginUser.getEmail())) && (hashedPassword.equals(checkLoginUser.getPassword()))){
                mav.addObject("userInfo", checkLoginUser);
            } else {
                mav.addObject("checkLoginUser", checkLoginUser);
            }
        }
        return mav;
    }

    // sign up new User
    @PostMapping("/signUp")
    // BindingResult to get error when have validation error
    public ModelAndView signUp(@ModelAttribute @Valid User user, BindingResult result){
        ModelAndView mav = new ModelAndView();
        if (result.hasErrors()) {
            mav.setViewName("signUp");
            return mav;
        }
        User checkUser = userRepository.findUserByEmail(user.getEmail());
        if (checkUser == null){
            // use MD5 生成hash password
            String hashedPassword = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
            user.setPassword(hashedPassword);
            userRepository.save(user);
            mav.setViewName("login");
        } else {
            mav.addObject("existUser", checkUser);
            mav.setViewName("signUp");
        }
        return mav;
    }
}
