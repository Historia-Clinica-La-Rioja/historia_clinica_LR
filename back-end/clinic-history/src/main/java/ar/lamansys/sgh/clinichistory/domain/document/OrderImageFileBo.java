package ar.lamansys.sgh.clinichistory.domain.document;

import lombok.Getter;

@Getter
public class OrderImageFileBo {

    private final Integer id;

    private final String path;

    private final String contentType;

    private final Long size;

    private final Integer orderId;

    private final String name;

    private final Boolean deleted;

    public OrderImageFileBo(Integer id, String path, String contentType, Long size, Integer orderId, String name, Boolean deleted){
        this.id = id;
		this.path = path;
		this.contentType = contentType;
		this.size = size;
		this.orderId = orderId;
		this.name = name;
		this.deleted = deleted;
    }
}
