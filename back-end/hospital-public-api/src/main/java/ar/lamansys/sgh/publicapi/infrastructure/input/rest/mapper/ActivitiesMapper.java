package ar.lamansys.sgh.publicapi.infrastructure.input.rest.mapper;

import ar.lamansys.sgh.publicapi.domain.*;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.*;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ActivitiesMapper {

    private final LocalDateMapper localDateMapper;

    public ActivitiesMapper(LocalDateMapper localDateMapper) {
        this.localDateMapper = localDateMapper;
    }

    public List<AttentionInfoDto> mapTo(List<AttentionInfoBo> toothDrawingsBoList) {
        return toothDrawingsBoList.stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

    public AttentionInfoDto mapTo(AttentionInfoBo attentionInfoBo) {
        return AttentionInfoDto.builder()
                .id(attentionInfoBo.getId())
                .attentionDate(localDateMapper.toDateDto(attentionInfoBo.getAttentionDate()))
                .speciality(ClinicalSpecialityDto.builder().snomed(mapToSnomed(attentionInfoBo.getSpeciality())).build())
                .patient(mapToPatient(attentionInfoBo.getPatient()))
                .coverage(mapToCoverage(attentionInfoBo.getCoverage()))
                .scope(attentionInfoBo.getScope().name())
                .internmentInfo(mapToInternment(attentionInfoBo.getInternmentInfo()))
                .responsibleDoctor(mapToProfessional(attentionInfoBo.getResponsibleDoctor()))
                .build();
    }

    public List<SnomedDto> mapToSnomed(List<SnomedBo> snomedBoList) {
        return snomedBoList.stream()
                .map(this::mapToSnomed)
                .collect(Collectors.toList());
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
                .relocationDate(localDateMapper.toDateTimeDto(bedRelocationInfoBo.getRelocationDate()))
                .careTypeId(bedRelocationInfoBo.getCareTypeId())
                .service(ClinicalSpecialityDto.builder().snomed(mapToSnomed(bedRelocationInfoBo.getService())).build())
                .build();
    }

    private PersonInfoDto mapToPatient(PersonInfoBo personInfoBo) {
        return PersonInfoDto.builder()
                .firstName(personInfoBo.getFirstName())
                .lastName(personInfoBo.getLastName())
                .identificationNumber(personInfoBo.getIdentificationNumber())
                .genderId(personInfoBo.getGender().getGenderId())
                .birthDate(localDateMapper.toDateDto(personInfoBo.getBirthDate()))
                .build();
    }

    private CoverageActivityInfoDto mapToCoverage(CoverageActivityInfoBo coverageActivityInfoBo) {
        return CoverageActivityInfoDto.builder()
                .affiliateNumber(coverageActivityInfoBo.getAffiliateNumber())
                .attentionCoverage(coverageActivityInfoBo.getAffiliateNumber() != null)
                .cuitCoverage("")
                .build();
    }

    private InternmentDto mapToInternment(InternmentBo internmentBo) {
        return InternmentDto.builder()
                .id(internmentBo.getId())
                .entryDate(localDateMapper.toDateDto(internmentBo.getEntryDate()))
                .dischargeDate(localDateMapper.toDateDto(internmentBo.getDischargeDate()))
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
}
