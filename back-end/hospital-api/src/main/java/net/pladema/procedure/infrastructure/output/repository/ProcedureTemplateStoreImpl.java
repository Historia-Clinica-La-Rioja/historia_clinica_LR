package net.pladema.procedure.infrastructure.output.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DiagnosticReportRepository;
import lombok.RequiredArgsConstructor;
import net.pladema.procedure.application.port.ProcedureTemplateStore;
import net.pladema.procedure.domain.EProcedureTemplateStatusBo;
import net.pladema.procedure.domain.ProcedureTemplateVo;
import net.pladema.procedure.domain.fullsummary.ProcedureParameterFullSummaryVo;
import net.pladema.procedure.domain.fullsummary.ProcedureParameterLoincCodeFullSummaryVo;
import net.pladema.procedure.domain.fullsummary.ProcedureParameterTextOptionFullSummaryVo;
import net.pladema.procedure.domain.fullsummary.ProcedureParameterUnitOfMeasureFullSummaryVo;
import net.pladema.procedure.domain.fullsummary.ProcedureTemplateFullSummaryVo;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureParameter;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureParameterType;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureTemplate;
import net.pladema.procedure.infrastructure.output.repository.mapper.ProcedureTemplateMapper;
import net.pladema.snowstorm.repository.SnomedGroupRepository;
import net.pladema.snowstorm.repository.entity.SnomedGroup;

@Service
@RequiredArgsConstructor
public class ProcedureTemplateStoreImpl implements ProcedureTemplateStore {

	private final ProcedureTemplateRepository repository;
	private final DiagnosticReportRepository diagnosticReportRepository;
	private final ProcedureTemplateSnomedRepository procedureTemplateSnomedRepository;
	private final ProcedureTemplateMapper procedureTemplateMapper;
	private final ProcedureTemplateRepository procedureTemplateRepository;
	private final ProcedureParameterRepository procedureParameterRepository;
	private final ProcedureParameterTypeRepository procedureParameterTypeRepository;
	private final SnomedGroupRepository snomedGroupRepository;

	@Override
	public Optional<EProcedureTemplateStatusBo> findParameterStatus(Integer procedureTemplateId) {
		return repository.findById(procedureTemplateId).map(x -> x.toStatusBo());
	}

	@Override
	public void updateStatus(Integer procedureTemplateId, EProcedureTemplateStatusBo nextState) {
		repository.updateStatus(procedureTemplateId, ProcedureTemplate.getStatusId(nextState));
	}

	@Override
	public List<ProcedureTemplateVo> findAvailableForDiagnosticReport(Integer diagnosticReportId) {
		return diagnosticReportRepository
			.findById(diagnosticReportId)
			.stream()
			.flatMap(diagnosticReport -> procedureTemplateSnomedRepository.findProcedureTemplateBySnomedId(diagnosticReport.getSnomedId()).stream())
			.filter(ProcedureTemplate::isActive)
			.map(procedureTemplateMapper::toProcedureTemplateVo)
			.collect(Collectors.toList());
	}

	@Override
	public Optional<ProcedureTemplateFullSummaryVo> findFullSummaryById(Integer procedureTemplateId) {
		return procedureTemplateRepository.findById(procedureTemplateId)
		.map(procedureTemplate ->
			ProcedureTemplateFullSummaryVo
					.builder()
					.id(procedureTemplate.getId())
					.description(procedureTemplate.getDescription())
					.parameters(buildParameters(procedureTemplate.getId()))
					.build()
		);
	}

