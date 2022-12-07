package com.project.dia.repository;

import com.project.dia.model.JobModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobModelRepository extends JpaRepository<JobModel, Integer> {

    // Cari Jobs berdasarkan nama pekerjaan
    Optional<JobModel> findByJobName(String jobName);

    // get all posting job active only and sorting by job id desc
    @Query(value = "select j from JobModel j " +
            "where j.jobStatus = 'visible' order by j.jobId DESC")
    List<JobModel> findAllJobPosting();

    //get posting job with pagination
    @Query(value = "select j from JobModel j " +
            "where j.jobStatus = 'visible' order by j.jobId DESC")
    Page<JobModel> findAllJobPostingPagination(Pageable pageable);

    //get all data where status != not-active
    List<JobModel> findByJobStatusNot(String jobStatus);

    //get job based on recruiterId
    @Query(value = "select j from JobModel j " +
            "where j.recruiterId = :recruiterId and (j.jobStatus = 'active' or j.jobStatus = 'hidden' or j.jobStatus = 'visible') order by j.jobId DESC")
    List<JobModel> findPostingJob(@Param("recruiterId") int recruiterId);

    //get all job visible on recruiterId
    @Query(value = "select j from JobModel j " +
            "where j.recruiterId = :recruiterId and j.jobStatus = 'visible' order by j.jobId DESC")
    List<JobModel> findAllJobVisible(@Param("recruiterId") int recruiterId);


    //query search from table JobModel and RecruiterModel
    @Query(value = "select j.*, r.recruiter_company from job j " +
            "left join recruiter r on j.recruiter_id = r.recruiter_id " +
            "where (j.job_name like %:keyword% or r.recruiter_company like %:keyword%) " +
            "and j.job_status = 'visible' order by j.job_id DESC", nativeQuery = true)
    List<JobModel> searchJobByKeyword(@Param("keyword") String keyword);

    //query data from jobmodel only status visible and active
    @Query(value = "select j from JobModel j " +
            "where j.jobId = :jobId and (j.jobStatus = 'visible' or j.jobStatus = 'active')")
    Optional<JobModel> findJobById(@Param("jobId") int jobId);

    //get all job where status = HIDDEN based on recruiterId
    @Query(value = "select j from JobModel j " +
            "where j.recruiterId = :recruiterId and j.jobStatus = 'hidden' order by j.jobId DESC")
    List<JobModel> findAllJobHidden(@Param("recruiterId") int recruiterId);

    //get all job name
    @Query(value = "select j.jobName from JobModel j")
    List<JobModel> jobNameList();

    //get list job based on recruiter id and status
    @Query(value = "select j from JobModel j " + "where j.recruiterId = :recruiterId and j.jobStatus = :jobStatus ")
    List<JobModel> getJobListByStatus(@Param("recruiterId") int recruiterId, @Param("jobStatus") String jobStatus);

    //get list job based on job id and status
    @Query(value = "select j from JobModel j " +
            "where j.jobId = :jobId and j.jobStatus = :jobStatus ")
    JobModel getJobList(@Param("jobId") int jobId, @Param("jobStatus") String jobStatus);

    //get job by id
    @Query(value = "select j from JobModel j " +
            "where j.jobId = :jobId")
    JobModel getByJobId(@Param("jobId") int jobId);

}
