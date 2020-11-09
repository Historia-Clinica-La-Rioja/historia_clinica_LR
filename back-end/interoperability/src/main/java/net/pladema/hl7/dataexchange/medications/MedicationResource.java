package net.pladema.hl7.dataexchange.medications;

import net.pladema.hl7.dataexchange.IResourceFhir;
import net.pladema.hl7.supporting.exchange.database.FhirPersistentStore;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;
import net.pladema.hl7.dataexchange.model.domain.MedicationVo;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.Ratio;
import org.hl7.fhir.r4.model.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MedicationResource extends IResourceFhir {

    @Autowired
    public MedicationResource(FhirPersistentStore store){
        super(store);
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.Medication;
    }

    public Medication fetch(MedicationVo medication) {
        Medication resource = new Medication()
                .setCode(newCodeableConcept(CodingSystem.SNOMED, medication.get()))
                .setForm(newCodeableConcept(CodingSystem.SNOMED, medication.getForm()))
                .setIngredient(buildIngredientsData(medication));
        resource.setId(medication.getId());
        return resource;
    }

    private List<Medication.MedicationIngredientComponent> buildIngredientsData(MedicationVo medication) {
        List<Medication.MedicationIngredientComponent> ingredients = new ArrayList<>();
        medication.getIngredients().forEach((ingredient) ->
            ingredients.add(new Medication.MedicationIngredientComponent()
                    .setItem(newCodeableConcept(CodingSystem.SNOMED, ingredient.get()))
                    .setIsActive(ingredient.isActive())
                    .setStrength(new Ratio()
                            .setNumerator(newQuantity(ingredient.getUnitMeasure(), ingredient.getUnitValue()))
                            .setDenominator(newQuantity(ingredient.getPresentationUnit(), ingredient.getPresentationValue()))))
        );
        return ingredients;
    }

}
