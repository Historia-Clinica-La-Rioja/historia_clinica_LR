package ar.lamansys.refcounterref.domain.file;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReferenceCounterReferenceFileBo {

    private Integer referenceCounterReferenceId;
    private Integer fileId;
    private String fileName;

    public ReferenceCounterReferenceFileBo(Integer referenceCounterReferenceId, Integer fileId, String fileName) {
        this.referenceCounterReferenceId = referenceCounterReferenceId;
        this.fileId = fileId;
        this.fileName = fileName;
    }

    public ReferenceCounterReferenceFileBo(ReferenceCounterReferenceFileBo referenceCounterReferenceFileBo) {
        this.referenceCounterReferenceId = referenceCounterReferenceFileBo.getReferenceCounterReferenceId();
        this.fileId = referenceCounterReferenceFileBo.getFileId();
        this.fileName = referenceCounterReferenceFileBo.getFileName();
    }

}
