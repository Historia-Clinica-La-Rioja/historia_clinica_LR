import { Injectable } from '@angular/core';
import { ApiErrorDto, AttentionPlacesQuantityDto, BedInfoDto, ResponseEmergencyCareDto } from '@api-rest/api-model';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { SectorService } from '@api-rest/services/sector.service';
import { processErrors } from '@core/utils/form.utils';
import { AttentionPlace } from '@historia-clinica/constants/summaries';
import { BedAssignmentComponent } from '@historia-clinica/dialogs/bed-assignment/bed-assignment.component';
import { OperationDeniedComponent } from '@historia-clinica/modules/ambulatoria/dialogs/diagnosis-required/operation-denied.component';
import { AttendPlace } from '@historia-clinica/modules/guardia/components/emergency-care-dashboard-actions/emergency-care-dashboard-actions.component';
import { Episode } from '@historia-clinica/modules/guardia/components/emergency-care-patients-summary/emergency-care-patients-summary.component';
import { SECTOR_GUARDIA } from '@historia-clinica/modules/guardia/constants/masterdata';
import { AttentionPlaceDialogComponent } from '@historia-clinica/modules/guardia/dialogs/attention-place-dialog/attention-place-dialog.component';
import { EpisodeStateService } from '@historia-clinica/modules/guardia/services/episode-state.service';
import { GuardiaRouterService } from '@historia-clinica/modules/guardia/services/guardia-router.service';
import { DialogService, DialogWidth } from '@presentation/services/dialog.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { BehaviorSubject } from 'rxjs';

const TRANSLATE_KEY_PREFIX = 'guardia.home.episodes.episode.actions';

@Injectable({
  providedIn: 'root'
})
export class EmergencyCareEpisodeAttendService {

	isFromEmergencyCareEpisodeList: boolean = false;
	isCalling: boolean = false;
	loadEpisode$ = new BehaviorSubject<boolean>(false);

	constructor(
		private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private readonly sectorService: SectorService,
		private readonly episodeStateService: EpisodeStateService,
		private readonly snackBarService: SnackBarService,
		private readonly guardiaRouterService: GuardiaRouterService,
		private readonly dialogAttentionPlaceService: DialogService<AttentionPlaceDialogComponent>,
		private readonly dialogForOperationDeniedService: DialogService<OperationDeniedComponent>,
		private readonly dialogForBedAssignmentService: DialogService<BedAssignmentComponent>,
	) {}

	callOrAttendPatient(episodeId: number, isFromEmergencyCareEpisodeList: boolean, isCalling:boolean ) {
		this.isFromEmergencyCareEpisodeList = isFromEmergencyCareEpisodeList;

		this.isCalling = isCalling;

		this.emergencyCareEpisodeService.getAdministrative(episodeId).subscribe((response: ResponseEmergencyCareDto) => {
			const episode: Episode = this.mapToEpisode(response);

			this.setQuantityAttentionPlacesBySectorType(episode);
		})
	}

	private setQuantityAttentionPlacesBySectorType(episode: Episode) {
		this.sectorService.quantityAttentionPlacesBySectorType(SECTOR_GUARDIA).subscribe((quantity: AttentionPlacesQuantityDto) => {
			if (quantity.shockroom == 0 && quantity.doctorsOffice == 0 && quantity.bed == 0) {
				return this.dialogForOperationDeniedService.open(OperationDeniedComponent,
					{ dialogWidth: DialogWidth.MEDIUM },
					{ message: `${TRANSLATE_KEY_PREFIX}.atender.NO_AVAILABLE_PLACES` }
				)
			}
			this.openPlaceAttendDialog(episode, quantity);
		});
	}

	private goToEpisode(episode: Episode, patient: { typeId: number, id: number }) {
		this.guardiaRouterService.goToEpisode(episode.state.id, { id: patient.id, typeId: patient.typeId });
	}

	private mapToEpisode(response: ResponseEmergencyCareDto) {
		const episode: Episode = {
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
		this.dialogAttentionPlaceService.open(AttentionPlaceDialogComponent,
			{ dialogWidth: DialogWidth.SMALL },
			{ quantity, isCall: this.isCalling }
		)
		.afterClosed()
		.subscribe((attendPlace: AttendPlace) => {
			if (!attendPlace) return;

			const paramsMap = {
				[AttentionPlace.CONSULTORIO]: { doctorsOfficeId: attendPlace.id, bedId: null, shockroomId: null },
				[AttentionPlace.SHOCKROOM]: { shockroomId: attendPlace.id, bedId: null, doctorsOfficeId: null },
				[AttentionPlace.HABITACION]: { shockroomId: null, bedId: null, doctorsOfficeId: null }
			};

			const params = paramsMap[attendPlace.attentionPlace];

			const attendOrCall = this.isCalling
			? this.episodeStateService.call(episode.id,params)
			: this.episodeStateService.attend(episode.id,params);

			const dialogHandlers = {
			[AttentionPlace.CONSULTORIO]: (attendOrCall, episode) => {
				attendOrCall.subscribe(
					response => this.handleResponse(response, episode),
					(error: ApiErrorDto) => processErrors(error, msg => this.snackBarService.showError(msg))
				);
			},
			[AttentionPlace.SHOCKROOM]: (attendOrCall, episode) => {
				attendOrCall.subscribe(
					(response: boolean) => this.handleResponse(response, episode),
					(error: ApiErrorDto) => processErrors(error, msg => this.snackBarService.showError(msg))
				);
			},
			[AttentionPlace.HABITACION]: (attendOrCall, episode) => {
				this.dialogForBedAssignmentService.open(BedAssignmentComponent,
				{ dialogWidth: DialogWidth.LARGE },
				{ sectorsType: [SECTOR_GUARDIA] })
				.afterClosed()
				.subscribe((bed: BedInfoDto) => {
					if (!bed) return;

					const bedParams = { ...params, bedId: bed.bed.id};

					const bedAttendOrCall = this.isCalling
                        ? this.episodeStateService.call(episode.id, bedParams)
                        : this.episodeStateService.attend(episode.id, bedParams);


					bedAttendOrCall.subscribe(
						(response: boolean) => this.handleResponse(response, episode),
						(error: ApiErrorDto) => processErrors(error, msg => this.snackBarService.showError(msg))
					);
				});
			}
			};

			const handler = dialogHandlers[attendPlace.attentionPlace];

			if (handler) {
				handler(attendOrCall, episode);
			}
		});
	}

	private handleResponse(response: boolean, episode: Episode) {
		this.attendOrCallMessage(response);
		if (response) {
			this.toEpisodeOrLoadEpisode(episode);
		}
	}

	private attendOrCallMessage(response:boolean) {
		const status = response ? 'SUCCESS' : 'ERROR';
		const action = this.isCalling ? 'call' : 'atender';

		this.snackBarService.showSuccess(`${TRANSLATE_KEY_PREFIX}.${action}.${status}`);
	}

	private toEpisodeOrLoadEpisode(episode: Episode) {
		(this.isFromEmergencyCareEpisodeList)
			? this.goToEpisode(episode, { typeId: episode.patient.typeId, id: episode.patient.id })
			: this.loadEpisode$.next(true);
	}
}
