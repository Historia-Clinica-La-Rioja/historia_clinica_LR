package net.pladema.medication.application.port;

import net.pladema.medication.domain.decodedResponse.ArticleList;

public interface CommercialMedicationArticlePort {

	void saveAll(ArticleList articles);

}
