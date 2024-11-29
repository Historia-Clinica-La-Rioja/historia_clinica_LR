package ar.lamansys.sgh.publicapi.activities.infrastructure.input.mapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import ar.lamansys.sgh.publicapi.activities.application.fetchactivitiesbyfilter.exception.DiagnosticNotFoundException;
import ar.lamansys.sgh.publicapi.activities.domain.DocumentInfoBo;
import ar.lamansys.sgh.publicapi.activities.domain.PersonInfoExtendedBo;
import ar.lamansys.sgh.publicapi.activities.domain.SnomedCIE10Bo;
import ar.lamansys.sgh.publicapi.activities.domain.SupplyInformationBo;
import ar.lamansys.sgh.publicapi.activities.domain.datetimeutils.DateTimeBo;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.AttentionInfoDto;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.DiagnosesDto;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.DocumentInfoDto;
import ar.lamansys.sgh.publicapi.activities.domain.SingleDiagnosticBo;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.PersonExtendedInfoDto;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.SingleAttentionInfoDto;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.SingleDiagnosticDto;

import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.SnomedCIE10Dto;

import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.SupplyInformationDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProblemTypeEnum;

import lombok.AllArgsConstructor;

import lombok.EqualsAndHashCode;

import org.springframework.stereotype.Component;

