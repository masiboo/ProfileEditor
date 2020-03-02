package com.spark.profile_editor.service;

import com.spark.profile_editor.model.Profile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProfileService {

    void saveOrUpdateExpense(Profile profile);

    void deleteProfile(long id);

    void deleteAll();

    List<Profile> findAll();

    Profile findById(long id);

    String storeFile(MultipartFile file, String path);
}
