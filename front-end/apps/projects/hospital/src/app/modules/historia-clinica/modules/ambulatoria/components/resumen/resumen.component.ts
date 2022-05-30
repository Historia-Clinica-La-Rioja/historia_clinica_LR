import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import {
	AllergyIntoleranceDto,
	ConditionDto,
	HCEAllergyDto,
	HCEAnthropometricDataDto,
	HCELast2RiskFactorsDto,
	HCEMedicationDto,
	HCEPersonalHistoryDto,
	MedicationInteroperabilityDto,
	PatientSummaryDto
} from '@api-rest/api-model';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ANTECEDENTES_FAMILIARES, MEDICACION_HABITUAL, PROBLEMAS_ANTECEDENTES } from '../../../../constants/summaries';
import { AmbulatoriaSummaryFacadeService } from '../../services/ambulatoria-summary-facade.service';
import { TableModel } from '@presentation/components/table/table.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-resumen',
	templateUrl: './resumen.component.html',
	styleUrls: ['./resumen.component.scss']
})
export class ResumenComponent implements OnInit, OnChanges {

	readonly familyHistoriesHeader = ANTECEDENTES_FAMILIARES;
	readonly personalProblemsHeader = PROBLEMAS_ANTECEDENTES;
	readonly medicationsHeader = MEDICACION_HABITUAL;
	allergies$: Observable<HCEAllergyDto[]>;
	patientId: number;
	familyHistories$: Observable<HCEPersonalHistoryDto[]>;
	personalHistory$: Observable<HCEPersonalHistoryDto[]>;
	medications$: Observable<HCEMedicationDto[]>;
	riskFactors$: Observable<HCELast2RiskFactorsDto>;
	anthropometricDataList$: Observable<HCEAnthropometricDataDto[]>;
	loadExternal = false;
	healthConditionsTable: TableModel<ConditionDto>;
	allergiesTable: TableModel<AllergyIntoleranceDto>;
	medicationsTable: TableModel<MedicationInteroperabilityDto>;
	@Input() patientExternalSummary: PatientSummaryDto;

	constructor(
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

	ngOnChanges(changes: SimpleChanges): void {
		if (!changes.patientExternalSummary.isFirstChange()) {
			this.loadExternalTables(false);
		}
	}

	initSummaries(): void {
		this.allergies$ = this.ambulatoriaSummaryFacadeService.allergies$;
		this.familyHistories$ = this.ambulatoriaSummaryFacadeService.familyHistories$;
		this.personalHistory$ = this.ambulatoriaSummaryFacadeService.personalHistories$;
		this.medications$ = this.ambulatoriaSummaryFacadeService.medications$;
		this.riskFactors$ = this.ambulatoriaSummaryFacadeService.riskFactors$;
		this.anthropometricDataList$ = this.ambulatoriaSummaryFacadeService.anthropometricDataList$;
	}

	loadExternalTables(fromInit: boolean): void {
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
