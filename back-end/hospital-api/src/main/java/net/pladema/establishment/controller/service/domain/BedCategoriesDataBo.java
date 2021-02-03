package net.pladema.establishment.controller.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BedCategoriesDataBo {
    private BedCategoryBo category;
    private Integer freeBeds;
    private Integer occupiedBeds;
}
