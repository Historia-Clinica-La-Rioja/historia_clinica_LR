package net.pladema.snowstorm.controller.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm.SharedSnowstormPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm.SharedSnowstormSearchDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm.SharedSnowstormSearchItemDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm.exceptions.SnowstormPortEnumException;
import ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm.exceptions.SnowstormPortException;
import net.pladema.snowstorm.services.SnowstormService;
import net.pladema.snowstorm.services.domain.SnowstormItemResponse;
import net.pladema.snowstorm.services.domain.SnowstormSearchResponse;
import net.pladema.snowstorm.services.domain.nomivac.SnowstormNomivacItemResponse;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;
import net.pladema.snowstorm.services.fetchNomivacRefset.FetchNomivacRefset;
import net.pladema.snowstorm.services.impl.CalculateCie10CodesServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SnowstormExternalService implements SharedSnowstormPort {

    private final Logger logger;

    private final FetchNomivacRefset fetchNomivacRefset;

	private final SnowstormService snowstormService;

    public SnowstormExternalService(FetchNomivacRefset fetchNomivacRefset, SnowstormService snowstormService) {
        super();
        this.fetchNomivacRefset = fetchNomivacRefset;
        logger = LoggerFactory.getLogger(CalculateCie10CodesServiceImpl.class);
		this.snowstormService = snowstormService;
    }

    @Override
    public String mapSctidToNomivacId(String sctid)  throws SnowstormPortException {
        logger.debug("Start map sctid {} to NomivacId", sctid);
        SnowstormNomivacItemResponse result = null;
        try {
            result = fetchNomivacRefset.run(sctid);
        } catch (SnowstormApiException e) {
            throw new SnowstormPortException(SnowstormPortEnumException.valueOf(e.getCode().name()), e.getStatusCode(), e.getMessage());
        }
        if (result == null)
            return null;
        logger.debug("Finish map sctid {} to NomivacId {}", sctid, result.getMapTarget());
        return result.getMapTarget();
    }

	@Override
	public SharedSnowstormSearchDto getConcepts(String term, String ecl) throws SnowstormPortException {
		logger.debug("Input data -> term: {} , ecl: {} ", term, ecl);
		SharedSnowstormSearchDto result;
		try {
			var snowstormResponse = snowstormService.getConcepts(term, ecl);
			result = mapToSharedSnowstormSearchDto(snowstormResponse);
		} catch (SnowstormApiException e) {
			throw new SnowstormPortException(SnowstormPortEnumException.valueOf(e.getCode().name()), e.getStatusCode(), e.getMessage());
		}
		logger.debug("size {}", result.getTotal());
		return result;
	}

	@Override
	public SharedSnowstormSearchDto getConceptsByEcl(String ecl) throws SnowstormPortException {
		logger.debug("Input data -> ecl: {} ", ecl);
		SharedSnowstormSearchDto result;
		try {
			var snowstormResponse = snowstormService.getConceptsByEcl(ecl);
			result = mapToSharedSnowstormSearchDto(snowstormResponse);
		} catch (SnowstormApiException e) {
			throw new SnowstormPortException(SnowstormPortEnumException.valueOf(e.getCode().name()), e.getStatusCode(), e.getMessage());
		}
		logger.debug("size {}", result.getTotal());
		return result;
	}

	@Override
	public SharedSnowstormSearchDto getConcepts(List<Integer> ids) throws SnowstormPortException {
		logger.debug("Input data -> ids: {} ", ids.toString());
		SharedSnowstormSearchDto result;
		try {
			var snowstormResponse = snowstormService.getConcepts(ids);
			result = mapToSharedSnowstormSearchDto(snowstormResponse);
		} catch (SnowstormApiException e) {
			throw new SnowstormPortException(SnowstormPortEnumException.valueOf(e.getCode().name()), e.getStatusCode(), e.getMessage());
		}
		logger.debug("size {}", result.getTotal());
		return result;
	}

	@Override
	public SharedSnowstormSearchItemDto getConceptById(String conceptId) throws SnowstormPortException {
		logger.debug("Input data -> id: {} ", conceptId);
		SharedSnowstormSearchItemDto result;
		try {
			var snowstormItemResponse = snowstormService.getConceptById(conceptId);
			result = mapToSharedSnowstormItemDto(snowstormItemResponse);
		} catch (SnowstormApiException e) {
			throw new SnowstormPortException(SnowstormPortEnumException.valueOf(e.getCode().name()), e.getStatusCode(), e.getMessage());
		}
		logger.debug("result {}", result.toString());
		return result;
	}

	private SharedSnowstormSearchDto mapToSharedSnowstormSearchDto(SnowstormSearchResponse response) {
		return new SharedSnowstormSearchDto(
				response.getItems()
						.stream()
						.map(this::mapToSharedSnowstormItemDto)
						.collect(Collectors.toList()),
				response.getLimit(),
				response.getTotal(),
				response.getSearchAfter()
		);
	}

	private SharedSnowstormSearchItemDto mapToSharedSnowstormItemDto(SnowstormItemResponse itemResponse) {
		return new SharedSnowstormSearchItemDto(
				itemResponse.getId(),
				itemResponse.getConceptId(),
				itemResponse.getActive(),
				itemResponse.getPt().get("term").textValue()
		);
	}

}
