package net.pladema.digitalsignature.infrastructure.output;

import static java.util.Map.entry;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.DigitalSignatureDataDto;
import ar.lamansys.sgh.shared.infrastructure.DigitalSignatureDocumentContentDto;
import ar.lamansys.sgx.shared.filestorage.infrastructure.output.repository.BlobStorage;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import ar.lamansys.sgx.shared.restclient.services.RestClient;
import ar.lamansys.sgx.shared.restclient.services.RestClientInterface;
import lombok.extern.slf4j.Slf4j;
import net.pladema.digitalsignature.application.port.DigitalSignatureWSStorage;
import net.pladema.digitalsignature.domain.FirmadorDocumentRequestBody;
import net.pladema.digitalsignature.domain.FirmadorLinkResponse;
import net.pladema.digitalsignature.domain.FirmadorRequestBody;
import net.pladema.digitalsignature.infrastructure.output.configuration.FirmadorRestTemplateAuth;
import net.pladema.digitalsignature.infrastructure.output.configuration.FirmadorWSConfig;

@Service
@Slf4j
public class DigitalSignatureWSStorageImpl implements DigitalSignatureWSStorage {

	private final static String TYPE = "HASH";

	private final FirmadorWSConfig wSConfig;

	private final RestClientInterface restClientInterface;

	@Value("${digital.signature.url.redirect}")
	private String urlRedirect;

	private final BlobStorage blobStorage;

	public DigitalSignatureWSStorageImpl(FirmadorWSConfig wSConfig,
										 FirmadorRestTemplateAuth restTemplateAuth,
										 BlobStorage blobStorage) {
		this.wSConfig = wSConfig;
		this.restClientInterface = new RestClient(restTemplateAuth, wSConfig);
		this.blobStorage = blobStorage;

	}


	@Override
	public String generateDigitalSigningLink(DigitalSignatureDataDto digitalSignatureData) {
		log.debug("Input parameters -> digitalSignatureData {}", digitalSignatureData);
		ResponseEntity<FirmadorLinkResponse> response;
		List<FirmadorDocumentRequestBody> documents = digitalSignatureData.getDocuments()
				.stream().map(document -> createRequestBody(document, digitalSignatureData.getPersonId())).collect(Collectors.toList());
		FirmadorRequestBody body = FirmadorRequestBody.builder()
				.cuil(digitalSignatureData.getCuil())
				.documentos(documents)
				.type(TYPE)
				.urlRedirect(String.format(urlRedirect, digitalSignatureData.getInstitutionId()))
				.build();
		log.debug("Body -> {}", body);
		response = exchangePost(wSConfig.getPathFirmador(), body);
		String link = response.getHeaders().get("Location").get(0);
		log.debug("Output result -> {}", link);
		return link;
	}

	private FirmadorDocumentRequestBody createRequestBody(DigitalSignatureDocumentContentDto document, Integer personId) {
		return FirmadorDocumentRequestBody.builder()
				.documento(document.getHash())
				.metadata(Map.ofEntries(entry("documentId", document.getId()),
						entry("personId", personId)))
				.build();
	}

	private ResponseEntity<FirmadorLinkResponse> exchangePost(String url,
															  FirmadorRequestBody requestBody) {
		log.debug("Input parameters ->  body digital signature request {}", requestBody);
		ResponseEntity<FirmadorLinkResponse> response;
		try {
			response = restClientInterface.exchangePost(url, requestBody, FirmadorLinkResponse.class);
		} catch (RestTemplateApiException e) {
			throw new RuntimeException(e);
		}
		return response;
	}

}
