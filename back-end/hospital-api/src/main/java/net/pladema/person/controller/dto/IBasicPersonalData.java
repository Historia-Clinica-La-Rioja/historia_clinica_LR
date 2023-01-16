package net.pladema.person.controller.dto;

import java.time.LocalDate;

public interface IBasicPersonalData {

    String getFirstName();

	String getMiddleNames();

    String getLastName();

	String getOtherLastNames();

    String getIdentificationNumber();

    String getPhoneNumber();

    Short getIdentificationTypeId();

	LocalDate getBirthDate();
}
