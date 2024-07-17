import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ApiErrorDto, AttentionPlacesQuantityDto, BedInfoDto, ResponseEmergencyCareDto } from '@api-rest/api-model';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { SectorService } from '@api-rest/services/sector.service';
import { processErrors } from '@core/utils/form.utils';
import { AttentionPlace } from '@historia-clinica/constants/summaries';
import { BedAssignmentComponent } from '@historia-clinica/dialogs/bed-assignment/bed-assignment.component';
import { OperationDeniedComponent } from '@historia-clinica/modules/ambulatoria/dialogs/diagnosis-required/operation-denied.component';
import { AttendPlace } from '@historia-clinica/modules/guardia/components/emergency-care-dashboard-actions/emergency-care-dashboard-actions.component';
import { SECTOR_GUARDIA } from '@historia-clinica/modules/guardia/constants/masterdata';
import { AttentionPlaceDialogComponent } from '@historia-clinica/modules/guardia/dialogs/attention-place-dialog/attention-place-dialog.component';
import { Episode } from '@historia-clinica/modules/guardia/routes/home/home.component';
import { EpisodeStateService } from '@historia-clinica/modules/guardia/services/episode-state.service';
import { GuardiaRouterService } from '@historia-clinica/modules/guardia/services/guardia-router.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { BehaviorSubject } from 'rxjs';

const TRANSLATE_KEY_PREFIX = 'guardia.home.episodes.episode.actions';

@Injectable({
  providedIn: 'root'
})
export class EmergencyCareEpisodeAttendService {

	isFromEmergencyCareEpisodeList: boolean = false;
	loadEpisode$ = new BehaviorSubject<boolean>(false);

	constructor(private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private readonly sectorService: SectorService,
		private readonly episodeStateService: EpisodeStateService,
		private readonly snackBarService: SnackBarService,
		private readonly guardiaRouterService: GuardiaRouterService,
		private readonly dialog: MatDialog) {}

	attend(episodeId: number, isFromEmergencyCareEpisodeList: boolean): void {
		this.isFromEmergencyCareEpisodeList = isFromEmergencyCareEpisodeList;

		this.emergencyCareEpisodeService.getAdministrative(episodeId).subscribe((response: ResponseEmergencyCareDto) => {
			const episode: Episode = this.mapToEpisode(response);

			this.setQuantityAttentionPlacesBySectorType(episode);
		})
	}

	private setQuantityAttentionPlacesBySectorType(episode: Episode) {
		this.sectorService.quantityAttentionPlacesBySectorType(SECTOR_GUARDIA).subscribe((quantity: AttentionPlacesQuantityDto) => {
			if (quantity.shockroom == 0 && quantity.doctorsOffice == 0 && quantity.bed == 0) {
				return this.dialog.open(OperationDeniedComponent, {
					autoFocus: false,
					data: {
						message: 'No existen lugares disponibles para realizar la atención'
					}
				})
			}
			this.openPlaceAttendDialog(episode, quantity);
		});
	}

	private goToEpisode(episode: Episode, patient: { typeId: number, id: number }) {
		this.guardiaRouterService.goToEpisode(episode.state.id, { id: patient.id, typeId: patient.typeId });
	}

	private mapToEpisode(response: ResponseEmergencyCareDto) {
		const episode: Episode = {
			waitingTime: 0,
			waitingHours: 0,
			creationDate: undefined,
			doctorsOffice: undefined,
			id: response.id,
			patient: response.patient,
			state: response.emergencyCareState,
			triage: undefined,
			type: undefined,
			relatedProfessional: undefined
		}
		return episode;
	}

	private openPlaceAttendDialog(episode: Episode, quantity: AttentionPlacesQuantityDto) {
		const dialogRef = this.dialog.open(AttentionPlaceDialogComponent, {
			width: '35%',
			data: {
				quantity
			}
		});

		dialogRef.afterClosed().subscribe((attendPlace: AttendPlace) => {
			if (!attendPlace) return;

			if (attendPlace.attentionPlace === AttentionPlace.CONSULTORIO) {
				this.episodeStateService.atender(episode.id, attendPlace.id).subscribe(changed => {
					if (changed) {
						this.snackBarService.showSuccess(`${TRANSLATE_KEY_PREFIX}.atender.SUCCESS`);
						this.toEpisodeOrLoadEpisode(episode);
					} else {
						this.snackBarService.showError(`${TRANSLATE_KEY_PREFIX}.atender.ERROR`);
					}
				}, (error: ApiErrorDto) => processErrors(error, (msg) => this.snackBarService.showError(msg)))
			}

			if (attendPlace.attentionPlace === AttentionPlace.SHOCKROOM) {
				this.episodeStateService.atender(episode.id, null, attendPlace.id).subscribe((response: boolean) => {
					if (!response) this.snackBarService.showError(`${TRANSLATE_KEY_PREFIX}.atender.ERROR`);

					this.snackBarService.showSuccess(`${TRANSLATE_KEY_PREFIX}.atender.SUCCESS`);
					this.toEpisodeOrLoadEpisode(episode);

				}, (error: ApiErrorDto) => processErrors(error, (msg) => this.snackBarService.showError(msg)))
			}

			if (attendPlace.attentionPlace === AttentionPlace.HABITACION) {
				this.dialog.open(BedAssignmentComponent, {
						data: {
							sectorsType: [SECTOR_GUARDIA]
						}
					})
					.afterClosed()
					.subscribe((bed: BedInfoDto) => {
						if (!bed) return;

						this.episodeStateService.atender(episode.id, null, null, bed.bed.id).subscribe((response: boolean) => {
							if (!response) this.snackBarService.showError(`${TRANSLATE_KEY_PREFIX}.atender.ERROR`);

							this.snackBarService.showSuccess(`${TRANSLATE_KEY_PREFIX}.atender.SUCCESS`);
							this.toEpisodeOrLoadEpisode(episode);

						}, (error: ApiErrorDto) => processErrors(error, (msg) => this.snackBarService.showError(msg)))
					});
			}
		});
	}

	private toEpisodeOrLoadEpisode(episode: Episode) {
		(this.isFromEmergencyCareEpisodeList)
			? this.goToEpisode(episode, { typeId: episode.patient.typeId, id: episode.patient.id })
			: this.loadEpisode$.next(true);
	}
}
