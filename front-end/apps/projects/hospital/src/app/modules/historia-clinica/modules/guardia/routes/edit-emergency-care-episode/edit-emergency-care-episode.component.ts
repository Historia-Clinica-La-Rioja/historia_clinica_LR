import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { NewEmergencyCareDto } from '@api-rest/api-model';
import { EmergencyCareEpisodeStateService } from '@api-rest/services/emergency-care-episode-state.service';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { ContextService } from '@core/services/context.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { EstadosEpisodio } from '../../constants/masterdata';
import { GuardiaMapperService } from '../../services/guardia-mapper.service';
import { AdministrativeAdmission } from '../../services/new-episode.service';

@Component({
	selector: 'app-edit-emergency-care-episode',
	templateUrl: './edit-emergency-care-episode.component.html',
	styleUrls: ['./edit-emergency-care-episode.component.scss']
})
export class EditEmergencyCareEpisodeComponent implements OnInit {

	private readonly routePrefix = 'institucion/' + this.contextService.institutionId;
	initData: Observable<AdministrativeAdmission>;
	isDoctorOfficeEditable: boolean;
	private episodeId: number;

	constructor(
		private router: Router,
		private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private readonly contextService: ContextService,
		private readonly route: ActivatedRoute,
		private readonly emergencyCareEpisodeStateService: EmergencyCareEpisodeStateService,
		private readonly dialog: MatDialog,
		private readonly snackBarService: SnackBarService,) { }

	ngOnInit(): void {

		this.route.paramMap.subscribe(
			(params) => {
				this.episodeId = Number(params.get('id'));

				this.initData = this.emergencyCareEpisodeService.getAdministrative(this.episodeId).
					pipe(
						map(GuardiaMapperService._toAdministrativeAdmission),
					);

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
					this.goToEpisodeDetails();

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
				this.goToEpisodeDetails();
			}
		});
	}

	private goToEpisodeDetails(): void {
		const url = `${this.routePrefix}/guardia/episodio/${this.episodeId}`;
		this.router.navigateByUrl(url);
	}
}
