package com.project.dia.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.dia.dto.HandlerAuthDTO;
import com.project.dia.dto.HandlerUpdateAuthDTO;
import com.project.dia.dto.RegisterDTO;

import com.project.dia.dto.UpdateProfileDTO;
import com.project.dia.model.RecruiterModel;
import com.project.dia.repository.RecruiterRepository;
import freemarker.core.ParseException;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;


@Service
public class AuthService {
    private static final long EXPIRATION_TIME = 1800000; // 30 minutes
    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private Configuration config;

//    @Value("classpath:/logo.png")
//    Resource resourceFile;


    //convert registerDTO
    private RegisterDTO convertRegister(RecruiterModel recruiterModel){
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setRecruiterId(recruiterModel.getRecruiterId());
        registerDTO.setRecruiterEmail(recruiterModel.getRecruiterEmail());
        registerDTO.setRecruiterCompany(recruiterModel.getRecruiterCompany());
        registerDTO.setRecruiterIndustry(recruiterModel.getRecruiterIndustry());
        return registerDTO;
    }
    
    //convert updateProfileDTO
    private UpdateProfileDTO convertProfile(RecruiterModel recruiterModel){
        UpdateProfileDTO updateProfileDTO = new UpdateProfileDTO();
        updateProfileDTO.setRecruiterId(recruiterModel.getRecruiterId());
        updateProfileDTO.setRecruiterEmail(recruiterModel.getRecruiterEmail());
        updateProfileDTO.setRecruiterCompany(recruiterModel.getRecruiterCompany());
        updateProfileDTO.setRecruiterIndustry(recruiterModel.getRecruiterIndustry());
        updateProfileDTO.setRecruiterPhone(recruiterModel.getRecruiterPhone());
        updateProfileDTO.setRecruiterStaff(recruiterModel.getRecruiterStaff());
        updateProfileDTO.setRecruiterDesc(recruiterModel.getRecruiterDesc());
        updateProfileDTO.setRecruiterAddress(recruiterModel.getRecruiterAddress());
        updateProfileDTO.setRecruiterFb(recruiterModel.getRecruiterFb());
        updateProfileDTO.setRecruiterIg(recruiterModel.getRecruiterIg());
        updateProfileDTO.setRecruiterLinkedin(recruiterModel.getRecruiterLinkedin());
        updateProfileDTO.setRecruiterCulture(recruiterModel.getRecruiterCulture());
        updateProfileDTO.setRecruiterBenefit(recruiterModel.getRecruiterBenefit());
        updateProfileDTO.setRecruiterWebsite(recruiterModel.getRecruiterWebsite());
        updateProfileDTO.setRecruiterImage(recruiterModel.getRecruiterImage());
        return updateProfileDTO;
    }

    //get user by id
    public UpdateProfileDTO getRecruiterById(int recruiterId){
        return convertProfile(recruiterRepository.findById(recruiterId).get());
    }

