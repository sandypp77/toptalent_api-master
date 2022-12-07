package com.project.dia.repository;

import com.project.dia.model.JobseekerSkillModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JobseekerSkillRepository extends JpaRepository<JobseekerSkillModel, Integer> {

    @Query(value = "SELECT * FROM jobseeker_skill as js " +
            "inner join jobseeker as j on js.jobseeker_id = j.jobseeker_id where js.jobseeker_id = :jobseekerId", nativeQuery = true)
    List<JobseekerSkillModel> findByJobseekerId(@Param("jobseekerId") int jobseekerId);

    @Query(value = "SELECT * FROM jobseeker_skill as a " +
            "where a.jobseeker_id = :jobseekerId and a.skill_id = :skillId", nativeQuery = true)
    JobseekerSkillModel findSkillByJobSeekerId(@Param("jobseekerId") int jobseekerId, @Param("skillId") int skillId);

    //delete jobseeker_skill by jobseeker_id
    @Transactional
    void deleteByJobseekerId(int jobseekerId);


}
