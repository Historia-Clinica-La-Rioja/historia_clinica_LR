package ar.lamansys.sgh.publicapi.infrastructure.output;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "v_attention")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class VAttention extends SGXAuditableEntity<Long> {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "patient_id")
    private Integer patientId;

    @Column(name = "institution_id")
    private Integer institutionId;

    @Column(name = "clinical_speciality_id")
    private Integer clinicalSpecialityId;

    @Column(name = "performed_date")
    private LocalDateTime performedDate;

    @Column(name = "scope_id")
    private Short scopeId;

    @Column(name = "encounter_id")
    private Integer encounterId;

    @Column(name = "doctor_id")
    private Integer doctorId;
}
