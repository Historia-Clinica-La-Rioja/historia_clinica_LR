package net.pladema.person.controller.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonPhotoDto {

    private String imageData;

    @Override
    public String toString() {
        return "PersonPhotoDto{" +
                "exists imageData='" + (imageData != null) + '\'' +
                '}';
    }
}
