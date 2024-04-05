package net.pladema.errata.general.endpoints.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRepository;
import net.pladema.errata.common.dto.ErrataRequestDTO;
import net.pladema.errata.common.repository.ErrataRepository;
import net.pladema.errata.common.repository.entity.Errata;

@Service
public class ErrataService {

	@Autowired
	private final ErrataRepository errataRepository;

    public ErrataService(ErrataRepository errataRepository) {
        this.errataRepository = errataRepository;
    }

	public Errata createErrata(ErrataRequestDTO requestDTO) {
		boolean errataExists = errataRepository.existsByDocumentId(requestDTO.getDocumentId());
		if (errataExists) {
			throw new RuntimeException("An errata already exists for the document with the ID " + requestDTO.getDocumentId());
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
