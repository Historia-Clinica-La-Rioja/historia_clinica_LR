import { Injectable } from '@angular/core';
import { BehaviorSubject, forkJoin, Subject } from "rxjs";
import { InternmentStateService } from "@api-rest/services/internment-state.service";
import { AllergyConditionDto, AnthropometricDataDto, DocumentSearchFilterDto, HCEAllergyDto, HCEPersonalHistoryDto, HealthHistoryConditionDto, InternmentSummaryDto, PatientDischargeDto } from "@api-rest/api-model";
import { DocumentSearchService } from "@api-rest/services/document-search.service";
import { InternacionService } from "@api-rest/services/internacion.service";
import { momentParseDateTime } from "@core/utils/moment.utils";
import { InternmentEpisodeService } from "@api-rest/services/internment-episode.service";
import { HceGeneralStateService } from "@api-rest/services/hce-general-state.service";

@Injectable()
export class InternmentSummaryFacadeService {

	private internmentEpisodeId: number;

	private allergiesSubject: Subject<any> = new BehaviorSubject<any>([]);
	private familyHistoriesSubject: Subject<any> = new BehaviorSubject<any>([]);
	private personalHistorySubject: Subject<any> = new BehaviorSubject<any>([]);
	private medicationsSubject: Subject<any> = new BehaviorSubject<any>([]);
	private riskFactorsSubject: Subject<any> = new BehaviorSubject<any>([]);
	private heightAndWeightDataListSubject: Subject<AnthropometricDataDto[]> = new BehaviorSubject<AnthropometricDataDto[]>([]);
	private bloodTypeDataSubject: Subject<string> = new BehaviorSubject<string>(null);
	private immunizationsSubject: Subject<any> = new BehaviorSubject<any>([]);
	private mainDiagnosisSubject: Subject<any> = new BehaviorSubject<any>([]);
	private diagnosisSubject: Subject<any> = new BehaviorSubject<any>([]);
	private clinicalEvaluationSubject: Subject<any> = new BehaviorSubject<any>([]);

	private anamnesisSubject: Subject<any> = new BehaviorSubject<any>([]);
	private epicrisisSubject: Subject<any> = new BehaviorSubject<any>([]);
	private evolutionNoteSubject: Subject<any> = new BehaviorSubject<any>([]);
	private hasMedicalDischargeSubject: Subject<any> = new BehaviorSubject<any>([]);
	private lastProbableDischargeDateSubject: Subject<any> = new BehaviorSubject<any>([]);
	private hasPhysicalDischargeSubject = new BehaviorSubject<any>("");

	searchFilter: DocumentSearchFilterDto;
	readonly allergies$ = this.allergiesSubject.asObservable();
	readonly familyHistories$ = this.familyHistoriesSubject.asObservable();
	readonly personalHistory$ = this.personalHistorySubject.asObservable();
	readonly medications$ = this.medicationsSubject.asObservable();
	readonly riskFactors$ = this.riskFactorsSubject.asObservable();
	readonly heightAndWeightDataList$ = this.heightAndWeightDataListSubject.asObservable();

	readonly bloodTypeData$ = this.bloodTypeDataSubject.asObservable();
	readonly immunizations$ = this.immunizationsSubject.asObservable();
	readonly mainDiagnosis$ = this.mainDiagnosisSubject.asObservable();
	readonly diagnosis$ = this.diagnosisSubject.asObservable();
	readonly clinicalEvaluation$ = this.clinicalEvaluationSubject.asObservable();
	readonly anamnesis$ = this.anamnesisSubject.asObservable();
	readonly epicrisis$ = this.epicrisisSubject.asObservable();
	readonly evolutionNote$ = this.evolutionNoteSubject.asObservable();
	readonly hasMedicalDischarge$ = this.hasMedicalDischargeSubject.asObservable();
	readonly lastProbableDischargeDate$ = this.lastProbableDischargeDateSubject.asObservable();
	readonly hasPhysicalDischarge$ = this.hasPhysicalDischargeSubject.asObservable();

	constructor(
		private readonly internmentStateService: InternmentStateService,
		private readonly documentSearchService: DocumentSearchService,
		private readonly internmentService: InternacionService,
		private readonly internmentEpisodeService: InternmentEpisodeService,
		private readonly hceGeneralStateService: HceGeneralStateService
	) { }

	initDischargeObservable() {
		this.hasPhysicalDischargeSubject.next("");
	}

	setInternmentEpisodeId(internmentEpisodeId: number): void {
		this.internmentEpisodeId = internmentEpisodeId;
		this.initDischargeObservable();
		this.updateInternmentEpisode();
	}

	setInternmentEpisodeInformation(internmentEpisodeId: number, bloodType: boolean, updateData: boolean): void {
		if (!bloodType) {
			this.bloodTypeDataSubject.next();
		}
		this.setInternmentEpisodeId(internmentEpisodeId);
		if (updateData) {
			this.setFieldsToUpdate({
				allergies: true,
				familyHistories: true,
				personalHistories: true,
				riskFactors: true,
				medications: true,
				heightAndWeight: true,
				bloodType: bloodType,
				immunizations: true,
				evolutionClinical: true,
			});
		}
	}

