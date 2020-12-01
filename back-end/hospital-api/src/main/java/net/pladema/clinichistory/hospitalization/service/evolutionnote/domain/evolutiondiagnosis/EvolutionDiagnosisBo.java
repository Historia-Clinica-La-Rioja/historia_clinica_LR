package net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.evolutiondiagnosis;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.service.ips.domain.DocumentObservationsBo;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class EvolutionDiagnosisBo {

    private DocumentObservationsBo notes;

    private List<Integer> diagnosesId = new ArrayList<>();

}
