package com.example.reader.finereader.controller;

import com.example.reader.finereader.entities.DeniedBlack;
import com.example.reader.finereader.repository.DeniedBlackRepository;
import com.example.reader.finereader.repository.DeniedWhiteRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Controller
@RequestMapping("/")
public class LoginController {


    @Autowired
    private DeniedBlackRepository deniedBlackRepository;

    @Autowired
    private DeniedWhiteRepository deniedWhiteRepository;

    @GetMapping
    public ModelAndView startsApp() {
            return new ModelAndView("redirect:/login");
    }

    @GetMapping("/login")
    public ModelAndView startsAppLogin(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("index");
        var address = request.getRemoteAddr();
        if (deniedWhiteRepository.findByIP(address).size() == 0){
            return new ModelAndView("redirect:/authorization");
        }
        return mav;
    }

    @GetMapping("/authorization")
    public ModelAndView authorization(HttpServletRequest request){
//        ModelAndView mav = new ModelAndView("authorization");
        ModelAndView mav = new ModelAndView("blackList");
        DeniedBlack deniedBlack = new DeniedBlack();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime localDateTime = LocalDateTime.now();
        String formatDateTime = localDateTime.format(format);
        deniedBlack.setIp(request.getRemoteAddr());
        deniedBlack.setDates(formatDateTime);
        mav.addObject("addDenied", deniedBlack);
        return mav;
    }

    @PostMapping("/authorization")
    public String authorizationForm(@ModelAttribute("addDenied") @Valid DeniedBlack deniedBlack,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return "blackList";
        }
        deniedBlackRepository.save(deniedBlack);
        return "redirect:/denied";
    }

    @GetMapping("/denied")
    public ModelAndView deniedForm(HttpServletRequest request){
        ModelAndView mav = new ModelAndView("denied");
        mav.addObject("yourIP", request.getRemoteAddr());
        return mav;
    }
}
