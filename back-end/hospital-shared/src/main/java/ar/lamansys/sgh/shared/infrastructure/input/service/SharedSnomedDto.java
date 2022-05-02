package ar.lamansys.sgh.shared.infrastructure.input.service;

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
public class SharedSnomedDto implements Serializable {

    private static final long serialVersionUID = -198432836028268437L;
    
    private String sctid;

    private String pt;

    private String parentId;

    private String parentFsn;

    public SharedSnomedDto(String sctid, String pt) {
        this.sctid = sctid;
        this.pt = pt;
    }
}
