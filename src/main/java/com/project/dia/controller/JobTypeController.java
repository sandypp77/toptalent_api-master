package com.project.dia.controller;

import com.project.dia.dto.JobDTO;
import com.project.dia.dto.JobTypeDTO;
import com.project.dia.response.DataResponse;
import com.project.dia.response.HandlerResponse;
import com.project.dia.service.JobService;
import com.project.dia.service.JobTypeService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
@RequestMapping("/api/v1/job/type")
public class JobTypeController {
    private JobTypeService jobTypeService;
    public JobTypeController(JobTypeService jobTypeService){
        this.jobTypeService = jobTypeService;
    }

    //testing
    @GetMapping("/hello")
    public String hello(){
        return "ini adalah api untuk job type";
    }

    //add job type
    @PostMapping("/add")
    public void addJobType(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam("jobType") String jobType){
        JobTypeDTO jobTypeDTO = jobTypeService.addJobType(jobType);

        if (jobTypeDTO != null){
            DataResponse<JobTypeDTO> dataResponse = new DataResponse<>();
            dataResponse.setData(jobTypeDTO);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        }else{
            HandlerResponse.responseBadRequest(response, "001", "Failed to add job type");
        }

    }

    //get list job type
    @GetMapping("/list")
    public void listJobType(HttpServletRequest request, HttpServletResponse response) {
        List<JobTypeDTO> jobTypeDTO = jobTypeService.listJobType();
        if (jobTypeDTO != null){
            DataResponse<List<JobTypeDTO>> dataResponse = new DataResponse<>();
            dataResponse.setData(jobTypeDTO);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        }else {
            HandlerResponse.responseBadRequest(response, "001", "Failed to load job type");
        }
    }
}
