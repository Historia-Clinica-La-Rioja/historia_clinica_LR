package net.pladema.clinichistory.outpatient.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.repository.listener.InternationAuditableEntity;
import net.pladema.clinichistory.hospitalization.repository.listener.InternationListener;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "outpatient_consultation")
@EntityListeners(InternationListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class OutpatientConsultation extends InternationAuditableEntity {

    /**
     *
     */
    private static final long serialVersionUID = 2944833030892268840L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "patient_id", nullable = false)
    private Integer patientId;

    @Column(name = "clinical_specialty_id", nullable = false)
    private Integer clinicalSpecialtyId;

    @Column(name = "institution_id", nullable = false)
    private Integer institutionId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Column(name = "doctor_id", nullable = false)
    private Integer doctorId;

    @Column(name = "billable", nullable = false)
    private Boolean billable;

    public OutpatientConsultation(Integer institutionId, Integer patientId, Integer doctorId, boolean billable) {
        super();
        this.institutionId = institutionId;
        this.patientId = patientId;
        this.billable = billable;
        this.startDate = LocalDate.now();
        this.doctorId = doctorId;
    }
}