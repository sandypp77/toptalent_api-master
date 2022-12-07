package com.project.dia.repository;

import com.project.dia.model.ApplicationModel;
import com.project.dia.model.JobseekerModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ApplicationModelRepository extends JpaRepository<ApplicationModel, Integer> {
    //get applicants data
    @Query(value = "SELECT application.application_id, jobseeker.jobseeker_name, jobseeker.jobseeker_email, jobseeker.jobseeker_resume" +
            "FROM jobseeker" +
            "INNER JOIN application ON jobseeker.jobseeker_id =application.jobseeker_id" +
            "ORDER BY application_id", nativeQuery = true)
    List<JobseekerModel> findApplicants();

    @Query(value = "SELECT a from ApplicationModel a " +
            "where a.jobId = :jobId ORDER BY a.applicationId ASC")
    List<ApplicationModel> findApplicants(@Param("jobId") int jobId);

    // query for get a applied data and order descending
    @Query(value = "select a from ApplicationModel a " +
            "inner join JobModel j on a.jobId = j.jobId " +
            "where a.jobseekerId = :jobseekerId and j.jobStatus = 'visible' order by a.applicationId DESC")
    Page<ApplicationModel> findApplyJobs(@Param("jobseekerId") int jobseekerId, Pageable pageable);

    Page<ApplicationModel> findByJobId(int jobId, Pageable pageable);

    //get application for dashboard
    @Query(value = "select a from ApplicationModel a " +
            "inner join JobModel j on a.jobId = j.jobId " +
            "where a.recruiterId = :recruiterId and a.applicationStatus = 'sent' " +
            "and (j.jobStatus = 'hidden' or j.jobStatus = 'visible') order by a.applicationId ASC")
    List<ApplicationModel> applicantDashboard(@Param("recruiterId") int recruiterId);


    //get count application status = accepted for dashboard
    @Query(value = "select a from ApplicationModel a " +
            "where a.recruiterId = :recruiterId and a.applicationStatus = 'accepted' order by a.applicationId DESC")
    List<ApplicationModel> getCountAccepted(@Param("recruiterId") int recruiterId);

    //get count application status = rejected for dashboard
    @Query(value = "select a from ApplicationModel a " +
            "where a.recruiterId = :recruiterId and a.applicationStatus = 'rejected' order by a.applicationId DESC")
    List<ApplicationModel> getCountRejected(@Param("recruiterId") int recruiterId);

    //get count application status = sent
    @Query(value = "select a from ApplicationModel a " +
            "where a.recruiterId = :recruiterId and a.applicationStatus = 'sent' order by a.applicationId DESC")
    List<ApplicationModel> getCountSent(@Param("recruiterId") int recruiterId);

    // get data sent
    @Query(value = "select * from application as a " +
            "where a.job_id = :jobId and a.jobseeker_id = :jobseekerId and a.application_status = 'sent'", nativeQuery = true)
    Optional<ApplicationModel> findJobIdAndJobSeekerId(@Param("jobId") int jobId, @Param("jobseekerId") int jobseekerId);


    @Query(value = "select * from application as a " +
            "where a.job_id = :jobId and a.jobseeker_id = :jobseekerId", nativeQuery = true)
    Optional<ApplicationModel> findJobDetail(@Param("jobId") int jobId, @Param("jobseekerId") int jobseekerId);

    //get data all applicant based on recruiter
    @Query(value = "select a from ApplicationModel a " +
            "where a.recruiterId = :recruiterId")
    List<ApplicationModel> getAllApplications(@Param("recruiterId") int recruiterId);

    // get count data all applicant from every jobmodel
    @Query(value = "select a from ApplicationModel a " +
            "where a.jobId = :jobId order by a.applicationId DESC")
    List<ApplicationModel> getCountAllApplicant(@Param("jobId") int jobId);

    //get application status visible
    @Query(value = "select a from ApplicationModel a " +
            "inner join JobModel j on a.jobId = j.jobId " +
            "where a.recruiterId = :recruiterId and a.applicationStatus = 'sent' " +
            "and j.jobStatus = 'visible' order by a.applicationId ASC")
    List<ApplicationModel> applicantStatusVisible(@Param("recruiterId") int recruiterId);

    //get application status hidden
    @Query(value = "select a from ApplicationModel a " +
            "inner join JobModel j on a.jobId = j.jobId " +
            "where a.recruiterId = :recruiterId and a.applicationStatus = 'sent' " +
            "and j.jobStatus = 'hidden' order by a.applicationId ASC")
    List<ApplicationModel> applicantStatusHidden(@Param("recruiterId") int recruiterId);

    //get count how many applicants applied for job status visible
    @Query(value = "select a from ApplicationModel a " +
            "inner join JobModel j on a.jobId = j.jobId " +
            "where a.recruiterId = :recruiterId and a.applicationStatus = 'sent' " +
            "and j.jobStatus = 'visible' and a.jobId = :jobId order by a.applicationId ASC")
    List<ApplicationModel> applicantCountStatusVisible(@Param("recruiterId") int recruiterId, @Param("jobId") int jobId);

    //get count how many applicants applied for job status hidden
    @Query(value = "select a from ApplicationModel a " +
            "inner join JobModel j on a.jobId = j.jobId " +
            "where a.recruiterId = :recruiterId and a.applicationStatus = 'sent' " +
            "and j.jobStatus = 'hidden' and a.jobId = :jobId order by a.applicationId ASC")
    List<ApplicationModel> applicantCountStatusHidden(@Param("recruiterId") int recruiterId, @Param("jobId") int jobId);

    //get job id based on job applied
    @Query(value = "select a from ApplicationModel a " +
            "where a.recruiterId = :recruiterId and a.applicationStatus = 'sent' order by a.applicationId DESC")
    List<ApplicationModel> jobIdByApplication(@Param("recruiterId") int recruiterId);
}


