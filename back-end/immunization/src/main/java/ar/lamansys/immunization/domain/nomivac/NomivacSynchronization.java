package ar.lamansys.immunization.domain.nomivac;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NomivacSynchronization {

    private Integer patientId;

    private LocalDateTime lastSynchronizationTime;

}
