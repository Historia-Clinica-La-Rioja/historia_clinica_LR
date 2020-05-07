package net.pladema.patient.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "patient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Patient implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 7559172653664261066L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "person_id")
    private Integer personId;

    @Column(name = "type_id", nullable = false)
    private Short typeId;

    @Column(name = "possible_duplicate", nullable = false)
    private Boolean possibleDuplicate = false;

    @Column(name = "national_id")
    private Integer nationalId;
    
    @Column(name = "comments", length = 255)
    private String comments;
    
    @Column(name = "identity_verification_status_id")
    private Short identityVerificationStatusId;
    
    @Column( name = "medical_coverage_name", length = 255)
    private String medicalCoverageName;
 
    @Column( name = "medical_coverage_affiliate_number", length = 150)
    private String medicalCoverageAffiliateNumber;

    @Column(name = "health_insurance_id")
    private Integer healthInsuranceId;
}


