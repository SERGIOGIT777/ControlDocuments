package com.example.reader.finereader.controller;

import com.example.reader.finereader.entities.Control;
import com.example.reader.finereader.entities.DeniedBlack;
import com.example.reader.finereader.entities.DeniedWhite;
import com.example.reader.finereader.entities.Users;
import com.example.reader.finereader.repository.ControlRepository;
import com.example.reader.finereader.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/controlDashboard")
public class ControlController {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ControlRepository controlRepository;

    //----------------------------------Главная страница------------------------------//

    @GetMapping("/dashboard")
    public ModelAndView home(HttpServletRequest request) {
        var mav = new ModelAndView("controlDashboard/index");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String users = user.getUsername();
        List<Users> users1 = usersRepository.findByLogin(users);
        var address = request.getRemoteAddr();
        var people = users1.get(0).getIp();
//        if (!people.equals(address)) {
//            return new ModelAndView("redirect:/adminDashboard/error");
//        }
        LocalDate localDate = LocalDate.now().plusDays(10);
        LocalDate localDate1 = LocalDate.now();
        mav.addObject("people", users1.get(0).getPerson());
        mav.addObject("author", users1.get(0).getAuthority());
        mav.addObject("listControl", controlRepository.findByStatus(localDate));
        mav.addObject("listError", controlRepository.findByStatusError(localDate1));
        mav.addObject("finder", new Users());
        mav.addObject("finderIPWhite", new DeniedWhite());
        mav.addObject("finderIPBlack", new DeniedBlack());
        mav.addObject("findControl", new Control());
        mav.addObject("countControl", controlRepository.count());
        mav.addObject("countNowControl", controlRepository.findByStatus(localDate).stream().count());
        mav.addObject("countErrorControl", controlRepository.findByStatusError(localDate1).stream().count());
        return mav;
    }

    @GetMapping("/error")
    public ModelAndView errorAuthorize() {
        return new ModelAndView("controlDashboard/errorAuthorize");
    }

    //----------------------------------Метод удаления контрольных документов------------------------------//

    @DeleteMapping("/deleteDocs")
    public String deleteDocs(@RequestParam Long id) {
        controlRepository.deleteById(id);
        return "redirect:/controlDashboard/dashboard";
    }

    //----------------------------------Метод отбора документов по ID до 10 дней-----------------------------------//

    @GetMapping("/findDocs")
    public ModelAndView findDocs(@RequestParam Long id) {
        var mav = new ModelAndView("controlDashboard/index2");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String users = user.getUsername();
        List<Users> users1 = usersRepository.findByLogin(users);
        LocalDate localDate = LocalDate.now().plusDays(10);
        LocalDate localDate1 = LocalDate.now();
        mav.addObject("people", users1.get(0).getPerson());
        mav.addObject("author", users1.get(0).getAuthority());
        mav.addObject("listControl", controlRepository.getId(id));
        mav.addObject("finder", new Users());
        mav.addObject("finderIPWhite", new DeniedWhite());
        mav.addObject("finderIPBlack", new DeniedBlack());
        mav.addObject("findControl", new Control());
        mav.addObject("countControl", controlRepository.count());
        mav.addObject("countNowControl", controlRepository.findByStatus(localDate).stream().count());
        mav.addObject("countErrorControl", controlRepository.findByStatusError(localDate1).stream().count());
        return mav;
    }

    //----------------------------------Метод отбора документов по ID просроченных-----------------------------------//

    @GetMapping("/findDocsError")
    public ModelAndView findDocsError(@RequestParam Long id) {
        var mav = new ModelAndView("controlDashboard/index3");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String users = user.getUsername();
        List<Users> users1 = usersRepository.findByLogin(users);
        LocalDate localDate = LocalDate.now().plusDays(10);
        LocalDate localDate1 = LocalDate.now();
        mav.addObject("people", users1.get(0).getPerson());
        mav.addObject("author", users1.get(0).getAuthority());
        mav.addObject("listControl", controlRepository.getId(id));
        mav.addObject("finder", new Users());
        mav.addObject("finderIPWhite", new DeniedWhite());
        mav.addObject("finderIPBlack", new DeniedBlack());
        mav.addObject("findControl", new Control());
        mav.addObject("countControl", controlRepository.count());
        mav.addObject("countNowControl", controlRepository.findByStatus(localDate).stream().count());
        mav.addObject("countErrorControl", controlRepository.findByStatusError(localDate1).stream().count());
        return mav;
    }

    //----------------------------------Страница пользователя------------------------------//

    @GetMapping("/profile")
    public ModelAndView profile() {
        ModelAndView mav = new ModelAndView("controlDashboard/pages-profile");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String users = user.getUsername();
        List<Users> users1 = usersRepository.findByLogin(users);
        mav.addObject("update", usersRepository.getId(users1.get(0).getId()));
        mav.addObject("people", users1.get(0).getPerson());
        mav.addObject("author", users1.get(0).getAuthority());
        mav.addObject("department", users1.get(0).getDepart());
        mav.addObject("ip", users1.get(0).getIp());
        mav.addObject("login", users1.get(0).getLogin());
        mav.addObject("oldPassword", users1.get(0).getPassword());
        return mav;
    }

