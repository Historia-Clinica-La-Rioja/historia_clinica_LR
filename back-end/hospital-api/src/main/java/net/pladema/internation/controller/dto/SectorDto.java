package net.pladema.internation.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class SectorDto implements Serializable {

    private Integer id;

    private String description;
}
