package com.project.dia.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.dia.dto.*;
import com.project.dia.model.SkillsModel;
import com.project.dia.response.DataResponse;
import com.project.dia.response.HandlerResponse;
import com.project.dia.service.JobseekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.List;

@ControllerAdvice
@RestController
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
@RequestMapping("/api/v1/jobseeker")
public class JobseekerController {

    @Autowired
    private JobseekerService jobseekerService;


    //get jobseeker by id
    @GetMapping("/user")
    public void getJobseekerById(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam("jobseekerId") int jobseekerId) {
        final var jobseekerById = jobseekerService.getJobseekerById(jobseekerId);
        if (jobseekerById != null) {
            DataResponse<JobseekerDetailDTO> dataResponse = new DataResponse<>();
            dataResponse.setData(jobseekerById);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "01", "Jobseeker not found");
        }
    }

    // get all user jobseeker
    @GetMapping("/users")
    public List<JobseekerDTO> getAllJobseeker() {
        return jobseekerService.getAllJobseeker();
    }

    // get all jobs with pagination data
    @GetMapping("/pagination")
    public Page<PostJobDTO> getPaginationData(@RequestParam("page") int page, @RequestParam("size") int size) {
        return jobseekerService.getAllJobsWithPagination(page, size);
    }

    // get recent job limit 5 descending
    @GetMapping("/recent")
    public void getRecentJob(HttpServletRequest request, HttpServletResponse response) {
        List<PostJobDTO> jobRecent = jobseekerService.getRecentJob();
        if (jobRecent != null) {
            DataResponse<List<PostJobDTO>> dataResponse = new DataResponse<>();
            dataResponse.setData(jobRecent);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "05", "Job not found");
        }
    }


    // get apply job status pagination
    @GetMapping("/apply/{jobseekerId}")
    public Page<ApplyDTO> getApplicants(@PathVariable("jobseekerId") int jobseekerId,
                                        @RequestParam("page") int page,
                                        @RequestParam("size") int size) {

        return jobseekerService.getApplyJobStatus(jobseekerId, page, size);
    }


    // get job detail by id
    @GetMapping("/job")
    public void getJobById(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam("jobId") int jobId,
                           @RequestParam("jobseekerId") int jobseekerId) {
        JobDetailsResponseDTO jobById = jobseekerService.getJobById(jobId, jobseekerId);
        if (jobById.getCode() == 1) {
            HandlerResponse.responseBadRequest(response, "01", "Job not found");
        } else if (jobById.getCode() == 2) {
            DataResponse<JobDetailsDTO> dataResponse = new DataResponse<>();
            dataResponse.setData(jobById.getJobDetails());
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            DataResponse<JobDetailsWithStatusDTO> dataResponse = new DataResponse<>();
            dataResponse.setData(jobById.getJobDetailsWithStatus());
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        }
    }

    // get post job data by id
    @GetMapping("/post/{jobid}")
    public void getPostJobById(HttpServletRequest request, HttpServletResponse response,
                               @PathVariable("jobid") int jobid) {
        PostJobDTO jobById = jobseekerService.getPostJobById(jobid);
        if (jobById != null) {
            DataResponse<PostJobDTO> dataResponse = new DataResponse<>();
            dataResponse.setData(jobById);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "05", "Job not found");
        }
    }

    // Register user
    @PostMapping("/register")
    public void createUser(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam("jobseekerName") String jobseekerName,
                           @RequestParam("jobseekerEmail") String jobseekerEmail,
                           @RequestParam("jobseekerPassword") String jobseekerPassword,
                           Model model) {
        String name = jobseekerName;
        model.addAttribute("name", name);
        HandlerRegisterDTO user = jobseekerService.createUser(jobseekerName, jobseekerEmail, jobseekerPassword);
        switch (user.getErrorCode()) {
            case "00":
                DataResponse<RegisterJobseekerDTO> dataResponse = new DataResponse<>();
                dataResponse.setData(user.getRegisterJobseekerDTO());
                HandlerResponse.responseSuccessWithData(response, dataResponse);
                break;
            case "01":
                HandlerResponse.responseBadRequest(response, "01", "Please fill all field");
                break;
            case "02":
                HandlerResponse.responseBadRequest(response, "02", "Password must be at least 8 characters and contain at least one number, one lowercase and one uppercase letter and one special character");
                break;
            case "03":
                HandlerResponse.responseBadRequest(response, "03", "Email already exist");
                break;
            default:
                HandlerResponse.responseBadRequest(response, "04", "Error");
        }
    }

    //Login User
    @PostMapping("/login")
    public void loginUser(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam("jobseekerEmail") String jobseekerEmail,
                          @RequestParam("jobseekerPassword") String jobseekerPassword) {
        HandlerAuthJobSeekerDTO loginJobseeker = jobseekerService.loginUser(jobseekerEmail, jobseekerPassword);
        switch (loginJobseeker.getErrorCode()) {
            case "05":
                DataResponse<HandlerAuthJobSeekerDTO> dataResponse = new DataResponse<>();
                dataResponse.setData(loginJobseeker);
                HandlerResponse.responseSuccessWithData(response, dataResponse);
                break;
            case "06":
                HandlerResponse.responseBadRequest(response, "06", "Your email is not verified");
                break;
            case "07":
                HandlerResponse.responseBadRequest(response, "07", "Your email or password invalid");
                break;
            case "013":
                HandlerResponse.responseBadRequest(response, "019", "Email not found");
                break;
            default:
                HandlerResponse.responseBadRequest(response, "08", "Error");
        }
//        if(user != null){
//            DataResponse<RegisterJobseekerDTO> dataResponse = new DataResponse<>();
//            dataResponse.setData(user);
//            HandlerResponse.responseSuccessWithData(response, dataResponse);
//        }else {
//            HandlerResponse.responseBadRequest(response, "02", "user not found");
//        }
    }

    // Update resume
    @PostMapping("/user/update/resume")
    public void updateResume(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam("jobseekerId") int jobseekerId,
                             @RequestParam("jobseekerResume") MultipartFile jobseekerResume) {
        JobseekerDTO jobseekerDTO = jobseekerService.updateResume(jobseekerId, jobseekerResume);
        if (jobseekerDTO != null) {
            DataResponse<JobseekerDTO> dataResponse = new DataResponse<>();
            dataResponse.setData(jobseekerDTO);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "02", "Update resume failed");
        }
    }

    @GetMapping("/resume/get")
    public void getResume(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam("jobseekerId") int jobseekerId) throws MalformedURLException {
        Resource resource = jobseekerService.getResume(jobseekerId);
        if (resource != null) {
            DataResponse<Resource> dataResponse = new DataResponse<>();
            dataResponse.setData(resource);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "02", "Update resume failed");
        }
    }

    // Update Image
    @PostMapping("/user/update/image")
    public void updatePhotoProfile(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam("jobseekerId") int jobseekerId,
                                   @RequestParam("jobseekerImage") MultipartFile jobseekerImage) {
        JobseekerDTO jobseekerDTO = jobseekerService.updatePhotoProfile(jobseekerId, jobseekerImage);
        if (jobseekerDTO != null) {
            DataResponse<JobseekerDTO> dataResponse = new DataResponse<>();
            dataResponse.setData(jobseekerDTO);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "03", "update failure");
        }
    }

    // Update Data
    @PatchMapping("/user/update")
    public void updateJobseeker(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam("jobseekerId") int jobseekerId,
                                @RequestParam("jobseekerName") String jobseekerName,
                                @RequestParam("jobseekerEmail") String jobseekerEmail,
                                @RequestParam("jobseekerEducation") String jobseekerEducation,
                                @RequestParam("jobseekerAddress") String jobseekerAddress,
                                @RequestParam("jobseekerPhone") String jobseekerPhone,
                                @RequestParam("jobseekerAbout") String jobseekerAbout,
                                @RequestParam("jobseekerDateOfBirth") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate jobseekerDateOfBirth,
                                @RequestParam("jobseekerProfession") String jobseekerProfession,
                                @RequestParam("jobseekerPortfolio") String jobseekerPortfolio,
                                @RequestParam("jobseekerMedsos") String jobseekerMedsos,
                                @RequestParam("jobseekerSkill") String jobseekerSkill,
                                @RequestParam("jobseekerCompany") String jobseekerCompany,
                                @RequestParam(value = "workStartYear", defaultValue = "0") int workStartYear,
                                @RequestParam(value = "workEndYear", defaultValue = "0") int workEndYear) {
        JobseekerDetailDTO updateJob = jobseekerService.
                updateJobseeker(jobseekerId, jobseekerName,
                        jobseekerEmail, jobseekerEducation,
                        jobseekerAddress, jobseekerPhone, jobseekerAbout, jobseekerDateOfBirth,
                        jobseekerProfession, jobseekerPortfolio, jobseekerMedsos, jobseekerSkill, jobseekerCompany, workStartYear, workEndYear);
        if (updateJob != null) {
            DataResponse<JobseekerDetailDTO> dataResponse = new DataResponse<>();
            dataResponse.setData(updateJob);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "03", "update failure");
        }
    }

    // Apply Job
    @PutMapping("/job/apply")
    public void applyJob(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam("jobId") int jobId,
                         @RequestParam("jobseekerId") int jobseekerId) {

        int applicationModel = jobseekerService.applyJob(jobId, jobseekerId);
        if (applicationModel == 0) {
            HandlerResponse.responseSuccessOK(response, "Successfully applied");
        } else if (applicationModel == 1) {
            HandlerResponse.responseBadRequest(response, "01", "Data not found");
        } else {
            HandlerResponse.responseBadRequest(response, "02", "You have already applied this job");
        }
    }

    //controller to verify jwt token
    @GetMapping("/activate")
    public ModelAndView verifyToken(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam("token") String token) {
        boolean verify = jobseekerService.verifyToken(token);
        ModelAndView modelAndView;

        if (verify) {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            String email = jwt.getClaim("email").asString();
            modelAndView = new ModelAndView("verified");
            modelAndView.addObject("email", email);
        } else {
            modelAndView = new ModelAndView("expired");
            modelAndView.addObject("message", "Please resend your email to activate your account");
        }
        return modelAndView;
    }

    //controller to resendtoken to jobseeker
    @PostMapping("/resend")
    public void resendToken(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam("jobseekerEmail") String jobseekerEmail) {
        String token = jobseekerService.resendToken(jobseekerEmail);
        if (token != null) {
            DataResponse<String> dataResponse = new DataResponse<>();
            dataResponse.setData(token);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "03", "Resend Failure");
        }
    }

    //controller to search job by keyword
    @GetMapping("/search")
    public void searchJob(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam("keyword") String keyword) {
        List<PostJobDTO> jobList = jobseekerService.searchJobByKeyword(keyword);
        if (jobList != null) {
            DataResponse<List<PostJobDTO>> dataResponse = new DataResponse<>();
            dataResponse.setData(jobList);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "01", "Data not found");
        }
    }

    //controller to search job by keyword
    @GetMapping("/search-count")
    public void searchJobCount(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam("keyword") String keyword) {
        int jobList = jobseekerService.searchJobByKeywordCount(keyword);
        if (jobList != 0) {
            DataResponse<Integer> dataResponse = new DataResponse<>();
            dataResponse.setData(jobList);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "01", "Data not found");
        }
    }

    //send reset password email link
    @PostMapping("/reset")
    public void resetPassword(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam("jobseekerEmail") String jobseekerEmail) {
        String data = jobseekerService.resetPassword(jobseekerEmail);
        if (data != null) {
            DataResponse<String> dataResponse = new DataResponse<>();
            dataResponse.setData(data);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else if (data == "Not Found") {
            HandlerResponse.responseBadRequest(response, "01", "Data not found");
        } else {
            HandlerResponse.responseBadRequest(response, "02", "Reset password failed");
        }
    }

    // verify verifyTokenPassword controller
    @GetMapping("/verify")
    public ModelAndView verifyTokenPassword(HttpServletRequest request, HttpServletResponse response,
                                            @RequestParam("token") String token) throws Exception {
        boolean verify = jobseekerService.verifyTokenPassword(token);
        ModelAndView modelAndView;
        if (verify) {
            response.sendRedirect("http://54.251.83.205/toptalent/#/change-password/" + token);
        } else {
            modelAndView = new ModelAndView("expired");
            modelAndView.addObject("message", "Please resend your email to reset your password");
        }
        return new ModelAndView("expired");
    }

    //controller to change password
    @PostMapping("/change-password")
    public void changePassword(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam("confirmPassword") String confirmPassword) {
        final var updatePassword = jobseekerService.updatePassword(email, password, confirmPassword);
        switch (updatePassword) {
            case 0:
                HandlerResponse.responseSuccessOK(response, "Password has been changed");
                break;
            case 1:
                HandlerResponse.responseBadRequest(response, "01", "Data not found");
                break;
            case 2:
                HandlerResponse.responseBadRequest(response, "02", "Password must be at least 8 characters and contain at least one number, one uppercase and one lowercase letter");
                break;
            case 3:
                HandlerResponse.responseBadRequest(response, "03", "Password and confirm password must be the same");
                break;
            case 4:
                HandlerResponse.responseBadRequest(response, "04", "Please fill in all the fields");
                break;
            default:
                HandlerResponse.responseBadRequest(response, "05", "Change password failed");
        }
    }

    @GetMapping("/get-skill")
    public void getSkill(HttpServletRequest request, HttpServletResponse response) {
        List<SkillsModel> skillList = jobseekerService.getAllSkills();
        DataResponse<List<SkillsModel>> dataResponse = new DataResponse<>();
        dataResponse.setData(skillList);
        HandlerResponse.responseSuccessWithData(response, dataResponse);
    }

    @GetMapping("/detail/get-skill")
    public void getSkillById(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam("jobseekerId") int jobseekerId) {
        List<SkillNameDTO> skillList = jobseekerService.getAllSkillsById(jobseekerId);
        DataResponse<List<SkillNameDTO>> dataResponse = new DataResponse<>();
        dataResponse.setData(skillList);
        HandlerResponse.responseSuccessWithData(response, dataResponse);
    }

    //add jobseeker skill
    @PostMapping("/add/skill")
    public void addJobseekerSkill(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam("jobseekerId") int jobseekerId,
                                  @RequestParam("skillId") int skillId) {
        JobseekerSkillDTO jobseekerSkillDTO = jobseekerService.addJobseekerSkill(jobseekerId, skillId);

        if (jobseekerSkillDTO != null) {
            DataResponse<JobseekerSkillDTO> dataResponse = new DataResponse<>();
            dataResponse.setData(jobseekerSkillDTO);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "001", "Failed to add jobseeker skill");
        }

    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public void handleMaxUploadSizeExceededException(HttpServletResponse response) throws IOException {
        HandlerResponse.responseBadRequest(response, "01", "File size is too large, max file size is 5MB");
    }

    //send reset password email link
    @PostMapping("/detail/salary")
    public void addJobseekerDetailSalary(HttpServletRequest request, HttpServletResponse response,
                                         @RequestParam("jobseekerId") int jobseekerId,
                                         @RequestParam("currentCurrency") String currentCurrency,
                                         @RequestParam("expectedCurrency") String expectedCurrency,
                                         @RequestParam("expectedMinimum") float expectedMinimum,
                                         @RequestParam("expectedMaximum") float expectedMaximum,
                                         @RequestParam("currentSalary") float currentSalary) {
        JobseekerSalaryDTO jobseekerSalaryDTO = jobseekerService.addJobseekerDetailSalary(jobseekerId, currentCurrency, expectedCurrency, expectedMinimum, expectedMaximum, currentSalary);
        if (jobseekerSalaryDTO != null) {
            DataResponse<JobseekerSalaryDTO> dataResponse = new DataResponse<>();
            dataResponse.setData(jobseekerSalaryDTO);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "001", "Failed to add jobseeker salary");
        }

    }


}
