package net.pladema.assets.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class Assets {

    private String contentType;
    private String nameFile;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Assets{");
        sb.append("contentType='").append(contentType).append('\'');
        sb.append(", nameFile='").append(nameFile).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

