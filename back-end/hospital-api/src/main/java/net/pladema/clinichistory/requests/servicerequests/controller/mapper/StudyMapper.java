package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import java.util.List;

import net.pladema.clinichistory.requests.controller.dto.PrescriptionDto;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionItemDto;
import net.pladema.clinichistory.requests.controller.dto.TranscribedServiceRequestDto;
import net.pladema.clinichistory.requests.service.domain.ExtendedServiceRequestBo;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.DiagnosticReportSummaryDto;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.StudyOrderWorkListDto;
import net.pladema.clinichistory.requests.servicerequests.domain.StudyOrderWorkListBo;
import net.pladema.clinichistory.requests.transcribed.infrastructure.input.rest.dto.TranscribedServiceRequestSummaryDto;
import net.pladema.clinichistory.requests.transcribed.domain.TranscribedServiceRequestBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {SnomedMapper.class, LocalDateMapper.class, StudyOrderPatientMapper.class})
public interface StudyMapper {
    @Named("parseToList")
    @IterableMapping(qualifiedByName = "parseTo")
    List<DiagnosticReportBo> parseToList(List<PrescriptionItemDto> studyDtoList);

    @Named("parseTo")
    DiagnosticReportBo parseTo(PrescriptionItemDto studyDto);

    @Named("toDiagnosticReportBoFromSnomedDto")
    @Mapping(target = "snomed.id", source = "id")
    @Mapping(target = "snomed.sctid", source = "sctid")
    @Mapping(target = "snomed.pt", source = "pt")
    @Mapping(target = "id", ignore = true)
    DiagnosticReportBo toDiagnosticReportBoFromSnomedDto(SnomedDto snomedDto);

    @Named("toDiagnosticReportBoListFromSnomedDtoList")
    @IterableMapping(qualifiedByName = "toDiagnosticReportBoFromSnomedDto")
    List<DiagnosticReportBo> toDiagnosticReportBoListFromSnomedDtoList(List<SnomedDto> snomedDtos);

    @Named("toTranscribedServiceRequestBo")
    @Mapping(target = "healthCondition.snomed", source = "healthCondition")
    @Mapping(target = "diagnosticReports", source = "diagnosticReports", qualifiedByName = "toDiagnosticReportBoListFromSnomedDtoList")
    TranscribedServiceRequestBo toTranscribedServiceRequestBo(TranscribedServiceRequestDto transcribedServiceRequest);

    @Named("toTranscribedServiceRequestSummaryDto")
    @Mapping(target = "serviceRequestId", source = "id")
    @Mapping(target = "diagnosticReports", source = "diagnosticReports", qualifiedByName = "toDiagnosticReportSummaryDto")
    TranscribedServiceRequestSummaryDto toTranscribedServiceRequestSummaryDto(TranscribedServiceRequestBo transcribedServiceRequestBo);

    @Named("toDiagnosticReportSummaryDto")
    @Mapping(target = "diagnosticReportId", expression = "java(diagnosticReportBo.getId())")
    @Mapping(target = "pt", expression = "java(diagnosticReportBo.getDiagnosticReportSnomedPt())")
    DiagnosticReportSummaryDto toDiagnosticReportSummaryDto(DiagnosticReportBo diagnosticReportBo);

	@Named("toExtendedServiceRequestBo")
	@Mapping(target = "patientInfo", source = "patientDto")
	@Mapping(target = "doctorId", source = "doctorId")
	@Mapping(target = "medicalCoverageId", expression = "java(serviceRequestDto.getMedicalCoverageId())")
	@Mapping(target = "diagnosticReports", source = "studies")
	@Mapping(target = "observations", expression = "java(serviceRequestDto.getObservations())")
	@Mapping(target = "studyTypeId", expression = "java(serviceRequestDto.getStudyType().getId())")
	ExtendedServiceRequestBo toExtendedServiceRequestBo(BasicPatientDto patientDto, Integer doctorId, String categoryId, PrescriptionDto serviceRequestDto, List<PrescriptionItemDto> studies);

	@Named("toStudyOrderWorkListDto")
	@Mapping(target = "studyId", source = "studyId")
	@Mapping(target = "snomed", source = "snomed")
	@Mapping(target = "studyTypeId", expression = "java(EStudyType.map(studyOrderWorkListBo.getStudyTypeId()))")
	@Mapping(target = "requiresTransfer", source = "requiresTransfer")
	@Mapping(target = "sourceTypeId", expression = "java(ESourceType.map(studyOrderWorkListBo.getSourceTypeId()))")
	@Mapping(target = "deferredDate", source = "deferredDate")
	@Mapping(target = "status", expression = "java(EDiagnosticReportStatus.map(studyOrderWorkListBo.getStatus()))")
	@Mapping(target = "patientDto", source = "patientBo")
	@Mapping(target = "createdDate", source = "createdDate")
	@Mapping(target = "patientLocation", source = "patientLocation")
	StudyOrderWorkListDto toStudyOrderWorkListDto(StudyOrderWorkListBo studyOrderWorkListBo);

}
