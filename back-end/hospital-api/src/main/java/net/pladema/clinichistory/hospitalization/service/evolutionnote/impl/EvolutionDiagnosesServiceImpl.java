package net.pladema.clinichistory.hospitalization.service.evolutionnote.impl;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.EvolutionDiagnosesService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class EvolutionDiagnosesServiceImpl implements EvolutionDiagnosesService {

    private static final Logger LOG = LoggerFactory.getLogger(EvolutionDiagnosesServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final InternmentEpisodeService internmentEpisodeService;

	private final DocumentFactory documentFactory;

	private final DateTimeProvider dateTimeProvider;

	public EvolutionDiagnosesServiceImpl(InternmentEpisodeService internmentEpisodeService,
										 DateTimeProvider dateTimeProvider,
										 DocumentFactory documentFactory) {
        this.internmentEpisodeService = internmentEpisodeService;
		this.documentFactory = documentFactory;
		this.dateTimeProvider = dateTimeProvider;
    }

    @Override
	@Transactional
    public Long execute(EvolutionNoteBo evolutionNote) {
        LOG.debug("Input parameters -> evolutionNote {}", evolutionNote);
		evolutionNote.getDiagnosis().forEach(diagnosisBo -> diagnosisBo.setId(null));
		evolutionNote.setAllergies(Collections.emptyList());
		evolutionNote.setImmunizations(Collections.emptyList());
		evolutionNote.setProcedures(Collections.emptyList());
		evolutionNote.setPerformedDate(dateTimeProvider.nowDateTime());
		evolutionNote.setId(documentFactory.run(evolutionNote, true));

		internmentEpisodeService.addEvolutionNote(evolutionNote.getInstitutionId(), evolutionNote.getId());
		Long result = evolutionNote.getId();
        LOG.debug(OUTPUT, result);
        return result;
    }

}
