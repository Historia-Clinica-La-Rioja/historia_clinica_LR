package net.pladema.hl7.concept.administration;

import net.pladema.hl7.dataexchange.ISingleResourceFhir;
import net.pladema.hl7.supporting.exchange.database.FhirPersistentStore;
import net.pladema.hl7.supporting.terminology.coding.CodingProfile;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;
import net.pladema.hl7.dataexchange.model.domain.PatientVo;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PatientResource extends ISingleResourceFhir {

    @Autowired
    public PatientResource(FhirPersistentStore store){
        super(store);
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.Patient;
    }

    @Override
    public Patient fetch(String id, Reference[] references) {
        PatientVo patient = store.getPatient(id);

        Patient resource = new Patient();
        resource.setId(id);

        resource.addIdentifier(newIdentifier(resource, resource.getId()));
        resource.addIdentifier(newIdentifier(CodingSystem.Patient.IDENTIFIER, patient.getIdentificationNumber()));

        Extension fatherSurname = new Extension(CodingProfile.Patient.EXT_FATHER, new StringType(patient.getLastname()));
        resource.addName()
                .setUse(HumanName.NameUse.OFFICIAL)
                .addGiven(patient.getFirstname())
                .setFamily(patient.getLastname())
                .setText(patient.getFullName());
        resource.getName().get(0).getFamilyElement().addExtension(fatherSurname);

        resource.setGender(Enumerations.AdministrativeGender.fromCode(patient.getGender()));
        resource.setActive(true);

        resource.setMeta(newMeta(CodingProfile.Patient.URL));
        addNonRequiredAttributes(resource, patient);
        return resource;
    }

    private void addNonRequiredAttributes(Patient resource, PatientVo patient){
        if(patient.hasMiddlenamesData())
            resource.getName().get(0).addGiven(patient.getMiddlenames());

        if(patient.hasOtherLastName()) {
            Extension matherSurname = new Extension(CodingProfile.Patient.EXT_MATHER, new StringType(patient.getOtherLastName()));
            resource.getName().get(0).getFamilyElement().addExtension(matherSurname);
        }

        if(patient.hasAddressData())
            resource.addAddress(newAddress(patient.getFullAddress()));

        if(patient.hasPhoneNumber())
            resource.addTelecom(newTelecom(patient.getPhoneNumber()));

        if(patient.hasBirthDateData()) {
            DateType birthdate = new DateType();
            birthdate.fromStringValue(patient.getBirthdate());
            resource.setBirthDateElement(birthdate);
        }
    }
}
