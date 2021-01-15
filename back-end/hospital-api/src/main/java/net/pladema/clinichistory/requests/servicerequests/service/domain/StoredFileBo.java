package net.pladema.clinichistory.requests.servicerequests.service.domain;

import lombok.*;
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
