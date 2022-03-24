package com.volunteer.Volunteer.Organization.service;

import com.volunteer.Volunteer.Organization.repository.CandidatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadsPhotosService {

    @Autowired
    private CandidatesRepository candidatesRepository;

    private final String UPLOAD_PATH = System.getProperty("user.dir") + "/src/main/resources/static/icon/uploads/candidates";

    private final String UPLOAD_PATH_TARGET = System.getProperty("user.dir") + "/target/classes/static/icon/uploads/candidates";

    private String filenameWithUUID;

    public UploadsPhotosService()  {
        checkFolderOnExist();
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

    public BufferedImage cropImageSquare(byte[] image) throws IOException {
        InputStream in = new ByteArrayInputStream(image);
        BufferedImage originalImage = ImageIO.read(in);

        int height = originalImage.getHeight();
        int width = originalImage.getWidth();

        int squareSize = (height > width ? width : height);

        int xc = width / 2;
        int yc = height / 2;

        BufferedImage croppedImage = originalImage.getSubimage(
                xc - (squareSize / 2),
                yc - (squareSize / 2),
                squareSize,
                squareSize
        );
        return croppedImage;
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

    public String getUPLOAD_PATH_TARGET()   {
        return UPLOAD_PATH_TARGET;
    }
}
