package com.volunteer.Volunteer.Organization.service;

import com.volunteer.Volunteer.Organization.models.Form;
import com.volunteer.Volunteer.Organization.repository.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadsPhotosService {

    @Autowired
    private FormRepository formRepository;

    private final String UPLOAD_PATH = System.getProperty("user.dir") + "/src/main/resources/static/icon/uploads/candidates";

    private final String UPLOAD_PATH_TARGET = System.getProperty("user.dir") + "/target/classes/static/icon/uploads/candidates";

    private String filename;

    private String filenameWithUUID;

    public UploadsPhotosService()  {
        checkFolderOnExist();
    }

    public UploadsPhotosService(String filename)   {
        this.filename = filename;
        checkFolderOnExist();
    }

    public Boolean checkFormatFile(String namefile)    {
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
        File uploadDir = new File(UPLOAD_PATH);
        if(!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        File uploadDirTarget = new File(UPLOAD_PATH_TARGET);
        if(!uploadDirTarget.exists()){
            uploadDirTarget.mkdir();
        }
    }

    public void saveFileToServer(MultipartFile file) throws IOException {
        File dirUpload = new File(getUPLOAD_PATH() + File.separator + getFilenameWithUUID());
        File dirUploadTarget = new File(getUPLOAD_PATH_TARGET() + File.separator + getFilenameWithUUID());
        file.transferTo( new File(String.valueOf(dirUpload)));
        Files.copy(Paths.get(String.valueOf(dirUpload)), Paths.get(String.valueOf(dirUploadTarget)));
    }

    public String createFilenameWithUUID()    {
        String uuidfile = UUID.randomUUID().toString();
        filenameWithUUID = uuidfile + "_" + filename;
        return filenameWithUUID;
    }

    public String getFilenameWithUUID() {
        return filenameWithUUID;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUPLOAD_PATH() {
        return UPLOAD_PATH;
    }

    public String getUPLOAD_PATH_TARGET()   {
        return UPLOAD_PATH_TARGET;
    }
}
