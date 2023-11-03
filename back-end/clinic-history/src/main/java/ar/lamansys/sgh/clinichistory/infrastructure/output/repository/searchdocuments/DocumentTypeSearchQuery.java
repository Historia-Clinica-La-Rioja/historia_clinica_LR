package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments;

import ar.lamansys.sgx.shared.repositories.QueryPart;

public class DocumentTypeSearchQuery extends DocumentSearchQuery {

	public DocumentTypeSearchQuery(String plainText) {
		super(plainText);
	}

	@Override
	public QueryPart where() {
		return super.where().concatPart(new QueryPart(
				"AND documenttype.id = " + plainText));
	}

	@Override
	public String noResultMessage(){
		return "No se encontraron documentos del tipo " + plainText;
	}

}
