package net.pladema.internation.service.documents.evolutionnote.domain.evolutiondiagnosis;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.service.ips.domain.DocumentObservationsBo;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class EvolutionDiagnosisBo {

    private DocumentObservationsBo notes;

    private List<Integer> diagnosesId = new ArrayList<>();

}
