package net.pladema.medication.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import net.pladema.medication.application.port.CommercialMedicationArticlePort;

import net.pladema.medication.domain.commercialMedicationArticle.Article;
import net.pladema.medication.domain.commercialMedicationArticle.NewDrug;
import net.pladema.medication.domain.decodedResponse.ArticleList;

import net.pladema.medication.infrastructure.repository.CommercialMedicationArticleAtcRepository;
import net.pladema.medication.infrastructure.repository.CommercialMedicationArticleBarCodeRepository;
import net.pladema.medication.infrastructure.repository.CommercialMedicationArticleCoverageRepository;
import net.pladema.medication.infrastructure.repository.CommercialMedicationArticleDrugRepository;
import net.pladema.medication.infrastructure.repository.CommercialMedicationArticleGtinRepository;
import net.pladema.medication.infrastructure.repository.CommercialMedicationArticleRepository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationArticle;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationArticleAtc;
import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationArticleAtcPK;
import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationArticleBarCode;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationArticleBarCodePK;
import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationArticleCoverage;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationArticleDrug;
import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationArticleGtin;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationArticleGtinPK;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommercialMedicationArticlePortImpl implements CommercialMedicationArticlePort {

	private final CommercialMedicationArticleRepository commercialMedicationArticleRepository;

	private final CommercialMedicationArticleBarCodeRepository commercialMedicationArticleBarCodeRepository;

	private final CommercialMedicationArticleCoverageRepository commercialMedicationArticleCoverageRepository;

	private final CommercialMedicationArticleAtcRepository commercialMedicationArticleAtcRepository;

	private final CommercialMedicationArticleDrugRepository commercialMedicationArticleDrugRepository;

	private final CommercialMedicationArticleGtinRepository commercialMedicationArticleGtinRepository;

	@Override
	public void saveAll(ArticleList articles) {
		List<CommercialMedicationArticle> articlesEntities = new ArrayList<>();
		List<CommercialMedicationArticleBarCode> articleBarCodes = new ArrayList<>();
		List<CommercialMedicationArticleCoverage> articleCoverages = new ArrayList<>();
		List<CommercialMedicationArticleAtc> articleAtcs = new ArrayList<>();
		List<CommercialMedicationArticleDrug> articleDrugs = new ArrayList<>();
		List<CommercialMedicationArticleGtin> articleGtins = new ArrayList<>();
		articles.getArticles().forEach(article -> handleArticle(article, articlesEntities, articleBarCodes, articleCoverages, articleAtcs, articleDrugs, articleGtins));
		commercialMedicationArticleRepository.saveAll(articlesEntities);
		commercialMedicationArticleBarCodeRepository.saveAll(articleBarCodes);
		commercialMedicationArticleCoverageRepository.saveAll(articleCoverages);
		commercialMedicationArticleAtcRepository.saveAll(articleAtcs);
		commercialMedicationArticleDrugRepository.saveAll(articleDrugs);
		commercialMedicationArticleGtinRepository.saveAll(articleGtins);
	}

	private void handleArticle(Article article, List<CommercialMedicationArticle> articlesEntities,
							   List<CommercialMedicationArticleBarCode> articleBarCodes,
							   List<CommercialMedicationArticleCoverage> articleCoverages, List<CommercialMedicationArticleAtc> articleAtcs,
							   List<CommercialMedicationArticleDrug> articleDrugs, List<CommercialMedicationArticleGtin> articleGtins) {
		articlesEntities.add(parseToArticleEntity(article));
		if (article.getBarCodeList() != null && article.getBarCodeList().getBarCodes() != null)
			articleBarCodes.addAll(parseToArticleBarCodeEntityList(article));
		if (article.getVademecumAndCoverage() != null)
			articleCoverages.add(parseToArticleCoverageEntity(article));
		if (article.getAtcList() != null && article.getAtcList().getAtcs() != null)
			articleAtcs.addAll(parseToArticleAtcEntityList(article));
		if (article.getNewDrugList() != null && article.getNewDrugList().getDrugs() != null)
			articleDrugs.addAll(parseToArticleDrugEntityList(article));
		if (article.getGtinList() != null && article.getGtinList().getGtins() != null)
			articleGtins.addAll(parseToArticleGtinEntityList(article));
	}

	private List<CommercialMedicationArticleGtin> parseToArticleGtinEntityList(Article article) {
		return article.getGtinList().getGtins().stream().map(gtin -> parseToArticleGtinEntity(article.getId(), gtin)).collect(Collectors.toList());
	}

	private CommercialMedicationArticleGtin parseToArticleGtinEntity(Integer id, String gtin) {
		return new CommercialMedicationArticleGtin(new CommercialMedicationArticleGtinPK(id, gtin));
	}

	private List<CommercialMedicationArticleDrug> parseToArticleDrugEntityList(Article article) {
		return article.getNewDrugList().getDrugs().stream().map(drug -> parseToArticleDrugEntity(article.getId(), drug)).collect(Collectors.toList());
	}

	private CommercialMedicationArticleDrug parseToArticleDrugEntity(Integer id, NewDrug drug) {
		return new CommercialMedicationArticleDrug(id, drug);
	}

	private List<CommercialMedicationArticleAtc> parseToArticleAtcEntityList(Article article) {
		return article.getAtcList().getAtcs().stream().map(atc -> parseToArticleAtcEntity(article.getId(), atc)).collect(Collectors.toList());
	}

	private CommercialMedicationArticleAtc parseToArticleAtcEntity(Integer id, String atc) {
		return new CommercialMedicationArticleAtc(new CommercialMedicationArticleAtcPK(id, atc));
	}

	private CommercialMedicationArticleCoverage parseToArticleCoverageEntity(Article article) {
		return new CommercialMedicationArticleCoverage(article.getId(), article.getVademecumAndCoverage());
	}

	private List<CommercialMedicationArticleBarCode> parseToArticleBarCodeEntityList(Article article) {
		return article.getBarCodeList().getBarCodes().stream().map(barCode -> parseToArticleBarCodeEntity(article.getId(), barCode)).collect(Collectors.toList());
	}

	private CommercialMedicationArticleBarCode parseToArticleBarCodeEntity(Integer id, String barCode) {
		return new CommercialMedicationArticleBarCode(new CommercialMedicationArticleBarCodePK(id, barCode));
	}

	private CommercialMedicationArticle parseToArticleEntity(Article article) {
		return new CommercialMedicationArticle(article);
	}


}
