package net.pladema.person.controller.dto;

import lombok.*;

import javax.annotation.Nullable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonPhotoDto {

    @Nullable
    private Integer personId;

    private String imageData;

    @Override
    public String toString() {
        return "PersonPhotoDto{" +
                "personId" + personId +
                "exists imageData='" + (imageData != null) + '\'' +
                '}';
    }
}
