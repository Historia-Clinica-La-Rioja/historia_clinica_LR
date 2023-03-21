package net.pladema.clinichistory.hospitalization.application.getEpisodeDocument;

import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentResponseBo;

import java.util.List;

public interface FetchEpisodeDocument {

	List<EpisodeDocumentResponseBo> run(Integer internmentEpisodeId);
}
