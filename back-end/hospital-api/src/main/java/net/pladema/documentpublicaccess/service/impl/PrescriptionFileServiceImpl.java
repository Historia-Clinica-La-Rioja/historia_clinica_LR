package net.pladema.documentpublicaccess.service.impl;

import ar.lamansys.sgh.clinichistory.application.fetchdocumentfile.FetchDocumentFileById;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import ar.lamansys.sgx.shared.token.JWTUtils;
import net.pladema.documentpublicaccess.service.PrescriptionFileService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrescriptionFileServiceImpl implements PrescriptionFileService {

	private final FetchDocumentFileById fetchDocumentFileById;

	private final String secret;

	public PrescriptionFileServiceImpl(FetchDocumentFileById fetchDocumentFileById, @Value("${token.secret}") String secret) {
		this.fetchDocumentFileById = fetchDocumentFileById;
		this.secret = secret;
	}


	@Override
	public Optional<StoredFileBo> getFile(String accessId) {
		return extractFileIdFrom(accessId)
				.map(fetchDocumentFileById::run);
	}

	private Optional<Long> extractFileIdFrom(String accessId) {
		var accessIdLocal = JWTUtils.parseClaims(accessId, secret);

		if(accessIdLocal.isEmpty() || accessIdLocal.get().get("accessId") == null) {
			return Optional.empty();
		} else {
			return Optional.of(Long.valueOf((String)accessIdLocal.get().get("accessId")));
		}
	}
}
