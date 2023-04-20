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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


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
        if (!people.equals(address)) {
            return new ModelAndView("redirect:/adminDashboard/error");
        }
        LocalDate localDate = LocalDate.now().plusDays(10);
        LocalDate localDate1 = LocalDate.now();
        mav.addObject("people", users1.get(0).getPerson());
        mav.addObject("author", users1.get(0).getAuthority());
        mav.addObject("listControl", controlRepository.findByStatus(localDate));
        mav.addObject("listError", controlRepository.findByStatusError(localDate1));
        mav.addObject("deniedIPBlack", deniedBlackRepository.findAll());
        mav.addObject("deniedIPWhite", deniedWhiteRepository.findAll());
        mav.addObject("finder", new Users());
        mav.addObject("finderIPWhite", new DeniedWhite());
        mav.addObject("finderIPBlack", new DeniedBlack());
        mav.addObject("findControl", new Control());
        mav.addObject("countUsers", usersRepository.count());
        mav.addObject("countControl", controlRepository.count());
        mav.addObject("countNowControl", controlRepository.findByStatus(localDate).stream().count());
        mav.addObject("countErrorControl", controlRepository.findByStatusError(localDate1).stream().count());
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

    //----------------------------------Метод удаления белых IP---------------------------------------//

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

    //----------------------------------Метод отбора документов по ID до 10 дней-----------------------------------//

    @GetMapping("/findDocs")
    public ModelAndView findDocs(@RequestParam Long id) {
        var mav = new ModelAndView("adminDashboard/index2");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String users = user.getUsername();
        List<Users> users1 = usersRepository.findByLogin(users);
        LocalDate localDate = LocalDate.now().plusDays(10);
        LocalDate localDate1 = LocalDate.now();
        mav.addObject("people", users1.get(0).getPerson());
        mav.addObject("author", users1.get(0).getAuthority());
        mav.addObject("listControl", controlRepository.getId(id));
        mav.addObject("deniedIPBlack", deniedBlackRepository.findAll());
        mav.addObject("deniedIPWhite", deniedWhiteRepository.findAll());
        mav.addObject("finder", new Users());
        mav.addObject("finderIPWhite", new DeniedWhite());
        mav.addObject("finderIPBlack", new DeniedBlack());
        mav.addObject("findControl", new Control());
        mav.addObject("countUsers", usersRepository.count());
        mav.addObject("countControl", controlRepository.count());
        mav.addObject("countNowControl", controlRepository.findByStatus(localDate).stream().count());
        mav.addObject("countErrorControl", controlRepository.findByStatusError(localDate1).stream().count());
        mav.addObject("countDeniedWhite", deniedWhiteRepository.count());
        mav.addObject("countDeniedBlack", deniedBlackRepository.count());
        return mav;
    }

    //----------------------------------Метод отбора документов по ID просроченных-----------------------------------//

    @GetMapping("/findDocsError")
    public ModelAndView findDocsError(@RequestParam Long id) {
        var mav = new ModelAndView("adminDashboard/index3");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String users = user.getUsername();
        List<Users> users1 = usersRepository.findByLogin(users);
        LocalDate localDate = LocalDate.now().plusDays(10);
        LocalDate localDate1 = LocalDate.now();
        mav.addObject("people", users1.get(0).getPerson());
        mav.addObject("author", users1.get(0).getAuthority());
        mav.addObject("listControl", controlRepository.getId(id));
        mav.addObject("deniedIPBlack", deniedBlackRepository.findAll());
        mav.addObject("deniedIPWhite", deniedWhiteRepository.findAll());
        mav.addObject("finder", new Users());
        mav.addObject("finderIPWhite", new DeniedWhite());
        mav.addObject("finderIPBlack", new DeniedBlack());
        mav.addObject("findControl", new Control());
        mav.addObject("countUsers", usersRepository.count());
        mav.addObject("countControl", controlRepository.count());
        mav.addObject("countNowControl", controlRepository.findByStatus(localDate).stream().count());
        mav.addObject("countErrorControl", controlRepository.findByStatusError(localDate1).stream().count());
        mav.addObject("countDeniedWhite", deniedWhiteRepository.count());
        mav.addObject("countDeniedBlack", deniedBlackRepository.count());
        return mav;
    }

    //----------------------------------Методы поиска Белых и Черных IP----------------------------------//

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

    //----------------------------------Страница регистрации белых IP---------------------------------//

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

    //--------------------------Страница добавления заблокированных IP в белые-----------------------//

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

    //----------------------------------Метод обновления белых IP-----------------------------------//

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

    //----------------------------------Страница регистрации пользователя------------------------------//

    @GetMapping("/saveUsers")
    public ModelAndView addUserForm() {
        ModelAndView mav = new ModelAndView("adminDashboard/regUsers");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String users = user.getUsername();
        List<Users> users1 = usersRepository.findByLogin(users);
        mav.addObject("people", users1.get(0).getPerson());
        mav.addObject("author", users1.get(0).getAuthority());
        mav.addObject("addPerson", new Users());
        return mav;
    }

    @PostMapping("/saveUsers")
    public String saveUsers(@ModelAttribute("addPerson") @Valid Users users,
                            BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "adminDashboard/regUsers";
        }

        if (!users.getPassword().equals(users.getConfirm_password())) {
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "adminDashboard/regUsers";
        }

        var list = usersRepository.findByLogin(users.getLogin());

        if (list.size() > 0) {
            model.addAttribute("loginError", "Пользователь с таким логином уже существует");
            return "adminDashboard/regUsers";
        } else if (list.isEmpty()) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            users.setPassword(passwordEncoder.encode(users.getPassword()));
            usersRepository.save(users);
            model.addAttribute("addPeople", usersRepository.findAll());
        }
        return "redirect:/adminDashboard/dashboard";
    }

    //----------------------------------Страница таблицы с пользователями системы------------------------------//

    @GetMapping("/listUsers")
    public ModelAndView listUsers() {
        ModelAndView mav = new ModelAndView("adminDashboard/listUsers");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String users = user.getUsername();
        List<Users> users1 = usersRepository.findByLogin(users);
        mav.addObject("people", users1.get(0).getPerson());
        mav.addObject("author", users1.get(0).getAuthority());
        mav.addObject("listUsers", usersRepository.findAll());
        return mav;
    }

    //----------------------------------Метод удаления пользователей------------------------------//

    @DeleteMapping("/deleteUsers")
    public String deleteUsers(@RequestParam Long id) {
        usersRepository.deleteById(id);
        return "redirect:/adminDashboard/dashboard";
    }

    //----------------------------------Метод обновления пользователей------------------------------//

    @GetMapping("/updateUsers")
    public ModelAndView updateUsersForm(@RequestParam long id) {
        ModelAndView mav = new ModelAndView("adminDashboard/updateUsers");
        mav.addObject("update", usersRepository.getId(id));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String users = user.getUsername();
        List<Users> users1 = usersRepository.findByLogin(users);
        mav.addObject("people", users1.get(0).getPerson());
        mav.addObject("author", users1.get(0).getAuthority());
        List<String> authorities = new ArrayList<>();
        authorities.add("ROLE_ADMIN");
        authorities.add("ROLE_USER");
        mav.addObject("authorities", authorities);
        mav.addObject("authors", usersRepository.getId(id).getAuthority());
        List<String> departments = new ArrayList<>();
        departments.add("1 отдел");
        departments.add("2 отдел");
        departments.add("3 отдел");
        departments.add("4 отдел");
        departments.add("начальник управления");
        mav.addObject("departments", departments);
        mav.addObject("department", usersRepository.getId(id).getDepart());
        return mav;
    }

    @Transactional
    @PostMapping("/updateUsers")
    public String updateUsers(Long id, String login, String password, String authority,
                              String person, String department, String ip) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordNew = passwordEncoder.encode(password);
        usersRepository.updateUsers(id, login, passwordNew, authority, person, department, ip);
        return "redirect:/adminDashboard/dashboard";
    }

    //----------------------------------Страница пользователя------------------------------//

    @GetMapping("/profile")
    public ModelAndView profile() {
        ModelAndView mav = new ModelAndView("adminDashboard/pages-profile");
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
            return "redirect:/adminDashboard/errorsPassword";
        }
        return "redirect:/adminDashboard/dashboard";
    }

    //----------------------------------Страница ошибки при изменении пароля------------------------------//

    @GetMapping("/errorsPassword")
    public ModelAndView error() {
        return new ModelAndView("adminDashboard/pages-error-404");
    }

    //----------------------------------Страница таблицы с документами------------------------------//

    @GetMapping("/listDocument")
    public ModelAndView listEquipment() {
        ModelAndView mav = new ModelAndView("adminDashboard/listDocument");
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
        ModelAndView mav = new ModelAndView("adminDashboard/listDocument");
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
        return "redirect:/adminDashboard/dashboard";
    }

    //----------------------------------Страница регистрации документов------------------------------//

    @GetMapping("/saveDocument")
    public ModelAndView addEquipmentForm() {
        ModelAndView mav = new ModelAndView("adminDashboard/addDocument");
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
            return "adminDashboard/addDocument";
        }

        control.setDates(localDate);
        control.setData_plane(localDate1);
        controlRepository.save(control);

        return "redirect:/adminDashboard/dashboard";
    }

    //----------------------------------Метод обновления документов------------------------------//

    @GetMapping("/updateDocuments")
    public ModelAndView updateDocumentsForm(@RequestParam long id) {
        ModelAndView mav = new ModelAndView("adminDashboard/updateDocuments");
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
        return "redirect:/adminDashboard/dashboard";
    }

    //----------------------------------Метод исполнения документов------------------------------//

    @GetMapping("/executeDocuments")
    public ModelAndView executeDocumentsForm(@RequestParam long id) {
        ModelAndView mav = new ModelAndView("adminDashboard/executeDocuments");
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
        return "redirect:/adminDashboard/dashboard";
    }

}
