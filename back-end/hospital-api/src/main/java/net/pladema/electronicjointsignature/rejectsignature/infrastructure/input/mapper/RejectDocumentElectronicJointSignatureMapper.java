package net.pladema.electronicjointsignature.rejectsignature.infrastructure.input.mapper;

import net.pladema.electronicjointsignature.rejectsignature.domain.RejectDocumentElectronicJointSignatureBo;

import net.pladema.electronicjointsignature.rejectsignature.infrastructure.input.rest.dto.RejectDocumentElectronicJointSignatureDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface RejectDocumentElectronicJointSignatureMapper {

	@Mapping(source = "rejectReason.id", target = "reason.rejectReasonId")
	@Mapping(source = "description", target = "reason.description")
	@Named("fromRejectDocumentElectronicJointSignatureDto")
	RejectDocumentElectronicJointSignatureBo fromRejectDocumentElectronicJointSignatureDto(RejectDocumentElectronicJointSignatureDto rejectDocumentElectronicJointSignatureDto);

}
