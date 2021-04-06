package net.pladema.hl7.concept.administration;

import net.pladema.hl7.dataexchange.ISingleResourceFhir;
import net.pladema.hl7.dataexchange.model.adaptor.FhirDateMapper;
import net.pladema.hl7.dataexchange.model.adaptor.FhirString;
import net.pladema.hl7.supporting.conformance.InteroperabilityCondition;
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
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Conditional(InteroperabilityCondition.class)
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
    public Patient fetch(String id, Map<ResourceType, Reference> references) {
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

        resource.addTelecom(newTelecom(patient.getPhoneNumber()));

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

        if(patient.hasBirthDateData()) {
            DateType birthdate = new DateType();
            birthdate.fromStringValue(patient.getBirthdate());
            resource.setBirthDateElement(birthdate);
        }
    }

    public static PatientVo encode(Resource baseResource){
        PatientVo data = new PatientVo();
        Patient resource = (Patient) baseResource;

        data.setId(resource.getId());
        if(resource.hasName()){
            HumanName humanName = resource.getName().get(0);
            List<StringType> names = humanName.getGiven();
            try {
                data.setFirstname(names.get(0).getValue());
                data.setMiddlenames(names.get(1).getValue());
            }
            catch (ArrayIndexOutOfBoundsException  ex){
                //nothing to do
            }
            data.setLastname(humanName.getFamily());
            if(humanName.hasFamilyElement()) {
                String otherLastNames = humanName.getFamilyElement().getExtension()
                        .stream()
                        .map(e -> e.getValue().toString())
                        .filter(e -> !e.equals(data.getLastname()))
                        .collect(Collectors.joining(" "));
                if(FhirString.hasText(otherLastNames))
                    data.setOtherLastName(otherLastNames);
            }
        }
        if(resource.hasGender())
            data.setGender(resource.getGender().getDisplay());
        if(resource.hasBirthDate())
            data.setBirthdate(FhirDateMapper.toLocalDate(resource.getBirthDate()));
        if(resource.hasTelecom())
            data.setPhoneNumber(resource.getTelecom().get(0).getValue());
        if(resource.hasAddress())
            data.setFullAddress(decodeAddress(resource.getAddress().get(0)));
        return data;
    }
}
