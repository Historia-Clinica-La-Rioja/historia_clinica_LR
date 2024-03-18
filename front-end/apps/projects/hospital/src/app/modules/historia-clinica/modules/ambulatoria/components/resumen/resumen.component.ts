import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {
	AllergyIntoleranceDto,
	ConditionDto,
	HCEAllergyDto,
	HCEAnthropometricDataDto,
	HCELast2RiskFactorsDto,
	HCEMedicationDto,
	HCEHealthConditionDto,
	MedicationInteroperabilityDto,
	PatientSummaryDto,
	HCEPersonalHistoryDto
} from '@api-rest/api-model';
import { dateISOParseDate, } from '@core/utils/moment.utils';
import { TableModel } from '@presentation/components/table/table.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ANTECEDENTES_FAMILIARES, ANTECEDENTES_PERSONALES, MEDICACION_HABITUAL, PROBLEMAS_ANTECEDENTES } from '../../../../constants/summaries';
import { AmbulatoriaSummaryFacadeService } from '../../services/ambulatoria-summary-facade.service';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';

@Component({
	selector: 'app-resumen',
	templateUrl: './resumen.component.html',
	styleUrls: ['./resumen.component.scss']
})
export class ResumenComponent implements OnInit, OnChanges {

	readonly familyHistoriesHeader = ANTECEDENTES_FAMILIARES;
	readonly personalHistoriesHeader = ANTECEDENTES_PERSONALES;
	readonly personalProblemsHeader = PROBLEMAS_ANTECEDENTES;
	readonly medicationsHeader = MEDICACION_HABITUAL;
	allergies$: Observable<HCEAllergyDto[]>;
	patientId: number;
	personalHistories$: Observable<HCEPersonalHistoryDto[]>;
	familyHistories$: Observable<HCEHealthConditionDto[]>;
	patientProblems$: Observable<HCEHealthConditionDto[]>;
	medications$: Observable<HCEMedicationDto[]>;
	riskFactors$: Observable<HCELast2RiskFactorsDto>;
	anthropometricDataList$: Observable<HCEAnthropometricDataDto[]>;
	loadExternal = false;
	healthConditionsTable: TableModel<ConditionDto>;
	allergiesTable: TableModel<AllergyIntoleranceDto>;
	medicationsTable: TableModel<MedicationInteroperabilityDto>;
	@Input() patientExternalSummary: PatientSummaryDto;
	@Input() canOnlyViewSelfAddedProblems: boolean;

	constructor(
		private route: ActivatedRoute,
		private readonly ambulatoriaSummaryFacadeService: AmbulatoriaSummaryFacadeService,
		private readonly snackBarService: SnackBarService,
		private readonly dateFormatPipe: DateFormatPipe
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
		if (this.canOnlyViewSelfAddedProblems) {
			this.patientProblems$ = this.ambulatoriaSummaryFacadeService.patientProblemsByRole$.pipe(
				map(p => this.formatProblemsDates(p,this.dateFormatPipe))
			);
		} else {
			this.allergies$ = this.ambulatoriaSummaryFacadeService.allergies$;
			this.personalHistories$ = this.ambulatoriaSummaryFacadeService.personalHistories$;
			this.familyHistories$ = this.ambulatoriaSummaryFacadeService.familyHistories$;
			this.patientProblems$ = this.ambulatoriaSummaryFacadeService.patientProblems$.pipe(
				map(p => this.formatProblemsDates(p,this.dateFormatPipe))
			);
			this.medications$ = this.ambulatoriaSummaryFacadeService.medications$;
			this.riskFactors$ = this.ambulatoriaSummaryFacadeService.riskFactors$;
			this.anthropometricDataList$ = this.ambulatoriaSummaryFacadeService.anthropometricDataList$;
		}
	}

	private formatProblemsDates(problemas: HCEHealthConditionDto[], dateFormatPipe: DateFormatPipe) {
		return problemas.map((problema: HCEHealthConditionDto) => {
			return {
				...problema,
				startDate: problema.startDate ? dateFormatPipe.transform(dateISOParseDate(problema.startDate), 'date') : undefined,
				inactivationDate: problema.inactivationDate ? dateFormatPipe.transform(dateISOParseDate(problema.inactivationDate), 'date') : undefined
			};
		});
	}

	loadExternalTables(fromInit: boolean): void {
		if (!this.canOnlyViewSelfAddedProblems) {
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
