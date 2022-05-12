package com.volunteer.Volunteer.Organization.service;

import com.volunteer.Volunteer.Organization.repository.VolunteersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class UploadsPhotosService {

    @Autowired
    private VolunteersRepository volunteersRepository;

    @Value("${upload.path}")
    private String UPLOAD_PATH;

    public static final String PATH_TO_PHOTO = "/image/volunteers/";

    private String filenameWithUUID;

    public UploadsPhotosService()  {
       // checkFolderOnExist();
    }

    public Boolean isAllowedFileFormat(String namefile)    {
        String[] str = new String[2];
        str = namefile.split("\\.", 2);
        String fileFormat = str[1];

        if(fileFormat.equals("png") || fileFormat.equals("jpg") || fileFormat.equals("jpeg"))    {
            return true;
        }   else {
            return false;
        }
    }

    public void checkFolderOnExist()  {
        System.out.println(UPLOAD_PATH);
        File uploadDir = new File(UPLOAD_PATH);
        if(!uploadDir.exists()) {
            uploadDir.mkdir();
        }
    }

    public void saveFileToServer(MultipartFile file) throws IOException {
        File dirUpload = new File(System.getProperty("user.dir") + "/uploads/" + getFilenameWithUUID());
        file.transferTo( new File(String.valueOf(dirUpload)));
    }

    public String createFilenameWithUUID(String filename)    {
        String uuidfile = UUID.randomUUID().toString();
        filenameWithUUID = uuidfile + "_" + filename;
        return filenameWithUUID;
    }

    public String getFilenameWithUUID() {
        return filenameWithUUID;
    }

    public String getUPLOAD_PATH() {
        return UPLOAD_PATH;
    }
}
