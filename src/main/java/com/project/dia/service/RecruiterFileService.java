package com.project.dia.service;

import com.project.dia.dto.UpdatePhotoDTO;
import com.project.dia.model.RecruiterModel;
import com.project.dia.repository.RecruiterRepository;
import com.project.dia.utils.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class RecruiterFileService {
    //private final Path root = Paths.get("/home/ubuntu/final/images/");//diganti jadi /home
    private final Path root = Paths.get("X:\\Upload");

    @Autowired
    private RecruiterRepository recruiterRepository;
    private RecruiterModel recruiterModel;

    //    convert updatePhotoDTO
    private UpdatePhotoDTO convertPhoto(RecruiterModel recruiterModel){
        return new UpdatePhotoDTO(recruiterModel.getRecruiterId(), recruiterModel.getRecruiterImage());
    }

    //function untuk upload foto
    public boolean saveFile(MultipartFile file){
        try {
            if (!Files.exists(root)){
                Files.createDirectory(root);
            }

            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));

            return true;
        }catch (Exception e){
            return false;
        }
    }

    //    update photo profile
    public String updatePhoto(int recruiterId, MultipartFile file) {
        RandomStringGenerator randomStringGenerator = new RandomStringGenerator();
        Optional<RecruiterModel> currentRecruiterOpt = recruiterRepository.findById(recruiterId);
        //cek apakah data ada
        if (currentRecruiterOpt.isEmpty()) {
            return null;
        }

        String image;
        try {
            if (!Files.exists(root)) {
                Files.createDirectory(root);
            }

            //copy ke filesystem
            //get content type
            String contentType = file.getContentType();

            contentType = contentType.toLowerCase().replaceAll("image/", ".");

            //generated random string to a variable
            String originalApplicationFileName = randomStringGenerator.getRandom();
            // copy file into directory with a random string
            Files.copy(file.getInputStream(), this.root.resolve(originalApplicationFileName + contentType));

            //store filesystem location ke database
            //mengambil data current recruiter
            RecruiterModel currentPhoto = currentRecruiterOpt.get();
            //ambil location
            image = originalApplicationFileName + contentType;
            currentPhoto.setRecruiterImage(image);
            recruiterRepository.save(currentPhoto);


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return image;
    }

}