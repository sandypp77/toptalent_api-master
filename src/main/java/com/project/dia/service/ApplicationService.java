package com.project.dia.service;

import com.project.dia.dto.*;
import com.project.dia.model.ApplicationModel;
import com.project.dia.model.JobModel;
import com.project.dia.model.JobseekerModel;
import com.project.dia.repository.ApplicationModelRepository;
import com.project.dia.repository.JobModelRepository;
import com.project.dia.repository.JobseekerRepository;
import com.project.dia.repository.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApplicationService  {
    @Autowired
    private ApplicationModelRepository applicationModelRepository;
    @Autowired
    private JobModelRepository jobModelRepository;
    @Autowired
    private JobseekerRepository jobseekerRepository;
    @Autowired
    private RecruiterRepository recruiterRepository;

    // Convert Application DTO
    public ApplicationDTO convertApplication(ApplicationModel applicationModel){
        var jobseekerModel = jobseekerRepository.findById(applicationModel.getJobseekerId());
        var recruiterModel = recruiterRepository.findById(applicationModel.getRecruiterId());

        return jobseekerModel.map(model -> new ApplicationDTO(
                applicationModel.getApplicationId(),
                applicationModel.getJobId(),
                jobseekerModel.get().getJobseekerId(),
                recruiterModel.get().getRecruiterId(),
                applicationModel.getApplicationStatus(),
                jobseekerModel.get().getJobseekerResume(),
                applicationModel.getCreatedAt()
        )).orElseGet(ApplicationDTO::new);
    }

    //convert ApplicantDTO
    public ApplicantDTO convertApplicant(ApplicationModel applicationModel){
        var jobseekerModel = jobseekerRepository.findById(applicationModel.getJobseekerId());
        var jobModel = jobModelRepository.findById(applicationModel.getJobId());
        return jobseekerModel.map(model -> new ApplicantDTO(
                applicationModel.getApplicationId(),
                applicationModel.getJobId(),
                jobModel.get().getJobName(),
                jobModel.get().getJobPosition(),
                model.getJobseekerId(),
                model.getJobseekerName(),
                model.getJobseekerEmail(),
                model.getJobseekerResume(),
                model.getJobseekerImage(),
                model.getJobseekerAddress(),
                model.getJobseekerPhone(),
                model.getJobseekerDateOfBirth(),
                model.getJobseekerProfession(),
                model.getJobseekerPortfolio(),
                applicationModel.getApplicationStatus(),
                applicationModel.getCreatedAt()
        )).orElseGet(ApplicantDTO::new);

    }

    // convert DTO Jobseeker
    public JobseekerDTO convertJobseekerDTO(JobseekerModel jobseekerModel){
        JobseekerDTO jobseekerDTO = new JobseekerDTO();
        jobseekerDTO.setJobseekerId(jobseekerModel.getJobseekerId());
        jobseekerDTO.setJobseekerName(jobseekerModel.getJobseekerName());
        jobseekerDTO.setJobseekerEmail(jobseekerModel.getJobseekerEmail());
        jobseekerDTO.setJobseekerImage(jobseekerModel.getJobseekerImage());
        jobseekerDTO.setJobseekerEducation(jobseekerModel.getJobseekerEducation());
        jobseekerDTO.setJobseekerResume(jobseekerModel.getJobseekerResume());
        jobseekerDTO.setJobseekerAddress(jobseekerModel.getJobseekerAddress());
        jobseekerDTO.setJobseekerPhone(jobseekerModel.getJobseekerPhone());
        jobseekerDTO.setJobseekerAbout(jobseekerModel.getJobseekerAbout());
        jobseekerDTO.setJobseekerDateOfBirth(jobseekerModel.getJobseekerDateOfBirth());
        jobseekerDTO.setJobseekerProfession(jobseekerModel.getJobseekerProfession());
        jobseekerDTO.setJobseekerPortfolio(jobseekerModel.getJobseekerPortfolio());
        jobseekerDTO.setJobseekerMedsos(jobseekerModel.getJobseekerMedsos());
        jobseekerDTO.setJobseekerSkill(jobseekerModel.getJobseekerSkill());
        return jobseekerDTO;
    }

    // Convert DTO PostJob
    public PostJobDTO convertPostJobDTO(JobModel jobModel) {
        var recruiterModel = recruiterRepository.findById(jobModel.getRecruiterId());
        if (recruiterModel.isPresent()) {
            return new PostJobDTO(
                    jobModel.getJobId(),
                    jobModel.getJobName(),
                    jobModel.getJobSalary(),
                    jobModel.getJobPosition(),
                    jobModel.getJobAddress(),
                    jobModel.getJobDesc(),
                    jobModel.getJobRequirement(),
                    jobModel.getJobStatus(),
                    jobModel.getCreatedAt(),
                    recruiterModel.get().getRecruiterImage(),
                    recruiterModel.get().getRecruiterCompany(),
                    recruiterModel.get().getRecruiterDesc());

        } else {
            return null;
        }
    }



    //update application status = ACCEPTED
    public ApplicationDTO updateApplicationAccepted(int applicationId){
        Optional<ApplicationModel> currentApplicationOpt = applicationModelRepository.findById(applicationId);

        if (currentApplicationOpt.isEmpty()){
            return null;
        }

        ApplicationModel currentApplication = currentApplicationOpt.get();
        currentApplication.setApplicationStatus("accepted");

        return convertApplication(applicationModelRepository.save(currentApplication));

    }

    //update application status = REJECTED
    public ApplicationDTO updateApplicationRejected(int applicationId){
        Optional<ApplicationModel> currentApplicationOpt = applicationModelRepository.findById(applicationId);

        if (currentApplicationOpt.isEmpty()){
            return null;
        }

        ApplicationModel currentApplication = currentApplicationOpt.get();
        currentApplication.setApplicationStatus("rejected");

        return convertApplication(applicationModelRepository.save(currentApplication));

    }

    //update application status back to SENT
        public ApplicationDTO updateApplicationSent(int applicationId){
        Optional<ApplicationModel> currentApplicationOpt = applicationModelRepository.findById(applicationId);

        if (currentApplicationOpt.isEmpty()){
            return null;
        }

        ApplicationModel currentApplication = currentApplicationOpt.get();
        currentApplication.setApplicationStatus("sent");

        return  convertApplication(applicationModelRepository.save(currentApplication));
    }

    //update application status
    public ApplicationDTO updateApplication(int applicationId, String applicationStatus){
        Optional<ApplicationModel> currentApplicationOpt = applicationModelRepository.findById(applicationId);

        if (currentApplicationOpt.isEmpty()){
            return null;
        }

        ApplicationModel currentApplication = currentApplicationOpt.get();
        currentApplication.setApplicationStatus(applicationStatus);

        return convertApplication(applicationModelRepository.save(currentApplication));

    }

    //get all data applicants
    public List<ApplicantDTO> getAllApplicants()
    {
        List<ApplicationModel> applicationModels = applicationModelRepository.findAll();
        return applicationModels.stream().map(this::convertApplicant).collect(Collectors.toList());
    }

    //get data applicant sesuai postingan job
    public List<JobseekerDTO> getApplicants()
    {
        List<JobseekerModel> applicationModels = applicationModelRepository.findApplicants();
        return applicationModels.stream().map(this::convertJobseekerDTO).collect(Collectors.toList());
    }

    //cara 2
    public List<ApplicantDTO> getApplicant(int jobId){
        var applicants = applicationModelRepository.findApplicants(jobId);
        return applicants.stream().map(this::convertApplicant).collect(Collectors.toList());
    }

    // get posting job by id recruiter
    public List<PostJobDTO> getPostingJob(int recruiterId){
        final var postingJob = jobModelRepository.findPostingJob(recruiterId);
        return postingJob.stream().map(this::convertPostJobDTO).collect(Collectors.toList());
    }

    //get applicants menggunakan pagination
    public Page<ApplicantDTO> getAllApplicants(int jobId, Integer pageNo, Integer pageSize, String sortBy)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<ApplicationModel> pagedResult = applicationModelRepository.findByJobId(jobId, paging);
        return pagedResult.map(this::convertApplicant);

    }

    //get applicant by id
    public ApplicantDTO getApplicantById(int applicationId){
        Optional<ApplicationModel> jobseekerModelOpt = applicationModelRepository.findById(applicationId);

        System.out.println(jobseekerModelOpt);
        if (jobseekerModelOpt.isEmpty()){
            return null;
        }
        return convertApplicant(jobseekerModelOpt.get());
    }

    //get applicant with applicationStatus=sent limit = 10
    public List<ApplicantDTO> applicantDashboard(int recruiterId){
        List<ApplicationModel> applicants = applicationModelRepository.applicantDashboard(recruiterId);

        if (applicants.isEmpty()){
            return null;
        }
        return applicants.stream().map(this::convertApplicant).limit(10).collect(Collectors.toList());

    }

    //get applicant with applicationStatus=accepted
    public int countAccepted (int recruitedId){
        List<ApplicationModel> applicants = applicationModelRepository.getCountAccepted(recruitedId);
        int result = 0;
        if (applicants.isEmpty()){
            return result;
        }else{
            result = applicants.size();
            return result;
        }

    }

    //get applicant with applicationStatus=rejected
    public int countRejected (int recruitedId){
        List<ApplicationModel> applicants = applicationModelRepository.getCountRejected(recruitedId);
        int result = 0;
        if (applicants.isEmpty()){
            return result;
        }else {
            result = applicants.size();
            return result;
        }
    }

    //get count applicant status = sent
    public int countSent (int recruiterId){
        List<ApplicationModel> applicants = applicationModelRepository.getCountSent(recruiterId);
        int result = 0;
        if (applicants.isEmpty()){
            return result;
        }else{
            result = applicants.size();
            return result;
        }
    }

    //get applicant who applied to certain job status visible
    public int countAppliedVisible (int recruitedId){
        List<ApplicationModel> applicants = applicationModelRepository.applicantStatusVisible(recruitedId);
        int result = 0;
        if (applicants.isEmpty()){
            return result;
        }else{
            result = applicants.size();
            return result;
        }

    }

    //get applicant who applied to certain job status hidden
    public int countAppliedHidden (int recruitedId){
        List<ApplicationModel> applicants = applicationModelRepository.applicantStatusHidden(recruitedId);
        int result = 0;
        if (applicants.isEmpty()){
            return result;
        }else{
            result = applicants.size();
            return result;
        }

    }

    //get count how many applicants applied for job status visible
    public int countAppliedJobVisible (int recruitedId, int jobId){
        List<ApplicationModel> applicants = applicationModelRepository.applicantCountStatusVisible(recruitedId, jobId);
        int result = 0;
        if (applicants.isEmpty()){
            return result;
        }else{
            result = applicants.size();
            return result;
        }

    }

    //get count how many applicants applied for job status hidden
    public int countAppliedJobHidden(int recruitedId, int jobId){
        List<ApplicationModel> applicants = applicationModelRepository.applicantCountStatusHidden(recruitedId, jobId);
        int result = 0;
        if (applicants.isEmpty()){
            return result;
        }else{
            result = applicants.size();
            return result;
        }

    }

    //get size all application
    public int allApplications(int recruiterId){
        List<ApplicationModel> applications = applicationModelRepository.getAllApplications(recruiterId);
        int result = 0;
        if (applications.isEmpty()){
            return result;
        }else {
            result = applications.size();
            return result;
        }
    }

    //get count new resume
    public int countNewResume(int recruiterId){
        List<ApplicationModel> countNewResume = applicationModelRepository.applicantDashboard(recruiterId);
        int result = 0;
        if (countNewResume.isEmpty()){
            return result;
        }else {
            result = countNewResume.size();
            return result;
        }
    }

    //get count applicant from every job posting
    public int countApplicants(int jobId){
        final var countAllApplicant = applicationModelRepository.getCountAllApplicant(jobId);
        int result = 0;
        if (countAllApplicant.isEmpty()){
            return result;
    }else {
        result = countAllApplicant.size();
        return result;
    }
    }

    //get 1 data applicant for right sidebar
    public List<ApplicantDTO> applicantRightSidebar(int recruiterId){
        List<ApplicationModel> applicants = applicationModelRepository.applicantDashboard(recruiterId);

        if (applicants.isEmpty()){
            return null;
        }
        return applicants.stream().map(this::convertApplicant).limit(1).collect(Collectors.toList());

    }

}