    @Transactional
    @PostMapping("/profile")
    public String updatePassword(Long id, String password,
                                 @RequestParam("oldPassword") String oldPassword) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordNew = passwordEncoder.encode(password);
        String users = user.getUsername();
        List<Users> users1 = usersRepository.findByLogin(users);
        if (passwordEncoder.matches(oldPassword, users1.get(0).getPassword())) {
            usersRepository.updatePassword(id, passwordNew);
        } else {
            return "redirect:/controlDashboard/errorsPassword";
        }
        return "redirect:/controlDashboard/dashboard";
    }

    //----------------------------------Страница ошибки при изменении пароля------------------------------//

    @GetMapping("/errorsPassword")
    public ModelAndView error() {
        return new ModelAndView("controlDashboard/pages-error-404");
    }

    //----------------------------------Страница таблицы с документами------------------------------//

    @GetMapping("/listDocument")
    public ModelAndView listEquipment() {
        ModelAndView mav = new ModelAndView("controlDashboard/listDocument");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String users = user.getUsername();
        List<Users> users1 = usersRepository.findByLogin(users);
        mav.addObject("people", users1.get(0).getPerson());
        mav.addObject("author", users1.get(0).getAuthority());
        mav.addObject("listDocument", controlRepository.findAll());
        mav.addObject("findDocument", new Control());
        mav.addObject("persons", usersRepository.findAll());
        return mav;
    }

    //----------------------------------Поиск документов по номеру------------------------------//

    @GetMapping("/findDocument")
    public ModelAndView findEquipment(@RequestParam String reg_number) {
        ModelAndView mav = new ModelAndView("controlDashboard/listDocument");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String users = user.getUsername();
        List<Users> users1 = usersRepository.findByLogin(users);
        mav.addObject("people", users1.get(0).getPerson());
        mav.addObject("author", users1.get(0).getAuthority());
        mav.addObject("listDocument", controlRepository.findByNumber(reg_number));
        mav.addObject("findDocument", new Control());
        mav.addObject("persons", usersRepository.findAll());
        return mav;
    }

    //----------------------------------Метод удаления документов------------------------------//

    @DeleteMapping("/deleteDocument")
    public String deleteDocument(@RequestParam Long id) {
        controlRepository.deleteById(id);
        return "redirect:/controlDashboard/dashboard";
    }

    //----------------------------------Страница регистрации документов------------------------------//

    @GetMapping("/saveDocument")
    public ModelAndView addEquipmentForm() {
        ModelAndView mav = new ModelAndView("controlDashboard/addDocument");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String users = user.getUsername();
        List<Users> users1 = usersRepository.findByLogin(users);
        mav.addObject("people", users1.get(0).getPerson());
        mav.addObject("author", users1.get(0).getAuthority());
        mav.addObject("addDocument", new Control());
        return mav;
    }

    @PostMapping("/saveDocument")
    public String saveEquipment(@ModelAttribute("addDocument") @Valid Control control,
                                BindingResult result,
                                @RequestParam("dates")
                                @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate localDate,
                                @RequestParam("data_plane")
                                @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate localDate1) {

        if (result.hasErrors()) {
            return "controlDashboard/addDocument";
        }

        control.setDates(localDate);
        control.setData_plane(localDate1);
        controlRepository.save(control);

        return "redirect:/controlDashboard/dashboard";
    }

    //----------------------------------Метод обновления документов------------------------------//

    @GetMapping("/updateDocuments")
    public ModelAndView updateDocumentsForm(@RequestParam long id) {
        ModelAndView mav = new ModelAndView("controlDashboard/updateDocuments");
        mav.addObject("update", controlRepository.getId(id));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String users = user.getUsername();
        List<Users> users1 = usersRepository.findByLogin(users);
        mav.addObject("people", users1.get(0).getPerson());
        mav.addObject("author", users1.get(0).getAuthority());
        return mav;
    }

    @Transactional
    @PostMapping("/updateDocuments")
    public String updateDocuments(Long id, String assignment, String correspondent, String reg_number,
                                  LocalDate dates, String author_resolution, String resolution, LocalDate data_plane,
                                  String executor, LocalDate data_fact, String reg_answer, String answer) {
        controlRepository.updateDocuments(id, assignment, correspondent, reg_number, dates, author_resolution, resolution,
                data_plane, executor, data_fact, reg_answer, answer);
        return "redirect:/controlDashboard/dashboard";
    }

    //----------------------------------Метод исполнения документов------------------------------//

    @GetMapping("/executeDocuments")
    public ModelAndView executeDocumentsForm(@RequestParam long id) {
        ModelAndView mav = new ModelAndView("controlDashboard/executeDocuments");
        mav.addObject("execute", controlRepository.getId(id));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String users = user.getUsername();
        List<Users> users1 = usersRepository.findByLogin(users);
        mav.addObject("people", users1.get(0).getPerson());
        mav.addObject("author", users1.get(0).getAuthority());
        mav.addObject("assignment", controlRepository.getId(id).getAssignment());
        mav.addObject("correspondent", controlRepository.getId(id).getCorrespondent());
        mav.addObject("reg_number", controlRepository.getId(id).getReg_number());
        mav.addObject("dates", controlRepository.getId(id).getDates());
        mav.addObject("author_resolution", controlRepository.getId(id).getAuthor_resolution());
        mav.addObject("resolution", controlRepository.getId(id).getResolution());
        mav.addObject("data_plane", controlRepository.getId(id).getData_plane());
        mav.addObject("executor", controlRepository.getId(id).getExecutor());
        return mav;
    }

    @Transactional
    @PostMapping("/executeDocuments")
    public String executeDocuments(Long id, LocalDate data_fact, String reg_answer, String answer) {
        controlRepository.executeDocuments(id, data_fact, reg_answer, answer);
        return "redirect:/controlDashboard/dashboard";
    }
}
