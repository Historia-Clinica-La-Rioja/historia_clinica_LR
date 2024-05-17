package net.pladema.errata.general.endpoints.service;

import net.pladema.errata.common.repository.CustomDocumentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.pladema.errata.common.dto.ErrataRequestDTO;
import net.pladema.errata.common.repository.ErrataRepository;
import net.pladema.errata.common.repository.entity.Errata;

@Service
public class ErrataService {

	@Autowired
	private final ErrataRepository errataRepository;
	@Autowired
	private final CustomDocumentRepository customDocumentRepository;

	public ErrataService(ErrataRepository errataRepository, CustomDocumentRepository customDocumentRepository) {
        this.errataRepository = errataRepository;
		this.customDocumentRepository = customDocumentRepository;
	}

	public Errata createErrata(ErrataRequestDTO requestDTO) {

		boolean errataExists = errataRepository.existsByDocumentId(requestDTO.getDocumentId());
		if (errataExists) {
			throw new RuntimeException("An errata already exists for the document with the ID " + requestDTO.getDocumentId());
		}

		boolean isAuthorized = customDocumentRepository.existsByIdAndCreatedByOrUpdatedBy(Long.valueOf(requestDTO.getDocumentId()), requestDTO.getHealthcareProfessionalId(), requestDTO.getHealthcareProfessionalId());
		if (!isAuthorized) {
			throw new RuntimeException("You are not authorized to create an errata for document with ID " + requestDTO.getDocumentId());
		}

		Errata errata = new Errata();
		errata.setDescription(requestDTO.getDescription());
		errata.setDocumentId(requestDTO.getDocumentId());
		return errataRepository.save(errata);
	}

	public Errata getErrataByDocumentId(Integer documentId) {
		return errataRepository.findByDocumentId(documentId);
	}
}