    //register/create user
    public HandlerAuthDTO createUser(String recruiterEmail,
                                     String recruiterPassword,
                                     String recruiterCompany,
                                     String recruiterIndustry
    ){
        //validasi apakah data user alr exists
        Optional<RecruiterModel> recruiterOpt = recruiterRepository.findByRecruiterEmail(recruiterEmail);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        RecruiterModel recruiterModel = new RecruiterModel();
        String passwordHash = "";
        if (recruiterOpt.isEmpty()){
            if (recruiterEmail.isEmpty() || recruiterPassword.isEmpty() || recruiterCompany.isEmpty() || recruiterIndustry.isEmpty()){
//                throw new IllegalArgumentException("Please fill all the fields");
                return new HandlerAuthDTO("01", new RegisterDTO());
            }else{
                if (recruiterPassword.length() < 8){
//                    throw new IllegalArgumentException("Password must be at least 8 characters");
                    return new HandlerAuthDTO("02", new RegisterDTO());
                }else{
                    if (recruiterPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.])(?=\\S+$).{8,}$")){
//
                            try{
                                passwordHash = passwordEncoder.encode(recruiterPassword);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            try {
                                Algorithm algorithm = Algorithm.HMAC256("secret");
                                String token = JWT.create()
                                        .withIssuer("auth0")
                                        .withClaim("email", recruiterEmail)
                                        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                                        .sign(algorithm);

                                MimeMessage mimeMessage = emailSender.createMimeMessage();
                                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

//                                String link = "http://localhost:9091/api/v1/auth/activate?token=" + token; //local
                                String link = "https://toptalentapp.com:9091/api/v1/auth/activate?token=" + token; //server

                                Template template = config.getTemplate("verify.html");
                                Map<String, String> model = new HashMap<>();
                                model.put("name", recruiterCompany);
                                model.put("link", link);
                                String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
                                //String link = "http://54.255.4.75:9091/api/v1/auth/activate?token=" + token;


                                helper.setFrom("springframework007@gmail.com");
                                helper.setTo(recruiterEmail);
                                helper.setSubject("Activate Your Email");
                                helper.setText(html, true);
//                                helper.setText("To activate your email, please click link down below:\n" + "http://54.255.4.75:9091/api/v1/auth/activate?token=" + token);
//                                message.setText("To activate your email, please click link down below:\n" + "http://localhost:9091/api/v1/auth/activate?token=" + token);
                                emailSender.send(mimeMessage);
                            }catch (JWTCreationException exception){
                                //Invalid Signing configuration / Couldn't convert Claims.
                                return null;
                            } catch (MessagingException e) {
                                e.printStackTrace();
                            } catch (TemplateNotFoundException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            } catch (MalformedTemplateNameException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
//                            } catch (TemplateException e) {
                                e.printStackTrace();
                            } catch (TemplateException e) {
                                e.printStackTrace();
                            }
                        Date date = new Date();
                            Timestamp createdAt = new Timestamp(date.getTime());

                            recruiterModel.setRecruiterCompany(recruiterCompany);
                            recruiterModel.setRecruiterEmail(recruiterEmail);
                            recruiterModel.setRecruiterPassword(passwordHash);
                            recruiterModel.setRecruiterIndustry(recruiterIndustry);
                            recruiterModel.setRecruiterStatus("notactive");
                            recruiterModel.setCreatedAt(createdAt);

                            var registerDTO = convertRegister(recruiterRepository.save(recruiterModel));
                            return new HandlerAuthDTO("00",registerDTO);

//
                    }else{
                        return new HandlerAuthDTO("03", new RegisterDTO());
                    }
                }
            }
        }else{
            return new HandlerAuthDTO("04", new RegisterDTO());
        }

    }

    //login
    public HandlerAuthDTO login(String recruiterEmail, String recruiterPassword){
        Optional<RecruiterModel> recruiterOpt = recruiterRepository.findByRecruiterEmail(recruiterEmail);

        if (recruiterOpt.isEmpty()){
            return new HandlerAuthDTO("013", new RegisterDTO());
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean isPasswordMatch = passwordEncoder.matches(recruiterPassword, recruiterOpt.get().getRecruiterPassword());

        if (recruiterOpt.isPresent() && isPasswordMatch){
            if (Objects.equals(recruiterOpt.get().getRecruiterStatus(), "active")){
                 var login = convertRegister(recruiterOpt.get());
                return new HandlerAuthDTO("05", login);
            }else{
                //status == nonactive
                return new HandlerAuthDTO("06", new RegisterDTO());
            }

        }else{
            return new HandlerAuthDTO("07", new RegisterDTO());
        }

    }


    public UpdateProfileDTO updateProfile(int recruiterId,
                                          String recruiterEmail,
                                          String recruiterCompany,
                                          String recruiterIndustry,
                                          String recruiterPhone,
                                          String recruiterStaff,
                                          String recruiterDesc,
                                          String recruiterAddress,
                                          String recruiterFb,
                                          String recruiterIg,
                                          String recruiterLinkedin,
                                          String recruiterCulture,
                                          String recruiterBenefit,
                                          String recruiterWebsite){
        Optional<RecruiterModel> currentRecruiterOpt = recruiterRepository.findById(recruiterId);

        //cek apa datanya ada
        if (currentRecruiterOpt.isEmpty()){
            return null;
        }

        //kalau ada update data
        RecruiterModel recruiterModel = currentRecruiterOpt.get();

        recruiterModel.setRecruiterEmail(recruiterEmail);
        recruiterModel.setRecruiterCompany(recruiterCompany);
        recruiterModel.setRecruiterIndustry(recruiterIndustry);
        recruiterModel.setRecruiterPhone(recruiterPhone);
        recruiterModel.setRecruiterStaff(recruiterStaff);
        recruiterModel.setRecruiterDesc(recruiterDesc);
        recruiterModel.setRecruiterAddress(recruiterAddress);
        recruiterModel.setRecruiterFb(recruiterFb);
        recruiterModel.setRecruiterIg(recruiterIg);
        recruiterModel.setRecruiterLinkedin(recruiterLinkedin);
        recruiterModel.setRecruiterCulture(recruiterCulture);
        recruiterModel.setRecruiterBenefit(recruiterBenefit);
        recruiterModel.setRecruiterWebsite(recruiterWebsite);
        recruiterModel.setRecruiterImage(recruiterModel.getRecruiterImage());

        return convertProfile(recruiterRepository.save(recruiterModel));
    }

    //api verify token jwt and get email from token and update status to active
    public boolean verifyToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            String email = jwt.getClaim("email").asString();
            Optional<RecruiterModel> recruiterModelOptional = recruiterRepository.findByRecruiterEmail(email);
            RecruiterModel recruiterModel = recruiterModelOptional.get();
            recruiterModel.setRecruiterStatus("active");
            recruiterRepository.save(recruiterModel);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //resend jwt token verification
    public String resendToken(String email, String recruiterCompany){
        Optional<RecruiterModel> recruiterModelOptional = recruiterRepository.findByRecruiterEmail(email);
        if (recruiterModelOptional.isEmpty()){
            return null;
        }else{
            try{
                Algorithm algorithm = Algorithm.HMAC256("secret");
                String token = JWT.create().withClaim("email", email)
                        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                        .sign(algorithm);

                MimeMessage mimeMessage = emailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

//                String link = "http://localhost:9091/api/v1/auth/activate?token=" + token; //local
                String link = "https://toptalentapp.com:9091/api/v1/auth/activate?token=" + token; //server

                Template template = config.getTemplate("verify.html");
                Map<String, String> model = new HashMap<>();
                model.put("name", recruiterCompany);
                model.put("link", link);
                String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

                helper.setFrom("springframework007@gmail.com");
                helper.setTo(email);
                helper.setSubject("Activate Your Email");
                helper.setText(html, true);

                emailSender.send(mimeMessage);

                return token;

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }

    //reset password, check if email exists in db
    public int resetPassword(String email){
        Optional<RecruiterModel> recruiterOpt = recruiterRepository.findByRecruiterEmail(email);

        if (recruiterOpt.isEmpty()){
            return 1;
        }

        try{
            Algorithm algorithm = Algorithm.HMAC256("secret");
            String token = JWT.create().withClaim("email", email)
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .sign(algorithm);

            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
//            String link = "http://localhost:9091/api/v1/auth/verify?token=" + token; //local
            String link = "https://toptalentapp.com:9091/api/v1/auth/verify?token=" + token; //server

            Template t = config.getTemplate("forgot-password.html");
            Map<String, String> model = new HashMap<>();
            model.put("name", recruiterOpt.get().getRecruiterCompany());
            model.put("link", link);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            helper.setFrom("springframework007@gmail.com");
            helper.setTo(email);
            helper.setSubject("Reset your password");
            helper.setText(html, true);
            emailSender.send(mimeMessage);
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }

        return 2;
    }

    //verify token change password
    public boolean verifyTokenPassword(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            String email = jwt.getClaim("email").asString();
//            Optional<RecruiterModel> recruiterOpt = recruiterRepository.findByRecruiterEmail(email);
//            RecruiterModel recruiterModel = recruiterOpt.get();
//            recruiterModel.setRecruiterPassword("");
//            recruiterRepository.save(recruiterModel);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //handle change password
    public HandlerAuthDTO changePassword(String email,
                                         String newPassword,
                                         String confirmPassword){

        Optional<RecruiterModel> currentRecruiterOpt = recruiterRepository.findByRecruiterEmail(email);

        System.out.println(currentRecruiterOpt);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        RecruiterModel recruiterModel = currentRecruiterOpt.get();
        String passwordHash="";

        if(newPassword.isEmpty() || confirmPassword.isEmpty()){
            return new HandlerAuthDTO("08", new RegisterDTO());
        }else{
            if (newPassword.length() < 8){
                return new HandlerAuthDTO("09", new RegisterDTO());
            }else{
                if (newPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.])(?=\\S+$).{8,}$")){
                    if (newPassword.equals(confirmPassword)){
                        try{
                            passwordHash = passwordEncoder.encode(newPassword);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        recruiterModel.setRecruiterPassword(passwordHash);
                        var registerDTO = convertRegister(recruiterRepository.save(recruiterModel));
                        return new HandlerAuthDTO("010", registerDTO);
                    }else{
                        return new HandlerAuthDTO("011", new RegisterDTO());
                    }

                }else{
                    return new HandlerAuthDTO("012", new RegisterDTO());
                }
            }
        }
    }

    //phone number validation
    public HandlerUpdateAuthDTO phoneNumberValidation(int recruiterId,
                                         String recruiterPhone){
        Optional<RecruiterModel> currentRecruiterOpt = recruiterRepository.findById(recruiterId);
        if (currentRecruiterOpt.isEmpty()){
            //data not found
            return new HandlerUpdateAuthDTO("014", new UpdateProfileDTO());
        }

        RecruiterModel recruiterModel = currentRecruiterOpt.get();

        if (recruiterPhone.matches("^(\\+62|62|0)8[1-9][0-9]{6,9}$")){
            //change 08 to +62
            if (recruiterPhone.matches("^08[1-9][0-9]{6,9}$")){
                String newPhone = recruiterPhone.replaceAll("^08$", "\\+62");
                recruiterModel.setRecruiterPhone(newPhone);
                System.out.println(newPhone);
            }else {
                recruiterModel.setRecruiterPhone(recruiterPhone);
            }

            var updateProfileDTO = convertProfile(recruiterRepository.save(recruiterModel));
            return new HandlerUpdateAuthDTO("015", updateProfileDTO);
        }else {
            //format salah
            return new HandlerUpdateAuthDTO("016", new UpdateProfileDTO());
        }
    }

}
