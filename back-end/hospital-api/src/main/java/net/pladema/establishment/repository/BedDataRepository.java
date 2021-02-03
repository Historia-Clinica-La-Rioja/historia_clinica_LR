package net.pladema.establishment.repository;

import net.pladema.establishment.repository.domain.BedCategoriesDataFilterVo;
import net.pladema.establishment.repository.domain.BedCategoriesDataVo;

import java.util.List;

public interface BedDataRepository {
    public List<BedCategoriesDataVo> execute(BedCategoriesDataFilterVo filter);
}
