package com.volunteer.Volunteer.Organization.service;

import com.volunteer.Volunteer.Organization.exceptions.ItLastPageException;
import com.volunteer.Volunteer.Organization.exceptions.NotExistsNextPageException;
import com.volunteer.Volunteer.Organization.models.ProjectInfo;
import com.volunteer.Volunteer.Organization.repository.ProjectInfoRepository;
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

    public String passwordEncoder(String password)    {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        return encodedPassword;
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

    public ProjectInfo editProjectName(String name)   {
        ProjectInfo projectInfo = projectInfoRepository.getById(Long.valueOf(1));
        projectInfo.setName(name);
        projectInfoRepository.save(projectInfo);
        return projectInfo;
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
}