import ar.lamansys.sgh.publicapi.activities.domain.AttentionInfoBo;
import ar.lamansys.sgh.publicapi.activities.domain.BedRelocationInfoBo;
import ar.lamansys.sgh.publicapi.activities.domain.CoverageActivityInfoBo;
import ar.lamansys.sgh.publicapi.activities.domain.InternmentBo;
import ar.lamansys.sgh.publicapi.activities.domain.PersonInfoBo;
import ar.lamansys.sgh.publicapi.activities.domain.ProcedureInformationBo;
import ar.lamansys.sgh.publicapi.activities.domain.ProfessionalBo;
import ar.lamansys.sgh.publicapi.activities.domain.SnomedBo;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.BedRelocationInfoDto;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.ClinicalSpecialityDto;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.CoverageActivityInfoDto;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.InternmentDto;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.PersonInfoDto;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.ProcedureInformationDto;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.ProfessionalDto;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.SnomedDto;
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
				.attentionDateWithTime(mapToAttentionDateWithTimeDto(attentionInfoBo.getAttentionDateWithTime()))
				.personExtendedInfo(mapToPersonExtendedInfoDto(attentionInfoBo.getPersonInfoExtended()))
				.emergencyCareAdministrativeDischargeDateTime(mapToAttentionDateWithTimeDto(attentionInfoBo.getEmergencyCareAdministrativeDischargeDateTime()))
				.billable(attentionInfoBo.getBillable())
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
				.attentionDateWithTime(mapToAttentionDateWithTimeDto(attentionInfoBo.getAttentionDateWithTime()))
				.personExtendedInfo(mapToPersonExtendedInfoDto(attentionInfoBo.getPersonInfoExtended()))
				.emergencyCareAdministrativeDischargeDateTime(mapToAttentionDateWithTimeDto(attentionInfoBo.getEmergencyCareAdministrativeDischargeDateTime()))
				.billable(attentionInfoBo.getBillable())
				.build();
	}

	private PersonExtendedInfoDto mapToPersonExtendedInfoDto(PersonInfoExtendedBo personInfoExtended) {
		return PersonExtendedInfoDto.builder()
				.email(personInfoExtended.getEmail())
				.middleNames(personInfoExtended.getMiddleNames())
				.nameSelfDetermination(personInfoExtended.getNameSelfDetermination())
				.otherLastNames(personInfoExtended.getOtherLastNames())
				.genderSelfDeterminationId(personInfoExtended.getGenderSelfDeterminationId())
				.build();
	}

	private DateTimeDto mapToAttentionDateWithTimeDto(DateTimeBo attentionDateWithTime) {
		if (attentionDateWithTime != null)
			return new DateTimeDto(
				new DateDto(attentionDateWithTime.getDate().getYear(), attentionDateWithTime.getDate().getMonth(), attentionDateWithTime.getDate().getDay()),
				new TimeDto(attentionDateWithTime.getTime().getHours(), attentionDateWithTime.getTime().getMinutes(), attentionDateWithTime.getTime().getSeconds()));
		return null;
	}

	private SingleDiagnosticDto mapToSingleDiagnosticBo(SingleDiagnosticBo singleDiagnosticBo) {
		return new SingleDiagnosticDto(mapToSnomedCIE10(singleDiagnosticBo.getDiagnostic()),
				singleDiagnosticBo.getIsMain(),
				singleDiagnosticBo.getDiagnosisType(),
				singleDiagnosticBo.getDiagnosisVerificationStatus(),
				singleDiagnosticBo.getUpdatedOn());
	}

	private SnomedCIE10Dto mapToSnomedCIE10(SnomedCIE10Bo snomedCIE10Bo) {
		return SnomedCIE10Dto.builder()
				.sctId(snomedCIE10Bo.getSctId())
				.pt(snomedCIE10Bo.getPt())
				.CIE10Id(snomedCIE10Bo.getCie10Id())
				.build();
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
				.bedNumber(bedRelocationInfoBo.getBedNumber())
				.bedCategory(bedRelocationInfoBo.getBedCategory())
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

	public SupplyInformationDto mapTo(SupplyInformationBo supplyInformationBo){
		return SupplyInformationDto.builder()
				.supplyType(supplyInformationBo.getSupplyType())
				.status(supplyInformationBo.getStatus())
				.snomed(mapTo(supplyInformationBo.getSnomedBo()))
				.administrationTime(supplyInformationBo.getAdministrationTime())
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

	@AllArgsConstructor
	@EqualsAndHashCode
	private class AttentionGroupKey {
		Long encounterId;
		String scope;
	}

	public List<AttentionInfoDto> groupDiagnosis(List<SingleAttentionInfoDto> resultBo) {
		List<AttentionInfoDto> result = new ArrayList<>();

		/**
		 * Attentions are grouped by (encounter, scope)
		 * See {@link ar.lamansys.sgh.publicapi.domain.ScopeEnum}
		 * This pair can be traced to the fields:
		 * 	 * v_attention.encounter_id -> document.source_id
		 * 	 * v_attention.scope_id -> document.source_type_id
		 */
		Map<AttentionGroupKey, List<SingleAttentionInfoDto>> groupedAttentions = resultBo
			.stream()
			.collect(groupingBy(attention -> new AttentionGroupKey(attention.getEncounterId(), attention.getScope())));

		/**
		 * Starting from hsi-10498 and hsi-9370, the diagnosis field is optional. Before, attentions without a diagnosis
		 * attached weren't returned by this endpoint.
		 *
		 * Certain activities don't have a diagnosis attached:
		 * 	* Medications, vaccines and 'plan parenteral' administered during a hospitalization
		 * 	* Outpatient consultations without an assigned problem
		 */
		for (var attentionGroup : groupedAttentions.entrySet()) {
			AttentionInfoDto newAttention;
			List<SingleAttentionInfoDto> attentions = attentionGroup.getValue();

			//There's a diagnosis
			if (groupContainsDiagnosis(attentions)) {
				SingleAttentionInfoDto lastMainDiagnosis = attentions
						.stream()
						.filter(a -> a.getSingleDiagnosticDto().getUpdatedOn() != null)
						.max(Comparator.comparing(a -> a.getSingleDiagnosticDto().getUpdatedOn()))
						.orElseThrow(this::noDiagnosticFound);

				DiagnosesDto diagnoses = buildDiagnosis(attentions, lastMainDiagnosis);
				newAttention = mapToAttention(lastMainDiagnosis, diagnoses);

			}
			//No diagnosis
			else {
				SingleAttentionInfoDto lastMainDiagnosis = attentions.get(0);
				DiagnosesDto diagnoses = DiagnosesDto.emptyDiagnoses();
				newAttention = mapToAttention(lastMainDiagnosis, diagnoses);
			}

			result.add(newAttention);
		}

		return result
			.stream()
			.sorted(Comparator.comparingLong(AttentionInfoDto::getId).reversed())
			.collect(Collectors.toList());
	}

	private static DiagnosesDto buildDiagnosis(List<SingleAttentionInfoDto> attentions, SingleAttentionInfoDto lastMainDiagnosis) {
		DiagnosesDto diagnosis = new DiagnosesDto();
		diagnosis.setMain(lastMainDiagnosis.getSingleDiagnosticDto().getDiagnosis());
		diagnosis.setOthers(attentions.stream()
				.filter(diag -> diag.getSingleDiagnosticDto().getDiagnosisType() != null)
				.filter(diag -> ProblemTypeEnum.map(diag.getSingleDiagnosticDto().getDiagnosisType()).equals(ProblemTypeEnum.DIAGNOSIS))
				.filter(diag -> !diag.getSingleDiagnosticDto().getDiagnosis().equals(lastMainDiagnosis.getSingleDiagnosticDto().getDiagnosis()))
				.map(diag -> diag.getSingleDiagnosticDto().getDiagnosis())
				.distinct()
				.collect(Collectors.toList()));
		return diagnosis;
	}


	private DiagnosticNotFoundException noDiagnosticFound() {
		return new DiagnosticNotFoundException();
	}

	private boolean groupContainsDiagnosis(List<SingleAttentionInfoDto> attentionGroup) {
		return attentionGroup
		.stream()
		.anyMatch(res ->
			res.getSingleDiagnosticDto().getDiagnosis().getPt() != null &&
			res.getSingleDiagnosticDto().getUpdatedOn() != null
		);
	}

	private static AttentionInfoDto mapToAttention(SingleAttentionInfoDto attentionSource, DiagnosesDto diagnoses) {
		return AttentionInfoDto.builder()
				.attentionDate(attentionSource.getAttentionDate())
				.coverage(attentionSource.getCoverage())
				.scope(attentionSource.getScope())
				.responsibleDoctor(attentionSource.getResponsibleDoctor())
				.speciality(attentionSource.getSpeciality())
				.patient(attentionSource.getPatient())
				.encounterId(attentionSource.getEncounterId())
				.internmentInfo(attentionSource.getInternmentInfo())
				.diagnoses(diagnoses)
				.id(attentionSource.getId())
				.attentionDateWithTime(attentionSource.getAttentionDateWithTime())
				.personExtendedInfo(attentionSource.getPersonExtendedInfo())
				.emergencyCareAdministrativeDischargeDateTime(attentionSource.getEmergencyCareAdministrativeDischargeDateTime())
				.billable(attentionSource.getBillable())
				.build();
	}
}