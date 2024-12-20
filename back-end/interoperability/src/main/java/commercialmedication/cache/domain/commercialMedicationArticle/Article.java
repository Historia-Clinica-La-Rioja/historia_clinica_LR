package commercialmedication.cache.domain.commercialMedicationArticle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import commercialmedication.cache.domain.decodedResponse.DatabaseUpdate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "articulo")
public class Article {

	@XmlElement(name = "reg")
	private Integer id;

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
	private String brand; //Ni idea como nombrarlo

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

	public Article(DatabaseUpdate databaseUpdate) {
		this.id = databaseUpdate.getArticleId();
		this.price = databaseUpdate.getPrice();
		this.priceValidFrom = databaseUpdate.getPriceValidFrom();
		this.name = databaseUpdate.getName();
		this.presentation = databaseUpdate.getPresentation();
		this.laboratoryId = databaseUpdate.getLaboratoryId();
		this.packageId = databaseUpdate.getPackageId();
		this.presentationStatus = databaseUpdate.getPresentationStatus();
		this.isImported = databaseUpdate.getIsImported();
		this.needRefrigeration = databaseUpdate.getNeedRefrigeration();
		this.hasIVATax = databaseUpdate.getHasIVATax();
		this.barCodeList = databaseUpdate.getBarCodeList();
		this.sellTypeId = databaseUpdate.getSellTypeId();
		this.publicSanityInternCodeId = databaseUpdate.getPublicSanityInternCodeId();
		this.sizeId = databaseUpdate.getSizeId();
		this.formId = databaseUpdate.getFormId();
		this.viaId = databaseUpdate.getViaId();
		this.drugId = databaseUpdate.getDrugId();
		this.actionId = databaseUpdate.getActionId();
		this.newDrugList = databaseUpdate.getNewDrugList();
		this.potency = databaseUpdate.getPotency();
		this.potencyUnitId = databaseUpdate.getPotencyUnitId();
		this.presentationUnit = databaseUpdate.getPresentationUnit();
		this.presentationUnitId = databaseUpdate.getPresentationUnitId();
		this.gtinList = databaseUpdate.getGtinList();
		this.brand = databaseUpdate.getBrand();
		this.isCeliacSuitable = databaseUpdate.getIsCeliacSuitable();
		this.vademecumAndCoverage = databaseUpdate.getVademecumAndCoverage();
		this.snomedId = databaseUpdate.getSnomedId();
		this.prospect = databaseUpdate.getProspect();
		this.atcList = databaseUpdate.getAtcList();
	}

}

