package net.pladema.internation.controller.dto.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class DocumentsSummaryDto implements Serializable {

    private AnamnesisSummaryDto anamnesis;
}
