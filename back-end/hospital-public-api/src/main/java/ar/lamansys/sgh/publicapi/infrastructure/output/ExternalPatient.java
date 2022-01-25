package ar.lamansys.sgh.publicapi.infrastructure.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "external_patient")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ExternalPatient {

    @EmbeddedId
    private ExternalPatientPK externalPatientPK;

}
