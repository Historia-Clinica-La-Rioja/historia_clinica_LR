package net.pladema.person.service;

import net.pladema.person.controller.dto.PersonPhotoDto;

public interface PersonPhotoService {

    PersonPhotoDto get(Integer personId);

    boolean save(Integer personId, String imageData);

}
