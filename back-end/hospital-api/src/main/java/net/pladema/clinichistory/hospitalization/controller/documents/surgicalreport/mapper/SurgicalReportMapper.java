package net.pladema.clinichistory.hospitalization.controller.documents.surgicalreport.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.DocumentHealthcareProfessionalMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.clinichistory.hospitalization.controller.documents.surgicalreport.dto.SurgicalReportDto;
import net.pladema.clinichistory.hospitalization.service.domain.SurgicalReportBo;

@Mapper(uses = {LocalDateMapper.class, SnomedMapper.class, DocumentHealthcareProfessionalMapper.class})
public interface SurgicalReportMapper {

    @Named("fromSurgicalReportDto")
	@Mapping(target = "healthcareProfessionals", source = "healthcareProfessionals", qualifiedByName = "toDocumentHealthcareProfessionalBoList")
	SurgicalReportBo fromSurgicalReportDto(SurgicalReportDto surgicalReportDto);
	
	@Named("fromSurgicalReportBo")
	@Mapping(target = "healthcareProfessionals", source = "healthcareProfessionals", qualifiedByName = "toDocumentHealthcareProfessionalDtoList")
	@Mapping(target = "surgicalTeam", source = "surgicalTeam", qualifiedByName = "toDocumentHealthcareProfessionalDtoList")
	@Mapping(target = "pathologist", source = "pathologist", qualifiedByName = "toDocumentHealthcareProfessionalDocumentDto")
	@Mapping(target = "transfusionist", source = "transfusionist", qualifiedByName = "toDocumentHealthcareProfessionalDocumentDto")
	SurgicalReportDto fromSurgicalReportBo(SurgicalReportBo surgicalReportBo);

}
