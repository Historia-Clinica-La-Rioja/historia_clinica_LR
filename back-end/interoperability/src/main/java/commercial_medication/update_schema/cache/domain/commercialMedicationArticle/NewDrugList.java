package commercial_medication.update_schema.cache.domain.commercialMedicationArticle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "mdrogasdetalle")
public class NewDrugList {

	@XmlElement(name = "mdroga")
	private List<NewDrug> drugs;

}
