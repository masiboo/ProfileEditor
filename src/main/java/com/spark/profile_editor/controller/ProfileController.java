package com.spark.profile_editor.controller;

import com.spark.profile_editor.model.Profile;
import com.spark.profile_editor.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ProfileController {

    @Autowired
    ProfileService profileService;

    private String picturePath = null;

    @GetMapping(path = "/profile")
    public ResponseEntity<?> findById(@RequestParam(required=true) long id) {
        Profile profile = profileService.findById(id);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @GetMapping(path = "/all/profile")
    public ResponseEntity<?> getAll() {
        List<Profile> result = profileService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(path = "/photo" , consumes = {"multipart/form-data"})
    public void saveOrUpdatePhoto(@RequestParam("file") MultipartFile file, String path) {
        // I will save the file here.
        System.out.println("file name:" + file.getOriginalFilename());
        if(file != null){
            picturePath = profileService.storeFile(file, path);
        }
    }

/*
    @PostMapping(path = "/test2")
    public ResponseEntity<?> test2(@RequestBody Profile profile) {
        // I will save the file here.
        //System.out.println("file name:"+file.getOriginalFilename());
        profile.setPicturePath(picturePath);
        profileService.saveOrUpdateExpense(profile);
        return new ResponseEntity<>("Expense added succcessfully", HttpStatus.OK);
    }
*/

    @PostMapping(path = "/profile/{id}")
    public ResponseEntity<?> addOrUpdateProfile(@RequestBody Profile profile,
                                                @PathVariable("id") long id) {
        profile.setPicturePath(picturePath);
        profileService.saveOrUpdateExpense(profile);
        picturePath = null;
        return new ResponseEntity<>("Profile added successfully", HttpStatus.OK);
    }

    @PostMapping(path = "/profile")
    public ResponseEntity<?> addOrUpdateProfileById(@RequestBody Profile profile) {
        profile.setPicturePath(picturePath);
        profileService.saveOrUpdateExpense(profile);
        picturePath = null;
        return new ResponseEntity<>("Profile submission successfully", HttpStatus.OK);
    }

/*    @PostMapping(path = "/profile" , consumes = {"multipart/form-data"})
    public ResponseEntity<?> addOrUpdateProfile(@RequestPart("profile") Profile profile,  @RequestParam("file") MultipartFile file) {
        System.out.println("file name:"+file.getOriginalFilename()+" file is here. Save it");
        profileService.saveOrUpdateExpense(profile);
        return new ResponseEntity<>("Profile added succcessfully", HttpStatus.OK);
    }*/

    @DeleteMapping(path = "/profile")
    public void deleteProfile(@RequestParam("id") long id) {
        profileService.deleteProfile(id);
    }

    @DeleteMapping(path = "/profile/deleteAll")
    public void deleteAllProfile() {
        profileService.deleteAll();
    }

}
