package com.techshard.batch.services;

import com.techshard.batch.utils.Response;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class VoltageService {

    @Value("${uploads.dir}")
    String externalResourceLocation;


    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;



    public void createDirectory(String fileName) {

        File file = new File(fileName);

        //Creating the directory
        boolean bool = file.mkdir();
        if(bool){
            System.out.println("Directory created successfully");
        }else{
            System.out.println("Sorry couldn’t create specified directory");
        }

    }





    public ResponseEntity<?> fileToDir (MultipartFile file) throws IOException {
        HttpStatus httpCode = HttpStatus.INTERNAL_SERVER_ERROR;
        Response resp = new Response();

        String customizeFileName = "";
        String fileName;
        fileName = file.getOriginalFilename();
        createDirectory(Paths.get(externalResourceLocation ).toAbsolutePath().toString());
        // fullDate = ft.format(dNow);
        customizeFileName = Paths.get(externalResourceLocation  + "/"  + fileName).toAbsolutePath().toString();
        byte[] bytes = file.getBytes();


        File convertFile = new File(customizeFileName);
        convertFile.createNewFile();
        FileOutputStream fout = new FileOutputStream(convertFile);
        fout.write(file.getBytes());
        fout.close();

        return new ResponseEntity<>(resp, HttpStatus.ACCEPTED);
    }


    // batch job will run every one minute after application is started.
    @Scheduled(cron = "0 */1 * * * ?")
    public void perform() throws Exception
    {
        System.out.println(":::::: RUNNING SCHEDULE ::::::");
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job, params);
    }


}
