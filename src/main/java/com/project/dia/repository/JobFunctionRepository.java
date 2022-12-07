package com.project.dia.repository;

import com.project.dia.model.JobFunctionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JobFunctionRepository extends JpaRepository<JobFunctionModel, Integer> {

    // Cari Job Function berdasarkan Function
    Optional<JobFunctionModel> findByJobFunction(String jobFunction);

    // get all job function
    @Query(value = "select j from JobFunctionModel j ")
    List<JobFunctionModel> getAllJobFunction();

}
