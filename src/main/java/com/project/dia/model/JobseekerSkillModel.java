package com.project.dia.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "jobseeker_skill")
public class JobseekerSkillModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "jobseeker_id")
    private int jobseekerId;
    @Column(name = "skill_id")
    private int skillId;
}
