package net.pladema.electronicjointsignature.getelectronicjointsignaturedocumentpossiblestatuses.infrastructure.input.mapper;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EElectronicSignatureStatus;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.GenericMasterDataDto;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = EElectronicSignatureStatus.class)
public interface ElectronicJointSignatureDocumentStatusesMapper {

	@Mapping(target = "id", expression = "java(EElectronicSignatureStatus.map(eElectronicSignatureStatus.getId()))")
	@Mapping(target = "description", source = "eElectronicSignatureStatus.description")
	@Named("fromElectronicJointSignatureStatus")
	GenericMasterDataDto<EElectronicSignatureStatus> fromElectronicJointSignatureStatus(EElectronicSignatureStatus eElectronicSignatureStatus);

	@IterableMapping(qualifiedByName = "fromElectronicJointSignatureStatus")
	@Named("fromElectronicJointSignatureStatusList")
	List<GenericMasterDataDto<EElectronicSignatureStatus>> fromElectronicJointSignatureStatusList(List<EElectronicSignatureStatus> eElectronicSignatureStatuses);

}
