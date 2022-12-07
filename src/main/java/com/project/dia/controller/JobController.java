package com.project.dia.controller;

import com.project.dia.dto.JobDTO;
import com.project.dia.dto.JobDetailsDTO;
import com.project.dia.dto.JobDetailsWithStatusCountDTO;
import com.project.dia.model.JobModel;
import com.project.dia.response.DataResponse;
import com.project.dia.response.HandlerResponse;
import com.project.dia.service.JobService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
@RequestMapping("/api/v1")
public class JobController {
    private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    //testing
    @GetMapping("/hello")
    public String hello() {
        return "ini adalah api untuk job";
    }

    //get job by name
    @GetMapping("/job/name/{jobName}")
    public JobModel getJobByName(@PathVariable("jobName") String jobName) {
        JobModel jobByName = jobService.getJobByName(jobName);
        if (jobByName != null) {
            return jobByName;
        } else {
            return new JobModel();
        }
    }

    @GetMapping("/job/list/name")
    public List<JobModel> getListJobName() {
        return jobService.jobNameList();
    }

    //get job by id
    @GetMapping("/job/{jobId}")
    public void getJobById(HttpServletRequest request, HttpServletResponse response,
                           @PathVariable("jobId") int jobId) {
        JobDTO jobById = jobService.getJobById(jobId);

        if (jobById != null) {
            DataResponse<JobDTO> dataResponse = new DataResponse<>();
            dataResponse.setData(jobById);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "004", "Data not found");
        }
    }


    //get all jobs
    @GetMapping("/jobs/{recruiterId}")
    public List<JobDTO> getAllJobs(@PathVariable("recruiterId") int recruiterId) {
        return jobService.findAllJobs(recruiterId);
    }

    //cara 2
    @GetMapping("/jobs2")
    public List<JobDTO> findJobs() {
        return jobService.findJobs();
    }

    //get all jobs by recruiter id and status
    @GetMapping("/jobs/status")
    public void findJobsByIdAndStatus(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam("recruiterId") int recruiterId,
                                      @RequestParam("jobStatus") String jobStatus) {
        List<JobDetailsWithStatusCountDTO> jobModels = jobService.findJobsByIdAndStatus(recruiterId, jobStatus);
        if (jobModels != null) {
            DataResponse<List<JobDetailsWithStatusCountDTO>> dataResponse = new DataResponse<>();
            dataResponse.setData(jobModels);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "003", "Data not found");
        }
    }

    //get job detail
    @GetMapping("/jobs/detail")
    public void findJobDetailed(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam("jobId") int jobId,
                                      @RequestParam("jobStatus") String jobStatus) {
        JobDetailsDTO jobModels = jobService.findJobDetailed(jobId, jobStatus);
        if (jobModels != null) {
            DataResponse<JobDetailsDTO> dataResponse = new DataResponse<>();
            dataResponse.setData(jobModels);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "003", "Data not found");
        }
    }

    //create job
    @PostMapping("/job/create")
    public void createjob(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam("jobName") String jobName,
                          @RequestParam("recruiterId") int recruiterId,
                          @RequestParam("jobSalary") int jobSalary,
                          @RequestParam("jobPosition") String jobPosition,
                          @RequestParam("jobAddress") String jobAddress,
                          @RequestParam("jobDesc") String jobDesc,
                          @RequestParam("jobRequirement") String jobRequirement) {


        JobDTO job = jobService.createJob(jobName, jobSalary, jobPosition, jobAddress, jobDesc, jobRequirement, recruiterId);

        if (job != null) {
            DataResponse<JobDTO> dataResponse = new DataResponse<>();
            dataResponse.setData(job);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "001", "Failed to create job");
        }

    }

    //update job
    @PatchMapping("/job/{jobId}")
    public void updateJob(HttpServletRequest request, HttpServletResponse response,
                          @PathVariable("jobId") int jobId,
                          @RequestParam("jobName") String jobName,
                          @RequestParam("jobSalary") int jobSalary,
                          @RequestParam("jobPosition") String jobPosition,
                          @RequestParam("jobAddress") String jobAddress,
                          @RequestParam("jobDesc") String jobDesc,
                          @RequestParam("jobRequirement") String jobRequirement,
                          @RequestParam("jobStatus") String jobStatus) {
        JobDTO updateJob = jobService.updateJob(jobId, jobName, jobSalary, jobPosition, jobAddress, jobDesc, jobRequirement, jobStatus);
        if (updateJob != null) {
            DataResponse<JobDTO> dataResponse = new DataResponse<>();
            dataResponse.setData(updateJob);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "002", "update failed");
        }
    }

    // UPDATE JOB STATUS
    @PatchMapping("/job/status")
    public void updateJobStatus(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam("jobId") int jobId,
                                @RequestParam("jobStatus") String jobStatus) {
        JobDTO updateJobStatus = jobService.updateJobStatus(jobId, jobStatus);
        if (updateJobStatus != null) {
            DataResponse<JobDTO> dataResponse = new DataResponse<>();
            dataResponse.setData(updateJobStatus);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "003", "Update failed");
        }
    }

    //delete job
    @PutMapping("/job/delete/{jobId}")
    public void deleteJob(HttpServletRequest request, HttpServletResponse response,
                          @PathVariable("jobId") int jobId) {
        JobDTO deleteJob = jobService.deleteJob(jobId);

        DataResponse<JobDTO> dataResponse = new DataResponse<>();
        dataResponse.setData(deleteJob);
        HandlerResponse.responseSuccessWithData(response, dataResponse);

    }

    //get jumlah job post based recruiter id
    @GetMapping("/job/total/{recruiterId}")
    public void total(HttpServletRequest request, HttpServletResponse response,
                      @PathVariable("recruiterId") int recruiterId) {
        int result = jobService.totalPosts(recruiterId);

        DataResponse<Integer> dataResponse = new DataResponse<>();
        dataResponse.setData(result);
        if (result != 0) {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        }
    }

    //get count job where status = visible based on recruiterId
    @GetMapping("/job/visible/{recruiterId}")
    public void totalVisible(HttpServletRequest request, HttpServletResponse response,
                             @PathVariable("recruiterId") int recruiterId) {
        int result = jobService.totalVisible(recruiterId);

        DataResponse<Integer> dataResponse = new DataResponse<>();
        dataResponse.setData(result);
        if (result != 0) {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        }
    }

    //get count job where status = hidden based on recruiterId
    @GetMapping("/job/hidden/{recruiterId}")
    public void totalHidden(HttpServletRequest request, HttpServletResponse response,
                            @PathVariable("recruiterId") int recruiterId) {
        int result = jobService.totalHidden(recruiterId);

        DataResponse<Integer> dataResponse = new DataResponse<>();
        dataResponse.setData(result);

        if (result != 0) {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        }
    }



}
