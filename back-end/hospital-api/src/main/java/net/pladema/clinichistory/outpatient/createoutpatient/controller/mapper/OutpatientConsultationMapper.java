package net.pladema.clinichistory.outpatient.createoutpatient.controller.mapper;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.HealthConditionNewConsultationDto;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.*;

import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.EvolutionSummaryBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProblemBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.staff.controller.mapper.HealthcareProfessionalMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class, HealthcareProfessionalMapper.class, SnomedMapper.class})
public interface OutpatientConsultationMapper {

    @Named("fromCreateOutpatientDto")
    OutpatientDocumentBo fromCreateOutpatientDto(CreateOutpatientDto createOutpatientDto);

    @Named("fromListReasonDto")
    List<ReasonBo> fromListReasonDto(List<OutpatientReasonDto> reasons);

    @Named("fromOutpatientImmunizationDto")
    ImmunizationBo fromOutpatientImmunizationDto(OutpatientImmunizationDto vaccineDto);

    @Named("fromOutpatientUpdateImmunizationDto")
    ImmunizationBo fromOutpatientImmunizationDto(OutpatientUpdateImmunizationDto outpatientUpdateImmunization);

    @Named("fromHealthConditionNewConsultationDto")
    @Mapping(source = "inactivationDate", target = "endDate")
    ProblemBo fromHealthConditionNewConsultationDto(HealthConditionNewConsultationDto healthConditionNewConsultationDto);

    @Named("fromOutpatientEvolutionSummaryBo")
    OutpatientEvolutionSummaryDto fromOutpatientEvolutionSummaryBo(EvolutionSummaryBo evolutionSummaryBo);

    @Named("fromListOutpatientEvolutionSummaryBo")
    @IterableMapping(qualifiedByName = "fromOutpatientEvolutionSummaryBo")
    List<OutpatientEvolutionSummaryDto> fromListOutpatientEvolutionSummaryBo(List<EvolutionSummaryBo> evolutionSummaryBos);

}

