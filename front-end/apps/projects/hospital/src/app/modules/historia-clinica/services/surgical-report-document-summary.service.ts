import { Injectable } from '@angular/core';
import { AnestheticTechniqueDto, DiagnosisDto, DocumentHealthcareProfessionalDto, EProfessionType, GenericMasterDataDto, HospitalizationProcedureDto, ProsthesisInfoDto, SurgicalReportDto } from '@api-rest/api-model';
import { DocumentsSummaryMapperService } from './documents-summary-mapper.service';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';
import { DescriptionItemDataSummary } from '@historia-clinica/components/description-item-data-summary/description-item-data-summary.component';
import { ANESTHETIC_TECHNIQUE_DESCRIPTION_ITEM, CULTURES_SURGERY_DESCRIPTION_ITEM, DRAINAGE_SURGERY_DESCRIPTION_ITEM, FROZEN_BIOPSY_SURGERY_DESCRIPTION_ITEM, PATHOLOGIST_DESCRIPTION_ITEM, PROTESIS_SURGERY_DESCRIPTION_ITEM, SURGERY_TEAM_DESCRIPTION_ITEM, TRANSFUSIONIST_DESCRIPTION_ITEM } from '@historia-clinica/constants/document-summary.constants';
import { CustomDiagnosesData, ItemsAndDescriptionData, SurgicalProcedures } from '@historia-clinica/utils/document-summary.model';
import { dateDtoToDate, timeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { RequestMasterDataService } from '@api-rest/services/request-masterdata.service';
import { getElementAtPosition } from '@core/utils/array.utils';

const INFO_DIVIDER = ' | ';
const PATHOLOGIST_INFO = {
	type: EProfessionType.PATHOLOGIST,
	description: 'Patólogo'
};
const TRANSFUSIONIST_INFO = {
	type: EProfessionType.TRANSFUSIONIST,
	description: 'Transfusionista'
};
const UNIQUE_DESCRIPTION_POSITION = 0;

@Injectable({
	providedIn: 'root'
})
export class SurgicalReportDocumentSummaryService {

	professions: GenericMasterDataDto<EProfessionType>[];

	constructor(
		private readonly documentsSummaryService: DocumentsSummaryMapperService,
		private readonly requestMasterDataService: RequestMasterDataService,
	) {
		this.requestMasterDataService.getSurgicalReportProfessionTypes().subscribe(professions => {
			this.professions = professions;
		})
	}

	mapToSurgicalReportViewFormat(surgicalReport: SurgicalReportDto): SurgicalReportViewFormat {
		return {
			...(surgicalReport.mainDiagnosis && { mainDiagnosis: this.mapToCustomDiagnosesData([surgicalReport.mainDiagnosis], 'Diagnósticos Pre-Operatorios') }),
			...(surgicalReport.preoperativeDiagnosis.length && { preOperativeDiagnosis: this.documentsSummaryService.mapDiagnosisToDescriptionItemData(surgicalReport.preoperativeDiagnosis) }),
			...(surgicalReport.surgeryProcedures.length && { proposedSurgeries: this.documentsSummaryService.mapProposedSurgeriesToDescriptionItemDataSummary(surgicalReport.surgeryProcedures) }),
			...(surgicalReport.surgicalTeam.length && { surgicalTeam: this.mapSurgeryTeamToDescriptionItemDataSummary(surgicalReport.surgicalTeam) }),
			...(surgicalReport.anesthesia.length && { anestheticTechniques: this.mapAnestheticTechniqueToDescriptionItemDataSummary(surgicalReport.anesthesia) }),
			surgicalProcedures: this.mapSurgicalProcedures(surgicalReport, 'historia-clinica.surgical-report.summary.PROCEDURES'),
			...(surgicalReport.pathologist && { pathologist: this.mapPathologistToDescriptionItemDataSummary([surgicalReport.pathologist]) }),
			...(surgicalReport.transfusionist && { transfusionist: this.mapTransfusionistToDescriptionItemDataSummary([surgicalReport.transfusionist]) }),
			...(surgicalReport.cultures.length && { cultures: this.mapCultureToItemDataSummary(surgicalReport.cultures) }),
			prosthesis: this.mapProsthesisDescriptionToItemDataSummary(surgicalReport.prosthesisInfo),
			...(surgicalReport.frozenSectionBiopsies.length && { frozenSectionBiopsies: this.mapFrozenBiopsyToItemDataSummary(surgicalReport.frozenSectionBiopsies) }),
			...(surgicalReport.drainages.length && { drainages: this.mapDrainageToItemDataSummary(surgicalReport.drainages) }),
			...(surgicalReport.postoperativeDiagnosis.length && { postOperativeDiagnosis: this.mapToCustomDiagnosesData(surgicalReport.postoperativeDiagnosis, 'Diagnósticos Post-Operatorios') }),
		}
	}

	private mapToCustomDiagnosesData(diagnoses: DiagnosisDto[], title: string): CustomDiagnosesData {
		return {
			title: title,
			diagnoses: this.documentsSummaryService.mapDiagnosisToDescriptionItemData(diagnoses)
		}
	}

	private mapProsthesisDescriptionToItemDataSummary(prosthesisInfo: ProsthesisInfoDto): ItemsAndDescriptionData {
		return {
			...PROTESIS_SURGERY_DESCRIPTION_ITEM,
			data: {
				items: prosthesisInfo?.hasProsthesis !== undefined
					? [{ description: prosthesisInfo.hasProsthesis ? 'Si' : 'No' }]
					: [{ description: 'sin información' }],
				note: prosthesisInfo?.description
					? [this.documentsSummaryService.toDescriptionItemData(prosthesisInfo.description)]
					: [],
			}
		};
	}

	private mapDrainageToItemDataSummary(hospitalizationProcedure: HospitalizationProcedureDto[]): ItemsAndDescriptionData {
		return this.mapProcedureToItemDataSummary(hospitalizationProcedure, DRAINAGE_SURGERY_DESCRIPTION_ITEM);
	}

	private mapFrozenBiopsyToItemDataSummary(hospitalizationProcedure: HospitalizationProcedureDto[]): ItemsAndDescriptionData {
		return this.mapProcedureToItemDataSummary(hospitalizationProcedure, FROZEN_BIOPSY_SURGERY_DESCRIPTION_ITEM);
	}

	private mapCultureToItemDataSummary(hospitalizationProcedure: HospitalizationProcedureDto[]): ItemsAndDescriptionData {
		return this.mapProcedureToItemDataSummary(hospitalizationProcedure, CULTURES_SURGERY_DESCRIPTION_ITEM);
	}

	private mapProcedureToItemDataSummary(hospitalizationProcedure: HospitalizationProcedureDto[], descriptionItem: any): ItemsAndDescriptionData {
		const note = getElementAtPosition(hospitalizationProcedure, UNIQUE_DESCRIPTION_POSITION).note;
		return {
			...descriptionItem,
			data: {
				items: this.mapProceduresToDescriptionItemDataSummary(hospitalizationProcedure),
				note: note ? [this.documentsSummaryService.toDescriptionItemData(note)] : []
			}
		};
	}

	private mapAnestheticTechniqueToDescriptionItemDataSummary(anestheticTechniques: AnestheticTechniqueDto[]): DescriptionItemDataSummary {
		return {
			summary: this.mapAnestheticTechniquesToDescriptionItemData(anestheticTechniques),
			...ANESTHETIC_TECHNIQUE_DESCRIPTION_ITEM,
		}
	}

	private mapAnestheticTechniquesToDescriptionItemData(anestheticTechniques: AnestheticTechniqueDto[]): DescriptionItemData[] {
		return anestheticTechniques.map(anestheticTechnique => this.documentsSummaryService.toDescriptionItemData(anestheticTechnique.snomed.pt));
	}

	private mapSurgicalProcedures(surgicalReport: SurgicalReportDto, title: string): SurgicalProcedures {
		return {
			title: title,
			startAndEndSurgicalDateTime: {
				surgicalEndDate: dateDtoToDate(surgicalReport?.endDateTime.date),
				surgicalEndTime: timeDtoToDate(surgicalReport?.endDateTime.time),
				surgicalStartDate: dateDtoToDate(surgicalReport?.startDateTime.date),
				surgicalStartTime: timeDtoToDate(surgicalReport?.startDateTime.time),
			},
			description: surgicalReport.description ? [this.documentsSummaryService.toDescriptionItemData(surgicalReport.description)] : [],
			procedures: this.mapProceduresToDescriptionItemDataSummary(surgicalReport.procedures)
		}
	}

	private mapProceduresToDescriptionItemDataSummary(proposedSurgeries: HospitalizationProcedureDto[]): DescriptionItemData[] {
		return proposedSurgeries.map(proposedSurgery => this.documentsSummaryService.toDescriptionItemData(proposedSurgery.snomed.pt));
	}

	private mapSurgeryTeamToDescriptionItemDataSummary(professionals: DocumentHealthcareProfessionalDto[]): ItemsAndDescriptionData {
		const items = this.mapHealthcareProfessionalsToDescriptionItemData(professionals);
		return {
			...SURGERY_TEAM_DESCRIPTION_ITEM,
			data: {
				items: items,
				note: [],
				firstItem: [items.length > 0 ? items[0] : undefined],
				remainingItems: items.length > 1 ? items.slice(1) : [],
			}
		};
	}

	private mapProfessionalToDescriptionItemDataSummary(professionals: DocumentHealthcareProfessionalDto[], descriptionItem: any): ItemsAndDescriptionData {
		const comments = getElementAtPosition(professionals, UNIQUE_DESCRIPTION_POSITION).comments;
		return {
			...descriptionItem,
			data: {
				items: this.mapHealthcareProfessionalsToDescriptionItemData(professionals),
				note: comments ? [this.documentsSummaryService.toDescriptionItemData(comments)] : [],
			}
		};
	}

	private mapTransfusionistToDescriptionItemDataSummary(professionals: DocumentHealthcareProfessionalDto[]): ItemsAndDescriptionData {
		return this.mapProfessionalToDescriptionItemDataSummary(professionals, TRANSFUSIONIST_DESCRIPTION_ITEM);
	}

	private mapPathologistToDescriptionItemDataSummary(professionals: DocumentHealthcareProfessionalDto[]): ItemsAndDescriptionData {
		return this.mapProfessionalToDescriptionItemDataSummary(professionals, PATHOLOGIST_DESCRIPTION_ITEM);
	}

	private mapHealthcareProfessionalsToDescriptionItemData(professionals: DocumentHealthcareProfessionalDto[]): DescriptionItemData[] {
		return professionals.map(professional => ({
			description: `${professional.healthcareProfessional.person.fullName} ${INFO_DIVIDER} ${this.mapProfessionalToProfession(professional)}`,
		}));
	}

	private mapProfessionalToProfession(professionSelected: DocumentHealthcareProfessionalDto): string {
		if (professionSelected.profession.type === PATHOLOGIST_INFO.type) {
			return PATHOLOGIST_INFO.description;
		}

		if (professionSelected.profession.type === TRANSFUSIONIST_INFO.type) {
			return TRANSFUSIONIST_INFO.description;
		}

		return this.professions.find(profession => profession.id === professionSelected.profession.type)?.description;
	}
}

export interface SurgicalReportViewFormat {
	mainDiagnosis: CustomDiagnosesData,
	preOperativeDiagnosis: DescriptionItemData[],
	proposedSurgeries: DescriptionItemDataSummary,
	surgicalTeam: ItemsAndDescriptionData,
	anestheticTechniques: DescriptionItemDataSummary,
	surgicalProcedures: SurgicalProcedures,
	pathologist: ItemsAndDescriptionData,
	transfusionist: ItemsAndDescriptionData,
	cultures: ItemsAndDescriptionData,
	prosthesis: ItemsAndDescriptionData,
	frozenSectionBiopsies: ItemsAndDescriptionData,
	drainages: ItemsAndDescriptionData,
	postOperativeDiagnosis: CustomDiagnosesData,
}

