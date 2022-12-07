package com.project.dia.repository;

import com.project.dia.model.JobseekerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicantsModelRepository extends JpaRepository<JobseekerModel, Integer> {

    // get all applicants by jobseeker id and recruiter id
//    @Query(value = "select j from JobModel j " +
//            "where j.recruiterId = :recruiterId and j.jobseekerid = :jobseekerId")
//    List<JobModel> findJobId(@Param("recruiterId") int recruiterId, @Param("jobseekerId") int jobseekerId);

    // get all applicants active only and sorting by job id desc
    @Query(value = "select j from JobseekerModel j " +
            "where j.status = 'active' order by j.jobseekerId DESC")
    List<JobseekerModel> findAllApplicants();

    // get all applicants active only and sorting by job id desc and recruiter id
    @Query(value = "select distinct j from JobseekerModel j " +
            "inner join ApplicationModel a on a.jobseekerId = j.jobseekerId " +
            "where j.status = 'active' and a.recruiterId = :recruiterId order by j.jobseekerId DESC")
    JobseekerModel findAllApplicantsByRecruiterId(@Param("recruiterId") int recruiterId);

    //get jobseeker detail who applied
    @Query(value = "select j from JobseekerModel j " +
            "where j.jobseekerId = :jobseekerId ")
    JobseekerModel findJobseekerById(@Param("jobseekerId") int jobseekerId);
}
