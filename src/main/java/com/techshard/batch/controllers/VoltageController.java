package com.techshard.batch.controllers;


import com.techshard.batch.exceptions.ConflictException;
import com.techshard.batch.services.VoltageService;
import com.techshard.batch.utils.CustomResponseCode;
import com.techshard.batch.utils.Response;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;


@RestController
@RequestMapping(value = "/api")
public class VoltageController {

//    @Autowired
//    JobLauncher jobLauncher;
//
//    @Autowired
//    Job job;

    @Autowired
    VoltageService voltageService;






    @RequestMapping(value = "/upload", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public ResponseEntity<?> fileUpload(@RequestParam("file") MultipartFile file) throws IOException {


        HttpStatus httpCode = HttpStatus.INTERNAL_SERVER_ERROR;
        Response resp = new Response();
        if (file.isEmpty()) {
            throw new ConflictException(CustomResponseCode.NO_CONTENT, " failed to upload");
        }
         voltageService.fileToDir(file);

        resp.setCode(CustomResponseCode.SUCCESS);
        resp.setDescription("Upload successful");

        return new ResponseEntity<>(resp, HttpStatus.ACCEPTED);
    }













//    @RequestMapping(value = "/upload", method = RequestMethod.POST,
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//
//    public ResponseEntity<?> fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
//
//
//
//        String customizeFileName = "";
//        String fileName;
//        fileName = file.getOriginalFilename();
//        createDirectory(Paths.get(externalResourceLocation ).toAbsolutePath().toString());
//        // fullDate = ft.format(dNow);
//        customizeFileName = Paths.get(externalResourceLocation  + "/"  + fileName).toAbsolutePath().toString();
//        byte[] bytes = file.getBytes();
//
//        HttpStatus httpCode = HttpStatus.INTERNAL_SERVER_ERROR;
//        Response resp = new Response();
//        if (file.isEmpty()) {
//            throw new ConflictException(CustomResponseCode.NO_CONTENT, " failed to upload");
//        }
//
//        // File convertFile = new File("/var/tmp/"+file.getOriginalFilename());
//        File convertFile = new File(customizeFileName);
//        convertFile.createNewFile();
//        FileOutputStream fout = new FileOutputStream(convertFile);
//        fout.write(file.getBytes());
//        fout.close();
//
//        resp.setCode(CustomResponseCode.SUCCESS);
//        resp.setDescription("Upload successful");
//
//        return new ResponseEntity<>(resp, HttpStatus.ACCEPTED);
//    }








}
