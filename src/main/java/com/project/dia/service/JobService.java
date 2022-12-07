package com.project.dia.service;

import com.project.dia.dto.JobDTO;
import com.project.dia.dto.JobDetailsDTO;
import com.project.dia.dto.JobDetailsWithStatusCountDTO;
import com.project.dia.dto.JobModelDTO;
import com.project.dia.model.ApplicationModel;
import com.project.dia.model.JobModel;
import com.project.dia.model.RecruiterModel;
import com.project.dia.repository.ApplicationModelRepository;
import com.project.dia.repository.JobModelRepository;
import com.project.dia.repository.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {
    @Autowired
    private ApplicationModelRepository applicationModelRepository;
    @Autowired
    private RecruiterRepository recruiterRepository;
    @Autowired
    private JobModelRepository jobModelRepository;

    //convert JobDTO

    private JobDTO convertJobs(JobModel jobModel, int recruiterId){
        var recruiterModel = recruiterRepository.findById(recruiterId);
        return new JobDTO(jobModel.getJobId(),
                jobModel.getRecruiterId(),
                recruiterModel.get().getRecruiterImage(),
                jobModel.getJobName(),
                jobModel.getJobSalary(),
                jobModel.getJobPosition(),
                jobModel.getJobAddress(),
                jobModel.getJobDesc(),
                jobModel.getJobRequirement(),
                jobModel.getJobStatus(),
                jobModel.getCreatedAt());
    }

    //convert JobDTODetailed
    public JobDetailsDTO convertJobDetailDTO(JobModel jobModel){
        var recruiterModel = recruiterRepository.findById(jobModel.getRecruiterId());
        if (recruiterModel.isPresent()) {
            return new JobDetailsDTO(
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
                    recruiterModel.get().getRecruiterAddress(),
                    recruiterModel.get().getRecruiterCompany(),
                    recruiterModel.get().getRecruiterDesc(),
                    recruiterModel.get().getRecruiterIndustry(),
                    recruiterModel.get().getRecruiterStaff(),
                    recruiterModel.get().getRecruiterBenefit(),
                    recruiterModel.get().getRecruiterFb(),
                    recruiterModel.get().getRecruiterLinkedin(),
                    recruiterModel.get().getRecruiterIg(),
                    recruiterModel.get().getRecruiterCulture(),
                    recruiterModel.get().getRecruiterWebsite());
        } else {
            return null;
        }
    }

    // Convert JobModelDTO
    public JobModelDTO convertJobModelDTO(JobModel jobModel){
        JobModelDTO jobModelDTO = new JobModelDTO();
        jobModelDTO.setJobId(jobModel.getJobId());
        jobModelDTO.setJobName(jobModel.getJobName());
        jobModelDTO.setJobSalary(jobModel.getJobSalary());
        jobModelDTO.setJobPosition(jobModel.getJobPosition());
        jobModelDTO.setJobAddress(jobModel.getJobAddress());
        jobModelDTO.setJobDesc(jobModel.getJobDesc());
        jobModelDTO.setJobRequirement(jobModel.getJobRequirement());
        jobModelDTO.setJobStatus(jobModel.getJobStatus());
        return jobModelDTO;
    }

    //convert JobDTODetailed
    public JobDetailsWithStatusCountDTO convertDetailsWithStatusCount(JobModel jobModel){
        List<ApplicationModel> applicants = applicationModelRepository.applicantCountStatusVisible(jobModel.getRecruiterId(), jobModel.getJobId());
        int result = 0;
        result = applicants.size();
        JobDetailsWithStatusCountDTO jobModelDTO = new JobDetailsWithStatusCountDTO();
        jobModelDTO.setJobId(jobModel.getJobId());
        jobModelDTO.setRecruiterId(jobModel.getRecruiterId());
        jobModelDTO.setJobName(jobModel.getJobName());
        jobModelDTO.setJobSalary(jobModel.getJobSalary());
        jobModelDTO.setJobPosition(jobModel.getJobPosition());
        jobModelDTO.setJobAddress(jobModel.getJobAddress());
        jobModelDTO.setJobDesc(jobModel.getJobDesc());
        jobModelDTO.setJobRequirement(jobModel.getJobRequirement());
        jobModelDTO.setJobStatus(jobModel.getJobStatus());
        jobModelDTO.setCreatedAt(jobModel.getCreatedAt());
        jobModelDTO.setAppliedCount(result);
        return jobModelDTO;
    }

    //get job by name
    public JobModel getJobByName(String jobName){
        Optional<JobModel> jobOpt = jobModelRepository.findByJobName(jobName);

        if (jobOpt.isEmpty()){
            return null;
        }
        return jobOpt.get();
    }

    //get job by id
    public JobDTO getJobById(int jobId){
        Optional<JobModel> jobOpt = jobModelRepository.findById(jobId);
        if (jobOpt.isEmpty()){
            return null;
        }
//        return convertJobs(jobOpt.get());
        return convertJobs(jobOpt.get(), jobOpt.get().getRecruiterId());
    }

    public List<JobModel> jobNameList(){
        return jobModelRepository.jobNameList();
    }

    //get all jobs
    public List<JobDTO> findAllJobs(int recruiterId){
        List<JobModel> jobModels = jobModelRepository.findPostingJob(recruiterId);
        return jobModels.stream().map(jobModel -> convertJobs(jobModel, recruiterId)).collect(Collectors.toList());
    }

    //cara 2
    public List<JobDTO> findJobs()
    {
        List<JobModel> jobModels = jobModelRepository.findByJobStatusNot("NonActive");
        return jobModels.stream().map(jobModel -> convertJobs(jobModel, jobModel.getRecruiterId())).collect(Collectors.toList());
    }

    //get all jobs by recruiter id and status
    public List<JobDetailsWithStatusCountDTO> findJobsByIdAndStatus(int recruiterId, String jobStatus)
    {
        List<JobModel> jobModels = jobModelRepository.getJobListByStatus(recruiterId, jobStatus);
        if (jobModels.isEmpty()){
            return null;
        };
        return jobModels.stream().map(jobModel -> convertDetailsWithStatusCount(jobModel)).collect(Collectors.toList());
    }

    //get job detail by job id and status
    public JobDetailsDTO findJobDetailed(int jobId, String jobStatus)
    {
        JobModel jobModels = jobModelRepository.getJobList(jobId, jobStatus);
        return convertJobDetailDTO(jobModels);
    }

    //create post job
    public JobDTO createJob(String jobName,
                            int jobSalary,
                            String jobPosition,
                            String jobAddress,
                            String jobDesc,
                            String jobRequirement,
                            int recruiterId){
        //cari apakah parameter recruiterId ada di database
        Optional<RecruiterModel> currentRecruiterOpt = recruiterRepository.findById(recruiterId);
        //kalau ada ambil id nya
        if (currentRecruiterOpt.isEmpty()){
            return null;
        }
        RecruiterModel recruiterModel = currentRecruiterOpt.get();

        JobModel jobModel = new JobModel();
        jobModel.setJobName(jobName);
        jobModel.setRecruiterId(recruiterId);
        jobModel.setJobSalary(jobSalary);
        jobModel.setJobPosition(jobPosition);
        jobModel.setJobAddress(jobAddress);
        jobModel.setJobDesc(jobDesc);
        jobModel.setJobRequirement(jobRequirement);
        jobModel.setJobStatus("Hidden");
        jobModel.setCreatedAt(LocalDateTime.now());
        System.out.println(LocalDateTime.now());

        return convertJobs(jobModelRepository.save(jobModel),recruiterId);
    }

    //update job
    public JobDTO updateJob(int jobId,
                            String jobName,
                            int jobSalary,
                            String jobPosition,
                            String jobAddress,
                            String jobDesc,
                            String jobRequirement,
                            String jobStatus){
        Optional<JobModel> currentJobOpt = jobModelRepository.findById(jobId);

        if (currentJobOpt.isEmpty()){
            return null;
        }

        JobModel jobModel = currentJobOpt.get();
        jobModel.setJobName(jobName);
        jobModel.setJobSalary(jobSalary);
        jobModel.setJobPosition(jobPosition);
        jobModel.setJobAddress(jobAddress);
        jobModel.setJobDesc(jobDesc);
        jobModel.setJobRequirement(jobRequirement);
        jobModel.setJobStatus(jobStatus);

        return convertJobs(jobModelRepository.save(jobModel), jobModel.getRecruiterId());

    }

    //UPDATE JOB STATUS ACTIVE/HIDDEN/VISIBLE
    public JobDTO updateJobStatus(int jobId, String jobStatus)
    {
        Optional<JobModel> currentJobOpt = jobModelRepository.findById(jobId);

        if (currentJobOpt.isEmpty())
        {
            return null;
        }

        JobModel currentJob = currentJobOpt.get();
        currentJob.setJobStatus(jobStatus);

        return convertJobs(jobModelRepository.save(currentJob), currentJob.getRecruiterId());
    }

    //delete job
    public JobDTO deleteJob(int jobId){
        Optional<JobModel> currentJobOpt = jobModelRepository.findById(jobId);


        if (currentJobOpt.isEmpty()){
            return null;
        }

        JobModel jobModel = currentJobOpt.get();
        jobModel.setJobName(jobModel.getJobName());
        jobModel.setJobSalary(jobModel.getJobSalary());
        jobModel.setJobPosition(jobModel.getJobPosition());
        jobModel.setJobAddress(jobModel.getJobAddress());
        jobModel.setJobDesc(jobModel.getJobDesc());
        jobModel.setJobRequirement(jobModel.getJobRequirement());
        jobModel.setJobStatus("NonActive");

        return convertJobs(jobModelRepository.save(jobModel), jobModel.getRecruiterId());

    }

    //get count job post based recruiter id
    public int totalPosts(int recruiterId){
        var postingJobs = jobModelRepository.findPostingJob(recruiterId);
        int result = 0;
        if (postingJobs.isEmpty()){
            return result;
        }else{
            result = postingJobs.size();
            return result;
        }
    }

    //get count job visible based recruiter id
    public int totalVisible(int recruiterId){
        var visibleJobs = jobModelRepository.findAllJobVisible(recruiterId);
        int result = 0;
        if (visibleJobs.isEmpty()){
            return result;
        }else{
            result = visibleJobs.size();
            return result;
        }
    }

    //get count job where status = hidden based on recruiterId
    public int totalHidden(int recruiterId){
        List<JobModel> hiddenJobs = jobModelRepository.findAllJobHidden(recruiterId);
        int result = 0;
        if (hiddenJobs.isEmpty()){
            return result;
        }else{
            result = hiddenJobs.size();
            return result;
        }
    }

}
