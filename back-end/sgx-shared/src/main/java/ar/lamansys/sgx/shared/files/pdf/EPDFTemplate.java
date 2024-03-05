package ar.lamansys.sgx.shared.files.pdf;

public enum EPDFTemplate {
	CONSENT_DOCUMENT("consent_document"),
	DAILY_APPOINTMENTS("daily-appointments"),
	FORM_REPORT("form_report"),
	RECIPE_ORDER_TABLE("recipe_order_table"),

	;

	public final String name;

	EPDFTemplate(String name) {
		this.name = name;
	}
}
