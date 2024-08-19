package net.pladema.procedure.infrastructure.output.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.procedure.domain.fullsummary.ProcedureParameterLoincCodeFullSummaryVo;
import net.pladema.procedure.domain.fullsummary.ProcedureParameterTextOptionFullSummaryVo;
import net.pladema.procedure.domain.fullsummary.ProcedureParameterUnitOfMeasureFullSummaryVo;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureParameter;

@Repository
public interface ProcedureParameterRepository extends SGXAuditableEntityJPARepository<ProcedureParameter, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT pp.orderNumber " +
			"FROM ProcedureParameter pp " +
			"WHERE pp.procedureTemplateId = :procedureTemplateId " +
			"ORDER BY pp.orderNumber desc")
	List<Short> getLastOrderParameterFromProcedureTemplateId(@Param("procedureTemplateId") Integer procedureTemplateId);

	@Modifying
	@Query(value = "UPDATE ProcedureParameter pp " +
			"SET pp.orderNumber = pp.orderNumber-1 " +
			"WHERE pp.procedureTemplateId = :procedureTemplateId " +
			"AND pp.orderNumber > :orderNumberDeleted")
	void updateOrderNumberAfterDelete(@Param("procedureTemplateId") Integer procedureTemplateId, @Param("orderNumberDeleted") Short orderNumberDeleted);

	@Transactional(readOnly = true)
	@Query(value = "SELECT (CASE WHEN count(pp.id) > 0 THEN TRUE ELSE FALSE END) " +
			"FROM ProcedureParameter pp " +
			"WHERE pp.procedureTemplateId = :procedureTemplateId " +
			"AND pp.loincId = :loincId " +
			"AND pp.deleteable.deleted = false")
	boolean templateHasSpecificLoincId(@Param("procedureTemplateId") Integer procedureTemplateId, @Param("loincId") Integer loincId);

	@Transactional(readOnly = true)
	Optional<ProcedureParameter> findByProcedureTemplateIdAndOrderNumber(Integer id, Short order);

	List<ProcedureParameter> findByProcedureTemplateId(Integer parentProcedureTemplate);

	@Query( value =
		"SELECT NEW net.pladema.procedure.domain.fullsummary.ProcedureParameterUnitOfMeasureFullSummaryVo(" +
		"	ppuom.pk.procedureParameterId, " +
		"	uom.id, " +
		"	uom.description, " +
		"	uom.code " +
		") FROM " +
	 	"UnitOfMeasure uom " +
	 	"JOIN ProcedureParameterUnitOfMeasure ppuom ON ppuom.pk.unitOfMeasureId = uom " +
	 	"WHERE ppuom.pk.procedureParameterId IN :parameterIds"
	)
	List<ProcedureParameterUnitOfMeasureFullSummaryVo> findUnitOfMeasureFullSummaryByParamterIdIn(@Param("parameterIds") List<Integer> parameterIds);

	@Query( value =
			"SELECT NEW net.pladema.procedure.domain.fullsummary.ProcedureParameterTextOptionFullSummaryVo(" +
					"	ppto.procedureParameterId, " +
					"	ppto.id, " +
					"	ppto.description " +
					") FROM " +
					"ProcedureParameterTextOption ppto " +
					"WHERE ppto.procedureParameterId IN :parameterIds"
	)
	List<ProcedureParameterTextOptionFullSummaryVo> findTextOptionFullSummaryByParamterIdIn(@Param("parameterIds") List<Integer> parameterIds);

	@Query( value =
			"SELECT NEW net.pladema.procedure.domain.fullsummary.ProcedureParameterLoincCodeFullSummaryVo(" +
					"	procedureParameter.id, " +
					"	loincCode.id, " +
					"	loincCode.statusId, " +
					"	loincStatus.description, " +
					"	loincCode.systemId, " +
					"	loincSystem.description, " +
					"	loincCode.description, " +
					"	loincCode.code, " +
					"	loincCode.displayName, " +
					"	loincCode.customDisplayName " +
					") FROM " +
					"ProcedureParameter procedureParameter " +
					"JOIN LoincCode loincCode ON procedureParameter.loincId = loincCode.id " +
					"JOIN LoincStatus loincStatus ON loincCode.statusId = loincStatus.id " +
					"JOIN LoincSystem loincSystem ON loincCode.systemId = loincSystem.id " +
					"WHERE procedureParameter.id IN :parameterIds"
	)
	List<ProcedureParameterLoincCodeFullSummaryVo> findLoincCodeFullSummaryByParamterIdIn(@Param("parameterIds") List<Integer> parameterIds);
}
