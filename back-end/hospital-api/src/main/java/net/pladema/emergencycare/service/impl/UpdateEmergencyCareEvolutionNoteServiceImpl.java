package net.pladema.emergencycare.service.impl;

import lombok.AllArgsConstructor;

import net.pladema.emergencycare.repository.EmergencyCareEvolutionNoteRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateEmergencyCareEvolutionNoteServiceImpl implements UpdateEmergencyCareEvolutionNoteService {

	private static final Logger LOG = LoggerFactory.getLogger(UpdateEmergencyCareEvolutionNoteServiceImpl.class);

	public static final String OUTPUT = "Output -> {}";

	private final EmergencyCareEvolutionNoteRepository emergencyCareEvolutionNoteRepository;

	@Override
	public boolean updateDocumentId(Integer emergencyCareEvolutionNote, Long documentId) {
		LOG.debug("Input parameters emergencyCareEvolutionNote {}, documentId {}", emergencyCareEvolutionNote, documentId);
		emergencyCareEvolutionNoteRepository.findById(emergencyCareEvolutionNote).ifPresent(emergencyCareEvolutionNote1 -> {
			emergencyCareEvolutionNote1.setDocumentId(documentId);
			emergencyCareEvolutionNoteRepository.save(emergencyCareEvolutionNote1);
		});
		LOG.debug(OUTPUT, true);
		return true;
	}

}
