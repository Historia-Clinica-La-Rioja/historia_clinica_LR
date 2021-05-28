package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments;


import ar.lamansys.sgx.shared.repositories.QueryPart;

public class DocumentCreatedOnSearchQuery extends DocumentSearchQuery {

    public DocumentCreatedOnSearchQuery(String plainText) {
        super(plainText);
    }

    @Override
    public QueryPart where() {
        return super.where().concatPart(new QueryPart(
                "AND DATE(document.creationable.createdOn) = '" + plainText + "' \n"));
    }

    @Override
    public String noResultMessage(){
        return "No se encontraron documentos creados el día " + plainText;
    }
}
