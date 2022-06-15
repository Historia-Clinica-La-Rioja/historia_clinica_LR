package net.pladema.clinichistory.hospitalization.repository;

import java.util.Optional;

import org.springframework.data.repository.query.Param;

import net.pladema.clinichistory.hospitalization.repository.domain.summary.InternmentSummaryVo;

public interface InternmentEpisodeStorage {


	Optional<InternmentSummaryVo> getSummary(@Param("internmentEpisodeId") Integer internmentEpisodeId);
}
