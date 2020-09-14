package net.pladema.clinichistory.outpatient.createoutpatient.controller.mapper;

import net.pladema.clinichistory.ips.controller.dto.HealthConditionNewConsultationDto;
import net.pladema.clinichistory.ips.service.domain.ImmunizationBo;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.*;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientDocumentBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientEvolutionSummaryBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProblemBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ReasonBo;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import net.pladema.staff.controller.mapper.HealthcareProfessionalMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class, HealthcareProfessionalMapper.class})
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
    OutpatientEvolutionSummaryDto fromOutpatientEvolutionSummaryBo(OutpatientEvolutionSummaryBo outpatientEvolutionSummaryBo);

    @Named("fromListOutpatientEvolutionSummaryBo")
    @IterableMapping(qualifiedByName = "fromOutpatientEvolutionSummaryBo")
    List<OutpatientEvolutionSummaryDto> fromListOutpatientEvolutionSummaryBo(List<OutpatientEvolutionSummaryBo> outpatientEvolutionSummaryBos);


}

