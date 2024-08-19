package net.pladema.clinichistory.outpatient.createoutpatient.controller.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProblemBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.HealthConditionNewConsultationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.CreateOutpatientDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientEvolutionSummaryDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientImmunizationDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientReasonDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientUpdateImmunizationDto;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.EvolutionSummaryBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientDocumentBo;
import net.pladema.clinichistory.outpatient.domain.ProblemErrorBo;
import net.pladema.clinichistory.outpatient.infrastructure.input.dto.ErrorProblemDto;
import net.pladema.clinichistory.outpatient.infrastructure.input.dto.ProblemInfoDto;
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

    @Named("fromErrorProblemDto")
    ProblemErrorBo fromErrorProblemDto(ErrorProblemDto errorProblemDto);

    @Named("fromProblemErrorBo")
    ProblemInfoDto fromProblemErrorBo(ProblemErrorBo problemErrorBo);

	@Named("toOutpatientReasonDto")
	List<OutpatientReasonDto> toOutpatientReasonDto(List<ReasonBo> reasons);

}

