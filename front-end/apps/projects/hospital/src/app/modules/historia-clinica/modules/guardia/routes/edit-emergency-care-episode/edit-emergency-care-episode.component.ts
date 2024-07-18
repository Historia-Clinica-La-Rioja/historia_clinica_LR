import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { NewEmergencyCareDto } from '@api-rest/api-model';
import { EmergencyCareEpisodeStateService } from '@api-rest/services/emergency-care-episode-state.service';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { EstadosEpisodio } from '../../constants/masterdata';
import { GuardiaMapperService } from '../../services/guardia-mapper.service';
import { GuardiaRouterService } from '../../services/guardia-router.service';
import { AdministrativeAdmission } from '../../services/new-episode.service';

@Component({
	selector: 'app-edit-emergency-care-episode',
	templateUrl: './edit-emergency-care-episode.component.html',
	styleUrls: ['./edit-emergency-care-episode.component.scss']
})
export class EditEmergencyCareEpisodeComponent implements OnInit {

	initData: AdministrativeAdmission;
	isDoctorOfficeEditable: boolean;
	private episodeId: number;
	private patientId: number;
	private patientTypeId: number;

	private from: string;

	constructor(
		private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private readonly route: ActivatedRoute,
		private readonly emergencyCareEpisodeStateService: EmergencyCareEpisodeStateService,
		private readonly dialog: MatDialog,
		private readonly snackBarService: SnackBarService,
		private readonly guardiaRouterService: GuardiaRouterService
	) { }

	ngOnInit(): void {

		this.route.paramMap.subscribe(
			(params) => {
				this.from = params.get('from');
				this.episodeId = Number(params.get('id'));

				this.emergencyCareEpisodeService.getAdministrative(this.episodeId).
					subscribe(r => {
						this.initData = GuardiaMapperService._toAdministrativeAdmission(r);
						this.patientId = this.initData.patientId;
						this.patientTypeId = r.patient.typeId;
					});

				this.emergencyCareEpisodeStateService.getState(this.episodeId)
					.subscribe(state => this.isDoctorOfficeEditable = state.id !== EstadosEpisodio.EN_ATENCION
					);
			});


	}

	confirm(administrativeAdmission: AdministrativeAdmission): void {
		const ecAdministrativeDto: NewEmergencyCareDto = GuardiaMapperService._toECAdministrativeDto(administrativeAdmission);
		this.emergencyCareEpisodeService.updateAdministrative(this.episodeId, ecAdministrativeDto)
			.subscribe(id => {
				if (id) {
					this.snackBarService.showSuccess('guardia.episode.edit.messages.SUCCESS');
					this.guardiaRouterService.goToEpisode(this.episodeId, { typeId: this.patientTypeId, id: administrativeAdmission.patientId })
				} else {
					this.snackBarService.showError('guardia.episode.edit.messages.ERROR');
				}
			}, (error) => error?.text ? this.snackBarService.showError(error.text) : this.snackBarService.showError('guardia.episode.edit.messages.ERROR'));
	}

	openDialog(): void {
		const dialogRef = this.dialog.open(ConfirmDialogComponent, {
			width: '450px',
			data: {
				title: 'guardia.episode.edit.cancel.TITLE',
				content: 'guardia.episode.edit.cancel.SUBTITLE',
				okButtonLabel: 'buttons.YES',
				cancelButtonLabel: 'buttons.NO'
			}
		});
		dialogRef.afterClosed().subscribe(confirmed => {
			if (confirmed) {
				this.from === 'patientClinicalHistory'
				? this.guardiaRouterService.goToEpisode(this.episodeId, { typeId: this.patientTypeId, id: this.patientId })
				: this.guardiaRouterService.goToEmergencyCareDashboard();
			}
		});
	}

}
