package net.pladema.person.service;

import net.pladema.person.controller.dto.PersonPhotoDto;

import java.util.List;

public interface PersonPhotoService {

    PersonPhotoDto get(Integer personId);

    List<PersonPhotoDto> get(List<Integer> personIds);

    boolean save(Integer personId, String imageData);

}
