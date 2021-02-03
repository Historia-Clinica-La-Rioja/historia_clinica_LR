package net.pladema.establishment.controller.service.impl;

import net.pladema.establishment.controller.service.BedCategoriesDataService;
import net.pladema.establishment.controller.service.domain.BedCategoriesDataBo;
import net.pladema.establishment.controller.service.domain.BedCategoriesDataFilterBo;
import net.pladema.establishment.controller.service.domain.BedCategoryBo;
import net.pladema.establishment.repository.BedDataRepository;
import net.pladema.establishment.repository.domain.BedCategoriesDataFilterVo;
import net.pladema.establishment.repository.domain.BedCategoriesDataVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BedCategoriesDataServiceImpl implements BedCategoriesDataService {
    private static final Logger LOG = LoggerFactory.getLogger(BedCategoriesDataServiceImpl.class);

    private final BedDataRepository bedDataRepository;

    public BedCategoriesDataServiceImpl(BedDataRepository bedDataRepository) {
        this.bedDataRepository = bedDataRepository;
    }

    @Override
    public List<BedCategoriesDataBo> execute(BedCategoriesDataFilterBo filter){
        LOG.debug("Input parameters -> filter {}", filter);
        BedCategoriesDataFilterVo filterVo = new BedCategoriesDataFilterVo(filter.getSectorDescription());
        List<BedCategoriesDataBo> result = bedDataRepository.execute(filterVo).stream()
                .map(this::createBedCategoriesDataBo)
                .collect(Collectors.toList());
        LOG.trace("OUTPUT List -> {}", result);
        return result;
    }

    private BedCategoriesDataBo createBedCategoriesDataBo(BedCategoriesDataVo bedCategoriesDataVo){
        BedCategoryBo bedCategoryBo =
                new BedCategoryBo(bedCategoriesDataVo.getCategory().getId(),bedCategoriesDataVo.getCategory().getDescription());
        return new BedCategoriesDataBo(bedCategoryBo,bedCategoriesDataVo.getFreeBeds(), bedCategoriesDataVo.getOccupiedBeds());
    }

}
