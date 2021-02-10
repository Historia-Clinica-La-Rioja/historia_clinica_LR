import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {
	AllergyIntoleranceDto,
	ConditionDto,
	HCEAllergyDto,
	HCEAnthropometricDataDto,
	HCELast2VitalSignsDto,
	HCEMedicationDto,
	HCEPersonalHistoryDto,
	MedicationInteroperabilityDto,
	OutpatientFamilyHistoryDto,
	PatientSummaryDto
} from '@api-rest/api-model';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {HceGeneralStateService} from '@api-rest/services/hce-general-state.service';
import {ANTECEDENTES_FAMILIARES, PROBLEMAS_ANTECEDENTES} from '../../../../constants/summaries';
import {AmbulatoriaSummaryFacadeService} from '../../services/ambulatoria-summary-facade.service';
import {TableModel} from '@presentation/components/table/table.component';
import {SnackBarService} from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-resumen',
	templateUrl: './resumen.component.html',
	styleUrls: ['./resumen.component.scss']
})
export class ResumenComponent implements OnInit, OnChanges {

	public allergies$: Observable<HCEAllergyDto[]>;
	public patientId: number;
	public familyHistories$: Observable<HCEPersonalHistoryDto[]>;
	public personalHistory$: Observable<HCEPersonalHistoryDto[]>;
	public medications$: Observable<HCEMedicationDto[]>;
	public vitalSigns$: Observable<HCELast2VitalSignsDto>;
	public anthropometricData$: Observable<HCEAnthropometricDataDto>;
	public readonly familyHistoriesHeader = ANTECEDENTES_FAMILIARES;
	public readonly personalProblemsHeader = PROBLEMAS_ANTECEDENTES;
	public loadExternal = false;
	public healthConditionsTable: TableModel<ConditionDto>;
	public allergiesTable: TableModel<AllergyIntoleranceDto>;
	public medicationsTable: TableModel<MedicationInteroperabilityDto>;
	public familyHistoriesTable: TableModel<OutpatientFamilyHistoryDto>;
	@Input() patientExternalSummary: PatientSummaryDto;

	constructor(
		private readonly hceGeneralStateService: HceGeneralStateService,
		private route: ActivatedRoute,
		private readonly ambulatoriaSummaryFacadeService: AmbulatoriaSummaryFacadeService,
		private readonly snackBarService: SnackBarService
		) {
	}

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
				this.initSummaries();
				this.loadExternalTables(true);
			});
	}

	ngOnChanges(changes: SimpleChanges) {
		if (!changes.patientExternalSummary.isFirstChange()) {
			this.loadExternalTables(false);
		}
	}

	initSummaries() {
		this.allergies$ = this.ambulatoriaSummaryFacadeService.allergies$;
		this.familyHistories$ = this.ambulatoriaSummaryFacadeService.familyHistories$;
		this.personalHistory$ = this.ambulatoriaSummaryFacadeService.personalHistories$;
		this.medications$ = this.ambulatoriaSummaryFacadeService.medications$;
		this.vitalSigns$ = this.ambulatoriaSummaryFacadeService.vitalSigns$;
		this.anthropometricData$ = this.ambulatoriaSummaryFacadeService.anthropometricData$;
	}

	loadExternalTables(fromInit: boolean) {
		if (this.externalSummaryIsLoaded()) {
			this.loadExternal = true;
			this.healthConditionsTable = this.buildHealthConditionTable(this.patientExternalSummary.conditions);
			this.allergiesTable = this.buildAllergiesTable(this.patientExternalSummary.allergies);
			this.medicationsTable = this.buildMedicationsTable(this.patientExternalSummary.medications);
		} else {
			this.loadExternal = false;
			if (!fromInit) {
				this.snackBarService.showError('ambulatoria.bus-interoperabilidad.PACIENTE-SIN-DATOS');
			}
		}
	}

	externalSummaryIsLoaded(): boolean {
		return !!this.patientExternalSummary && !!this.patientExternalSummary.organization;
	}

	buildHealthConditionTable(data: ConditionDto[]): TableModel<ConditionDto> {
		return {
			columns: [
				{
					columnDef: 'problema',
					text: (row) => row.sctidTerm
				}],
			data
		};
	}

	buildAllergiesTable(data: AllergyIntoleranceDto[]): TableModel<AllergyIntoleranceDto> {
		return {
			columns: [
				{
					columnDef: 'alergia',
					text: (row) => row.sctidTerm
				}],
			data
		};
	}

	buildMedicationsTable(data: MedicationInteroperabilityDto[]): TableModel<MedicationInteroperabilityDto> {
		return {
			columns: [
				{
					header: 'Medicación',
					columnDef: 'Medicacion',
					text: (row) => row.sctidTerm
				},
				{
					header: 'Estado',
					columnDef: 'Estado',
					text: (row) => row.status || 'No informado'

				}
				],
			data
		};
	}

}
