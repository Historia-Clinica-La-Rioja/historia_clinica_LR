package net.pladema.parameter.infrastructure.output;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.parameter.application.port.ParameterStorage;
import net.pladema.parameter.domain.ParameterBo;
import net.pladema.parameter.infrastructure.output.repository.ParameterRepository;

import net.pladema.parameter.infrastructure.output.repository.entity.Parameter;


import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParameterStorageImpl implements ParameterStorage {

	private final ParameterRepository parameterRepository;
	private final EntityManager entityManager;

	@Override
	public List<ParameterBo> findAllByIds(List<Integer> ids) {
		log.debug("Input parameters -> ids {}", ids);
		List<ParameterBo> result = parameterRepository.findAllById(ids).stream().map(this::mapToBo).collect(Collectors.toList());
		log.debug("Output -> result {}", result);
		return result;
	}

	@Override
	public List<ParameterBo> findByDescription(String description) {

		String sqlString = "" +
			"SELECT " +
			"	DISTINCT ON (computed_desc) " +
			"	parameter.id as id, " +
			"	COALESCE(loinc_code.custom_display_name,loinc_code.display_name, loinc_code.description, parameter.description) as computed_desc " +
			"FROM " +
			"   parameter " +
			"   LEFT JOIN loinc_code ON (loinc_code.id = parameter.loinc_id) " +
			"WHERE " +
			"	COALESCE(loinc_code.custom_display_name,loinc_code.display_name, loinc_code.description, parameter.description) ILIKE :description " +
			"ORDER BY " +
			"	computed_desc DESC, parameter.id ASC " +
			"LIMIT :limit";
		Query query = entityManager.createNativeQuery(sqlString);
		query.setParameter("description", String.format("%%%s%%",description));
		query.setParameter("limit", 100);

		List<Object[]> rows = query.getResultList();
		var result = rows.stream().map(row -> {
			var ret = new ParameterBo();
			ret.setId((Integer) row[0]);
			ret.setDescription((String) row[1]);
			return ret;
		})
		.collect(Collectors.toList());
		return result;
	}

	private ParameterBo mapToBo(Parameter entity){
		return ParameterBo.builder()
				.id(entity.getId())
				.loincId(entity.getLoincId())
				.description(entity.getDescription())
				.typeId(entity.getTypeId())
				.inputCount(entity.getInputCount())
				.snomedGroupId(entity.getSnomedGroupId())
				.build();
	}

}
