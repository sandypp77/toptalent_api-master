package com.project.dia.controller;

import com.project.dia.dto.*;
import com.project.dia.response.DataResponse;
import com.project.dia.response.HandlerResponse;
import com.project.dia.service.AuthService;
import com.project.dia.service.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
@RequestMapping("api/v1/auth")
public class RecruiterController {
    private AuthService authService;
    @Autowired
    public RecruiterController(AuthService authService){
        this.authService = authService;
    }

    @GetMapping("/hello")
    public String hello(){
        return "hello project akhir";
    }

    //get user by id
    @GetMapping("/recruiter/{recruiterId}")
    public UpdateProfileDTO getRecruiterById(@PathVariable("recruiterId") int recruiterId){
        return authService.getRecruiterById(recruiterId);
    }

    //register
    @PostMapping("/recruiter/register")
    public void createUser(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam("recruiterEmail")String recruiterEmail,
                           @RequestParam("recruiterPassword")String recruiterPassword,
                           @RequestParam("recruiterCompany")String recruiterCompany,
                           @RequestParam("recruiterIndustry")String recruiterIndustry,
                           Model model){
        String name = recruiterCompany;
        model.addAttribute("name", name);
        HandlerAuthDTO user = authService.createUser(recruiterEmail, recruiterPassword, recruiterCompany, recruiterIndustry);
        switch (user.getErrorCode()){
            case "00":
                DataResponse<RegisterDTO> dataResponse = new DataResponse<>();
                dataResponse.setData(user.getRegisterDTO());
                HandlerResponse.responseSuccessWithData(response, dataResponse);
                break;
            case "01":
                HandlerResponse.responseBadRequest(response, "01", "Please fill all the fields");
                break;
            case "02":
                HandlerResponse.responseBadRequest(response, "02", "Password must be at least 8 characters");
                break;
            case "03":
                HandlerResponse.responseBadRequest(response, "03", "Password must contain at least one number, one capital letter and one special character");
                break;
            case "04":
                HandlerResponse.responseBadRequest(response, "04", "User already exists");
                break;
            default:
                HandlerResponse.responseBadRequest(response, "05", "Error");
        }
    }

    //login
    @PostMapping("/recruiter/login")
    public void login(HttpServletRequest request, HttpServletResponse response,
                                               @RequestParam("recruiterEmail")String recruiterEmail,
                                               @RequestParam("recruiterPassword")String recruiterPassword){
        HandlerAuthDTO login =authService.login(recruiterEmail, recruiterPassword);
        switch (login.getErrorCode()){
            case "05":
                DataResponse<HandlerAuthDTO> dataResponse = new DataResponse<>();
                dataResponse.setData(login);
                HandlerResponse.responseSuccessWithData(response, dataResponse);
                break;
            case "06":
                HandlerResponse.responseBadRequest(response, "06", "Your email is not verified");
                break;
            case "07":
                HandlerResponse.responseBadRequest(response, "07", "Wrong email or password");
                break;
            case "013":
                HandlerResponse.responseBadRequest(response, "019", "Email not found");
                break;
            default:
                HandlerResponse.responseBadRequest(response, "08", "Error");
        }
    }

    //update profile
    @PutMapping("/recruiter/{recruiterId}")
    public void updateProfile(HttpServletRequest request, HttpServletResponse response,
                              @PathVariable("recruiterId") int recruiterId,
                              @RequestParam("recruiterEmail") String recruiterEmail,
                              @RequestParam("recruiterCompany") String recruiterCompany,
                              @RequestParam("recruiterIndustry") String recruiterIndustry,
                              @RequestParam("recruiterPhone") String recruiterPhone,
                              @RequestParam("recruiterStaff") String recruiterStaff,
                              @RequestParam("recruiterDesc") String recruiterDesc,
                              @RequestParam("recruiterAddress") String recruiterAddress,
                              @RequestParam("recruiterFb") String recruiterFb,
                              @RequestParam("recruiterIg") String recruiterIg,
                              @RequestParam("recruiterLinkedin") String recruiterLinkedin,
                              @RequestParam("recruiterCulture") String recruiterCulture,
                              @RequestParam("recruiterBenefit") String recruiterBenefit,
                              @RequestParam("recruiterWebsite") String recruiterWebsite
                              ){
        UpdateProfileDTO updatedRecruiter = authService.updateProfile(recruiterId, recruiterEmail,recruiterCompany,recruiterIndustry,recruiterPhone,
                recruiterStaff,recruiterDesc,recruiterAddress,recruiterFb,recruiterIg,recruiterLinkedin,recruiterCulture,recruiterBenefit,recruiterWebsite);
        if (updatedRecruiter != null){
            DataResponse<UpdateProfileDTO> data = new DataResponse<>();
            data.setData(updatedRecruiter);
            HandlerResponse.responseSuccessWithData(response,data);
        }else{
            HandlerResponse.responseBadRequest(response, "08", "Update failed");
        }
    }

    //verify JWT token register/login
    @GetMapping("/activate")
    public void verifyToken(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam("token") String token) throws IOException {
        boolean verify = authService.verifyToken(token);
        if (verify){

            response.sendRedirect("https://toptalentapp.com/login");
        }else{
            response.sendRedirect("https://toptalentapp.com/expiredsignup");
//            HandlerResponse.responseBadRequest(response, "09", "Failed");
        }
    }

    //resend JWT token
    @PostMapping("/resend")
    public void resendToken(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam("email") String email,
                            @RequestParam("recruiterCompany")String recruiterCompany,
                            Model model){
        String name = recruiterCompany;
        model.addAttribute("name", name);
        String token = authService.resendToken(email, recruiterCompany);

        if (token != null){
            DataResponse<String> dataResponse = new DataResponse<>();
            dataResponse.setData(token);
            HandlerResponse.responseSuccessWithData(response,dataResponse);
        }else {
            HandlerResponse.responseBadRequest(response, "010", "Failed");
        }
    }

