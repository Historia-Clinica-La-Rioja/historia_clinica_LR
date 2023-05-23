package net.pladema.edMonton.getPdfEdMonton.service;

import net.pladema.edMonton.getPdfEdMonton.dto.ImpresionEdMontonDto;

import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ImpresionEdMontonServiceImpl implements ImpresionEdMontonService{

	private final Logger LOG = LoggerFactory.getLogger(ImpresionEdMontonServiceImpl.class);

	public static final String EDMONTON_TEST_NOT_FOUND = "edmonton_test_not_found";




	@Override
	public List<ImpresionEdMontonDto> getImpresionEdMonton(Long edMontonId) {
		return null;
	}

	@Override
	public Map<String, Object> createEdMontonContext(List<ImpresionEdMontonDto> lst) {
		return null;
	}

	@Override
	public String getScore(Short score) {
		return null;
	}
}
