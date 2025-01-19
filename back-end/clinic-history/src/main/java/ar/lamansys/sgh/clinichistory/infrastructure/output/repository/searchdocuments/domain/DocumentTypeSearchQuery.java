package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments.domain;

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

}
