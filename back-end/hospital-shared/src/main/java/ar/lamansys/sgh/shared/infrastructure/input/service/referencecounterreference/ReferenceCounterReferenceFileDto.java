package ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReferenceCounterReferenceFileDto implements Serializable {

    private static final long serialVersionUID = 3267240547638036540L;

    private Integer fileId;

    private String fileName;

}