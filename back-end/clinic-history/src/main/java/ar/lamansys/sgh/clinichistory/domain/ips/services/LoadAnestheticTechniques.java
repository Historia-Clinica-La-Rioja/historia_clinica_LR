package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticTechniqueBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.AnestheticTechniqueRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.TrachealIntubationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.AnestheticTechnique;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.TrachealIntubation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoadAnestheticTechniques {

    private final SnomedService snomedService;
    private final AnestheticTechniqueRepository anestheticTechniqueRepository;
    private final DocumentService documentService;
    private final TrachealIntubationRepository trachealIntubationRepository;

    public List<AnestheticTechniqueBo> run(Long documentId, List<AnestheticTechniqueBo> anestheticTechniques) {
        log.debug("Input parameters -> documentId {} anestheticTechniques {}", documentId, anestheticTechniques);

        anestheticTechniques.forEach(anestheticTechniqueBo -> {
            Boolean trachealIntubation = anestheticTechniqueBo.getTrachealIntubation();
            if (anestheticTechniqueBo.getId() == null) {
                Integer snomedId = snomedService.getSnomedId(anestheticTechniqueBo.getSnomed())
                        .orElseGet(() -> snomedService.createSnomedTerm(anestheticTechniqueBo.getSnomed()));
                Short techniqueId = anestheticTechniqueBo.getTechniqueId();
                Short breathingId = anestheticTechniqueBo.getBreathingId();
                Short circuitId = anestheticTechniqueBo.getCircuitId();

                AnestheticTechnique anestheticTechnique = anestheticTechniqueRepository.save(new AnestheticTechnique(null, documentId, snomedId, techniqueId, trachealIntubation, breathingId, circuitId));
                anestheticTechniqueBo.setId(anestheticTechnique.getId());
            }
            documentService.createDocumentAnestheticTechnique(documentId, anestheticTechniqueBo.getId());

            if (trachealIntubation)
                anestheticTechniqueBo.getTrachealIntubationMethodIds()
                        .forEach(methodId -> trachealIntubationRepository.save(new TrachealIntubation(anestheticTechniqueBo.getId(), methodId)));
        });

        log.debug("Output -> {}", anestheticTechniques);
        return anestheticTechniques;
    }
}
