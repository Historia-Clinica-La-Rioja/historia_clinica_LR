package net.pladema.snowstorm.services.inferredrules;

import lombok.Getter;
import lombok.Setter;

@Setter
public class InferredAllergyAttributes {

    public enum TYPE {
        ALLERGY(1),
        INTOLERANCE(2),
        NULL(0);

        @Getter
        private Short id;

        TYPE(Number id){
            this.id = id.shortValue();
        }
    }

    public enum CATEGORY {
        FOOD(1),
        MEDICATION(2),
        ENVIRONMENT(3),
        BIOLOGIC(4),
        NULL(0);

        @Getter
        private Short id;

        CATEGORY(Number id){
            this.id = id.shortValue();
        }
    }

    private Short type;

    private Short category;

    public InferredAllergyAttributes(TYPE type, CATEGORY category){
        this.type = type.getId();
        this.category = category.getId();
    }

    public Short getType(){
        return type == 0 ? null : type;
    }

    public Short getCategory(){
        return category == 0 ? null : category;
    }
}
