package com.project.dia.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "recruiter_gallery")
public class RecruiterGalleryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruiter_gallery_id")
    private int recruiterGalleryId;

    @Column(name = "recruiter_id")
    private int recruiterId;

    @Column(name = "image")
    private String imageGallery;

    @Column(name = "created_at")
    private java.sql.Timestamp createdAt;
}
