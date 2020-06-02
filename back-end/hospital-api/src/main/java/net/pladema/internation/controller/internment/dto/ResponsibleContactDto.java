package net.pladema.internation.controller.internment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ResponsibleContactDto implements Serializable {

    private String fullName;

    private String phoneNumber;

    private String relationship;
}
