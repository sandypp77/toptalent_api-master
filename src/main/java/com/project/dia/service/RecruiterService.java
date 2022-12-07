package com.project.dia.service;

import com.project.dia.dto.JobseekerDetailDTO;
import com.project.dia.dto.RecentApplicantsDTO;
import com.project.dia.dto.SkillNameDTO;
import com.project.dia.model.ApplicationModel;
import com.project.dia.model.JobModel;
import com.project.dia.model.JobseekerModel;
import com.project.dia.model.JobseekerSkillModel;
import com.project.dia.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecruiterService {
    @Autowired
    private ApplicantsModelRepository applicantsModelRepository;

    @Autowired
    private ApplicationModelRepository applicationModelRepository;

    @Autowired
    private JobseekerSkillRepository jobseekerSkillRepository;

    @Autowired
    private JobModelRepository jobModelRepository;

    @Autowired
    private SkillsRepository skillsRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;


    //convert DTO JobseekerDetail
    public JobseekerDetailDTO convertDTO(JobseekerModel jobseekerModel) {
        JobseekerDetailDTO jobseekerDetailDTO = new JobseekerDetailDTO();
        List<SkillNameDTO> results = new ArrayList<>();

        jobseekerDetailDTO.setJobseekerId(jobseekerModel.getJobseekerId());
        jobseekerDetailDTO.setJobseekerName(jobseekerModel.getJobseekerName());
        jobseekerDetailDTO.setJobseekerEmail(jobseekerModel.getJobseekerEmail());
        jobseekerDetailDTO.setJobseekerImage(jobseekerModel.getJobseekerImage());
        jobseekerDetailDTO.setJobseekerEducation(jobseekerModel.getJobseekerEducation());
        jobseekerDetailDTO.setJobseekerResume(jobseekerModel.getJobseekerResume());
        jobseekerDetailDTO.setJobseekerAddress(jobseekerModel.getJobseekerAddress());
        jobseekerDetailDTO.setJobseekerPhone(jobseekerModel.getJobseekerPhone());
        jobseekerDetailDTO.setJobseekerAbout(jobseekerModel.getJobseekerAbout());
        jobseekerDetailDTO.setJobseekerDateOfBirth(jobseekerModel.getJobseekerDateOfBirth());
        jobseekerDetailDTO.setJobseekerProfession(jobseekerModel.getJobseekerProfession());
        jobseekerDetailDTO.setJobseekerPortfolio(jobseekerModel.getJobseekerPortfolio());
        jobseekerDetailDTO.setJobseekerMedsos(jobseekerModel.getJobseekerMedsos());


        List<JobseekerSkillModel> jobseekerSkillModels = jobseekerSkillRepository.findByJobseekerId(jobseekerModel.getJobseekerId());

        List<Integer> skills = jobseekerSkillModels.stream().map(JobseekerSkillModel::getSkillId).distinct().collect(Collectors.toList());

        skills.forEach(skill -> {
            SkillNameDTO skillNameDTO = new SkillNameDTO();
            skillNameDTO.setSkillId(skillsRepository.findById(skill).get().getSkillId());
            skillNameDTO.setSkillName(skillsRepository.findById(skill).get().getSkillName());
            results.add(skillNameDTO);
        });

        jobseekerDetailDTO.setSkills(results);
        jobseekerDetailDTO.setJobseekerCompany(jobseekerModel.getJobseekerCompany());
        jobseekerDetailDTO.setWorkStartYear(jobseekerModel.getWorkStartYear());
        jobseekerDetailDTO.setWorkEndYear(jobseekerModel.getWorkEndYear());
        return jobseekerDetailDTO;
    }

    //convert DTO Recent
    public RecentApplicantsDTO convertRecentDTO(ApplicationModel applicationModel) {
        RecentApplicantsDTO recentApplicantsDTO = new RecentApplicantsDTO();
        JobModel jobModel = jobModelRepository.getByJobId(applicationModel.getJobId());
        JobseekerModel jobseekerModels = applicantsModelRepository.findJobseekerById(applicationModel.getJobseekerId());
        List<SkillNameDTO> results = new ArrayList<>();
        recentApplicantsDTO.setJobId(jobModel.getJobId());
        recentApplicantsDTO.setJobName(jobModel.getJobName());
        recentApplicantsDTO.setJobPosition(jobModel.getJobPosition());
        recentApplicantsDTO.setJobseekerId(jobseekerModels.getJobseekerId());
        recentApplicantsDTO.setJobseekerName(jobseekerModels.getJobseekerName());
        recentApplicantsDTO.setJobseekerEmail(jobseekerModels.getJobseekerEmail());
        recentApplicantsDTO.setJobseekerImage(jobseekerModels.getJobseekerImage());
        recentApplicantsDTO.setJobseekerEducation(jobseekerModels.getJobseekerEducation());
        recentApplicantsDTO.setJobseekerResume(jobseekerModels.getJobseekerResume());
        recentApplicantsDTO.setJobseekerAddress(jobseekerModels.getJobseekerAddress());
        recentApplicantsDTO.setJobseekerPhone(jobseekerModels.getJobseekerPhone());
        recentApplicantsDTO.setJobseekerAbout(jobseekerModels.getJobseekerAbout());
        recentApplicantsDTO.setJobseekerDateOfBirth(jobseekerModels.getJobseekerDateOfBirth());
        recentApplicantsDTO.setJobseekerProfession(jobseekerModels.getJobseekerProfession());
        recentApplicantsDTO.setJobseekerPortfolio(jobseekerModels.getJobseekerPortfolio());
        recentApplicantsDTO.setJobseekerMedsos(jobseekerModels.getJobseekerMedsos());


        List<JobseekerSkillModel> jobseekerSkillModels = jobseekerSkillRepository.findByJobseekerId(jobseekerModels.getJobseekerId());

        List<Integer> skills = jobseekerSkillModels.stream().map(JobseekerSkillModel::getSkillId).distinct().collect(Collectors.toList());

        skills.forEach(skill -> {
            SkillNameDTO skillNameDTO = new SkillNameDTO();
            skillNameDTO.setSkillId(skillsRepository.findById(skill).get().getSkillId());
            skillNameDTO.setSkillName(skillsRepository.findById(skill).get().getSkillName());
            results.add(skillNameDTO);
        });

        recentApplicantsDTO.setSkills(results);
        recentApplicantsDTO.setJobseekerCompany(jobseekerModels.getJobseekerCompany());
        recentApplicantsDTO.setWorkStartYear(jobseekerModels.getWorkStartYear());
        recentApplicantsDTO.setWorkEndYear(jobseekerModels.getWorkEndYear());
        return recentApplicantsDTO;
    }

    // API read recent applicants
    public List<JobseekerDetailDTO> getRecentApplicants() {
        List<JobseekerModel> jobseekerModels = applicantsModelRepository.findAllApplicants();
        if (jobseekerModels.isEmpty()) {
            return null;
        }
        return jobseekerModels.stream().map(this::convertDTO).limit(5).collect(Collectors.toList());
    }

    // API read recent applicants base on recruiter id
    public List<RecentApplicantsDTO> getRecentApplicantsById(int recruiterId) {
        List<ApplicationModel> applicationModels = applicationModelRepository.jobIdByApplication(recruiterId);
        if (applicationModels.isEmpty()) {
            return null;
        }
        return applicationModels.stream().map(this::convertRecentDTO).limit(5).collect(Collectors.toList());
    }

    //get list applicant based on job
    public List<JobseekerDetailDTO> applicantList(int jobId, int recruiterId) {
        List<JobseekerModel> applicants = recruiterRepository.applicantList(jobId, recruiterId);
        if (applicants.isEmpty()) {
            return null;
        }
        return applicants.stream().map(this::convertDTO).collect(Collectors.toList());
    }

    //get list applicant based on job
    public List<JobseekerDetailDTO> applicantDetailed(int jobId) {
        List<JobseekerModel> applicants = recruiterRepository.applicantDetailed(jobId);
        if (applicants.isEmpty()) {
            return null;
        }
        return applicants.stream().map(this::convertDTO).collect(Collectors.toList());
    }
}
