package ar.lamansys.sgh.publicapi.infrastructure.input.rest.mapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ar.lamansys.sgh.publicapi.domain.DocumentInfoBo;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.AttentionInfoDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.DiagnosesDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.DocumentInfoDto;
import ar.lamansys.sgh.publicapi.domain.SingleDiagnosticBo;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.SingleAttentionInfoDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.SingleDiagnosticDto;

import org.springframework.stereotype.Component;

import ar.lamansys.sgh.publicapi.domain.AttentionInfoBo;
import ar.lamansys.sgh.publicapi.domain.BedRelocationInfoBo;
import ar.lamansys.sgh.publicapi.domain.CoverageActivityInfoBo;
import ar.lamansys.sgh.publicapi.domain.InternmentBo;
import ar.lamansys.sgh.publicapi.domain.PersonInfoBo;
import ar.lamansys.sgh.publicapi.domain.ProcedureInformationBo;
import ar.lamansys.sgh.publicapi.domain.ProfessionalBo;
import ar.lamansys.sgh.publicapi.domain.SnomedBo;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.BedRelocationInfoDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.ClinicalSpecialityDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.CoverageActivityInfoDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.InternmentDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.PersonInfoDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.ProcedureInformationDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.ProfessionalDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.SnomedDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;

import static java.util.stream.Collectors.groupingBy;

@Component
public class ActivitiesMapper {

	private final LocalDateMapper localDateMapper;

	public ActivitiesMapper(LocalDateMapper localDateMapper) {
		this.localDateMapper = localDateMapper;
	}

	public List<SingleAttentionInfoDto>  mapTo(List<AttentionInfoBo> toothDrawingsBoList) {
		return toothDrawingsBoList.stream()
				.map(this::mapTo)
				.collect(Collectors.toList());
	}

	public SingleAttentionInfoDto mapTo(AttentionInfoBo attentionInfoBo) {
		return SingleAttentionInfoDto.builder()
				.id(attentionInfoBo.getId())
				.encounterId(attentionInfoBo.getEncounterId())
				.attentionDate(localDateMapper.toDateDto(attentionInfoBo.getAttentionDate()))
				.speciality(ClinicalSpecialityDto.builder().snomed(mapToSnomed(attentionInfoBo.getSpeciality())).build())
				.patient(mapToPatient(attentionInfoBo.getPatient()))
				.coverage(mapToCoverage(attentionInfoBo.getCoverage()))
				.scope(attentionInfoBo.getScope().name())
				.internmentInfo(mapToInternment(attentionInfoBo.getInternmentInfo()))
				.responsibleDoctor(mapToProfessional(attentionInfoBo.getResponsibleDoctor()))
				.singleDiagnosticDto(mapToSingleDiagnosticBo(attentionInfoBo.getSingleDiagnosticBo()))
				.build();
	}

	public AttentionInfoDto mapToAttentionInfoDto(AttentionInfoBo attentionInfoBo) {
		return AttentionInfoDto.builder()
				.id(attentionInfoBo.getId())
				.encounterId(attentionInfoBo.getEncounterId())
				.attentionDate(localDateMapper.toDateDto(attentionInfoBo.getAttentionDate()))
				.speciality(ClinicalSpecialityDto.builder().snomed(mapToSnomed(attentionInfoBo.getSpeciality())).build())
				.patient(mapToPatient(attentionInfoBo.getPatient()))
				.coverage(mapToCoverage(attentionInfoBo.getCoverage()))
				.scope(attentionInfoBo.getScope().name())
				.internmentInfo(mapToInternment(attentionInfoBo.getInternmentInfo()))
				.responsibleDoctor(mapToProfessional(attentionInfoBo.getResponsibleDoctor()))
				.build();
	}

	private SingleDiagnosticDto mapToSingleDiagnosticBo(SingleDiagnosticBo singleDiagnosticBo) {
		return new SingleDiagnosticDto(mapToSnomed(singleDiagnosticBo.getDiagnostic()),
				singleDiagnosticBo.getIsMain(),
				singleDiagnosticBo.getDiagnosisType(),
				singleDiagnosticBo.getDiagnosisVerificationStatus(),
				singleDiagnosticBo.getUpdatedOn());
	}

	private SnomedDto mapToSnomed(SnomedBo snomedBo) {
		return SnomedDto.builder()
				.sctId(snomedBo.getSctId())
				.pt(snomedBo.getPt())
				.build();
	}

	public List<BedRelocationInfoDto> mapToBedRelocation(List<BedRelocationInfoBo> bedRelocationInfoBoList) {
		return bedRelocationInfoBoList.stream()
				.map(this::mapToBedRelocation)
				.collect(Collectors.toList());
	}

	private BedRelocationInfoDto mapToBedRelocation(BedRelocationInfoBo bedRelocationInfoBo) {
		return BedRelocationInfoDto.builder()
				.bedId(bedRelocationInfoBo.getBedId())
				.relocationDate(localDateMapper.toDateTimeDto(bedRelocationInfoBo.getRelocationDate()))
				.careType(bedRelocationInfoBo.getCareType())
				.service(ClinicalSpecialityDto.builder().snomed(mapToSnomed(bedRelocationInfoBo.getService())).build())
				.build();
	}

	private PersonInfoDto mapToPatient(PersonInfoBo personInfoBo) {
		return PersonInfoDto.builder()
				.firstName(personInfoBo.getFirstName())
				.lastName(personInfoBo.getLastName())
				.identificationNumber(personInfoBo.getIdentificationNumber())
				.genderId(personInfoBo.getGender() == null ? null : personInfoBo.getGender().getId())
				.birthDate(localDateMapper.toDateDto(personInfoBo.getBirthDate()))
				.build();
	}

