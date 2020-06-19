package net.pladema.clinichistory.hospitalization.controller.externalservice;

public interface InternmentEpisodeExternalService {

	Boolean existsActiveForBedId(Integer bedId);

}
