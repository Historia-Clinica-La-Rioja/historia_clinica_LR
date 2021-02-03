package net.pladema.establishment.controller.service;

import net.pladema.establishment.controller.service.domain.BedCategoriesDataBo;
import net.pladema.establishment.controller.service.domain.BedCategoriesDataFilterBo;

import java.util.List;


public interface BedCategoriesDataService {
    public List<BedCategoriesDataBo> execute(BedCategoriesDataFilterBo filter);
}