	private List<ProcedureParameterFullSummaryVo> buildParameters(Integer procedureTemplateId) {
		List<ProcedureParameter> parameters = procedureParameterRepository.findByProcedureTemplateId(procedureTemplateId);
		var parameterIds = parameters.stream().map(ProcedureParameter::getId).collect(Collectors.toList());
		Map<Short, ProcedureParameterType> parameterTypes = asMap(procedureParameterTypeRepository.findAll());
		Map<Integer, List<ProcedureParameterUnitOfMeasureFullSummaryVo>> unitsOfMeasure = getUnitsOfMeasure(parameterIds);
		Map<Integer, List<ProcedureParameterTextOptionFullSummaryVo>> textOptions = getTextOptions(parameterIds);
		Map<Integer, ProcedureParameterLoincCodeFullSummaryVo> loincCodes = getLoincCodes(parameterIds);
		Map<Integer, SnomedGroup> snomedGroupDescriptions = getSnomedGroupDescriptions(parameters);
		return parameters.stream().map(parameter -> {
			return ProcedureParameterFullSummaryVo.builder()
					.id(parameter.getId())
					.procedureTemplateId(parameter.getProcedureTemplateId())
					.loincCode(loincCodes.get(parameter.getId()))
					.orderNumber(parameter.getOrderNumber())
					.typeId(parameter.getTypeId())
					.typeDescription(parameterTypes.get(parameter.getTypeId()).getDescription())
					.inputCount(parameter.getInputCount())
					.snomedGroupId(parameter.getSnomedGroupId())
					.snomedGroupDescription(getSnomedGroupDescription(snomedGroupDescriptions, parameter))
					.unitsOfMeasure(unitsOfMeasure.get(parameter.getId()))
					.textOptions(textOptions.get(parameter.getId()))
					.build();
		})
		.collect(Collectors.toList());
	}

	private static String getSnomedGroupDescription(Map<Integer, SnomedGroup> snomedGroupDescriptions, ProcedureParameter parameter) {
		if (
			parameter.getSnomedGroupId() == null ||
			!snomedGroupDescriptions.containsKey(parameter.getSnomedGroupId())
			) return null;
		var foundSnomed = snomedGroupDescriptions.get(parameter.getSnomedGroupId());
		return foundSnomed == null ? null : foundSnomed.getDescription();
	}

	private Map<Integer, SnomedGroup> getSnomedGroupDescriptions(List<ProcedureParameter> parameters) {
		var groupIds = parameters.stream().map(ProcedureParameter::getSnomedGroupId).filter(Objects::nonNull).collect(Collectors.toList());
		if (groupIds.isEmpty())
			return Collections.emptyMap();
		return snomedGroupRepository.findAllById(groupIds).stream()
		.collect(Collectors.toMap(SnomedGroup::getId, x -> x));
	}

	private Map<Integer, List<ProcedureParameterUnitOfMeasureFullSummaryVo>> getUnitsOfMeasure(List<Integer> parameterIds) {
		return procedureParameterRepository.findUnitOfMeasureFullSummaryByParamterIdIn(parameterIds)
				.stream()
				.collect(Collectors.groupingBy(ProcedureParameterUnitOfMeasureFullSummaryVo::getProcedureParameterId));
	}

	private Map<Integer, ProcedureParameterLoincCodeFullSummaryVo> getLoincCodes(List<Integer> parameterIds) {
		return procedureParameterRepository.findLoincCodeFullSummaryByParamterIdIn(parameterIds)
				.stream()
				.collect(Collectors.toMap(ProcedureParameterLoincCodeFullSummaryVo::getProcedureParameterId, x -> x));
	}

	private Map<Integer, List<ProcedureParameterTextOptionFullSummaryVo>> getTextOptions(List<Integer> parameterIds) {
		return procedureParameterRepository.findTextOptionFullSummaryByParamterIdIn(parameterIds)
				.stream()
				.collect(Collectors.groupingBy(ProcedureParameterTextOptionFullSummaryVo::getProcedureParameterId));
	}

	private Map<Short, ProcedureParameterType> asMap(List<ProcedureParameterType> all) {
		Map<Short, ProcedureParameterType> ret = new HashMap<>();
		all.stream().forEach(x -> ret.put(x.getId(), x));
		return ret;
	}
}
