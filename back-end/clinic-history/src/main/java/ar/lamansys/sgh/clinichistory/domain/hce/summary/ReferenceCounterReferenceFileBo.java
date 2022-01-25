package ar.lamansys.sgh.clinichistory.domain.hce.summary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReferenceCounterReferenceFileBo {

    private Integer fileId;

    private String fileName;

}
