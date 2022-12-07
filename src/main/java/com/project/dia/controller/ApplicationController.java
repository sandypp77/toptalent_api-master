package com.project.dia.controller;

import com.project.dia.dto.*;
import com.project.dia.response.DataResponse;
import com.project.dia.response.HandlerResponse;
import com.project.dia.service.ApplicationService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
@RequestMapping("/api/v1/application")
public class ApplicationController {
    private ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    //testing
    @GetMapping("/hello")
    public String hello() {
        return "ini adalah api untuk application";
    }

    //update application status = accepted
    @PostMapping("/status/accepted")
    public void updateApplicationAccepted(HttpServletRequest request, HttpServletResponse response,
                                          @RequestParam("applicationId") int applicationId) {
        ApplicationDTO updateApplicationStatus = applicationService.updateApplicationAccepted(applicationId);
        if (updateApplicationStatus != null) {
            DataResponse<ApplicationDTO> dataResponse = new DataResponse<>();
            dataResponse.setData(updateApplicationStatus);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "004", "Update failed");
        }
    }

    //update application status = rejected
    @PostMapping("/status/rejected")
    public void updateApplicationRejected(HttpServletRequest request, HttpServletResponse response,
                                          @RequestParam("applicationId") int applicationId) {
        ApplicationDTO updateApplicationStatus = applicationService.updateApplicationRejected(applicationId);
        if (updateApplicationStatus != null) {
            DataResponse<ApplicationDTO> dataResponse = new DataResponse<>();
            dataResponse.setData(updateApplicationStatus);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "005", "Update failed");
        }
    }

    //update application status back to SENT
    @PostMapping("/status/sent")
    public void updateApplicationSent(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam("applicationId") int applicationId) {
        ApplicationDTO updateApplicationStatus = applicationService.updateApplicationSent(applicationId);
        if (updateApplicationStatus != null) {
            DataResponse<ApplicationDTO> dataResponse = new DataResponse<>();
            dataResponse.setData(updateApplicationStatus);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "008", "Update Failed");
        }
    }

    //update application status
    @PostMapping("/status")
    public void updateApplication(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam("applicationId") int applicationId,
                                  @RequestParam("applicationStatus") String applicationStatus) {
        ApplicationDTO updateApplicationStatus = applicationService.updateApplication(applicationId, applicationStatus);
        if (updateApplicationStatus != null) {
            DataResponse<ApplicationDTO> dataResponse = new DataResponse<>();
            dataResponse.setData(updateApplicationStatus);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "005", "Update failed");
        }
    }

    //get all applicants
    @GetMapping("/applicants-all")
    public List<ApplicantDTO> getAllApplicants() {
        return applicationService.getAllApplicants();
    }

    //get data applicant sesuai postingan job
    @GetMapping("/applicants")
    public List<JobseekerDTO> getApplicants() {
        return applicationService.getApplicants();
    }

    @GetMapping("/applicants/{jobId}")
    public List<ApplicantDTO> getApplicants(@PathVariable("jobId") int jobId) {
        return applicationService.getApplicant(jobId);
    }

    // get posting job by id recruiter
    @GetMapping("/applicants/post/{recruiterId}")
    public List<PostJobDTO> getPostingJob(@PathVariable("recruiterId") int recruiterId) {
        return applicationService.getPostingJob(recruiterId);
    }

    //get applicant using pagination
    @GetMapping("/applicants/pagination")
    public Page<ApplicantDTO> getAllApplicants(@RequestParam("jobId") int jobId,
                                               @RequestParam(defaultValue = "0") Integer pageNo,
                                               @RequestParam(defaultValue = "10") Integer pageSize,
                                               @RequestParam(defaultValue = "applicationId") String sortBy) {
        return applicationService.getAllApplicants(jobId, pageNo, pageSize, sortBy);

    }

    //get applicant by id
    @GetMapping("/applicant")
    public void getApplicant(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam("applicationId") int applicationId) {
        ApplicantDTO jobseeker = applicationService.getApplicantById(applicationId);

        if (jobseeker != null) {
            DataResponse<ApplicantDTO> dataResponse = new DataResponse<>();
            dataResponse.setData(jobseeker);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "006", "No data");
        }
    }

    //get applicant for dashboard
    @GetMapping("/dashboard/{recruiterId}")
    public void applicantDashboard(HttpServletRequest request, HttpServletResponse response,
                                   @PathVariable("recruiterId") int recruiterId) {
        List<ApplicantDTO> applicants = applicationService.applicantDashboard(recruiterId);

        if (Objects.nonNull(applicants)) {
            DataResponse<List<ApplicantDTO>> dataResponse = new DataResponse<>();
            dataResponse.setData(applicants);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "007", "No data");
        }
    }

    @GetMapping("/right-sidebar/{recruiterId}")
    public void applicantRightSidebar(HttpServletRequest request, HttpServletResponse response,
                                      @PathVariable("recruiterId") int recruiterId) {
        List<ApplicantDTO> applicants = applicationService.applicantRightSidebar(recruiterId);

        if (Objects.nonNull(applicants)) {
            DataResponse<List<ApplicantDTO>> dataResponse = new DataResponse<>();
            dataResponse.setData(applicants);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "009", "No data");
        }
    }

    //get applicant with applicationStatus=accepted
    @GetMapping("/count-accepted/{recruiterId}")
    public void countAccepted(HttpServletRequest request, HttpServletResponse response,
                              @PathVariable("recruiterId") int recruiterId) {
        int result = applicationService.countAccepted(recruiterId);

        DataResponse<Integer> dataResponse = new DataResponse<>();
        dataResponse.setData(result);
        if (result != 0) {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        }
    }

    //get applicant with applicationStatus=rejected
    @GetMapping("/count-rejected/{recruiterId}")
    public void countRejected(HttpServletRequest request, HttpServletResponse response,
                              @PathVariable("recruiterId") int recruiterId) {
        int result = applicationService.countRejected(recruiterId);

        DataResponse<Integer> dataResponse = new DataResponse<>();
        dataResponse.setData(result);
        if (result != 0) {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        }
    }

    //get total applicant where applicationStatus = sent
    @GetMapping("/count-sent/{recruiterId}")
    public void countSent(HttpServletRequest request, HttpServletResponse response,
                          @PathVariable("recruiterId") int recruiterId) {
        int result = applicationService.countSent(recruiterId);

        DataResponse<Integer> dataResponse = new DataResponse<>();
        dataResponse.setData(result);

        if (result != 0) {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        }
    }

    //get size application by recruiterId
    @GetMapping("/applications/{recruiterId}")
    public void getApplications(HttpServletRequest request, HttpServletResponse response,
                                @PathVariable("recruiterId") int recruiterId) {
        int result = applicationService.allApplications(recruiterId);

        DataResponse<Integer> dataResponse = new DataResponse<>();
        dataResponse.setData(result);
        if (result != 0) {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        }
    }

    //get count new resume
    @GetMapping("/new-resume/{recruiterId}")
    public void getCountNewResume(HttpServletRequest request, HttpServletResponse response,
                                  @PathVariable("recruiterId") int recruiterId) {
        int result = applicationService.countNewResume(recruiterId);

        DataResponse<Integer> dataResponse = new DataResponse<>();
        dataResponse.setData(result);
        if (result != 0) {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        }
    }

    // get count all applicant from every job posting
    @GetMapping("/count-applicants/{jobId}")
    public void getCountApplicants(HttpServletRequest request, HttpServletResponse response,
                                   @PathVariable("jobId") int jobId) {
        int result = applicationService.countApplicants(jobId);

        DataResponse<Integer> dataResponse = new DataResponse<>();
        dataResponse.setData(result);
        if (result != 0) {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        }
    }

    //get count all applicant who applied job status visible
    @GetMapping("/count-visible/{recruiterId}")
    public void countVisible(HttpServletRequest request, HttpServletResponse response,
                             @PathVariable("recruiterId") int recruiterId) {
        int result = applicationService.countAppliedVisible(recruiterId);

        DataResponse<Integer> dataResponse = new DataResponse<>();
        dataResponse.setData(result);
        if (result != 0) {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        }
    }

    //get count all applicant who applied job status visible
    @GetMapping("/count-hidden/{recruiterId}")
    public void countHidden(HttpServletRequest request, HttpServletResponse response,
                            @PathVariable("recruiterId") int recruiterId) {
        int result = applicationService.countAppliedHidden(recruiterId);

        DataResponse<Integer> dataResponse = new DataResponse<>();
        dataResponse.setData(result);
        if (result != 0) {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        }
    }

    //get count how many applicants applied for job status visible
    @GetMapping("/job/count-visible")
    public void countJobsAppliedVisible(HttpServletRequest request, HttpServletResponse response,
                                        @RequestParam("recruiterId") int recruiterId,
                                        @RequestParam("jobId") int jobId) {
        int result = applicationService.countAppliedJobVisible(recruiterId, jobId);

        DataResponse<Integer> dataResponse = new DataResponse<>();
        dataResponse.setData(result);
        if (result != 0) {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        }
    }

    //get count how many applicants applied for job status visible
    @GetMapping("/job/count-hidden")
    public void countJobsAppliedHidden(HttpServletRequest request, HttpServletResponse response,
                                       @RequestParam("recruiterId") int recruiterId,
                                       @RequestParam("jobId") int jobId) {
        int result = applicationService.countAppliedJobHidden(recruiterId, jobId);

        DataResponse<Integer> dataResponse = new DataResponse<>();
        dataResponse.setData(result);
        if (result != 0) {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        }
    }

}
