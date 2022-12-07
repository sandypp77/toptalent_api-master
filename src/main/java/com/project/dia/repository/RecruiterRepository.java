package com.project.dia.repository;

import com.project.dia.model.JobseekerModel;
import com.project.dia.model.RecruiterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecruiterRepository extends JpaRepository<RecruiterModel, Integer> {
    //cari data berdasarkan email
    Optional<RecruiterModel> findByRecruiterEmail(String recruiterEmail);

    //login
    Optional<RecruiterModel> findByRecruiterEmailAndRecruiterPassword(String recruiterEmail, String recruiterPassword);

    //get list applicant based on job
    @Query(value = "select a from JobseekerModel a " +
            "inner join ApplicationModel j on a.jobseekerId = j.jobseekerId " +
            "where j.jobId = :jobId and j.recruiterId = :recruiterId and j.applicationStatus = 'sent' " +
            "order by j.applicationId ASC")
    List<JobseekerModel> applicantList(@Param("jobId") int jobId, @Param("recruiterId") int recruiterId);

    //get applicant detailed
    @Query(value = "select a from JobseekerModel a " +
            "where a.jobseekerId = :jobseekerId ")
    List<JobseekerModel> applicantDetailed(@Param("jobseekerId") int jobseekerId);

}
