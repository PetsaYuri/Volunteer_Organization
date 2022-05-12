package com.volunteer.Volunteer.Organization.service;

import com.volunteer.Volunteer.Organization.models.Users;
import com.volunteer.Volunteer.Organization.models.Volunteers;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class ExportCSVService {

    public HttpServletResponse settingsOfResponse(HttpServletResponse response, String contentName)    {
        response.setContentType("text/csv");
        String filename = "Volunteer_Organization_export_";

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + filename + contentName + ".csv";

        response.setHeader(headerKey, headerValue);
        return response;
    }

    public void createAndSendCsvFileVolunteers(HttpServletResponse response, Iterable<Volunteers> volunteers)
            throws IOException {
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
        String[] csvHeader = {"Name", "Email", "Phone", "City", "Description", "Status"};
        String[] candidatesMapping = {"name", "email", "phone", "city", "description", "status"};
        csvWriter.writeHeader(csvHeader);

        for(Volunteers volunteer : volunteers)    {
            csvWriter.write(volunteer, candidatesMapping);
        }

        csvWriter.close();
    }

    public void createAndSendCsvFileUsers(HttpServletResponse response, Iterable<Users> users)
            throws IOException {
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
        String[] csvHeader = {"Name", "Email", "Role", "Blocked"};
        String[] usersMapping = {"name", "email", "role", "blocked"};
        csvWriter.writeHeader(csvHeader);

        for(Users user : users)    {
            csvWriter.write(user, usersMapping);
        }

        csvWriter.close();
    }
}
