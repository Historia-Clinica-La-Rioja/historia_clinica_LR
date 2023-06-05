package net.pladema.edMonton.getPdfEdMonton.service;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.edMonton.getPdfEdMonton.dto.ImpresionEdMontonDto;

import net.pladema.edMonton.getPdfEdMonton.repository.ImpresionEdMontonRepository;

import net.pladema.edMonton.repository.domain.Answer;

import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;


@Service
public class ImpresionEdMontonServiceImpl implements ImpresionEdMontonService{

	private final Logger LOG = LoggerFactory.getLogger(ImpresionEdMontonServiceImpl.class);

	public static final String EDMONTON_TEST_NOT_FOUND = "edmonton_test_not_found";

	public static final String OUTPUT = "Output -> {}";


	private final ImpresionEdMontonRepository impresionEdMontonRepository;

	public ImpresionEdMontonServiceImpl(ImpresionEdMontonRepository impresionEdMontonRepository) {
		this.impresionEdMontonRepository = impresionEdMontonRepository;
	}


	@Override
	public List<ImpresionEdMontonDto> getImpresionEdMonton(Long edMontonId) {
		LOG.debug("input parameters -> edMontonId {}", edMontonId);
		List<Answer> result = impresionEdMontonRepository.getEdMontonReportInfo(edMontonId)
				.orElseThrow(() -> new NotFoundException("bad-EdMontonTestId", EDMONTON_TEST_NOT_FOUND));

		if( result != null && result.size() == 0)
			throw new NotFoundException("bad-EdMontonId", EDMONTON_TEST_NOT_FOUND);

		 return cast(result);
	}

	@Override
	public Map<String, Object> createEdMontonContext(List<ImpresionEdMontonDto> lst) {
		Map<String, Object> ctx = new HashMap<>();

		for(Integer i=0; i < lst.size(); i++){
			ImpresionEdMontonDto value = new ImpresionEdMontonDto(lst.get(i));
		}

		ctx.put("answers", lst);

		return ctx;
	}

	@Override
	public String getScore(Short score) {
		return null;
	}

	@Override
	public String createEdMontonFileName(Long edMontonId, ZonedDateTime edMontonDate) {

		LOG.debug("input parameters -> edMontonId {}, findriscDate {}", edMontonId, edMontonDate);
		String outputFileName = String.format("%s. EdMonton %s.pdf", edMontonDate, edMontonId);
		LOG.debug(OUTPUT, outputFileName);
		return outputFileName;
	}

	private List<ImpresionEdMontonDto> cast(List<Answer> lst){
		List<ImpresionEdMontonDto> lstCast = new ArrayList<ImpresionEdMontonDto>();
		for(Answer reg : lst){
			lstCast.add( new ImpresionEdMontonDto());
		}
		return lstCast;
	}


}
