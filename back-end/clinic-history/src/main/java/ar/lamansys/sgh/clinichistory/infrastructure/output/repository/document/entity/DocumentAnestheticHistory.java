package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "anesthetic_history")
@Entity
public class DocumentAnestheticHistory {

    @Id
    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "state_id", nullable = false)
    private Short stateId;

    @Column(name = "zone_id")
    private Short zoneId;
}
