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
public class ProfileController2 {

    @Autowired
    ProfileService profileService;

    @GetMapping(path = "/profile2")
    public ResponseEntity<?> findById(@RequestParam(required=true) long id) {
        Profile profile = profileService.findById(id);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @GetMapping(path = "/profile/all2")
    public ResponseEntity<?> getAll() {
        List<Profile> result = profileService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(path = "/test2" , consumes = {"multipart/form-data"})
    public ResponseEntity<?> test(@RequestParam("file") MultipartFile file) {
        // I will save the file here.
        System.out.println("file name:"+file.getOriginalFilename());
        //profileService.saveOrUpdateExpense(profile);
        return new ResponseEntity<>("Expense added succcessfully", HttpStatus.OK);
    }

    @PostMapping(path = "/do2")
    public ResponseEntity<?> doPost(@RequestBody Profile profile){
        profileService.saveOrUpdateExpense(profile);
        return new ResponseEntity<>("Profile added succcessfully", HttpStatus.OK);
    }


     @PostMapping(path = "/profile2" , consumes = {"multipart/form-data"})
     public ResponseEntity<?> addOrUpdateProfile(@RequestBody Profile profile,
                                                @RequestParam("file") MultipartFile file) {
        System.out.println("file name:"+file.getOriginalFilename()+" file is here. Save it");
        profileService.saveOrUpdateExpense(profile);
        return new ResponseEntity<>("Expense added succcessfully", HttpStatus.OK);
    }

    @DeleteMapping(path = "/profile2")
    public void deleteProfile(@RequestParam("id") long id) {
        profileService.deleteProfile(id);
    }

    @DeleteMapping(path = "/profile/deleteAll2")
    public void deleteAllProfile() {
        profileService.deleteAll();
    }

}
