import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from "rxjs";
import { InternmentStateService } from "@api-rest/services/internment-state.service";
import { DocumentSearchFilterDto, InternmentSummaryDto, PatientDischargeDto } from "@api-rest/api-model";
import { DocumentSearchService } from "@api-rest/services/document-search.service";
import { InternacionService } from "@api-rest/services/internacion.service";
import { momentParseDateTime } from "@core/utils/moment.utils";
import { InternmentEpisodeService } from "@api-rest/services/internment-episode.service";

@Injectable({
	providedIn: 'root'
})
export class InternmentSummaryFacadeService {

	private internmentEpisodeId: number;

	private allergiesSubject: Subject<any> = new BehaviorSubject<any>([]);
	private familyHistoriesSubject: Subject<any> = new BehaviorSubject<any>([]);
	private personalHistorySubject: Subject<any> = new BehaviorSubject<any>([]);
	private medicationsSubject: Subject<any> = new BehaviorSubject<any>([]);
	private riskFactorsSubject: Subject<any> = new BehaviorSubject<any>([]);
	private anthropometricDataSubject: Subject<any> = new BehaviorSubject<any>([]);
	private immunizationsSubject: Subject<any> = new BehaviorSubject<any>([]);
	private mainDiagnosisSubject: Subject<any> = new BehaviorSubject<any>([]);
	private diagnosisSubject: Subject<any> = new BehaviorSubject<any>([]);
	private clinicalEvaluationSubject: Subject<any> = new BehaviorSubject<any>([]);

	private anamnesisSubject: Subject<any> = new BehaviorSubject<any>([]);
	private epicrisisSubject: Subject<any> = new BehaviorSubject<any>([]);
	private evolutionNoteSubject: Subject<any> = new BehaviorSubject<any>([]);
	private hasMedicalDischargeSubject: Subject<any> = new BehaviorSubject<any>([]);
	private lastProbableDischargeDateSubject: Subject<any> = new BehaviorSubject<any>([]);

	searchFilter: DocumentSearchFilterDto;
	readonly allergies$ = this.allergiesSubject.asObservable();
	readonly familyHistories$ = this.familyHistoriesSubject.asObservable();
	readonly personalHistory$ = this.personalHistorySubject.asObservable();
	readonly medications$ = this.medicationsSubject.asObservable();
	readonly riskFactors$ = this.riskFactorsSubject.asObservable();
	readonly anthropometricData$ = this.anthropometricDataSubject.asObservable();
	readonly immunizations$ = this.immunizationsSubject.asObservable();
	readonly mainDiagnosis$ = this.mainDiagnosisSubject.asObservable();
	readonly diagnosis$ = this.diagnosisSubject.asObservable();
	readonly clinicalEvaluation$ = this.clinicalEvaluationSubject.asObservable();
	readonly anamnesis$ = this.anamnesisSubject.asObservable();
	readonly epicrisis$ = this.epicrisisSubject.asObservable();
	readonly evolutionNote$ = this.evolutionNoteSubject.asObservable();
	readonly hasMedicalDischarge$ = this.hasMedicalDischargeSubject.asObservable();
	readonly lastProbableDischargeDate$ = this.lastProbableDischargeDateSubject.asObservable();

	constructor(
		private readonly internmentStateService: InternmentStateService,
		private readonly documentSearchService: DocumentSearchService,
		private readonly internmentService: InternacionService,
		private readonly internmentEpisodeService: InternmentEpisodeService,
	) { }

	setInternmentEpisodeInformation(internmentEpisodeId: number) {
		this.internmentEpisodeId = internmentEpisodeId;
		this.updateInternmentEpisode();
		this.setFieldsToUpdate({
			allergies: true,
			familyHistories: true,
			personalHistories: true,
			riskFactors: true,
			medications: true,
			anthropometricData: true,
			immunizations: true,
			evolutionClinical: true,
		});
	}

	setFieldsToUpdate(fieldsToUpdate: InternmentFields) {
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

		if (fieldsToUpdate.anthropometricData) {
			this.internmentStateService.getAnthropometricData(this.internmentEpisodeId).subscribe(a => this.anthropometricDataSubject.next(a));
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

	setSerchFilter(searchFilter: DocumentSearchFilterDto) {
		this.searchFilter = searchFilter;
		this.loadHistoric();
	}

	loadEvolutionNotes() {
		this.loadHistoric();
	}

	initializeEvolutionNoteFilterResult(internmentEpisodeId: number) {
		this.internmentEpisodeId = internmentEpisodeId;
		this.setSerchFilter(null);
	}

	private loadHistoric(): void {
		this.documentSearchService.getHistoric(this.internmentEpisodeId, this.searchFilter).subscribe(historicalData => this.clinicalEvaluationSubject.next(historicalData));
	}

	updateInternmentEpisode() {
		this.internmentService.getInternmentEpisodeSummary(this.internmentEpisodeId).subscribe(
			(internmentEpisode: InternmentSummaryDto) => {
				this.anamnesisSubject.next(internmentEpisode.documents?.anamnesis);
				this.epicrisisSubject.next(internmentEpisode.documents?.epicrisis);
				this.evolutionNoteSubject.next(internmentEpisode.documents?.lastEvaluationNote);
				this.lastProbableDischargeDateSubject.next(internmentEpisode.probableDischargeDate ? momentParseDateTime(internmentEpisode.probableDischargeDate) : undefined);
			});
		this.internmentEpisodeService.getPatientDischarge(this.internmentEpisodeId)
			.subscribe((patientDischarge: PatientDischargeDto) => {
				this.hasMedicalDischargeSubject.next(patientDischarge.dischargeTypeId !== 0);
			});
	}
}

export interface InternmentFields {
	allergies?: boolean;
	familyHistories?: boolean;
	personalHistories?: boolean;
	riskFactors?: boolean;
	medications?: boolean;
	anthropometricData?: boolean;
	immunizations?: boolean;
	mainDiagnosis?: boolean;
	diagnosis?: boolean;
	evolutionClinical?: boolean;
}
