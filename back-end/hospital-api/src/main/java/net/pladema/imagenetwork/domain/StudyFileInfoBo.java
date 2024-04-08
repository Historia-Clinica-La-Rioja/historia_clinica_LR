package net.pladema.imagenetwork.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StudyFileInfoBo {
    private String token;
    private String url;
    private String uuid;

    public StudyFileInfoBo(String url, String uuid) {
        this.url = url;
        this.uuid = uuid;
    }
}
