package com.project.dia.repository;

import com.project.dia.model.JobseekerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JobseekerRepository extends JpaRepository<JobseekerModel, Integer> {
    // sorting jobseeker menggunakan email
    Optional<JobseekerModel>findByJobseekerEmail(String jobseekerEmail);

    // sorting email for api login
    @Query(value = "select j from JobseekerModel j " +
            "where j.jobseekerEmail = :jobseekerEmail and j.status = 'active'")
    Optional<JobseekerModel> findJobseekerEmail(@Param("jobseekerEmail") String jobseekerEmail);



}
