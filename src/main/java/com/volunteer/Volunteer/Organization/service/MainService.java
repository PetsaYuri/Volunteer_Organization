package com.volunteer.Volunteer.Organization.service;

import com.volunteer.Volunteer.Organization.exceptions.ItLastPageException;
import com.volunteer.Volunteer.Organization.exceptions.NotExistsNextPageException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MainService {

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


}
