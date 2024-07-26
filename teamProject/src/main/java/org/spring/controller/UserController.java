package org.spring.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.spring.domain.UserDTO;
import org.spring.domain.job.JobBoardDTO;
import org.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserService service;
    

    @GetMapping("/test")
    public String getUser(HttpSession session) {
        String user_name = (String) session.getAttribute("user_name");
        String user_email = (String) session.getAttribute("user_email");
        
        UserDTO user = new UserDTO();
        user.setUser_name(user_name);
        user.setUser_email(user_email);
        
        if (user_email == null || user_name.isEmpty()) {
            // user_name이 세션에 없을 경우의 처리
            System.out.println("User name is not in session");
            return "redirect:/login"; // 에러 페이지로 리디렉션
        }

        System.out.println(user_name);
        
          UserDTO userInfo = service.getUserInfo(user);
        
        if (userInfo != null) {
            System.out.println(userInfo.getUser_num());
            session.setAttribute("user_info", userInfo);
            
        } else {
            // 유저 정보를 찾지 못했을 경우의 처리
            System.out.println("User not found");
            return "redirect:/error"; // 에러 페이지로 리디렉션
        }
        
        return "redirect:/main"; // 올바른 경로인지 확인
    }
    
    @PostMapping("/updateProfile")
    public String updateProfile(HttpSession session, 
                                @RequestParam("user_num") Integer userNum,
                                @RequestParam("user_name") String userName,
                                @RequestParam("user_email") String userEmail,
                                @RequestParam("user_phone") String userPhone
                                ) {
        UserDTO user = (UserDTO) session.getAttribute("user_info");
        if (user != null && user.getUser_num().equals(userNum)) {
            user.setUser_name(userName);
            user.setUser_email(userEmail);
            user.setUser_phone(userPhone);
            service.updateUserProfile(user);
            session.setAttribute("user_info", user);
            System.out.println("세션정보: " + user);
            System.out.println("성공");
        } else {
        	System.out.println("실패");
        }
        return "/myPage";
    }
}
