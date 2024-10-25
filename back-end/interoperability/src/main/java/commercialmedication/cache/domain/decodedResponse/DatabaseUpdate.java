package commercialmedication.cache.domain.decodedResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import commercialmedication.cache.domain.commercialMedicationArticle.ATCList;
import commercialmedication.cache.domain.commercialMedicationArticle.BarCodeList;
import commercialmedication.cache.domain.commercialMedicationArticle.GTINList;
import commercialmedication.cache.domain.commercialMedicationArticle.NewDrugList;
import commercialmedication.cache.domain.commercialMedicationArticle.VademecumAndCoverage;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "logMF")
public class DatabaseUpdate {

	@XmlElement(name = "log")
	private Long logId;

	@XmlElement(name = "tipo")
	private String operationType;

	@XmlElement(name = "tabla")
	private String tableName;

	@XmlElement(name = "id")
	private Integer affectedRecordId;

	@XmlElement(name = "des")
	private String newEntryDescription;

	@XmlElement(name = "reg")
	private Integer articleId;

	@XmlElement(name = "prc")
	private BigDecimal price;

	@XmlElement(name = "vig")
	private String priceValidFrom;

	@XmlElement(name = "nom")
	private String name;

	@XmlElement(name = "pres")
	private String presentation;

	@XmlElement(name = "labo")
	private Integer laboratoryId;

	@XmlElement(name = "tro")
	private Integer packageId;

	@XmlElement(name = "est")
	private String presentationStatus;

	@XmlElement(name = "imp")
	private String isImported;

	@XmlElement(name = "hel")
	private String needRefrigeration;

	@XmlElement(name = "iva")
	private String hasIVATax;

	@XmlElement(name = "bars")
	private BarCodeList barCodeList;

	@XmlElement(name = "tipov")
	private Integer sellTypeId;

	@XmlElement(name = "salud")
	private Integer publicSanityInternCodeId;

	@XmlElement(name = "tam")
	private Integer sizeId;

	@XmlElement(name = "for")
	private Integer formId;

	@XmlElement(name = "via")
	private Integer viaId;

	@XmlElement(name = "dro")
	private Integer drugId;

	@XmlElement(name = "acc")
	private Integer actionId;

	@XmlElement(name = "mdrogasdetalle")
	private NewDrugList newDrugList;

	@XmlElement(name = "pot")
	private String potency;

	@XmlElement(name = "upot")
	private Integer potencyUnitId;

	@XmlElement(name = "uni")
	private Integer presentationUnit;

	@XmlElement(name = "uuni")
	private Integer presentationUnitId;

	@XmlElement(name = "gtins")
	private GTINList gtinList;

	@XmlElement(name = "grav")
	private String brand;

	@XmlElement(name = "cel")
	private String isCeliacSuitable;

	@XmlElement(name = "cob")
	private VademecumAndCoverage vademecumAndCoverage;

	@XmlElement(name = "sno")
	private String snomedId;

	@XmlElement(name = "prospecto")
	private Integer prospect;

	@XmlElement(name = "atcs")
	private ATCList atcList;

}
