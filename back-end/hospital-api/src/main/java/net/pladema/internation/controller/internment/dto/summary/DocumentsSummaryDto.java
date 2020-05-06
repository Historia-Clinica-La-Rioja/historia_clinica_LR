package net.pladema.internation.controller.internment.dto.summary;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class DocumentsSummaryDto implements Serializable {

    private AnamnesisSummaryDto anamnesis;

    private EpicrisisSummaryDto epicrisis;
}
