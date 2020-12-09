package net.pladema.renaper.controller.dto;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
@ToString
public class MedicalCoverageDto {

    Integer id;

    String rnos;

    String name;

    String service;

    String acronym;

    String dateQuery;
}
