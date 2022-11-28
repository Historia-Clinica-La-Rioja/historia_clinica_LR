package net.pladema.establishment.service;

import net.pladema.establishment.service.domain.ClinicalSpecialtyBo;

import java.util.List;

public interface ClinicalSpecialtyCareLineService {

    List<ClinicalSpecialtyBo> getClinicalSpecialties(Integer careLineId);

}
