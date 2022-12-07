package com.project.dia.service;

import com.project.dia.dto.JobTypeDTO;
import com.project.dia.model.JobTypeModel;
import com.project.dia.repository.JobTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobTypeService {
    @Autowired
    private JobTypeRepository jobTypeRepository;

    private JobTypeDTO convertJobType(JobTypeModel jobTypeModel){
        return new JobTypeDTO(jobTypeModel.getJobTypeId(),
                jobTypeModel.getJobType());
    }

    public JobTypeDTO addJobType(String jobType){
        Optional<JobTypeModel> jobTypeOpt = jobTypeRepository.findByJobType(jobType);
        if (jobTypeOpt.isEmpty()){
            JobTypeModel jobTypeModel = new JobTypeModel();
            jobTypeModel.setJobType(jobType);
            return convertJobType(jobTypeRepository.save(jobTypeModel));
        }else{
            return null;
        }

    }

    public List<JobTypeDTO> listJobType()
    {
        List<JobTypeModel> jobTypeModels = jobTypeRepository.getAllJobType();

        return jobTypeModels.stream().map(jobTypeModel -> convertJobType(jobTypeModel)).collect(Collectors.toList());
    }
}
