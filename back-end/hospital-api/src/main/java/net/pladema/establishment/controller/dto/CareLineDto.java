package net.pladema.establishment.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class CareLineDto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3210933116471323297L;

    private Integer id;

    private String description;

}