package net.pladema.medication.infrastructure.port.output;

import ar.lamansys.sgx.shared.files.FileService;
import lombok.RequiredArgsConstructor;
import net.pladema.medication.application.port.CommercialMedicationUpdateFilePort;

import net.pladema.medication.application.port.SoapPort;
import net.pladema.medication.domain.CommercialMedicationFileUpdateBo;
import net.pladema.medication.domain.decodedResponse.CommercialMedicationDecodedResponse;
import net.pladema.medication.infrastructure.repository.CommercialMedicationUpdateFileRepository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationUpdateFile;

import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;

import java.io.InputStream;

@RequiredArgsConstructor
@Service
public class CommercialMedicationUpdateFilePortImpl implements CommercialMedicationUpdateFilePort {

	private final CommercialMedicationUpdateFileRepository commercialMedicationUpdateFileRepository;

	private final FileService fileService;

	private final SoapPort soapPort;

	@Override
	public boolean schemaAlreadyInitialized() {
		return commercialMedicationUpdateFileRepository.schemaAlreadyInitialized() != 0;
	}

	@Override
	public void saveNewEntry(Long logId) {
		commercialMedicationUpdateFileRepository.save(new CommercialMedicationUpdateFile(logId));
	}

	@Override
	public void updateEntryFilePath(Long logId, String filePath) {
		commercialMedicationUpdateFileRepository.updateEntryFilePath(logId, filePath);
	}

	@Override
	public CommercialMedicationFileUpdateBo getLastNonProcessedEntry() {
		return commercialMedicationUpdateFileRepository.fetchLastNonProcessedEntry();
	}

	@Override
	public void setEntryAsProcessed(Long oldLogId, Long lastLogId) {
		commercialMedicationUpdateFileRepository.setEntryAsProcessed(oldLogId);
		commercialMedicationUpdateFileRepository.save(new CommercialMedicationUpdateFile(lastLogId));
	}

	@Override
	public CommercialMedicationDecodedResponse getOldUpdateFile(String filePath) throws JAXBException {
		InputStream fileContent = fileService.loadFile(fileService.buildCompletePath(filePath)).stream;
		return soapPort.unmarshallCommercialMedicationDecodedResponseXml(fileContent);
	}

}
