package net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto.evolutiondiagnosis;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DocumentObservationsDto;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class EvolutionDiagnosisDto implements Serializable {

    @Nullable
    private DocumentObservationsDto notes;

    @Nullable
    private List<Integer> diagnosesId = new ArrayList<>();

}
