import { Injectable } from '@angular/core';
import { AnestheticTechniqueDto, DiagnosisDto, DocumentHealthcareProfessionalDto, EProfessionType, GenericMasterDataDto, HospitalizationProcedureDto, SurgicalReportDto } from '@api-rest/api-model';
import { DocumentsSummaryMapperService } from './documents-summary-mapper.service';
import { DescriptionItemData } from '@presentation/components/description-item/description-item.component';
import { DescriptionItemDataSummary } from '@historia-clinica/components/description-item-data-summary/description-item-data-summary.component';
import { ANESTHETIC_TECHNIQUE_DESCRIPTION_ITEM, CULTURES_SURGERY_DESCRIPTION_ITEM, DRAINAGE_SURGERY_DESCRIPTION_ITEM, FROZEN_BIOPSY_SURGERY_DESCRIPTION_ITEM, PATHOLOGIST_DESCRIPTION_ITEM, PROPOSED_SURGERIES_DESCRIPTION_ITEM, PROTESIS_SURGERY_DESCRIPTION_ITEM, SURGERY_TEAM_DESCRIPTION_ITEM, TRANSFUSIONIST_DESCRIPTION_ITEM } from '@historia-clinica/constants/document-summary.constants';
import { CustomDiagnosesData, ItemsAndDescriptionData, SurgicalProcedures } from '@historia-clinica/utils/document-summary.model';
import { dateDtoToDate, timeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { RequestMasterDataService } from '@api-rest/services/request-masterdata.service';

const INFO_DIVIDER = ' | ';
const PATHOLOGIST_INFO = {
	type: EProfessionType.PATHOLOGIST,
	description: 'Patólogo'
};
const TRANSFUSIONIST_INFO = {
	type: EProfessionType.TRANSFUSIONIST,
	description: 'Transfusionista'
};

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
			...(surgicalReport.mainDiagnosis && { mainDiagnosis: this.mapCustomDiagnosesData([surgicalReport.mainDiagnosis], 'Diagnósticos Pre-Operatorios') }),
			...(surgicalReport.preoperativeDiagnosis.length && { preOperativeDiagnosis: this.documentsSummaryService.mapDiagnosisToDescriptionItemData(surgicalReport.preoperativeDiagnosis) }),
			...(surgicalReport.surgeryProcedures.length && { proposedSurgeries: this.mapProposedSurgeriesToDescriptionItemDataSummary(surgicalReport.surgeryProcedures) }),
			...(surgicalReport.surgicalTeam.length && { surgicalTeam: this.mapSurgeryTeamToDescriptionItemDataSummary(surgicalReport.surgicalTeam) }),
			...(surgicalReport.anesthesia.length && { anestheticTechniques: this.mapAnestheticTechniqueToDescriptionItemDataSummary(surgicalReport.anesthesia) }),
			surgicalProcedures: this.mapSurgicalProcedures(surgicalReport, 'historia-clinica.surgical-report.summary.PROCEDURES'),
			...(surgicalReport.pathologist && { pathologist: this.mapPathologistToDescriptionItemDataSummary([surgicalReport.pathologist]) }),
			...(surgicalReport.transfusionist && { transfusionist: this.mapTransfusionistToDescriptionItemDataSummary([surgicalReport.transfusionist]) }),
			...(surgicalReport.cultures.length && { cultures: this.mapCultureToItemDataSummary(surgicalReport.cultures) }),
			...(surgicalReport.prosthesisDescription && { prosthesis: this.mapProsthesisDescriptionToItemDataSummary(surgicalReport.prosthesisDescription) }),
			...(surgicalReport.frozenSectionBiopsies.length && { frozenSectionBiopsies: this.mapFrozenBiopsyToItemDataSummary(surgicalReport.frozenSectionBiopsies) }),
			...(surgicalReport.drainages.length && { drainages: this.mapDrainageToItemDataSummary(surgicalReport.drainages) }),
			...(surgicalReport.postoperativeDiagnosis.length && { postOperativeDiagnosis: this.mapCustomDiagnosesData(surgicalReport.postoperativeDiagnosis, 'Diagnósticos Post-Operatorios') }),
		}
	}

	private mapCustomDiagnosesData(diagnoses: DiagnosisDto[], title: string): CustomDiagnosesData {
		return {
			title: title,
			diagnoses: this.documentsSummaryService.mapDiagnosisToDescriptionItemData(diagnoses)
		}
	}

	private mapProsthesisDescriptionToItemDataSummary(prosthesisDescription: string): ItemsAndDescriptionData {
		return {
			...PROTESIS_SURGERY_DESCRIPTION_ITEM,
			items: prosthesisDescription ? [{ description: 'Si' }] : [{ description: '' }],
			note: prosthesisDescription ? [this.documentsSummaryService.toDescriptionItemData(prosthesisDescription)] : [],
		};
	}

	private mapDrainageToItemDataSummary(hospitalizationProcedure: HospitalizationProcedureDto[]): ItemsAndDescriptionData {
		return {
			...DRAINAGE_SURGERY_DESCRIPTION_ITEM,
			items: this.mapProceduresToDescriptionItemDataSummary(hospitalizationProcedure),
			note: [this.documentsSummaryService.toDescriptionItemData(hospitalizationProcedure[0].note)],
		};
	}

	private mapFrozenBiopsyToItemDataSummary(hospitalizationProcedure: HospitalizationProcedureDto[]): ItemsAndDescriptionData {
		return {
			...FROZEN_BIOPSY_SURGERY_DESCRIPTION_ITEM,
			items: this.mapProceduresToDescriptionItemDataSummary(hospitalizationProcedure),
			note: [this.documentsSummaryService.toDescriptionItemData(hospitalizationProcedure[0].note)],
		};
	}

	private mapCultureToItemDataSummary(hospitalizationProcedure: HospitalizationProcedureDto[]): ItemsAndDescriptionData {
		return {
			...CULTURES_SURGERY_DESCRIPTION_ITEM,
			items: this.mapProceduresToDescriptionItemDataSummary(hospitalizationProcedure),
			note: [this.documentsSummaryService.toDescriptionItemData(hospitalizationProcedure[0].note)],
		};
	}

	private mapProposedSurgeriesToDescriptionItemDataSummary(proposedSurgeries: HospitalizationProcedureDto[]): DescriptionItemDataSummary {
		return {
			summary: this.mapProposedSurgeriesToDescriptionItemData(proposedSurgeries),
			...PROPOSED_SURGERIES_DESCRIPTION_ITEM,
		}
	}

	private mapProposedSurgeriesToDescriptionItemData(proposedSurgeries: HospitalizationProcedureDto[]): DescriptionItemData[] {
		return proposedSurgeries.map(proposedSurgery => this.documentsSummaryService.toDescriptionItemData(proposedSurgery.snomed.pt))
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
		return {
			...SURGERY_TEAM_DESCRIPTION_ITEM,
			items: this.mapHealthcareProfessionalsToDescriptionItemData(professionals),
		};
	}

	private mapTransfusionistToDescriptionItemDataSummary(professionals: DocumentHealthcareProfessionalDto[]): ItemsAndDescriptionData {
		return {
			...TRANSFUSIONIST_DESCRIPTION_ITEM,
			items: this.mapHealthcareProfessionalsToDescriptionItemData(professionals),
			note: [this.documentsSummaryService.toDescriptionItemData(professionals[0].comments)],
		};
	}

	private mapPathologistToDescriptionItemDataSummary(professionals: DocumentHealthcareProfessionalDto[]): ItemsAndDescriptionData {
		return {
			...PATHOLOGIST_DESCRIPTION_ITEM,
			items: this.mapHealthcareProfessionalsToDescriptionItemData(professionals),
			note: [this.documentsSummaryService.toDescriptionItemData(professionals[0].comments)],
		};
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

