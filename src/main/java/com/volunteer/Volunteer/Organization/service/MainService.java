package com.volunteer.Volunteer.Organization.service;

import com.volunteer.Volunteer.Organization.exceptions.ItLastPageException;
import com.volunteer.Volunteer.Organization.exceptions.NotExistsNextPageException;
import com.volunteer.Volunteer.Organization.models.Categories;
import com.volunteer.Volunteer.Organization.models.ProjectInfo;
import com.volunteer.Volunteer.Organization.models.Roles;
import com.volunteer.Volunteer.Organization.models.Users;
import com.volunteer.Volunteer.Organization.repository.CategoriesRepository;
import com.volunteer.Volunteer.Organization.repository.ProjectInfoRepository;
import com.volunteer.Volunteer.Organization.repository.RolesRepository;
import com.volunteer.Volunteer.Organization.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.volunteer.Volunteer.Organization.service.UploadsPhotosService.PATH_TO_PROJECT_INFO_UPLOADS;

@Service
public class MainService {

    @Autowired
    private ProjectInfoRepository projectInfoRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    public static String getCurrentDate() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String currentTime = timestamp.toString();
        String[] regexByDayAndHour = currentTime.split(" ");
        String[] regexByHourAndMin = regexByDayAndHour[1].split(":");
        String hourAndMin = regexByHourAndMin[0] + ":" + regexByHourAndMin[1];
        String[] regexByDayAndMonthAndYear = regexByDayAndHour[0].split("-");
        String dayAndMonthAndYear = regexByDayAndMonthAndYear[2] + "."
                + regexByDayAndMonthAndYear[1] + "." + regexByDayAndMonthAndYear[0];
        String date = hourAndMin + ", " + dayAndMonthAndYear;
        return date;
    }

    public List<Integer> getPages(int totalPages)   {
        List<Integer> pages = new ArrayList<>();
        for (int i = 0; i < totalPages; i++)    {
            pages.add(i);
        }
        return pages;
    }

    public String previousPage(Pageable pageable)  throws ItLastPageException  {
        if (pageable.hasPrevious()) {
            int previousPage = pageable.getPageNumber() - 1;
            return "?page=" + previousPage + "&&size=" + pageable.getPageSize();
        }   else {
            throw new ItLastPageException();
        }
    }

    public String nextPage(Pageable pageable, int totalPage)  throws NotExistsNextPageException  {
        if (pageable.getPageNumber() != totalPage) {
            int nextPage = pageable.getPageNumber() + 1;
            return "?page=" + nextPage + "&&size=" + pageable.getPageSize();
        }   else {
            throw new NotExistsNextPageException();
        }
    }

    public void editProjectName(String name)   {
        ProjectInfo projectInfo = projectInfoRepository.getById(Long.valueOf(1));
        projectInfo.setName(name);
        projectInfoRepository.save(projectInfo);
    }

    public void editContacts(String telegram, String email, String phone)  {
        ProjectInfo projectInfo = projectInfoRepository.getById(Long.valueOf(1));
        projectInfo.setTelegram(telegram);
        projectInfo.setEmail(email);
        projectInfo.setPhone(phone);
        projectInfoRepository.save(projectInfo);
    }

    public ProjectInfo getProjectInfo() {
        ProjectInfo projectInfo = projectInfoRepository.getById(Long.valueOf(1));
        if (projectInfo != null)    {
            return projectInfo;
        }   else {
            return null;
        }
    }

    public String getPathProjectInfo()  {
        return PATH_TO_PROJECT_INFO_UPLOADS;
    }

    public String[] splitDescriptionOnParagraph(String description)  {
        String[] str;
        str = description.split("\\n");
        return str;
    }

    public void addAdmin()  {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Roles roles = rolesRepository.findByRole("admin");
        String encodedPass = passwordEncoder.encode("123");
        Users admin = new Users(encodedPass, roles, "adm@com", "admin");
        usersRepository.save(admin);
    }

    public void addRoles()  {
        Roles roleAdmin = new Roles("admin", "role for administrations");
        rolesRepository.save(roleAdmin);
        Roles roleEditor = new Roles("editor", "role for editors");
        rolesRepository.save(roleEditor);
        Roles roleUser = new Roles("user", "role for users");
        rolesRepository.save(roleUser);
    }

    public void addCategories() {
        Categories categoryVolunteers = new Categories("Волонтерство", "дана категорія для дописів із тематикою волонтерства");
        categoriesRepository.save(categoryVolunteers);
        Categories categoryPersonalExp= new Categories("Особистий досвід", "дана категорія для дописів із тематикою проведення заходів, в яких розповідається про волонтерську справу");
        categoriesRepository.save(categoryPersonalExp);
        Categories categoryHowWork= new Categories("Як це працює?", "дана категорія для дописів із тематикою висвітлення не до кінця зрозумілих тем");
        categoriesRepository.save(categoryHowWork);
        Categories categoryOther = new Categories("Також варте", "дана категорія для дописів із іншими темами");
        categoriesRepository.save(categoryOther);
    }

    public ProjectInfo addProjectInfo()    {
        ProjectInfo projectInfo = new ProjectInfo("Volunteer Org", "https://t.me/V_Zelenskiy_official", "volunteerOrg@gmail.com", "380123456789");
        projectInfoRepository.save(projectInfo);
        System.out.println("11111111111123");
        return projectInfo;
    }
}
