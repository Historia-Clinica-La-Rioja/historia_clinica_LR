package net.pladema.hl7.dataexchange.medications;

import net.pladema.hl7.dataexchange.IResourceFhir;

import net.pladema.hl7.supporting.conformance.InteroperabilityCondition;
import net.pladema.hl7.supporting.exchange.database.FhirPersistentStore;
import net.pladema.hl7.dataexchange.model.domain.MedicationIngredientVo;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;
import net.pladema.hl7.dataexchange.model.domain.MedicationVo;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Ratio;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Conditional(InteroperabilityCondition.class)
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
        medication.getIngredients().forEach(ingredient ->
            ingredients.add(new Medication.MedicationIngredientComponent()
                    .setItem(newCodeableConcept(CodingSystem.SNOMED, ingredient.get()))
                    .setIsActive(ingredient.isActive())
                    .setStrength(new Ratio()
                            .setNumerator(newQuantity(ingredient.getUnitMeasure(), ingredient.getUnitValue()))
                            .setDenominator(newQuantity(ingredient.getPresentationUnit(), ingredient.getPresentationValue()))))
        );
        return ingredients;
    }

    public static void encode(MedicationVo data, Resource baseResource){
        Medication resource = (Medication) baseResource;
        data.setId(resource.getId());
        if(resource.hasCode()){
            Pair<String, String> coding = decodeCoding(resource.getCode());
            data.setSctidCode(coding.getKey());
            data.setSctidTerm(coding.getValue());
        }
        if(resource.hasForm()){
            Pair<String, String> coding = decodeCoding(resource.getForm());
            data.setFormCode(coding.getKey());
            data.setFormTerm(coding.getValue());
        }
        if(resource.hasIngredient())
            data.setIngredients(encodeIngredients(resource));

    }

    private static List<MedicationIngredientVo> encodeIngredients(Medication resource){
        List<MedicationIngredientVo> ingredients = new ArrayList<>();
        resource.getIngredient().forEach(ingredient->{
            MedicationIngredientVo ingredientData = new MedicationIngredientVo();

            if(ingredient.hasItemCodeableConcept()) {
                Pair<String, String> coding = decodeCoding((CodeableConcept) ingredient.getItem());
                ingredientData.setSctidCode(coding.getKey());
                ingredientData.setSctidTerm(coding.getValue());
            }
            if(ingredient.hasStrength()){
                if(ingredient.getStrength().hasNumerator()){
                    Quantity numerator = ingredient.getStrength().getNumerator();
                    ingredientData.setUnitMeasure(numerator.getUnit());
                    ingredientData.setUnitValue(numerator.getValue().doubleValue());
                }
                if(ingredient.getStrength().hasNumerator()){
                    Quantity denominator = ingredient.getStrength().getDenominator();
                    ingredientData.setPresentationUnit(denominator.getUnit());
                    ingredientData.setPresentationValue(denominator.getValue().doubleValue());
                }
            }
            ingredientData.setActive(ingredient.getIsActive());
            ingredients.add(ingredientData);
        });
        return ingredients;
    }

}
