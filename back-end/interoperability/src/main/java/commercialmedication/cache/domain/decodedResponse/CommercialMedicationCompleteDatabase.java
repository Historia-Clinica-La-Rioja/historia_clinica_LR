package commercialmedication.cache.domain.decodedResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "basecompleta")
public class CommercialMedicationCompleteDatabase {

	@XmlElement(name = "articulos")
	private ArticleList articleList;

	@XmlElement(name = "Laborato")
	private LaboratoryList laboratoryList;

	@XmlElement(name = "TipoVen")
	private SellTypeList sellTypeList;

	@XmlElement(name = "Tamanio")
	private SizeList sizeList;

	@XmlElement(name = "Formas")
	private FormList forms;

	@XmlElement(name = "Via")
	private ViaList viaList;

	@XmlElement(name = "Monodro")
	private DrugList drugList;

	@XmlElement(name = "Acciones")
	private ActionList actionList;

	@XmlElement(name = "NuevaDro")
	private NewDrugsList newDrugs;

	@XmlElement(name = "Potencia")
	private PotencyList potencyList;

	@XmlElement(name = "Cantidad")
	private QuantityList quantityList;

	@XmlElement(name = "Control")
	private PublicSanityInternCodeList publicSanityInternCodeList;

	@XmlElement(name = "ultimolog")
	private Long lastLog;
	
}
