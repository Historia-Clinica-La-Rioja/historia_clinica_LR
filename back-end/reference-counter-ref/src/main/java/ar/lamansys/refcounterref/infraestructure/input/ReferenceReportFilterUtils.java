package ar.lamansys.refcounterref.infraestructure.input;

import ar.lamansys.refcounterref.domain.report.ReferenceReportFilterBo;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ReferenceReportFilterUtils {

	public static ReferenceReportFilterBo parseFilter(String filter, ObjectMapper objectMapper) {
		ReferenceReportFilterBo searchFilter = null;
		try {
			searchFilter = objectMapper.readValue(filter, ReferenceReportFilterBo.class);
		} catch (IOException e) {
			log.error(String.format("Error mapping filter: %s", filter), e);
		}
		return searchFilter;
	}

}
