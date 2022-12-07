package com.project.dia.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.dia.dto.*;
import com.project.dia.model.*;
import com.project.dia.remote.digicourse.response.DigicourseResponse;
import com.project.dia.remote.digicourse.response.DigicourseService;
import com.project.dia.repository.*;
import com.project.dia.utils.RandomStringGenerator;
import freemarker.core.ParseException;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.Call;
import retrofit2.Response;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobseekerService {
    private static final long EXPIRATION_TIME = 1800000; // 30 minutes
    //    private final Path root = Paths.get("x:/Upload");
    private final Path root = Paths.get("/home/diabatch3/toptalent/cv/");
    private static final List<String> imageContentTypes = Arrays.asList("image/png", "image/jpeg", "image/jpg", "multipart/form-data");
    private static final List<String> applicationContentTypes = Arrays.asList("application/pdf", "multipart/form-data", "image/png");

    @Autowired
    private JobseekerRepository jobseekerRepository;

    @Autowired
    private JobModelRepository jobModelRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private JobseekerSalaryRepository jobseekerSalaryRepository;

    @Autowired
    private ApplicationModelRepository applicationModelRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private Configuration configuration;

    @Autowired
    private SkillsRepository skillsRepository;

    @Autowired
    private JobseekerSkillRepository jobseekerSkillRepository;

    @Autowired
    private DigicourseService digicourseService;

    @Autowired
    private ApplicantsModelRepository applicantsModelRepository;

    // convert DTO Register Jobseeker
    public RegisterJobseekerDTO convertRegister(JobseekerModel jobseekerModel) {
        RegisterJobseekerDTO registerJobseekerDTO = new RegisterJobseekerDTO();
        registerJobseekerDTO.setJobseekerId(jobseekerModel.getJobseekerId());
        registerJobseekerDTO.setJobseekerName(jobseekerModel.getJobseekerName());
        registerJobseekerDTO.setJobseekerEmail(jobseekerModel.getJobseekerEmail());
        return registerJobseekerDTO;
    }

    // convert DTO Jobseeker
    public JobseekerDTO convertJobseekerDTO(JobseekerModel jobseekerModel) {
        JobseekerDTO jobseekerDTO = new JobseekerDTO();
        jobseekerDTO.setJobseekerId(jobseekerModel.getJobseekerId());
        jobseekerDTO.setJobseekerName(jobseekerModel.getJobseekerName());
        jobseekerDTO.setJobseekerEmail(jobseekerModel.getJobseekerEmail());
        jobseekerDTO.setJobseekerImage(jobseekerModel.getJobseekerImage());
        jobseekerDTO.setJobseekerEducation(jobseekerModel.getJobseekerEducation());
        jobseekerDTO.setJobseekerResume(jobseekerModel.getJobseekerResume());
        jobseekerDTO.setJobseekerAddress(jobseekerModel.getJobseekerAddress());
        jobseekerDTO.setJobseekerPhone(jobseekerModel.getJobseekerPhone());
        jobseekerDTO.setJobseekerAbout(jobseekerModel.getJobseekerAbout());
        jobseekerDTO.setJobseekerDateOfBirth(jobseekerModel.getJobseekerDateOfBirth());
        jobseekerDTO.setJobseekerPortfolio(jobseekerModel.getJobseekerPortfolio());
        jobseekerDTO.setJobseekerProfession(jobseekerModel.getJobseekerProfession());
        jobseekerDTO.setJobseekerMedsos(jobseekerModel.getJobseekerMedsos());
        jobseekerDTO.setJobseekerSkill(jobseekerModel.getJobseekerSkill());
        jobseekerDTO.setJobseekerCompany(jobseekerModel.getJobseekerCompany());
        jobseekerDTO.setWorkStartYear(jobseekerModel.getWorkStartYear());
        jobseekerDTO.setWorkEndYear(jobseekerModel.getWorkEndYear());
        return jobseekerDTO;
    }


    //convert DTO JobseekerDetail
    public JobseekerDetailDTO convertDTO(JobseekerModel jobseekerModel) {
        JobseekerDetailDTO jobseekerDetailDTO = new JobseekerDetailDTO();
        List<SkillNameDTO> results = new ArrayList<>();

        jobseekerDetailDTO.setJobseekerId(jobseekerModel.getJobseekerId());
        jobseekerDetailDTO.setJobseekerName(jobseekerModel.getJobseekerName());
        jobseekerDetailDTO.setJobseekerEmail(jobseekerModel.getJobseekerEmail());
        if (jobseekerModel.getJobseekerImage() != null) {
            jobseekerDetailDTO.setJobseekerImage("http://54.251.83.205:9091/resources/photo/" + jobseekerModel.getJobseekerImage());
        } else {
            jobseekerDetailDTO.setJobseekerImage(jobseekerModel.getJobseekerImage());
        }
        jobseekerDetailDTO.setJobseekerEducation(jobseekerModel.getJobseekerEducation());
        if (jobseekerModel.getJobseekerResume() != null) {
            jobseekerDetailDTO.setJobseekerResume("http://54.251.83.205:9091/resources/cv/" + jobseekerModel.getJobseekerResume());
        } else {
            jobseekerDetailDTO.setJobseekerResume(jobseekerModel.getJobseekerResume());
        }
        jobseekerDetailDTO.setJobseekerAddress(jobseekerModel.getJobseekerAddress());
        jobseekerDetailDTO.setJobseekerPhone(jobseekerModel.getJobseekerPhone());
        jobseekerDetailDTO.setJobseekerAbout(jobseekerModel.getJobseekerAbout());
        jobseekerDetailDTO.setJobseekerDateOfBirth(jobseekerModel.getJobseekerDateOfBirth());
        jobseekerDetailDTO.setJobseekerProfession(jobseekerModel.getJobseekerProfession());
        jobseekerDetailDTO.setJobseekerPortfolio(jobseekerModel.getJobseekerPortfolio());
        jobseekerDetailDTO.setJobseekerMedsos(jobseekerModel.getJobseekerMedsos());

        List<JobseekerSkillModel> jobseekerSkillModels = jobseekerSkillRepository.findByJobseekerId(jobseekerModel.getJobseekerId());

        List<Integer> skills = jobseekerSkillModels.stream().map(JobseekerSkillModel::getSkillId).distinct().collect(Collectors.toList());

        skills.forEach(skill -> {
            SkillNameDTO skillNameDTO = new SkillNameDTO();
            skillNameDTO.setSkillId(skillsRepository.findById(skill).get().getSkillId());
            skillNameDTO.setSkillName(skillsRepository.findById(skill).get().getSkillName());
            results.add(skillNameDTO);
        });

        jobseekerDetailDTO.setSkills(results);
        jobseekerDetailDTO.setJobseekerCompany(jobseekerModel.getJobseekerCompany());

        JobseekerSalary jobseekerSalary = jobseekerSalaryRepository.getDataSalary(jobseekerModel.getJobseekerId());
        JobseekerSalaryDTO jobseekerSalaryDTO = new JobseekerSalaryDTO();
        if (jobseekerSalary != null){
            jobseekerSalaryDTO.setSalaryId(jobseekerSalary.getSalaryId());
            jobseekerSalaryDTO.setJobseekerId(jobseekerModel.getJobseekerId());
            jobseekerSalaryDTO.setCurrentSalary(jobseekerSalary.getCurrentSalary());
            jobseekerSalaryDTO.setCurrentCurrency(jobseekerSalary.getCurrentCurrency());
            jobseekerSalaryDTO.setExpectedCurrency(jobseekerSalary.getExpectedCurrency());
            jobseekerSalaryDTO.setExpectedMinimum(jobseekerSalary.getExpectedMinimum());
            jobseekerSalaryDTO.setExpectedMaximum(jobseekerSalary.getExpectedMaximum());
            jobseekerDetailDTO.setJobseekerSalary(jobseekerSalaryDTO);
        }else{
            jobseekerDetailDTO.setJobseekerSalary(null);
        }

        jobseekerDetailDTO.setWorkStartYear(jobseekerModel.getWorkStartYear());
        jobseekerDetailDTO.setWorkEndYear(jobseekerModel.getWorkEndYear());
        return jobseekerDetailDTO;
    }


    // convert DTO Job Details
    public JobDetailsWithStatusDTO convertJobDetailWithStatusDTO(ApplicationModel applicationModel) {
        var jobModel = jobModelRepository.findById(applicationModel.getJobId());
        var recruiterModel = recruiterRepository.findById(jobModel.get().getRecruiterId());
        if (recruiterModel.isPresent()) {
            return new JobDetailsWithStatusDTO(
                    jobModel.get().getJobId(),
                    jobModel.get().getJobName(),
                    jobModel.get().getJobSalary(),
                    jobModel.get().getJobPosition(),
                    jobModel.get().getJobAddress(),
                    jobModel.get().getJobDesc(),
                    jobModel.get().getJobRequirement(),
                    jobModel.get().getJobStatus(),
                    jobModel.get().getCreatedAt(),
                    recruiterModel.get().getRecruiterImage(),
                    recruiterModel.get().getRecruiterCompany(),
                    recruiterModel.get().getRecruiterDesc(),
                    recruiterModel.get().getRecruiterIndustry(),
                    recruiterModel.get().getRecruiterStaff(),
                    recruiterModel.get().getRecruiterBenefit(),
                    recruiterModel.get().getRecruiterFb(),
                    recruiterModel.get().getRecruiterLinkedin(),
                    recruiterModel.get().getRecruiterIg(),
                    recruiterModel.get().getRecruiterCulture(),
                    recruiterModel.get().getRecruiterWebsite(),
                    applicationModel.getApplicationStatus());
        } else {
            return null;
        }
    }

    public JobDetailsDTO convertJobDetailDTO(JobModel jobModel) {
        var recruiterModel = recruiterRepository.findById(jobModel.getRecruiterId());
        if (recruiterModel.isPresent()) {
            return new JobDetailsDTO(
                    jobModel.getJobId(),
                    jobModel.getJobName(),
                    jobModel.getJobSalary(),
                    jobModel.getJobPosition(),
                    jobModel.getJobAddress(),
                    jobModel.getJobDesc(),
                    jobModel.getJobRequirement(),
                    jobModel.getJobStatus(),
                    jobModel.getCreatedAt(),
                    recruiterModel.get().getRecruiterImage(),
                    recruiterModel.get().getRecruiterAddress(),
                    recruiterModel.get().getRecruiterCompany(),
                    recruiterModel.get().getRecruiterDesc(),
                    recruiterModel.get().getRecruiterIndustry(),
                    recruiterModel.get().getRecruiterStaff(),
                    recruiterModel.get().getRecruiterBenefit(),
                    recruiterModel.get().getRecruiterFb(),
                    recruiterModel.get().getRecruiterLinkedin(),
                    recruiterModel.get().getRecruiterIg(),
                    recruiterModel.get().getRecruiterCulture(),
                    recruiterModel.get().getRecruiterWebsite());
        } else {
            return null;
        }
    }

    // Convert DTO PostJob
    public PostJobDTO convertPostJobDTO(JobModel jobModel) {
        var recruiterModel = recruiterRepository.findById(jobModel.getRecruiterId());
        if (recruiterModel.isPresent()) {
            return new PostJobDTO(
                    jobModel.getJobId(),
                    jobModel.getJobName(),
                    jobModel.getJobSalary(),
                    jobModel.getJobPosition(),
                    jobModel.getJobAddress(),
                    jobModel.getJobDesc(),
                    jobModel.getJobRequirement(),
                    jobModel.getJobStatus(),
                    jobModel.getCreatedAt(),
                    recruiterModel.get().getRecruiterImage(),
                    recruiterModel.get().getRecruiterCompany(),
                    recruiterModel.get().getRecruiterDesc());

        } else {
            return null;
        }
    }

    // Convert Apply Job DTO
    public ApplicationDTO convertApplyJob(ApplicationModel applicationModel) {
        var jobseekerModel = jobseekerRepository.findById(applicationModel.getJobseekerId());
        var recruiterModel = recruiterRepository.findById(applicationModel.getRecruiterId());

        return jobseekerModel.map(model -> new ApplicationDTO(
                applicationModel.getApplicationId(),
                applicationModel.getJobId(),
                jobseekerModel.get().getJobseekerId(),
                recruiterModel.get().getRecruiterId(),
                applicationModel.getApplicationStatus(),
                jobseekerModel.get().getJobseekerResume(),
                applicationModel.getCreatedAt()
        )).orElseGet(ApplicationDTO::new);
    }

    // Convert Apply Job Status
    public ApplyDTO convertApplyJobStatus(ApplicationModel applicationModel) {
        var jobModel = jobModelRepository.findById(applicationModel.getJobId());
        var recruiterModel = recruiterRepository.findById(jobModel.get().getRecruiterId());
        return jobModel.map(model -> new ApplyDTO(
                applicationModel.getJobId(),
                model.getJobName(),
                recruiterModel.get().getRecruiterCompany(),
                recruiterModel.get().getRecruiterAddress(),
                applicationModel.getApplicationStatus(),
                recruiterModel.get().getRecruiterImage(),
                applicationModel.getCreatedAt())).orElseGet(ApplyDTO::new);
    }

    // API read jobseeker by id
    public JobseekerDetailDTO getJobseekerById(int jobseekerId) {
        Optional<JobseekerModel> jobseekerModelOptional = jobseekerRepository.findById((jobseekerId));
        if (jobseekerModelOptional.isEmpty()) {
            return null;
        }
        return convertDTO(jobseekerModelOptional.get());
    }


    // API read all jobseeker
    public List<JobseekerDTO> getAllJobseeker() {
        List<JobseekerModel> jobseekerModels = jobseekerRepository.findAll();
        return jobseekerModels.stream().map(this::convertJobseekerDTO).collect(Collectors.toList());
    }

    // API read recent job
    public List<PostJobDTO> getRecentJob() {
        List<JobModel> jobModels = jobModelRepository.findAllJobPosting();
        if (jobModels.isEmpty()) {
            return null;
        }
        return jobModels.stream().map(this::convertPostJobDTO).limit(5).collect(Collectors.toList());
    }

    // API read job by id already sent
    public JobDetailsResponseDTO getJobById(int jobId, int jobseekerId) {
        final var jobModelOptional = jobModelRepository.findJobById(jobId);
        final var jobDetail = applicationModelRepository.findJobDetail(jobId, jobseekerId);
        if (jobModelOptional.isEmpty()) {
            return new JobDetailsResponseDTO(1, new JobDetailsDTO(), new JobDetailsWithStatusDTO());
        } else {
            if (jobDetail.isPresent()) {
                return new JobDetailsResponseDTO(0, new JobDetailsDTO(), convertJobDetailWithStatusDTO(jobDetail.get()));
            }
            return new JobDetailsResponseDTO(2, convertJobDetailDTO(jobModelOptional.get()), new JobDetailsWithStatusDTO());
        }
    }


    // API read post job
    public PostJobDTO getPostJobById(int jobId) {
        Optional<JobModel> jobOpt = jobModelRepository.findById((jobId));
        if (jobOpt.isEmpty()) {
            return null;
        }
        return convertPostJobDTO(jobOpt.get());
    }

    // API read apply job status with pagination
    public Page<ApplyDTO> getApplyJobStatus(int jobseekerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("applicationId").descending());
        Page<ApplicationModel> applicationModel = applicationModelRepository.findApplyJobs(jobseekerId, pageable);
        return applicationModel.map(this::convertApplyJobStatus);
    }

    // API read all posting jobs with pagination
    public Page<PostJobDTO> getAllJobsWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("jobId").descending());
        Page<JobModel> jobModels = jobModelRepository.findAllJobPostingPagination(pageable);
        return jobModels.map(this::convertPostJobDTO);
    }


    // API Register Jobseeker
    public HandlerRegisterDTO createUser(String jobseekerName,
                                         String jobseekerEmail,
                                         String jobseekerPassword) {
        Optional<JobseekerModel> jobseekerOpt = jobseekerRepository.findByJobseekerEmail(jobseekerEmail);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        JobseekerModel jobseekerModel = new JobseekerModel();
        String passwordHash = "";
        if (jobseekerOpt.isEmpty()) {
            if (jobseekerEmail.isEmpty() || jobseekerPassword.isEmpty() || jobseekerName.isEmpty()) {
                return new HandlerRegisterDTO("01", new RegisterJobseekerDTO());
            } else {
                if (jobseekerPassword.length() < 8) {
                    return new HandlerRegisterDTO("02", new RegisterJobseekerDTO());
                } else {
                    if (jobseekerPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.])(?=\\S+$).{8,}$")) {
                        try {
                            passwordHash = passwordEncoder.encode(jobseekerPassword);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            Algorithm algorithm = Algorithm.HMAC256("secret");
                            String token = JWT.create()
                                    .withIssuer("auth0")
                                    .withClaim("email", jobseekerEmail)
                                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                                    .sign(algorithm);

                            MimeMessage mimeMessage = emailSender.createMimeMessage();
                            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

//                                String link = "https://localhost:9091/api/v1/jobseeker/activate?token=" + token;
                            String link = "http://54.251.83.205:9091/api/v1/jobseeker/activate?token=" + token;

                            Template t = configuration.getTemplate("verify.html");
                            Map<String, String> model = new HashMap<>();
                            model.put("name", jobseekerName);
                            model.put("link", link);
                            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);


                            helper.setFrom("springframework007@gmail.com");
                            helper.setTo(jobseekerEmail);
                            helper.setSubject("Activate your account");
                            helper.setText(html, true);
                            emailSender.send(mimeMessage);
                        } catch (JWTCreationException exception) {
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
                        } catch (TemplateException e) {
                            e.printStackTrace();
                        }
                        jobseekerModel.setJobseekerName(jobseekerName);
                        jobseekerModel.setJobseekerEmail(jobseekerEmail);
                        jobseekerModel.setJobseekerPassword(passwordHash);
                        jobseekerModel.setJobseekerCreatedAt(new Date());
                        jobseekerModel.setStatus("notactive");
                        final var registerJobseekerDTO = convertRegister(jobseekerRepository.save(jobseekerModel));

                        try {
                            String institutId = "13";

                            Call<DigicourseResponse> call = digicourseService.registerUSer(jobseekerEmail,
                                    jobseekerPassword, institutId);
                            Response<DigicourseResponse> response = call.execute();

                            DigicourseResponse body = response.body();

                            if (body.getCode().equals("200")) {
                                // success insert data to digicourse

                            } else {
                                // error insert data to digicourse
                            }
                        } catch (Exception e) {
                        }

                        return new HandlerRegisterDTO("00", registerJobseekerDTO);
                    } else {
                        return new HandlerRegisterDTO("02", new RegisterJobseekerDTO());
                    }
                }
            }
        } else {
            return new HandlerRegisterDTO("03", new RegisterJobseekerDTO());
        }
    }


    //API Login Jobseeker
    public HandlerAuthJobSeekerDTO loginUser(String jobseekerEmail,
                                             String jobseekerPassword) {
        Optional<JobseekerModel> jobseekerOpt = jobseekerRepository.findByJobseekerEmail(jobseekerEmail);

        if (jobseekerOpt.isEmpty()) {
            return new HandlerAuthJobSeekerDTO("013", new RegisterJobseekerDTO());
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean isPasswordMatch = passwordEncoder.matches(jobseekerPassword, jobseekerOpt.get().getJobseekerPassword());

        if (jobseekerOpt.isPresent() && isPasswordMatch) {
            var login = convertRegister(jobseekerOpt.get());
            return new HandlerAuthJobSeekerDTO("05", login);
        } else {
            return new HandlerAuthJobSeekerDTO("07", new RegisterJobseekerDTO());
        }

//        final var jobseekerModelOptional = jobseekerRepository.findJobseekerEmail(jobseekerEmail);
//        if (jobseekerModelOptional.isEmpty()) {
//            return null;
//        }
//        boolean isPasswordMatch = passwordEncoder.matches(jobseekerPassword, jobseekerModelOptional.get().getJobseekerPassword());
//        if (isPasswordMatch){
//            return convertRegister(jobseekerModelOptional.get());
//        }else {
//            return null;
//        }
    }

    //API update CV Jobseeker
    public JobseekerDTO updateResume(int jobseekerId,
                                     MultipartFile resume) {
        final var userId = jobseekerRepository.findById(jobseekerId);
        String applicationContentType = resume.getContentType();
        JobseekerModel jobseekerModel = userId.get();
        RandomStringGenerator randomStringGenerator = new RandomStringGenerator();
        String randomString = randomStringGenerator.getRandom();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        String nameApp = userId.get().getJobseekerName().replaceAll(" ", "");
        if (userId.isEmpty()) {
            return null;
        }
        if (applicationContentTypes.contains(applicationContentType)) {
            try {
                if (!Files.exists(root)) {
                    Files.createDirectory(root);
                }
                if (userId.get().getJobseekerResume() != null) {
                    Path file = root.resolve(userId.get().getJobseekerResume());
                    Files.deleteIfExists(file);
                }
                if (resume.getOriginalFilename().contains(".pdf")) {
                    String contentType = resume.getContentType();
                    contentType = contentType.toLowerCase().replaceAll(applicationContentType, ".pdf");

                    // copy file into directory
                    Files.copy(resume.getInputStream(), this.root.resolve(nameApp + "-" + dtf.format(now) + "-" + randomString + contentType));
                    // insert into database
                    jobseekerModel.setJobseekerResume(nameApp + "-" + dtf.format(now) + "-" + randomString + contentType);

                } else if (resume.getOriginalFilename().contains(".png")) {
                    // get content file type
                    String contentType = resume.getContentType();
                    contentType = contentType.toLowerCase().replaceAll(applicationContentType, ".png");

                    // copy file into directory with a random string
                    Files.copy(resume.getInputStream(), this.root.resolve(nameApp + "-" + dtf.format(now) + "-" + randomString + contentType));
                    // insert into database
                    jobseekerModel.setJobseekerResume(nameApp + "-" + dtf.format(now) + "-" + randomString + contentType);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
        return convertJobseekerDTO(jobseekerRepository.save(jobseekerModel));
    }

    //get CV Jobseeker
    public Resource getResume(int jobseekerId) throws MalformedURLException {
        final var userId = jobseekerRepository.findById(jobseekerId);
        if (userId.isEmpty()) {
            return null;
        }
        Path file = root.resolve(userId.get().getJobseekerResume());
        Resource resource = new UrlResource(file.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("Could not read the file!");
        }
    }

    //API update Photo Profile Jobseeker
    public JobseekerDTO updatePhotoProfile(int jobseekerId,
                                           MultipartFile image) {
        final var userId = jobseekerRepository.findById(jobseekerId);
        String imageContentType = image.getContentType();

        RandomStringGenerator randomStringGenerator = new RandomStringGenerator();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        String nameApp = userId.get().getJobseekerName().replaceAll(" ", "");
        if (userId.isEmpty()) {
            return null;
        }

        JobseekerModel jobseekerModel = userId.get();

        if (imageContentTypes.contains(imageContentType)) {
            try {
                if (!Files.exists(root)) {
                    Files.createDirectory(root);
                }

                // get content file type
                String contentType = image.getContentType();
                contentType = contentType.toLowerCase().replaceAll(imageContentType, ".png");

                // generated random string to a variable
                String randomString = randomStringGenerator.getRandom();

                // copy file into directory with a random string
                Files.copy(image.getInputStream(), this.root.resolve(nameApp + "-" + dtf.format(now) + "-" + randomString + contentType));
                // insert into database
                jobseekerModel.setJobseekerImage(nameApp + "-" + dtf.format(now) + "-" + randomString + contentType);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
        return convertJobseekerDTO(jobseekerRepository.save(jobseekerModel));
    }

    // API Update Data Jobseeker
    public JobseekerDetailDTO updateJobseeker(int jobseekerId,
                                              String jobseekerName,
                                              String jobseekerEmail,
                                              String jobseekerEducation,
                                              String jobseekerAddress,
                                              String jobseekerPhone,
                                              String jobseekerAbout,
                                              LocalDate jobseekerDateOfBirth,
                                              String jobseekerProfession,
                                              String jobseekerPortfolio,
                                              String jobseekerMedsos,
                                              String jobseekerSkill,
                                              String jobseekerCompany,
                                              int workStartYear,
                                              int workEndYear) {
        Optional<JobseekerModel> jobseekerModelOptional = jobseekerRepository.findById(jobseekerId);
        if (jobseekerModelOptional.isEmpty()) {
            return null;
        }
        JobseekerModel jobseekerModel = jobseekerModelOptional.get();
        jobseekerModel.setJobseekerName(jobseekerName);
        jobseekerModel.setJobseekerEmail(jobseekerEmail);
        jobseekerModel.setJobseekerEducation(jobseekerEducation);
        jobseekerModel.setJobseekerAddress(jobseekerAddress);
        jobseekerModel.setJobseekerPhone(jobseekerPhone);
        jobseekerModel.setJobseekerAbout(jobseekerAbout);
        jobseekerModel.setJobseekerDateOfBirth(jobseekerDateOfBirth);
        jobseekerModel.setJobseekerProfession(jobseekerProfession);
        jobseekerModel.setJobseekerPortfolio(jobseekerPortfolio);
        jobseekerModel.setJobseekerMedsos(jobseekerMedsos);

        if (!jobseekerSkill.isEmpty()) {
            if (jobseekerSkillRepository.findByJobseekerId(jobseekerId).isEmpty()) {
                String[] splitString = jobseekerSkill.split(",");
                for (String skill : splitString) {
                    JobseekerSkillModel jobseekerSkillModel = new JobseekerSkillModel();
                    jobseekerSkillModel.setJobseekerId(jobseekerId);
                    jobseekerSkillModel.setSkillId(Integer.parseInt(skill));
                    jobseekerSkillRepository.save(jobseekerSkillModel);
                }
            } else {
                jobseekerSkillRepository.deleteByJobseekerId(jobseekerId);
                String[] splitString = jobseekerSkill.split(",");
                for (String skill : splitString) {
                    JobseekerSkillModel jobseekerSkillModel = new JobseekerSkillModel();
                    jobseekerSkillModel.setJobseekerId(jobseekerId);
                    jobseekerSkillModel.setSkillId(Integer.parseInt(skill));
                    jobseekerSkillRepository.save(jobseekerSkillModel);
                }
            }
        }
        jobseekerModel.setJobseekerCompany(jobseekerCompany);
        jobseekerModel.setWorkStartYear(workStartYear);
        jobseekerModel.setWorkEndYear(workEndYear);
        return convertDTO(jobseekerRepository.save(jobseekerModel));

    }


    // API apply job
    public int applyJob(int jobId, int jobseekerId) {
        Optional<JobModel> jobModelOptional = jobModelRepository.findById(jobId);
        Optional<JobseekerModel> jobseekerModelOptional = jobseekerRepository.findById(jobseekerId);
        Optional<ApplicationModel> jobIdAndJobSeekerId = applicationModelRepository.findJobIdAndJobSeekerId(jobId, jobseekerId);
        ApplicationModel applicationModel = new ApplicationModel();

        if (jobModelOptional.isEmpty() || jobseekerModelOptional.isEmpty()) {
            return 1;
        }
        if (jobIdAndJobSeekerId.isEmpty()) {
            Date date = new Date();
//            Timestamp createdAt = new Timestamp(date.getTime());
            applicationModel.setJobId(jobId);
            applicationModel.setJobseekerId(jobseekerId);
            applicationModel.setApplicationStatus("Sent");
            applicationModel.setCreatedAt(LocalDateTime.now());
            applicationModel.setRecruiterId(jobModelOptional.get().getRecruiterId());
            convertApplyJob(applicationModelRepository.save(applicationModel));
            return 0;
        } else {
            return 2;
        }

    }


    // api verify token jwt and get the email from token and update jobseeker table to active
    public boolean verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            String email = jwt.getClaim("email").asString();
            Optional<JobseekerModel> jobseekerModelOptional = jobseekerRepository.findByJobseekerEmail(email);
            JobseekerModel jobseekerModel = jobseekerModelOptional.get();
            jobseekerModel.setStatus("active");
            jobseekerRepository.save(jobseekerModel);

            //send user data to digicourse
            String jobseekerEmail = jobseekerModel.getJobseekerEmail();
            try {
                Call<DigicourseResponse> activateCall = digicourseService.activate(jobseekerEmail);
                activateCall.execute();
            } catch (Exception e) {
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // resend email verification to jobseeker
    public String resendToken(String email) {
        Optional<JobseekerModel> jobseekerModelOptional = jobseekerRepository.findByJobseekerEmail(email);
        if (jobseekerModelOptional.isEmpty()) {
            return null;
        } else {
            try {
                Algorithm algorithm = Algorithm.HMAC256("secret");
                String token = JWT.create().withClaim("email", email)
                        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                        .sign(algorithm);
                MimeMessage mimeMessage = emailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

//                String link = "https://localhost:9091/api/v1/jobseeker/activate?token=" + token;
                String link = "http://54.251.83.205:9091/api/v1/jobseeker/activate?token=" + token;
                Template t = configuration.getTemplate("verify.html");
                Map<String, String> model = new HashMap<>();
                model.put("name", jobseekerModelOptional.get().getJobseekerName());
                model.put("link", link);
                String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

                helper.setFrom("springframework007@gmail.com");
                helper.setTo(email);
                helper.setSubject("Activate your account");
                helper.setText(html, true);
                emailSender.send(mimeMessage);
                return token;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    // service for search posting job
    public List<PostJobDTO> searchJobByKeyword(String keyword) {
        final var jobModels = jobModelRepository.searchJobByKeyword(keyword);
        if (jobModels.isEmpty()) {
            return null;
        }
        return jobModels.stream().map(this::convertPostJobDTO).collect(Collectors.toList());
    }

    // service for search posting job
    public int searchJobByKeywordCount(String keyword) {
        final var jobModels = jobModelRepository.searchJobByKeyword(keyword);
        int result = 0;
        if (jobModels.isEmpty()){
            return result;
        }else {
            result = jobModels.size();
            return result;
        }
    }

    // service for reset password using jwt token by email
    public String resetPassword(String email) {
        Optional<JobseekerModel> jobseekerModelOptional = jobseekerRepository.findByJobseekerEmail(email);
        if (jobseekerModelOptional.isEmpty()) {
            return "Not Found";
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256("password");
            String token = JWT.create().withClaim("email", email)
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .sign(algorithm);

            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            String link = "http://54.251.83.205/toptalent/#/change-password/" + token;

            Template t = configuration.getTemplate("forgot-password.html");
            Map<String, String> model = new HashMap<>();
            model.put("name", jobseekerModelOptional.get().getJobseekerName());
            model.put("link", link);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);


            helper.setFrom("springframework007@gmail.com");
            helper.setTo(email);
            helper.setSubject("Reset your password");
            helper.setText(html, true);
            emailSender.send(mimeMessage);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Failed";
    }

    // verify jwt reset password link
    public boolean verifyTokenPassword(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("password");
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            String email = jwt.getClaim("email").asString();
            Optional<JobseekerModel> jobseekerModelOptional = jobseekerRepository.findByJobseekerEmail(email);
            JobseekerModel jobseekerModel = jobseekerModelOptional.get();
            jobseekerModel.setJobseekerPassword(null);
            jobseekerRepository.save(jobseekerModel);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // create service to update password by redirect from verify jwt reset password link
    public int updatePassword(String email, String password, String confirmPassword) {
        Optional<JobseekerModel> jobseekerModelOptional = jobseekerRepository.findByJobseekerEmail(email);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (jobseekerModelOptional.isEmpty()) {
            return 1;
        } else {
            if (password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
                return 4;
            } else {
                if (password.length() >= 8) {
                    if (password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.])(?=\\S+$).{8,}$")) {
                        if (password.equals(confirmPassword)) {
                            try {
                                password = passwordEncoder.encode(password);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            jobseekerModelOptional.get().setJobseekerPassword(password);
                            jobseekerRepository.save(jobseekerModelOptional.get());
                            return 0;
                        }
                        return 3;
                    }
                }
                return 2;
            }
        }
    }

    public String forgotHtml() throws IOException {
        try {
            Template t = configuration.getTemplate("/template-main/forgot.html");
            Map<String, String> model = new HashMap<>();
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            return html;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<SkillsModel> getAllSkills() {
        final var all = skillsRepository.findAll();
        return all;
    }

    public List<SkillNameDTO> getAllSkillsById(int jobseekerId) {
        List<SkillNameDTO> results = new ArrayList<>();
        List<JobseekerSkillModel> jobseekerSkillModels = jobseekerSkillRepository.findByJobseekerId(jobseekerId);
        List<Integer> skills = jobseekerSkillModels.stream().map(JobseekerSkillModel::getSkillId).distinct().collect(Collectors.toList());

        skills.forEach(skill -> {
            SkillNameDTO skillNameDTO = new SkillNameDTO();
            skillNameDTO.setSkillId(skillsRepository.findById(skill).get().getSkillId());
            skillNameDTO.setSkillName(skillsRepository.findById(skill).get().getSkillName());
            results.add(skillNameDTO);
        });
        return results;
    }

    //input job salary
    private JobseekerSalaryDTO convertJobseekerDetailSalary(JobseekerSalary jobseekerSalary) {
        return new JobseekerSalaryDTO(jobseekerSalary.getSalaryId(),
                jobseekerSalary.getJobseekerId(),
                jobseekerSalary.getCurrentCurrency(),
                jobseekerSalary.getExpectedCurrency(),
                jobseekerSalary.getCurrentSalary(),
                jobseekerSalary.getExpectedMinimum(),
                jobseekerSalary.getExpectedMaximum());
    }

    public JobseekerSalaryDTO addJobseekerDetailSalary(int jobseekerId, String currentCurrency, String expectedCurrency, float currentSalary, float expectedMinimum, float expectedMaximum) {
        Optional<JobseekerSalary> jobseekerDetailSalary = jobseekerSalaryRepository.findByJobseekerId(jobseekerId);
        if (jobseekerDetailSalary.isEmpty()) {
            JobseekerSalary jobseekerSalary = new JobseekerSalary();
            jobseekerSalary.setJobseekerId(jobseekerId);
            jobseekerSalary.setCurrentCurrency(currentCurrency);
            jobseekerSalary.setExpectedCurrency(expectedCurrency);
            jobseekerSalary.setExpectedMinimum(expectedMinimum);
            jobseekerSalary.setExpectedMaximum(expectedMaximum);
            jobseekerSalary.setCurrentSalary(currentSalary);
            return convertJobseekerDetailSalary(jobseekerSalaryRepository.save(jobseekerSalary));
        } else {
            return null;
        }

    }

    private JobseekerSkillDTO convertJobseekerSkill(JobseekerSkillModel jobseekerSkillModel){
        return new JobseekerSkillDTO(jobseekerSkillModel.getJobseekerId(),
                jobseekerSkillModel.getSkillId()
                );
    }

    public JobseekerSkillDTO addJobseekerSkill(int jobseekerId, int skillId){
        JobseekerSkillModel jobseekerSkillModelOptional = jobseekerSkillRepository.findSkillByJobSeekerId(jobseekerId, skillId);
        if (jobseekerSkillModelOptional == null){
            JobseekerSkillModel jobseekerSkillModel = new JobseekerSkillModel();
            jobseekerSkillModel.setJobseekerId(jobseekerId);
            jobseekerSkillModel.setSkillId(skillId);
            return convertJobseekerSkill(jobseekerSkillRepository.save(jobseekerSkillModel));
        }else{
            return null;
        }

    }


}
