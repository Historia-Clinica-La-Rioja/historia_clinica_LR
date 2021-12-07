package ar.lamansys.refcounterref.domain.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.core.io.Resource;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StoredFileBo {

    private Resource resource;
    private String contentType;
    private long contentLenght;

}
