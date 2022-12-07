package com.project.dia.controller;

import com.project.dia.dto.JobFunctionDTO;
import com.project.dia.dto.JobTypeDTO;
import com.project.dia.response.DataResponse;
import com.project.dia.response.HandlerResponse;
import com.project.dia.service.JobFunctionService;
import com.project.dia.service.JobTypeService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
@RequestMapping("/api/v1/job/function")
public class JobFunctionController {
    private JobFunctionService jobFunctionService;
    public JobFunctionController(JobFunctionService jobFunctionService){
        this.jobFunctionService = jobFunctionService;
    }

    //testing
    @GetMapping("/hello")
    public String hello(){
        return "ini adalah api untuk job Function";
    }

    //add job type
    @PostMapping("/add")
    public void addJobType(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam("jobFunction") String jobFunction){
        JobFunctionDTO jobFunctionDTO = jobFunctionService.addJobFunction(jobFunction);

        if (jobFunctionDTO != null){
            DataResponse<JobFunctionDTO> dataResponse = new DataResponse<>();
            dataResponse.setData(jobFunctionDTO);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        }else{
            HandlerResponse.responseBadRequest(response, "001", "Failed to add job type");
        }

    }

    //get list job type
    @GetMapping("/list")
    public void listJobType(HttpServletRequest request, HttpServletResponse response) {
        List<JobFunctionDTO> jobFunctionDTO = jobFunctionService.listJobFunction();
        if (jobFunctionDTO != null){
            DataResponse<List<JobFunctionDTO>> dataResponse = new DataResponse<>();
            dataResponse.setData(jobFunctionDTO);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        }else {
            HandlerResponse.responseBadRequest(response, "001", "Failed to load job Function");
        }
    }
}
