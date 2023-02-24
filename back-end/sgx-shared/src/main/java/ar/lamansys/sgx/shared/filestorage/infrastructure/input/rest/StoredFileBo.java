package ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.core.io.Resource;

@Getter
@ToString
@AllArgsConstructor
public class StoredFileBo {

    private Resource resource;
    private String contentType;
    private long contentLenght;

}
