package net.pladema.internation.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ResponsableDoctorDto implements Serializable {

    private Integer id;

    private String name;

    private String surname;
}
