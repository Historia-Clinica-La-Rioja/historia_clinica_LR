package commercialmedication.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import commercialmedication.cache.application.port.CommercialMedicationArticlePort;

import commercialmedication.cache.domain.commercialMedicationArticle.Article;
import commercialmedication.cache.domain.commercialMedicationArticle.NewDrug;
import commercialmedication.cache.domain.decodedResponse.ArticleList;

import commercialmedication.cache.domain.decodedResponse.DatabaseUpdate;
import commercialmedication.cache.infrastructure.repository.CommercialMedicationArticleAtcRepository;
import commercialmedication.cache.infrastructure.repository.CommercialMedicationArticleBarCodeRepository;
import commercialmedication.cache.infrastructure.repository.CommercialMedicationArticleCoverageRepository;
import commercialmedication.cache.infrastructure.repository.CommercialMedicationArticleDrugRepository;
import commercialmedication.cache.infrastructure.repository.CommercialMedicationArticleGtinRepository;
import commercialmedication.cache.infrastructure.repository.CommercialMedicationArticleRepository;

import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationArticle;

import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationArticleAtc;
import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationArticleAtcPK;
import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationArticleBarCode;

import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationArticleBarCodePK;
import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationArticleCoverage;

import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationArticleDrug;
import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationArticleGtin;

import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationArticleGtinPK;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
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

	@Override
	public void saveAllNewArticlesFromUpdate(List<DatabaseUpdate> databaseUpdates) {
		ArticleList articleList = new ArticleList(databaseUpdates.stream().map(Article::new).collect(Collectors.toList()));
		saveAll(articleList);
	}

	@Override
	public void editArticles(List<DatabaseUpdate> databaseUpdates) {
		deleteAll(databaseUpdates);
		saveAllNewArticlesFromUpdate(databaseUpdates);
	}

	@Override
	public void updatePrices(List<DatabaseUpdate> databaseUpdates) {
		List<Integer> articleIds = databaseUpdates.stream().map(DatabaseUpdate::getArticleId).collect(Collectors.toList());
		List<CommercialMedicationArticle> articles = commercialMedicationArticleRepository.findAllById(articleIds);
		articles.forEach(article -> updateArticlePrice(databaseUpdates, article));
	}

	private void updateArticlePrice(List<DatabaseUpdate> databaseUpdates, CommercialMedicationArticle article) {
		Optional<DatabaseUpdate> latestArticlePriceUpdate = databaseUpdates.stream()
				.filter(update -> update.getArticleId().equals(article.getId()))
				.max(Comparator.comparingLong(DatabaseUpdate::getLogId));
		article.setPrice(latestArticlePriceUpdate.get().getPrice());
		article.setPriceValidityDate(latestArticlePriceUpdate.get().getPriceValidFrom());
	}

	@Override
	public void deleteAll(List<DatabaseUpdate> databaseUpdates) {
		databaseUpdates.forEach(this::deleteAllRelatedData);
	}

	private void deleteAllRelatedData(DatabaseUpdate update) {
		if (commercialMedicationArticleRepository.existsById(update.getArticleId()))
			commercialMedicationArticleRepository.deleteById(update.getArticleId());
		if (commercialMedicationArticleBarCodeRepository.existsByArticleId(update.getArticleId()) != null)
			commercialMedicationArticleBarCodeRepository.deleteByArticleId(update.getArticleId());
		if (commercialMedicationArticleCoverageRepository.existsById(update.getArticleId()))
			commercialMedicationArticleCoverageRepository.deleteById(update.getArticleId());
		if (commercialMedicationArticleAtcRepository.existsByArticleId(update.getArticleId()) != null)
			commercialMedicationArticleAtcRepository.deleteByArticleId(update.getArticleId());
		if (commercialMedicationArticleDrugRepository.existsByArticleId(update.getArticleId()) != null)
			commercialMedicationArticleDrugRepository.deleteByArticleId(update.getArticleId());
		if (commercialMedicationArticleGtinRepository.existsByArticleId(update.getArticleId()) != null)
			commercialMedicationArticleGtinRepository.deleteByArticleId(update.getArticleId());
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
