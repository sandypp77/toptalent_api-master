package com.project.dia.repository;

import com.project.dia.model.JobseekerSalary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JobseekerSalaryRepository extends JpaRepository<JobseekerSalary, Integer> {
    Optional<JobseekerSalary> findByJobseekerId(int jobseekerId);

    // get data
    @Query(value = "select j from JobseekerSalary j " +
            "where j.jobseekerId = :jobseekerId")
    JobseekerSalary getDataSalary(@Param("jobseekerId") int jobseekerId);
}
