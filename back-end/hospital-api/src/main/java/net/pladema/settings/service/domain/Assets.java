package net.pladema.settings.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class Assets {

    private Integer id;
    private String description;
    private String contentType;
    private String nameFile;
    private String extension;

}
