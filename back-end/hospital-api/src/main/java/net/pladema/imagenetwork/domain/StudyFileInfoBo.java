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
    private Integer pacServerId;

    public StudyFileInfoBo(String url, String uuid, Integer pacServerId) {
        this.url = url;
        this.uuid = uuid;
        this.pacServerId = pacServerId;
    }
}
