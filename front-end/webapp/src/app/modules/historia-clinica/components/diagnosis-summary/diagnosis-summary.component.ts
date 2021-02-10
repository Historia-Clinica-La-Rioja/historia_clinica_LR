import { Component, OnInit, Input } from '@angular/core';
import { DIAGNOSTICOS } from '../../constants/summaries';
import { MasterDataInterface, HealthConditionDto, ResponseEvolutionNoteDto, EvolutionNoteDto } from '@api-rest/api-model';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { TableModel, ActionDisplays } from '@presentation/components/table/table.component';
import { MatDialog } from '@angular/material/dialog';
import { RemoveDiagnosisComponent } from '../../dialogs/remove-diagnosis/remove-diagnosis.component';
import { HEALTH_CLINICAL_STATUS } from '../../modules/internacion/constants/ids';
import { Router } from '@angular/router';
import { EvolutionNoteService } from '@api-rest/services/evolution-note.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { EvolutionNotesListenerService } from '../../modules/internacion/services/evolution-notes-listener.service';

export const COVID_SNOMED = { sctid: '186747009', pt: 'infección por coronavirus' }

@Component({
	selector: 'app-diagnosis-summary',
	templateUrl: './diagnosis-summary.component.html',
	styleUrls: ['./diagnosis-summary.component.scss']
})
export class DiagnosisSummaryComponent implements OnInit {

	@Input() internmentEpisodeId: number;
	@Input() editable: boolean = true;

	diagnosticosSummary = DIAGNOSTICOS;
	verifications: MasterDataInterface<string>[];
	clinicalStatus: MasterDataInterface<string>[];
	tableModel: TableModel<HealthConditionDto>;
	viewCovidAlert: boolean = true;

	constructor(
		private readonly internmentStateService: InternmentStateService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly router: Router,
		private evolutionNoteService: EvolutionNoteService,
		private snackBarService: SnackBarService,
		public dialog: MatDialog,
		private evolutionNotesListenerService: EvolutionNotesListenerService,
	) { }

	ngOnInit(): void {
		this.loadClinicalStatus();
		this.loadVerifications();
		this.loadDiagnosesGeneral();
	}

	private loadClinicalStatus(): void {
		this.internacionMasterDataService.getHealthClinical().subscribe(healthClinical => {
			this.clinicalStatus = healthClinical;
		});
	}

	private loadVerifications(): void {
		this.internacionMasterDataService.getHealthVerification().subscribe(healthVerification => {
			this.verifications = healthVerification;
		});
	}

	private loadDiagnosesGeneral(): void {
		this.internmentStateService.getAlternativeDiagnosesGeneralState(this.internmentEpisodeId).subscribe(
			data => {
				this.viewCovidAlert = true;
				this.tableModel = this.buildTable(data);
				data.forEach(elem => {
					if (elem.snomed.sctid == COVID_SNOMED.sctid && elem.statusId == HEALTH_CLINICAL_STATUS.ACTIVO) {
						this.viewCovidAlert = false;
					}
				});
			}
		);
	}

	private buildTable(data: HealthConditionDto[]): TableModel<HealthConditionDto> {
		let model: TableModel<HealthConditionDto> = {
			columns: [
				{
					columnDef: 'diagnosis',
					header: 'internaciones.anamnesis.diagnosticos.DIAGNOSIS',
					text: (row) => row.snomed.pt
				},
				{
					columnDef: 'status',
					header: 'Estado',
					text: (row) => this.clinicalStatus?.find(status => status.id === row.statusId)?.description
				},
				{
					columnDef: 'verification',
					header: 'Verificación',
					text: (row) => this.verifications?.find(verification => verification.id === row.verificationId)?.description
				},
			],
			data
		};
		if (this.editable && data.some(d => d.statusId === HEALTH_CLINICAL_STATUS.ACTIVO)) {
			model.columns.push({
				columnDef: 'clinicalEval',
				header: 'Evaluación clínica',
				action: {
					displayType: ActionDisplays.ICON,
					display: 'note_add',
					matColor: 'primary',
					do: row => {
						this.router.navigate([`${this.router.url}/eval-clinica-diagnosticos/${row.id}`]);
					},
					hide: row => row.statusId !== HEALTH_CLINICAL_STATUS.ACTIVO
				},
			});
			model.columns.push({
				columnDef: 'remove',
				action: {
					displayType: ActionDisplays.ICON,
					display: 'delete',
					matColor: 'warn',
					do: row => {
						const diagnosis = { ...row };
						const dialogRef = this.dialog.open(RemoveDiagnosisComponent, {
							disableClose: true,
							data: {
								diagnosis,
								internmentEpisodeId: this.internmentEpisodeId
							}
						});
						dialogRef.afterClosed().subscribe(
							() => {
								this.loadDiagnosesGeneral();
								this.evolutionNotesListenerService.loadEvolutionNotes();
							}
						);
					},
					hide: row => row.statusId !== HEALTH_CLINICAL_STATUS.ACTIVO
				},
			});
		}
		return model;
	}

	generateDiagnosisCovid() {
		const evolutionNote: EvolutionNoteDto = {
			confirmed: true,
			allergies: [],
			anthropometricData: undefined,
			diagnosis: [
				{
					presumptive: true,
					snomed: { sctid: COVID_SNOMED.sctid, pt: COVID_SNOMED.pt, parentFsn: '', parentId: '' }
				}
			],
			immunizations: [],
			notes: undefined,
			vitalSigns: undefined
		};

		this.evolutionNoteService.createDocument(evolutionNote, this.internmentEpisodeId).subscribe(
			(evolutionNoteResponse: ResponseEvolutionNoteDto) => {
				this.loadClinicalStatus();
				this.loadVerifications();
				this.loadDiagnosesGeneral();
				this.evolutionNotesListenerService.loadEvolutionNotes();
				this.snackBarService.showSuccess('internaciones.alerta-covid.messages.SUCCESS');
			}, _ => this.snackBarService.showError('internaciones.alerta-covid.messages.ERROR'));
	}

}
