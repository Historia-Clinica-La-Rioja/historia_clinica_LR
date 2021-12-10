package ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ReferenceFileDto implements Serializable {

    private static final long serialVersionUID = 4707253517419628857L;

    private Integer fileId;
    private String fileName;

}