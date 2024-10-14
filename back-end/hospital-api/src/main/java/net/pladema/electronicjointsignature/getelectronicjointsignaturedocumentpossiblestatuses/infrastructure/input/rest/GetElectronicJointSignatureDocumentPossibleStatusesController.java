package net.pladema.electronicjointsignature.getelectronicjointsignaturedocumentpossiblestatuses.infrastructure.input.rest;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EElectronicSignatureStatus;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.GenericMasterDataDto;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.pladema.electronicjointsignature.getelectronicjointsignaturedocumentpossiblestatuses.infrastructure.input.mapper.ElectronicJointSignatureDocumentStatusesMapper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/electronic-joint-signature/master-data/get-document-possible-statuses")
@RestController
public class GetElectronicJointSignatureDocumentPossibleStatusesController {

	private final ElectronicJointSignatureDocumentStatusesMapper electronicJointSignatureDocumentStatusesMapper;

	@GetMapping
	public List<GenericMasterDataDto<EElectronicSignatureStatus>> run() {
		List<EElectronicSignatureStatus> statuses = EElectronicSignatureStatus.getAll();
		List<GenericMasterDataDto<EElectronicSignatureStatus>> result = electronicJointSignatureDocumentStatusesMapper.fromElectronicJointSignatureStatusList(statuses);
		log.debug("Output -> {}", result);
		return result;
	}

}
