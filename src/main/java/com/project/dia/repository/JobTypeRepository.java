package com.project.dia.repository;

import com.project.dia.model.JobTypeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JobTypeRepository extends JpaRepository<JobTypeModel, Integer> {

    // Cari Job type berdasarkan tipe
    Optional<JobTypeModel> findByJobType(String jobType);

    // get all job type
    @Query(value = "select j from JobTypeModel j ")
    List<JobTypeModel> getAllJobType();
}
