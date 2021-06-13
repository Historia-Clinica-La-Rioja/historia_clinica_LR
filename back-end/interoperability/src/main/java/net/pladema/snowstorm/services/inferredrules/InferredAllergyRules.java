package net.pladema.snowstorm.services.inferredrules;

import lombok.experimental.UtilityClass;
import net.pladema.snowstorm.services.domain.SnowstormItemResponse;

import java.util.List;

@UtilityClass
public class InferredAllergyRules {

    //420134006 |propensión a experimentar reacciones adversas (hallazgo)|
    private static final TreeNode<String, InferredAllergyAttributes> rules = new TreeNode<>("420134006", new InferredAllergyAttributes(
            InferredAllergyAttributes.TYPE.NULL,InferredAllergyAttributes.CATEGORY.NULL));

    static {
        //418038007 |propensión a experimentar reacciones adversas a sustancia (hallazgo)|
        TreeNode<String,InferredAllergyAttributes> propension = rules.addChild("418038007", new InferredAllergyAttributes(
                InferredAllergyAttributes.TYPE.NULL,InferredAllergyAttributes.CATEGORY.NULL));

        //419199007 |alergia a sustancia (hallazgo)|
        TreeNode<String,InferredAllergyAttributes> allergyToSubstance = propension.addChild("419199007", new InferredAllergyAttributes(
                InferredAllergyAttributes.TYPE.ALLERGY, InferredAllergyAttributes.CATEGORY.ENVIRONMENT));

        //414285001 |alergia alimentaria (hallazgo)|
        allergyToSubstance.addChild("414285001",
                new InferredAllergyAttributes(InferredAllergyAttributes.TYPE.ALLERGY,InferredAllergyAttributes.CATEGORY.FOOD));
        //416098002 |alergia medicamentosa (hallazgo)|
        allergyToSubstance.addChild("416098002",
                new InferredAllergyAttributes(InferredAllergyAttributes.TYPE.ALLERGY,InferredAllergyAttributes.CATEGORY.MEDICATION));

        //782197009 |intolerancia a sustancia (hallazgo)|
        TreeNode<String,InferredAllergyAttributes> intoleranceToSubstance = propension.addChild("782197009",
                new InferredAllergyAttributes(InferredAllergyAttributes.TYPE.INTOLERANCE,InferredAllergyAttributes.CATEGORY.ENVIRONMENT));

        //235719002 |intolerancia alimentaria (hallazgo)|
        intoleranceToSubstance.addChild("235719002",
                new InferredAllergyAttributes(InferredAllergyAttributes.TYPE.INTOLERANCE,InferredAllergyAttributes.CATEGORY.FOOD));
        //59037007 |intolerancia medicamentosa (hallazgo)|
        intoleranceToSubstance.addChild("59037007",
                new InferredAllergyAttributes(InferredAllergyAttributes.TYPE.INTOLERANCE,InferredAllergyAttributes.CATEGORY.MEDICATION));

        //609433001 |disposición hipersensible (hallazgo)|
        TreeNode<String,InferredAllergyAttributes> disposicionHipersensible = rules.addChild("609433001",
                new InferredAllergyAttributes(InferredAllergyAttributes.TYPE.ALLERGY,InferredAllergyAttributes.CATEGORY.NULL));

        //609328004 |disposición alérgica (hallazgo)|
        TreeNode<String,InferredAllergyAttributes> disposicionAlergica = disposicionHipersensible.addChild("609328004",
                new InferredAllergyAttributes(InferredAllergyAttributes.TYPE.ALLERGY,InferredAllergyAttributes.CATEGORY.BIOLOGIC));

        //419199007 |alergia a sustancia (hallazgo)|
        disposicionAlergica.addChild("419199007", new InferredAllergyAttributes(
                InferredAllergyAttributes.TYPE.ALLERGY, InferredAllergyAttributes.CATEGORY.ENVIRONMENT));
    }

    public static InferredAllergyAttributes inferred(List<SnowstormItemResponse> ancestors){
        return rules.findTreeNode(ancestors);
    }
}