    //send reset password email link
    @PostMapping("/reset")
    public void resetPassword(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam("recruiterEmail") String recruiterEmail){
        int data = authService.resetPassword(recruiterEmail);
        if (data == 0){
            HandlerResponse.responseSuccessOK(response, "Reset password link has been sent to your email");
        }else if (data == 1 ){
            HandlerResponse.responseBadRequest(response, "011", "Data not found");
        }else {
            HandlerResponse.responseBadRequest(response, "012", "Reset password failed");
        }
    }

    //verify token for reset password
    @GetMapping("/verify")
    public void verifyTokenPassword(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam("token") String token) throws IOException {
        boolean verify = authService.verifyTokenPassword(token);
        if (verify){
//            DataResponse<String> dataResponse = new DataResponse<>();
//            dataResponse.setData(token);
//            HandlerResponse.responseSuccessWithData(response, dataResponse);
            response.sendRedirect("https://toptalentapp.com/changepassword/"+token);
        }else{
//            HandlerResponse.responseBadRequest(response, "013", "Verify failed");
            response.sendRedirect("https://toptalentapp.com/expiredforgot");
        }
    }

    //handle change password
    @PostMapping("/change-password")
    public void changePassword(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam("email") String email,
                               @RequestParam("newPassword") String newPassword,
                               @RequestParam("confirmPassword") String confirmPassword){
        HandlerAuthDTO changePassword = authService.changePassword(email, newPassword, confirmPassword);
        switch (changePassword.getErrorCode()){
            case "010":
                DataResponse<RegisterDTO> dataResponse = new DataResponse<>();
                dataResponse.setData(changePassword.getRegisterDTO());
                HandlerResponse.responseSuccessWithData(response, dataResponse);
                break;
            case "08":
                HandlerResponse.responseBadRequest(response, "017", "Please fill all the fields");
                break;
            case "09":
                HandlerResponse.responseBadRequest(response, "014", "Password must be at least 8 characters");
                break;
            case "011":
                HandlerResponse.responseBadRequest(response, "016","Password does not match");
                break;
            case "012":
                HandlerResponse.responseBadRequest(response, "015", "Password must contain at least one number, one capital letter and one special character");
                break;
            default:
                HandlerResponse.responseBadRequest(response, "018", "Error");
        }
    }

    @PatchMapping("/phoneNumberValidation/{recruiterId}")
    public void phoneValidation(HttpServletRequest request, HttpServletResponse response,
                                @PathVariable("recruiterId") int recruiterId,
                                @RequestParam("recruiterPhone") String recruiterPhone){
        HandlerUpdateAuthDTO result = authService.phoneNumberValidation(recruiterId, recruiterPhone);

        switch (result.getErrorCode()){
            case "014":
                HandlerResponse.responseBadRequest(response, "020", "Data not found");
                break;
            case "015":
                DataResponse<UpdateProfileDTO> dataResponse = new DataResponse<>();
                dataResponse.setData(result.getUpdateProfileDTO());
                HandlerResponse.responseSuccessWithData(response, dataResponse);
                break;
            case "016":
                HandlerResponse.responseBadRequest(response, "021", "Wrong format");
                break;
            default:
                HandlerResponse.responseBadRequest(response, "022", "Error");
        }
    }

    @Autowired
    private RecruiterService recruiterService;

    // get recent applicants limit 5 descending
    @GetMapping("/applicants/recent")
    public void getRecentApplicants(HttpServletRequest request, HttpServletResponse response) {
        List<JobseekerDetailDTO> applicants = recruiterService.getRecentApplicants();
        if(applicants != null){
            DataResponse<List<JobseekerDetailDTO>> dataResponse = new DataResponse<>();
            dataResponse.setData(applicants);
            HandlerResponse.responseSuccessWithData(response,dataResponse);
        }else {
            HandlerResponse.responseBadRequest(response, "05", "Applicant not found");
        }
    }

    // get recent applicants limit 5 descending
    @GetMapping("/applicants/recent/id")
    public void getRecentApplicantsById(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam("recruiterId") int recruiterId) {
        List<RecentApplicantsDTO> applicants = recruiterService.getRecentApplicantsById(recruiterId);
        if(applicants != null){
            DataResponse<List<RecentApplicantsDTO>> dataResponse = new DataResponse<>();
            dataResponse.setData(applicants);
            HandlerResponse.responseSuccessWithData(response,dataResponse);
        }else {
            HandlerResponse.responseBadRequest(response, "05", "Applicant not found");
        }
    }

    //get list applicant based on job
    @GetMapping("/applicant/list")
    public void applicantList(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam("jobId") int jobId,
                              @RequestParam("recruiterId") int recruiterId) {
        List<JobseekerDetailDTO> jobseeker = recruiterService.applicantList(jobId, recruiterId);

        if (jobseeker != null) {
            DataResponse<List<JobseekerDetailDTO>> dataResponse = new DataResponse<>();
            dataResponse.setData(jobseeker);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "006", "No data found");
        }
    }

    //get applicant detailed
    @GetMapping("/applicant/detailed")
    public void applicantDetailed(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam("jobseekerId") int jobseekerId) {
        List<JobseekerDetailDTO> jobseeker = recruiterService.applicantDetailed(jobseekerId);

        if (jobseeker != null) {
            DataResponse<List<JobseekerDetailDTO>> dataResponse = new DataResponse<>();
            dataResponse.setData(jobseeker);
            HandlerResponse.responseSuccessWithData(response, dataResponse);
        } else {
            HandlerResponse.responseBadRequest(response, "006", "No data found");
        }
    }
}
