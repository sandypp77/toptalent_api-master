package com.project.dia.service;

import com.project.dia.dto.RecruiterGalleryDTO;
import com.project.dia.model.RecruiterGalleryModel;
import com.project.dia.repository.RecruiterGalleryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class RecruiterGalleryService {
    private final Path root = Paths.get("/home/ubuntu/final/images");
//    private final Path root = Paths.get("D:\\Upload");
    @Autowired
    private RecruiterGalleryRepository recruiterGalleryRepository;
//    private RecruiterModel recruiterModel;

    //convert recruiterGalleryDTO
    public RecruiterGalleryDTO convertRecruiterGalleryDTO(RecruiterGalleryModel recruiterGalleryModel){
        var recruiterGallery = recruiterGalleryRepository.findById(recruiterGalleryModel.getRecruiterId());

        RecruiterGalleryDTO recruiterGalleryDTO = new RecruiterGalleryDTO();

        recruiterGalleryDTO.setRecruiterGalleryId(recruiterGalleryModel.getRecruiterGalleryId());
        recruiterGalleryDTO.setRecruiterId(recruiterGalleryModel.getRecruiterId());
        recruiterGalleryDTO.setImageGallery(recruiterGalleryModel.getImageGallery());
        return recruiterGalleryDTO;

    }

    //ADD AN IMAGE
//    public String uploadImage(int recruiterId, MultipartFile file){
//        RandomStringGenerator randomStringGenerator = new RandomStringGenerator();
//        Optional<Recruiter>
//    }
}
