//
//HAPI provides a built-in and configurable mechanism for validating resources using
// FHIR's own conformance resources (StructureDefinition, ValueSet, CodeSystem, etc.).
// This mechanism is called the Instance Validator.
// Terminology: http://hl7.org/fhir/terminology-module.html
//

package net.pladema.hl7.supporting.implementer.validating;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import net.pladema.hl7.supporting.terminology.support.AllergyIntoleranceProfile;
import net.pladema.hl7.supporting.terminology.support.ConditionProfile;
import net.pladema.hl7.supporting.terminology.support.DeviceProfile;
import net.pladema.hl7.supporting.terminology.support.DocumentReferenceProfile;
import net.pladema.hl7.supporting.terminology.support.GeneralProfile;
import net.pladema.hl7.supporting.terminology.support.ImmunizationProfile;
import net.pladema.hl7.supporting.terminology.support.MedicationStatementProfile;
import net.pladema.hl7.supporting.terminology.support.PatientProfile;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;

public class ApiFhirInstanceValidator extends FhirInstanceValidator {

    public ApiFhirInstanceValidator(FhirContext theContext) {
        super(theContext);
        IValidationSupport chain = newValidationSupportChain(theContext);
        super.setValidationSupport(
                // Wrap the chain in a cache to improve performance
                new CachingValidationSupport(chain));
    }

    private IValidationSupport newValidationSupportChain(FhirContext context){
        return new ValidationSupportChain(
                terminologySuppport(context),
                new DefaultProfileValidationSupport(context),
                new SnapshotGeneratingValidationSupport(context),
                new InMemoryTerminologyServerValidationSupport(context),
                new CommonCodeSystemsTerminologyService(context)
        );
    }

    private PrePopulatedValidationSupport terminologySuppport(FhirContext context){
        PrePopulatedValidationSupport profiles = new PrePopulatedValidationSupport(context);

        //=====================ValueSets=====================
        ConditionProfile.allValueSet().forEach(profiles::addValueSet);
        GeneralProfile.allValueSet().forEach(profiles::addValueSet);

        profiles.addCodeSystem(GeneralProfile.absentUnknown());

        //===============StructureDefinitions================
        profiles.addStructureDefinition(AllergyIntoleranceProfile.structureDefinition());
        profiles.addStructureDefinition(ConditionProfile.structureDefinition());
        profiles.addStructureDefinition(DeviceProfile.structureDefinition());
        profiles.addStructureDefinition(ImmunizationProfile.structureDefinition());
        profiles.addStructureDefinition(MedicationStatementProfile.structureDefinition());
        profiles.addStructureDefinition(PatientProfile.structureDefinition());
        profiles.addStructureDefinition(DocumentReferenceProfile.structureDefinition());

        return profiles;
    }

}
