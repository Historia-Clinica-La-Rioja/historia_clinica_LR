package net.pladema.edMonton.getPdfEdMonton.service;

import net.pladema.edMonton.getPdfEdMonton.dto.ImpresionEdMontonDto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public interface ImpresionEdMontonService {

	List<ImpresionEdMontonDto> getImpresionEdMonton(Long edMontonId);

	Map<String, Object> createEdMontonContext(List <ImpresionEdMontonDto> lst, Object result);

	Object getScore(Long edMontonId);

	String createEdMontonFileName(Long edMontonId, ZonedDateTime edMontonDate);

}
