package net.pladema.establishment.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

@Getter
@Setter
@ToString
@AllArgsConstructor

public class BedCategoriesDataVo {
    private BedCategoryVo category;
    private Integer freeBeds;
    private Integer occupiedBeds;

    public BedCategoriesDataVo(short id, String description, BigInteger freeBeds, BigInteger occupiedBeds){
        this.category = new BedCategoryVo(id, description);
        this.freeBeds = freeBeds.intValueExact();
        this.occupiedBeds = occupiedBeds.intValueExact();
    }
}


