package commercialmedication.cache.infrastructure.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import commercialmedication.cache.domain.commercialMedicationArticle.Article;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "article", schema = "commercial_medication")
@Entity
public class CommercialMedicationArticle implements Serializable {

	private static final long serialVersionUID = -4431678384651272469L;

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "price")
	private BigDecimal price;

	@Column(name = "price_validity_date")
	private LocalDate priceValidityDate;

	@Column(name = "name", length = 44)
	private String name;

	@Column(name = "presentation", length = 24)
	private String presentation;

	@Column(name = "laboratory_id")
	private Integer laboratoryId;

	@Column(name = "troquel")
	private Integer troquel;

	@Column(name = "presentation_status", length = 1)
	private String presentationStatus;

	@Column(name = "is_imported", length = 1)
	private String isImported;

	@Column(name = "needs_fridge_preservation", length = 1)
	private String needsFridgePreservation;

	@Column(name = "iva", length = 1)
	private String iva;

	@Column(name = "sell_type_id")
	private Integer sellTypeId;

	@Column(name = "control_id")
	private Integer controlId;

	@Column(name = "size_id")
	private Integer sizeId;

	@Column(name = "form_id")
	private Integer formId;

	@Column(name = "via_id")
	private Integer viaId;

	@Column(name = "mono_drug_id")
	private Integer monoDrugId;

	@Column(name = "action_id")
	private Integer actionId;

	@Column(name = "potency_value")
	private String potencyValue;

	@Column(name = "potency_id")
	private Integer potencyId;

	@Column(name = "presentation_unit")
	private Integer presentationUnit;

	@Column(name = "quantity_id")
	private Integer quantityId;

	@Column(name = "gravamen", length = 1)
	private String gravamen;

	@Column(name = "gluten_free", length = 1)
	private String glutenFree;

	@Column(name = "snomed_id")
	private String snomedId;

	@Column(name = "prospect")
	private Integer prospect;

	public CommercialMedicationArticle(Article article) {
		this.id = article.getId();
		this.price = article.getPrice();
		this.priceValidityDate = LocalDate.parse(article.getPriceValidFrom(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		this.name = article.getName();
		this.presentation = article.getPresentation();
		this.laboratoryId = article.getLaboratoryId();
		this.troquel = article.getPackageId();
		this.presentationStatus = article.getPresentationStatus();
		this.isImported = article.getIsImported();
		this.needsFridgePreservation = article.getNeedRefrigeration();
		this.iva = article.getHasIVATax();
		this.sellTypeId = article.getSellTypeId();
		this.controlId =  article.getPublicSanityInternCodeId();
		this.sizeId = article.getSizeId();
		this.formId = article.getFormId();
		this.viaId = article.getViaId();
		this.monoDrugId = article.getDrugId();
		this.actionId = article.getActionId();
		this.potencyValue = article.getPotency();
		this.potencyId = article.getPotencyUnitId();
		this.presentationUnit = article.getPresentationUnit();
		this.quantityId = article.getPresentationUnitId();
		this.gravamen = article.getBrand();
		this.glutenFree = article.getIsCeliacSuitable();
		this.snomedId = article.getSnomedId();
		this.prospect = article.getProspect();
	}

	public void setPriceValidityDate(String priceValidityDate) {
		this.priceValidityDate = LocalDate.parse(priceValidityDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