	setFieldsToUpdate(fieldsToUpdate: InternmentFields): void {
		if (fieldsToUpdate.allergies) {
			this.internmentStateService.getAllergies(this.internmentEpisodeId).subscribe(a => this.allergiesSubject.next(a));
		}
		if (fieldsToUpdate.familyHistories) {
			this.internmentStateService.getFamilyHistories(this.internmentEpisodeId).subscribe(f => this.familyHistoriesSubject.next(f));
		}

		if (fieldsToUpdate.personalHistories) {
			this.internmentStateService.getPersonalHistories(this.internmentEpisodeId).subscribe(p => this.personalHistorySubject.next(p));
		}

		if (fieldsToUpdate.medications) {
			this.internmentStateService.getMedications(this.internmentEpisodeId).subscribe(m => this.medicationsSubject.next(m));
		}

		if (fieldsToUpdate.riskFactors) {
			this.internmentStateService.getRiskFactors(this.internmentEpisodeId).subscribe(v => this.riskFactorsSubject.next(v));

		}

		if (fieldsToUpdate.heightAndWeight || fieldsToUpdate.bloodType) {
			this.internmentStateService.getLast2AnthropometricData(this.internmentEpisodeId).subscribe(aD => {
				if (fieldsToUpdate.heightAndWeight) {
					this.heightAndWeightDataListSubject.next(aD);
				}

				if (fieldsToUpdate.bloodType) {
					this.bloodTypeDataSubject.next(aD[0]?.bloodType?.value);
				}
			});
		}

		if (fieldsToUpdate.immunizations) {
			this.internmentStateService.getImmunizations(this.internmentEpisodeId).subscribe(i => this.immunizationsSubject.next(i));
		}

		if (fieldsToUpdate.mainDiagnosis) {
			this.internmentStateService.getMainDiagnosis(this.internmentEpisodeId).subscribe(m => this.mainDiagnosisSubject.next(m));
		}

		if (fieldsToUpdate.diagnosis) {
			this.internmentStateService.getAlternativeDiagnosesGeneralState(this.internmentEpisodeId).subscribe(d => this.diagnosisSubject.next(d));
		}

		if (fieldsToUpdate.evolutionClinical) {
			this.loadHistoric();
		}
	}

	setSerchFilter(searchFilter: DocumentSearchFilterDto): void {
		this.searchFilter = searchFilter;
		this.loadHistoric();
	}

	loadEvolutionNotes(): void {
		this.loadHistoric();
	}

	initializeEvolutionNoteFilterResult(internmentEpisodeId: number): void {
		this.internmentEpisodeId = internmentEpisodeId;
		this.setSerchFilter(null);
	}

	updateInternmentEpisode(): void {
		this.internmentService.getInternmentEpisodeSummary(this.internmentEpisodeId).subscribe(
			(internmentEpisode: InternmentSummaryDto) => {
				this.anamnesisSubject.next(internmentEpisode.documents?.anamnesis);
				this.epicrisisSubject.next(internmentEpisode.documents?.epicrisis);
				this.evolutionNoteSubject.next(internmentEpisode.documents?.lastEvaluationNote);
				this.lastProbableDischargeDateSubject.next(internmentEpisode.probableDischargeDate ? momentParseDateTime(internmentEpisode.probableDischargeDate) : undefined);
			});
		this.internmentEpisodeService.getPatientDischarge(this.internmentEpisodeId)
			.subscribe((patientDischarge: PatientDischargeDto) => {
				this.hasMedicalDischargeSubject.next(patientDischarge.medicalDischargeDate);
				this.hasPhysicalDischargeSubject.next(patientDischarge.physicalDischargeDate)
			});
	}

	unifyAllergies(patientId: number): void {
		forkJoin([this.internmentStateService.getAllergies(this.internmentEpisodeId), this.hceGeneralStateService.getAllergies(patientId)])
			.subscribe(([allergiesI, allergiesHCE]) => {
				allergiesHCE.forEach(e => allergiesI.push(this.mapToAllergyConditionDto(e)));
				this.allergiesSubject.next(allergiesI)
			});
	}

	unifyFamilyHistories(patientId: number): void {
		forkJoin([this.internmentStateService.getFamilyHistories(this.internmentEpisodeId), this.hceGeneralStateService.getFamilyHistories(patientId)])
			.subscribe(([familyHistoriesI, familyHistoriesHCE]) => {
				familyHistoriesHCE.forEach(e => familyHistoriesI.push(this.mapToHealthHistoryConditionDto(e)));
				this.familyHistoriesSubject.next(familyHistoriesI)
			});
	}

	private mapToHealthHistoryConditionDto(familyHistory: HCEPersonalHistoryDto): HealthHistoryConditionDto {
		return {
			startDate: familyHistory.startDate,
			note: null,
			id: familyHistory.id,
			snomed: familyHistory.snomed,
			statusId: familyHistory.statusId
		}
	}

	private mapToAllergyConditionDto(allergie: HCEAllergyDto): AllergyConditionDto {
		return {
			categoryId: allergie.categoryId,
			criticalityId: allergie.criticalityId,
			date: null,
			id: allergie.id,
			snomed: allergie.snomed,
			statusId: allergie.statusId
		}
	}

	private loadHistoric(): void {
		this.documentSearchService.getHistoric(this.internmentEpisodeId, this.searchFilter).subscribe(historicalData => this.clinicalEvaluationSubject.next(historicalData));
	}
}

export interface InternmentFields {
	allergies?: boolean;
	familyHistories?: boolean;
	personalHistories?: boolean;
	riskFactors?: boolean;
	medications?: boolean;
	heightAndWeight?: boolean;
	bloodType?: boolean;
	immunizations?: boolean;
	mainDiagnosis?: boolean;
	diagnosis?: boolean;
	evolutionClinical?: boolean;

}
