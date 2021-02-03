package net.pladema.establishment.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.controller.service.domain.BedCategoriesDataBo;

@Getter
@Setter
@ToString
public class BedCategoriesDataDto {

    private BedCategoryDto category;
    private Integer freeBeds;
    private Integer occupiedBeds;

    public BedCategoriesDataDto(BedCategoriesDataBo bo){
        this.category = new BedCategoryDto();
        this.category.setDescription(bo.getCategory().getDescription());
        this.category.setId(bo.getCategory().getId().intValue());
        this.setFreeBeds(bo.getFreeBeds());
        this.setOccupiedBeds(bo.getOccupiedBeds());
    }
}
