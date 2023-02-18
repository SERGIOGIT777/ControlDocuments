package com.example.reader.finereader.controller;

import com.example.reader.finereader.entities.Control;
import com.example.reader.finereader.entities.DeniedBlack;
import com.example.reader.finereader.entities.DeniedWhite;
import com.example.reader.finereader.entities.Users;
import com.example.reader.finereader.repository.ControlRepository;
import com.example.reader.finereader.repository.DeniedBlackRepository;
import com.example.reader.finereader.repository.DeniedWhiteRepository;
import com.example.reader.finereader.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.time.LocalDate;
import java.util.List;
import java.util.Locale;


@Controller
@RequestMapping("/adminDashboard")
public class AdminController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ControlRepository controlRepository;

    @Autowired
    private DeniedBlackRepository deniedBlackRepository;

    @Autowired
    private DeniedWhiteRepository deniedWhiteRepository;

//----------------------------------Главная страница------------------------------//

    @GetMapping("/dashboard")
    public ModelAndView home(HttpServletRequest request) {
        var mav = new ModelAndView("adminDashboard/index");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String users = user.getUsername();
        List<Users> users1 = usersRepository.findByLogin(users);
        var address = request.getRemoteAddr();
        var people = users1.get(0).getIp();
//        if (!people.equals(address)) {
//            return new ModelAndView("redirect:/adminDashboard/error");
//        }
        LocalDate localDate = LocalDate.now().plusDays(10);
        mav.addObject("people", users1.get(0).getPerson());
        mav.addObject("author", users1.get(0).getAuthority());
        mav.addObject("listControl", controlRepository.findByStatus(localDate));
        mav.addObject("deniedIPBlack", deniedBlackRepository.findAll());
        mav.addObject("deniedIPWhite", deniedWhiteRepository.findAll());
        mav.addObject("finder", new Users());
        mav.addObject("finderIPWhite", new DeniedWhite());
        mav.addObject("finderIPBlack", new DeniedBlack());
        mav.addObject("findControl", new Control());
        mav.addObject("countUsers", usersRepository.count());
        mav.addObject("countControl", controlRepository.count());
        mav.addObject("countDeniedWhite", deniedWhiteRepository.count());
        mav.addObject("countDeniedBlack", deniedBlackRepository.count());
        return mav;
    }

    @GetMapping("/error")
    public ModelAndView errorAuthorize() {
        return new ModelAndView("adminDashboard/errorAuthorize");
    }

    //----------------------------------Метод удаления контрольных документов------------------------------//

    @DeleteMapping("/deleteDocs")
    public String deleteDocs(@RequestParam Long id) {
        controlRepository.deleteById(id);
        return "redirect:/adminDashboard/dashboard";
    }

    //----------------------------------Метод удаления белых IP------------------------------//

    @DeleteMapping("/deleteWhite")
    public String deleteDenied(@RequestParam Long id) {
        deniedWhiteRepository.deleteById(id);
        return "redirect:/adminDashboard/dashboard";
    }

    //----------------------------------Метод удаления заблокированных IP------------------------------//

    @DeleteMapping("/deleteBlack")
    public String deleteDeniedList(@RequestParam Long id) {
        deniedBlackRepository.deleteById(id);
        return "redirect:/adminDashboard/dashboard";
    }

    //----------------------------------Метод отбора документов по ID------------------------------//

    @GetMapping("/findDocs")
    public ModelAndView findDocs(@RequestParam Long id) {
        var mav = new ModelAndView("adminDashboard/index");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String users = user.getUsername();
        List<Users> users1 = usersRepository.findByLogin(users);
        mav.addObject("people", users1.get(0).getPerson());
        mav.addObject("author", users1.get(0).getAuthority());
        mav.addObject("listControl", controlRepository.getId(id));
        return mav;
    }

    //----------------------------------Методы поиска Белых и Черных IP------------------------------//

    @GetMapping("/findIPWhite")
    public ModelAndView findIPWhite(@RequestParam String ip) {
        var mav = new ModelAndView("adminDashboard/index");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String users = user.getUsername();
        List<Users> users1 = usersRepository.findByLogin(users);
        LocalDate localDate = LocalDate.now().plusDays(10);
        mav.addObject("people", users1.get(0).getPerson());
        mav.addObject("author", users1.get(0).getAuthority());
        mav.addObject("listControl", controlRepository.findByStatus(localDate));
        mav.addObject("deniedIPBlack", deniedBlackRepository.findAll());
        mav.addObject("deniedIPWhite", deniedWhiteRepository.findByIP(ip));
        mav.addObject("finder", new Users());
        mav.addObject("finderIPWhite", new DeniedWhite());
        mav.addObject("finderIPBlack", new DeniedBlack());
        mav.addObject("countUsers", usersRepository.count());
        mav.addObject("countControl", controlRepository.count());
        mav.addObject("countDeniedWhite", deniedWhiteRepository.count());
        mav.addObject("countDeniedBlack", deniedBlackRepository.count());
        return mav;
    }

    @GetMapping("/findIPBlack")
    public ModelAndView findIPBlack(@RequestParam String ip) {
        var mav = new ModelAndView("adminDashboard/index");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String users = user.getUsername();
        List<Users> users1 = usersRepository.findByLogin(users);
        LocalDate localDate = LocalDate.now().plusDays(10);
        mav.addObject("people", users1.get(0).getPerson());
        mav.addObject("author", users1.get(0).getAuthority());
        mav.addObject("listControl", controlRepository.findByStatus(localDate));
        mav.addObject("deniedIPBlack", deniedBlackRepository.findByIP(ip));
        mav.addObject("deniedIPWhite", deniedWhiteRepository.findAll());
        mav.addObject("finder", new Users());
        mav.addObject("finderIPWhite", new DeniedWhite());
        mav.addObject("finderIPBlack", new DeniedBlack());
        mav.addObject("countUsers", usersRepository.count());
        mav.addObject("countControl", controlRepository.count());
        mav.addObject("countDeniedWhite", deniedWhiteRepository.count());
        mav.addObject("countDeniedBlack", deniedBlackRepository.count());
        return mav;
    }

    //----------------------------------Страница регистрации белых IP------------------------------//

    @GetMapping("/saveIpWhite")
    public ModelAndView addIpWhite() {
        ModelAndView mav = new ModelAndView("adminDashboard/WhiteIp");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String users = user.getUsername();
        List<Users> users1 = usersRepository.findByLogin(users);
        mav.addObject("people", users1.get(0).getPerson());
        mav.addObject("author", users1.get(0).getAuthority());
        mav.addObject("addIpWhite", new DeniedWhite());
        return mav;
    }

    @PostMapping("/saveIpWhite")
    public String saveIpWhite(@ModelAttribute("addIpWhite") @Valid DeniedWhite deniedWhite,
                         BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "adminDashboard/WhiteIp";
        }

        deniedWhiteRepository.save(deniedWhite);
        model.addAttribute("addIpWhite", deniedWhiteRepository.findAll());
        return "redirect:/adminDashboard/dashboard";
    }

    //----------------------------------Страница добавления заблокированных IP в белые------------------------------//

    @GetMapping("/saveIpBlack")
    public ModelAndView addIpBlack(@RequestParam String ip) {
        ModelAndView mav = new ModelAndView("adminDashboard/BlackIp");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String users = user.getUsername();
        List<Users> users1 = usersRepository.findByLogin(users);
        mav.addObject("people", users1.get(0).getPerson());
        mav.addObject("author", users1.get(0).getAuthority());
        mav.addObject("addIpBlack", new DeniedWhite());
        mav.addObject("IpBlock", ip);
        return mav;
    }

    @PostMapping("/saveIpDeniedList")
    public String saveIpBlack(@ModelAttribute("addIpBlack") @Valid DeniedWhite deniedWhite,
                             BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "adminDashboard/BlackIp";
        }

        deniedWhiteRepository.save(deniedWhite);
        model.addAttribute("addIpBlack", deniedWhiteRepository.findAll());
        return "redirect:/adminDashboard/dashboard";
    }

    //----------------------------------Метод обновления белых IP------------------------------//

    @GetMapping("/updateIp")
    public ModelAndView updateIp(@RequestParam Long id) {
        ModelAndView mav = new ModelAndView("adminDashboard/UpdateIP");
        mav.addObject("updates", deniedWhiteRepository.getId(id));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String users = user.getUsername();
        List<Users> users1 = usersRepository.findByLogin(users);
        mav.addObject("people", users1.get(0).getPerson());
        mav.addObject("author", users1.get(0).getAuthority());
        return mav;
    }

    @Transactional
    @PostMapping("/updateIp")
    public String updateIpMethod(Long id, String ip, String person) {
        deniedWhiteRepository.updateDenied(id, ip, person);
        return "redirect:/adminDashboard/dashboard";
    }

}