	private CoverageActivityInfoDto mapToCoverage(CoverageActivityInfoBo coverageActivityInfoBo) {
		return CoverageActivityInfoDto.builder()
				.affiliateNumber(coverageActivityInfoBo.getAffiliateNumber())
				.attentionCoverage(coverageActivityInfoBo.getAffiliateNumber() != null)
				.cuitCoverage(coverageActivityInfoBo.getCuit())
				.plan(coverageActivityInfoBo.getPlan())
				.build();
	}

	private InternmentDto mapToInternment(InternmentBo internmentBo) {
		return InternmentDto.builder()
				.id(internmentBo.getId())
				.entryDate(localDateMapper.toDateTimeDto(internmentBo.getEntryDate()))
				.dischargeDate(localDateMapper.toDateTimeDto(internmentBo.getDischargeDate()))
				.build();
	}

	private ProfessionalDto mapToProfessional(ProfessionalBo professionalBo) {
		return ProfessionalDto.builder()
				.id(professionalBo.getId())
				.firstName(professionalBo.getFirstName())
				.lastName(professionalBo.getLastName())
				.licenceNumber(professionalBo.getLicenceNumber())
				.indentificationNumber(professionalBo.getIndentificationNumber())
				.build();
	}

	public SnomedDto mapTo(SnomedBo snomed) {
		return SnomedDto.builder()
				.sctId(snomed.getSctId())
				.pt(snomed.getPt())
				.build();
	}


	public ProcedureInformationDto mapTo(ProcedureInformationBo procedureInformationBo) {
		return ProcedureInformationDto.builder()
				.snomed(mapTo(procedureInformationBo.getSnomedBo()))
				.date(new DateTimeDto(new DateDto(procedureInformationBo.getAdministrationTime().getYear(),
						procedureInformationBo.getAdministrationTime().getMonth().getValue(),
						procedureInformationBo.getAdministrationTime().getDayOfMonth()),
						new TimeDto(procedureInformationBo.getAdministrationTime().getHour(),
								procedureInformationBo.getAdministrationTime().getMinute(),
								procedureInformationBo.getAdministrationTime().getSecond())))
				.build();
	}


	public List<DocumentInfoDto> mapToListDocumentInfoDto(List<DocumentInfoBo> documents) {
		return documents.stream()
				.map(this::mapToDocumentInfoDto)
				.collect(Collectors.toList());
	}

	private DocumentInfoDto mapToDocumentInfoDto(DocumentInfoBo document) {
		return DocumentInfoDto.builder()
				.id(document.getId())
				.updateOn(localDateMapper.toDateTimeDto(document.getUpdateOn()))
				.filePath(document.getFilePath())
				.fileName(document.getFileName())
				.type(document.getType())
				.build();
	}

	public List<AttentionInfoDto> groupDiagnosis(List<SingleAttentionInfoDto> resultBo) {
		List<AttentionInfoDto> result = new ArrayList<>();

		Map<Long, List<SingleAttentionInfoDto>> groupedAttentions = resultBo.stream()
				.filter(res -> res.getSingleDiagnosticDto().getDiagnosis().getPt() != null)
				.filter(res -> res.getSingleDiagnosticDto().getUpdatedOn() != null)
				.collect(groupingBy(SingleAttentionInfoDto::getEncounterId));

		for(var key : groupedAttentions.keySet()) {
			var list = groupedAttentions.get(key);
			var lastMainDiagnosisOptional = list.stream()
					.max(Comparator.comparing(a -> a.getSingleDiagnosticDto()
							.getUpdatedOn()));
			if(lastMainDiagnosisOptional.isPresent()) {
				var lastMainDiagnosis = lastMainDiagnosisOptional.get();
				DiagnosesDto diagnosis = new DiagnosesDto();
				diagnosis.setMain(lastMainDiagnosis.getSingleDiagnosticDto().getDiagnosis());
				diagnosis.setOthers(list.stream()
						.filter(diag -> ProblemTypeEnum.map(diag.getSingleDiagnosticDto().getDiagnosisType()).equals(ProblemTypeEnum.DIAGNOSIS))
						.filter(diag -> !diag.getSingleDiagnosticDto().getDiagnosis().equals(lastMainDiagnosis.getSingleDiagnosticDto().getDiagnosis()))
						.map(diag -> diag.getSingleDiagnosticDto().getDiagnosis())
						.distinct()
						.collect(Collectors.toList()));

				AttentionInfoDto attention = AttentionInfoDto.builder()
						.attentionDate(lastMainDiagnosis.getAttentionDate())
						.coverage(lastMainDiagnosis.getCoverage())
						.scope(lastMainDiagnosis.getScope())
						.responsibleDoctor(lastMainDiagnosis.getResponsibleDoctor())
						.speciality(lastMainDiagnosis.getSpeciality())
						.patient(lastMainDiagnosis.getPatient())
						.encounterId(lastMainDiagnosis.getEncounterId())
						.internmentInfo(lastMainDiagnosis.getInternmentInfo())
						.diagnoses(diagnosis)
						.id(lastMainDiagnosis.getId())
						.build();

				result.add(attention);
			}
			result = result.stream().sorted(Comparator.comparingLong(AttentionInfoDto::getId).reversed()).collect(Collectors.toList());
		}


		return result;
	}
}