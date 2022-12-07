package com.project.dia.service;

import com.project.dia.dto.JobFunctionDTO;
import com.project.dia.model.JobFunctionModel;
import com.project.dia.repository.JobFunctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobFunctionService {

    @Autowired
    private JobFunctionRepository jobFunctionRepository;

    private JobFunctionDTO convertJobFunction(JobFunctionModel jobFunctionModel){
        return new JobFunctionDTO(jobFunctionModel.getJobFunctionId(),
                jobFunctionModel.getJobFunction());
    }

    public JobFunctionDTO addJobFunction(String jobFunction){
        Optional<JobFunctionModel> jobFunctionOpt = jobFunctionRepository.findByJobFunction(jobFunction);
        if (jobFunctionOpt.isEmpty()){
            JobFunctionModel jobFunctionModel = new JobFunctionModel();
            jobFunctionModel.setJobFunction(jobFunction);
            return convertJobFunction(jobFunctionRepository.save(jobFunctionModel));
        }else{
            return null;
        }

    }

    public List<JobFunctionDTO> listJobFunction()
    {
        List<JobFunctionModel> jobFunctionModels = jobFunctionRepository.getAllJobFunction();

        return jobFunctionModels.stream().map(jobFunctionModel -> convertJobFunction(jobFunctionModel)).collect(Collectors.toList());
    }
}
