import { SelectionModel } from "@angular/cdk/collections";
import { FormControl } from "@angular/forms";
import { DiagnosisDto, HealthConditionDto } from "@api-rest/api-model";
import { InternacionMasterDataService } from "@api-rest/services/internacion-master-data.service";
import { InternmentStateService } from "@api-rest/services/internment-state.service";
import { TableService } from "@core/services/table.service";
import { pushTo } from "@core/utils/array.utils";
import { TableCheckbox, BasicTable } from "@material/model/table.model";


export class DiagnosisEpicrisisService {

	private table: TableCheckbox<DiagnosisDto> = {
		data: [],
		columns: [
			{
				def: 'diagnosis',
				header: 'internaciones.epicrisis.diagnosticos.table.columns.DIAGNOSIS',
				display: ap => ap.snomed.pt
			},
			{
				def: 'status',
				header: 'internaciones.epicrisis.diagnosticos.table.columns.STATUS',
				display: (row) => row.snomed.sctid === this.newMainDiagnosis.snomed.sctid ?
					this.getClinicalStatusActivo()
					: this.healthClinicalStatus?.find(status => status.id === row.statusId).description
			},
			{
				def: 'verificacion',
				header: 'internaciones.epicrisis.diagnosticos.table.columns.VERIFICATION',
				display: (row) => row.snomed.sctid === this.newMainDiagnosis.snomed.sctid ?
					this.getVerificationStatusConfirmado()
					: this.verifications?.find(verification => verification.id === row.verificationId)?.description
			},
		],
		displayedColumns: [],
		selection: new SelectionModel<DiagnosisDto>(true, [])
	};

	private newMainDiagnosis: DiagnosisDto;
	private internmentMainDiagnosis: DiagnosisDto;
	private activeAlternativeDiagnostics: HealthConditionDto[];
	private healthClinicalStatus;
	private verifications;

	constructor(
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly internmentStateService: InternmentStateService,
		private readonly tableService: TableService,
		private readonly internmentEpisodeId,
	) {

		const healthClinicalMasterData$ = this.internacionMasterDataService.getHealthClinical();
		healthClinicalMasterData$.subscribe(healthClinical => {
			this.healthClinicalStatus = healthClinical;
		});

		const healthVerificationMasterData$ = this.internacionMasterDataService.getHealthVerification();
		healthVerificationMasterData$.subscribe(healthVerification => {
			this.verifications = healthVerification;
		});

		const alternativeDiagnostics$ = this.internmentStateService.getActiveAlternativeDiagnosesGeneralState(this.internmentEpisodeId);
		alternativeDiagnostics$.subscribe((alternativeDiagnostics: HealthConditionDto[]) => {
			this.activeAlternativeDiagnostics = alternativeDiagnostics;
		});

		this.table.displayedColumns = (['mainDiagnosis']).concat(this.table.columns?.map(c => c.def)).concat(['select']);
	}

	public setInternmentMainDiagnosis(internmentMainDiagnosis: DiagnosisDto) {
		this.internmentMainDiagnosis = internmentMainDiagnosis;
		this.newMainDiagnosis = internmentMainDiagnosis;
	}

	public initTable(diagnosis: DiagnosisDto[]): void {
		this.table.data = [this.internmentMainDiagnosis].concat(diagnosis);
		this.table.selection.select(this.internmentMainDiagnosis);
	}

	public getTable(): BasicTable<DiagnosisDto> {
		return {
			data: this.table.data,
			columns: this.table.columns,
			displayedColumns: this.table.displayedColumns
		};
	}

	// selection
	public selectionHasValue(): boolean {
		return this.table.selection.hasValue();
	}

	public toggle(diagnosis: DiagnosisDto): void {
		this.table.selection.toggle(diagnosis);
	}

	public isSelected(diagnosis: DiagnosisDto): boolean {
		return this.table.selection.isSelected(diagnosis);
	}

	public masterToggle(): void {
		this.tableService.masterToggle(this.table.data, this.table.selection);
	}

	public diagnosisMasterToggle(): void {
		this.masterToggle();
		this.table.selection.select(this.newMainDiagnosis);
	}

	public isAllSelected(): boolean {
		return this.tableService.isAllSelected(this.table.data, this.table.selection);
	}

	// diagnosis
	public addMainDiagnosis(newDiagnosis: DiagnosisDto, mainDiagnosisFormControl): void {
		const duplicatedDiagnosis = this.table.data.find(diagnosis => diagnosis.snomed.sctid === newDiagnosis.snomed.sctid);
		if (duplicatedDiagnosis) {
			this.changeMainDiagnosis(duplicatedDiagnosis, mainDiagnosisFormControl);
		} else {
			this.table.data = pushTo<DiagnosisDto>(this.table.data, newDiagnosis);
			this.changeMainDiagnosis(newDiagnosis, mainDiagnosisFormControl);
		}
	}

	public changeMainDiagnosis(newMainDiagnosis: DiagnosisDto, mainDiagnosisRadioButton?: FormControl): void {
		if (mainDiagnosisRadioButton) {
			mainDiagnosisRadioButton.setValue(newMainDiagnosis);
		}
		this.table.selection.deselect(this.newMainDiagnosis);
		this.table.selection.select(newMainDiagnosis);
		this.newMainDiagnosis = newMainDiagnosis;
	}

	public getSelectedAlternativeDiagnostics(): DiagnosisDto[] {
		return this.table.selection.selected
			.filter(diagnosis => diagnosis.id !== this.newMainDiagnosis.id);
	}

	public isSelectableAsMain(diagnosis: DiagnosisDto): boolean {
		return (this.isActiveAlternative(diagnosis) || (diagnosis === this.internmentMainDiagnosis)) || (this.isNew(diagnosis));
	}

	private isActiveAlternative(diagnosis: DiagnosisDto): boolean {
		return !!this.activeAlternativeDiagnostics?.find(alternativeDiagnosis => alternativeDiagnosis.id === diagnosis?.id);
	}

	private isNew(diagnosis: DiagnosisDto): boolean {
		return !diagnosis.id;
	}

	private getClinicalStatusActivo() {
		return this.healthClinicalStatus.find(clinicalStatus => clinicalStatus.description === 'Activo').description;
	}

	private getVerificationStatusConfirmado() {
		return this.verifications.find(clinicalStatus => clinicalStatus.description === 'Confirmado').description;
	}

	checkDiagnosis(diagnosis: DiagnosisDto[]) {
		this.table.data.forEach(d => {
			const existEquals = !!diagnosis.find(diag => diag.snomed.sctid === d.snomed.sctid);
			if (existEquals)
				this.table.selection.select(d);
		})
	}
}
