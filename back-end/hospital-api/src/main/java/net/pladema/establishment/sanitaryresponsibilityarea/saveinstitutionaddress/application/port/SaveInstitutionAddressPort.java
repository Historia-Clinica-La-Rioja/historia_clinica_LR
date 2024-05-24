package net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionaddress.application.port;

import net.pladema.establishment.sanitaryresponsibilityarea.saveinstitutionaddress.domain.SaveInstitutionAddressBo;

public interface SaveInstitutionAddressPort {

	void run(SaveInstitutionAddressBo institutionAddressAndGlobalCoordinates);

}
