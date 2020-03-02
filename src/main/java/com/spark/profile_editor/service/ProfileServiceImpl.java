package com.spark.profile_editor.service;


import com.spark.profile_editor.exception.FileStorageException;
import com.spark.profile_editor.model.Profile;
import com.spark.profile_editor.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    ProfileRepository profileRepository;

    final String dir = System.getProperty("user.dir");
    @Value("${profilePictureDirectory}")
    private String profilePictureDirectory;
    @Value("${pathSeparator}")
    private String pathSeparator;

    public void saveOrUpdateExpense(Profile profile) {
        profileRepository.save(profile);
    }


    public void deleteProfile(long id) {
        profileRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        profileRepository.deleteAll();
    }

    @Override
    public List<Profile> findAll() {
        return profileRepository.findAll().stream()
                .map( ProfileServiceImpl::removeCityLatLon)
                .collect(Collectors.toList());
    }

    private static Profile removeCityLatLon(Profile profile){
        String[] splitStr = profile.getCityLocation().split("#");
        if(splitStr.length > 0){
            profile.setCityLocation(splitStr[0]);
            return profile;
        }else {
            return profile;
        }
    }

    @Override
    public Profile findById(long id) {
        Optional<Profile> profile = profileRepository.findById(id);
        return profile.orElse(null);
    }

    public String storeFile(MultipartFile file, String path) {
        String imageDir =  dir+pathSeparator+profilePictureDirectory+pathSeparator;
        try {
            Files.createDirectories(Paths.get(imageDir));
        } catch (IOException e) {
            throw new FileStorageException("Image directory does not exist");
        }

        if(!path.isEmpty()){
            try {
                Files.deleteIfExists(Paths.get(imageDir+path));
            } catch (IOException e) {
                // ignore this exception
                System.out.println(e.getMessage());
            }
        }
        if(file != null && file.getOriginalFilename() != null){
            String fileName =  generateTimestampFilename(file.getOriginalFilename());
            if(null == fileName || "" == fileName){
                throw new FileStorageException("file name not found");
            }
            String profilePicturePath = imageDir+fileName;
            try {
                // Copy file to the target location (Replacing existing file with the same name)
                Path targetLocation = Paths.get(profilePicturePath);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                throw new FileStorageException("Could not store file " + file.getOriginalFilename() + ". Please try again!", ex);
            }
            return profilePicturePath;
        }else{
            return null;
        }

    }

    private String generateTimestampFilename(String fileName){
        String timestampFilename = null;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if(null != fileName){
            String[] splitName =  fileName.split("\\.");
            if(splitName.length > 0){
                timestampFilename = splitName[0]+"-"+timestamp.toString()+"."+splitName[splitName.length-1];
            }
        }
        return timestampFilename;
    }



}
