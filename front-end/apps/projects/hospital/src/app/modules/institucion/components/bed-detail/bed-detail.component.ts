import { Component, OnInit, Input, OnChanges, SimpleChanges, Output, EventEmitter } from '@angular/core';
import { BedService } from '@api-rest/services/bed.service';
import { ApiErrorMessageDto, BedInfoDto } from '@api-rest/api-model';
import { InternmentPatientService } from "@api-rest/services/internment-patient.service";
import { InternacionService } from "@api-rest/services/internacion.service";
import { ConfirmDialogComponent } from "@presentation/dialogs/confirm-dialog/confirm-dialog.component";
import { MatDialog } from "@angular/material/dialog";
import { SnackBarService } from "@presentation/services/snack-bar.service";
import { InternmentEpisodeService } from "@api-rest/services/internment-episode.service";
import { NurseAssignComponent } from '@institucion/dialogs/nurse-assign/nurse-assign.component';

@Component({
	selector: 'app-bed-detail',
	templateUrl: './bed-detail.component.html',
	styleUrls: ['./bed-detail.component.scss']
})
export class BedDetailComponent implements OnInit, OnChanges {

	@Input() bedId: number;
	@Input() bedAssign = false;
	@Output() assignedBed?: EventEmitter<BedInfoDto> = new EventEmitter<BedInfoDto>();
	@Output() realeseBed = new EventEmitter<boolean>();

	bedInfo: BedInfoDto;
	patientHasAnamnesis = false;
	patientInternmentId: number;
	INTERNMENT_SECTOR_TYPE = 2;

	constructor(
		private readonly bedService: BedService,
		private readonly internmentPatientService: InternmentPatientService,
		private readonly internmentService: InternacionService,
		private readonly dialog: MatDialog,
		private readonly snackBarService: SnackBarService,
		private readonly internmentEpisodeService: InternmentEpisodeService,
	) { }

	ngOnInit(): void {
		if (this.bedId) {
			this.bedService.getBedInfo(this.bedId).subscribe(bedInfo => this.bedInfo = bedInfo);
		}
	}

	ngOnChanges(changes: SimpleChanges) {
		if (changes.bedId.currentValue) {
			this.bedService.getBedInfo(this.bedId).subscribe(bedInfo => {
				this.bedInfo = bedInfo;
				this.setPatientData();
			});
		}
	}

	private setPatientData() {
		if (this.bedInfo.patient) {
			this.internmentPatientService.internmentEpisodeIdInProcess(this.bedInfo.patient.id).subscribe(internmentEpisode => {
				if (internmentEpisode.inProgress) {
					this.patientInternmentId = internmentEpisode.id
					this.internmentService.getInternmentEpisodeSummary(internmentEpisode.id).subscribe(internmentSummaryDto =>
						this.patientHasAnamnesis = !!internmentSummaryDto.documents?.anamnesis)
				}
			});
			if (!this.bedInfo.patient.person)
				this.bedInfo.patient.person  = {
					firstName: 'Paciente temporal',
					...this.bedInfo.patient.person
				};
		}
	}

	assignBed() {
		this.assignedBed.emit(this.bedInfo);
	}

	physicalDischarge() {
		const messageHTML = `<strong>¿Desea confirmar el Alta Física?</strong>`;
		const dialogRef = this.dialog.open(ConfirmDialogComponent, {
			data: {
				content: `Si realiza el Alta Física la cama quedará Libre y el episodio continuará abierto hasta que se completen los documentos necesarios. ${messageHTML}`,
				okButtonLabel: `buttons.CONFIRM`,
				cancelButtonLabel: `buttons.CANCEL`,
				showMatIconError: true,
			},
			width: "25%",
			autoFocus: false,
			disableClose: true,
		});
		dialogRef.afterClosed().subscribe(confirm => {
			if (confirm) {
				this.internmentEpisodeService.physicalDischargeInternmentEpisode(this.patientInternmentId).subscribe(
					success => {
						this.snackBarService.showSuccess('internaciones.discharge.messages.PHYSICAL_DISCHARGE_SUCCESS');
						this.realeseBed.emit(true);
						delete this.bedInfo;
						delete this.bedId;
					},
					error => this.snackBarService.showError('internaciones.discharge.messages.PHYSICAL_DISCHARGE_ERROR')
				);
			}
		})
	}

	openNurseAssign() {
		this.dialog.open(NurseAssignComponent, {
			data: {
				bed: this.bedInfo
			}
		})
		.afterClosed()
		.subscribe((result: boolean) => {
			if (result)
				this.bedService.getBedInfo(this.bedId).subscribe(bedInfo => this.bedInfo = bedInfo);
		});
	}

	deleteAssignedNurse() {
		this.bedService.updateBedNurse(this.bedInfo.bed.id)
		.subscribe({
			next: (_) => {
				this.bedService.getBedInfo(this.bedId).subscribe(bedInfo => this.bedInfo = bedInfo);
				this.snackBarService.showSuccess('gestion-camas.detail.more-actions.DELETED_NURSE');
			},
			error: (err: ApiErrorMessageDto) => {
				this.snackBarService.showError(err.text);
			}
		})
	}
}
