package net.pladema.assets.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.Resource;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssetsFileBo {
    private Resource resource;
    private String contentType;
}
