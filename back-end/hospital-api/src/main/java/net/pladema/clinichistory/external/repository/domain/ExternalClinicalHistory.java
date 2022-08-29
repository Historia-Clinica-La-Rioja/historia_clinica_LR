package net.pladema.clinichistory.external.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "external_clinical_history")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ExternalClinicalHistory implements Serializable {

    private static final long serialVersionUID = 8293778155416487256L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "patient_document_type", nullable = false)
    private Short patientDocumentType;

    @Column(name = "patient_document_number", length = 11, nullable = false)
    private String patientDocumentNumber;

    @Column(name = "patient_gender", nullable = false)
    private Short patientGender;

    @Column(name = "professional_name", length = 120, nullable = false)
    private String professionalName;

    @Column(name = "professional_specialty", length = 100, nullable = false)
    private String professionalSpecialty;

    @Column(name = "notes", nullable = false, columnDefinition = "TEXT")
    private String notes;

    @Column(name = "consultation_date", nullable = false)
    private LocalDate consultationDate;

    @Column(name = "institution", nullable = false)
    private String institution;
}
