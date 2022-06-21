package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments;


import ar.lamansys.sgx.shared.repositories.QueryPart;

public class DocumentDoctorSearchQuery extends DocumentSearchQuery {


	private boolean featureFlagsService;

    public DocumentDoctorSearchQuery(String plainText, Boolean featureFlagsService ){
        super(plainText);
		this.featureFlagsService = featureFlagsService;
    }

    @Override
    public QueryPart where() {
        String pattern = escapeSqlText.toUpperCase();
		if(!featureFlagsService) {
			return super.where().concatPart(new QueryPart(
					"AND (UPPER(CONCAT(creator.firstName," + "' '" + ",creator.lastName)) LIKE '%" + pattern + "%') \n"));
		}
		else {
			return super.where().concatPart(new QueryPart(
					"AND (UPPER(CONCAT(personextended.nameSelfDetermination," + "' '" + ",creator.lastName)) LIKE '%" + pattern + "%') \n"));
		}
    }

    @Override
    public String noResultMessage(){
        return "No se encontraron documentos escritos por el médico " + plainText;
    }
}
