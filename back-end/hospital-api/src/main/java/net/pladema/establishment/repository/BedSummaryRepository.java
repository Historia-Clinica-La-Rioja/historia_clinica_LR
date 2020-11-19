package net.pladema.establishment.repository;

import net.pladema.establishment.repository.domain.BedSummaryVo;

import java.util.List;

public interface BedSummaryRepository {
    List<BedSummaryVo> execute(Integer institutionId);
}
