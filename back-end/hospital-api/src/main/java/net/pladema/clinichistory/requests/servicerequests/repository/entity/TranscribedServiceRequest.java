package net.pladema.clinichistory.requests.servicerequests.repository.entity;

import java.time.LocalDateTime;

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


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "transcribed_service_request")
@Entity
public class TranscribedServiceRequest {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "healthcare_professional_name", nullable = false)
    private String healthcareProfessionalName;

    @Column(name = "institution_name", nullable = false)
    private String institutionName;

    @Column(name = "patient_id", nullable = false)
    private Integer patientId;

    @Column(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(name = "observations", columnDefinition = "TEXT")
    private String observations;

    public TranscribedServiceRequest(String healthcareProfessionalName, String institutionName, Integer patientId, String observations) {
        this.healthcareProfessionalName = healthcareProfessionalName;
        this.institutionName = institutionName;
        this.patientId = patientId;
        this.observations = observations;
    }
}
