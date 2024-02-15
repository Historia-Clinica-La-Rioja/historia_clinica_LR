package net.pladema.establishment.infrastructure.input.rest.mapper;

import ar.lamansys.sgh.shared.infrastructure.input.service.rule.SharedRuleDto;

import net.pladema.establishment.repository.entity.Rule;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface RuleMapper {

	@Named("fromRule")
	SharedRuleDto fromRule(Rule rule);

}
