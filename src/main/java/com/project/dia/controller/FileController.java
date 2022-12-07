package com.project.dia.controller;

import com.project.dia.response.DataResponse;
import com.project.dia.response.HandlerResponse;
import com.project.dia.service.RecruiterFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
@RequestMapping("/api/v1/file")
public class FileController {
    @Autowired
    private RecruiterFileService recruiterFileService;

    //upload photo
    @PostMapping("/upload")
    public boolean uploadFile(@RequestParam("file")MultipartFile file){
        return recruiterFileService.saveFile(file);
    }

    //update photo profile
    @PostMapping("/recruiter/photo")
    public void updatePhoto(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam("recruiterId") int recruiterId,
                            @RequestParam("file")MultipartFile file){
        String originalFilename = recruiterFileService.updatePhoto(recruiterId, file);
        if (originalFilename != null){
            DataResponse<String> dataResponse = new DataResponse<>();
            dataResponse.setData(originalFilename);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        }else {
            HandlerResponse.responseBadRequest(response, "01", "Upload Failed");
        }
//        HandlerResponse.responseSuccessOK(response, originalFilename);

    }
}
