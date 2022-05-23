package com.volunteer.Volunteer.Organization.service;

import com.volunteer.Volunteer.Organization.models.Posts;
import com.volunteer.Volunteer.Organization.models.ProjectInfo;
import com.volunteer.Volunteer.Organization.models.SuggestedPosts;
import com.volunteer.Volunteer.Organization.models.Volunteers;
import com.volunteer.Volunteer.Organization.repository.PostsRepository;
import com.volunteer.Volunteer.Organization.repository.ProjectInfoRepository;
import com.volunteer.Volunteer.Organization.repository.SuggestedPostsRepository;
import com.volunteer.Volunteer.Organization.repository.VolunteersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private VolunteersRepository volunteersRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private SuggestedPostsRepository suggestedPostsRepository;

    @Autowired
    private ProjectInfoRepository projectInfoRepository;

    @Value("${upload.path}")
    private String UPLOAD_PATH;

    public static final String PATH_TO_VOLUNTEERS_UPLOADS = "/image/volunteers/";

    public static final String PATH_TO_POSTS_UPLOADS = "/image/posts/";

    public static final String PATH_TO_SUGGESTED_POSTS_UPLOADS = "/image/suggested_posts/";

    public static final String PATH_TO_PROJECT_INFO_UPLOADS = "/image/project_info/";

    public UploadsPhotosService()  {
       // checkFolderOnExist();
    }

    public Boolean isAllowedFileFormat(String filename)    {
        String[] str = new String[2];
        str = filename.split("\\.", 2);
        String fileFormat = str[1];

        if(fileFormat.equals("png") || fileFormat.equals("jpg") || fileFormat.equals("jpeg"))    {
            return true;
        }   else {
            return false;
        }
    }

    public Boolean isIcoFile(String filename)   {
        String[] str = new String[2];
        str = filename.split("\\.", 2);
        String fileFormat = str[1];

        if(fileFormat.equals("ico"))    {
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

    public void saveFile(MultipartFile file, String filename, Volunteers volunteer) throws IOException {
        volunteer.setPhoto(filename);
        volunteersRepository.save(volunteer);
        File dirUpload = new File(System.getProperty("user.dir") + "/uploads/volunteers/" + filename);
        file.transferTo( new File(String.valueOf(dirUpload)));
    }

    public void saveFile(MultipartFile file, String filename, Posts post) throws IOException {
        post.setImage(filename);
        postsRepository.save(post);
        File dirUpload = new File(System.getProperty("user.dir") + "/uploads/posts/" + filename);
        file.transferTo( new File(String.valueOf(dirUpload)));
    }

    public void saveFile(MultipartFile file, String filename, SuggestedPosts post) throws IOException {
        post.setImage(filename);
        suggestedPostsRepository.save(post);
        File dirUpload = new File(System.getProperty("user.dir") + "/uploads/suggested_posts/" + filename);
        file.transferTo( new File(String.valueOf(dirUpload)));
    }

    public void saveFile(MultipartFile file, String filename, ProjectInfo projectInfo) throws IOException {
        if (isIcoFile(filename)) {
            projectInfo.setLogo(filename);
        }   else {
            projectInfo.setImage(filename);
        }
        projectInfoRepository.save(projectInfo);
        File dirUpload = new File(System.getProperty("user.dir") + "/uploads/project_info/" + filename);
        file.transferTo( new File(String.valueOf(dirUpload)));
    }

    public void transferSuggestedImageToDirPosts(String filename) throws IOException {
        File dirSuggestedPosts = new File(UPLOAD_PATH + "suggested_posts/" + filename);
        File dirPosts = new File(UPLOAD_PATH + "posts/" + filename);
        Files.copy(Paths.get(String.valueOf(dirSuggestedPosts)), Paths.get(String.valueOf(dirPosts)));
    }

    public String createFilenameWithUUID(String filename)    {
        String uuidfile = UUID.randomUUID().toString();
        String filenameWithUUID = uuidfile + "_" + filename;
        return filenameWithUUID;
    }
}
