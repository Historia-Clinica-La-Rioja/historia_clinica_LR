package ar.lamansys.sgh.publicapi.infrastructure.output;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Embeddable
@EqualsAndHashCode
public class ExternalPatientPK implements Serializable {

    @Column(name = "external_id", nullable = false)
    private String externalId;

    @Column(name = "patient_id", nullable = false)
    private Integer patientId;
}
