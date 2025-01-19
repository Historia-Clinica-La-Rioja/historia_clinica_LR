package ar.lamansys.sgh.clinichistory.domain.document;

import lombok.Getter;

@Getter
public class OrderImageFileReducedBo {

    private final Integer id;

    private final String name;

    public OrderImageFileReducedBo(Integer id, String name){
        this.id = id;
		this.name = name;
    }
}
