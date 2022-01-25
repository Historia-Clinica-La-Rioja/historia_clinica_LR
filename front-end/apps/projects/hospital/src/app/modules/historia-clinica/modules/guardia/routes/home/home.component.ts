import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import {
	DateTimeDto,
	DoctorsOfficeDto, EmergencyCareEpisodeListTriageDto,
	EmergencyCareListDto,
	EmergencyCarePatientDto,
	MasterDataDto, MasterDataInterface,
	PatientPhotoDto
} from '@api-rest/api-model';
import { dateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { differenceInMinutes } from 'date-fns';
import { EstadosEpisodio, Triages } from '../../constants/masterdata';
import { ImageDecoderService } from '@presentation/services/image-decoder.service';
import { EpisodeStateService } from '../../services/episode-state.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { MatDialog } from '@angular/material/dialog';
import { SelectConsultorioComponent } from '../../dialogs/select-consultorio/select-consultorio.component';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { TriageDefinitionsService } from '../../services/triage-definitions.service';
import { PatientService } from '@api-rest/services/patient.service';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { EpisodeFilterService } from '../../services/episode-filter.service';
import { TriageCategoryDto, TriageMasterDataService } from '@api-rest/services/triage-master-data.service';
import { FormBuilder } from '@angular/forms';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { getError, hasError } from '@core/utils/form.utils';
import { EmergencyCareEpisodeAdministrativeDischargeService } from '@api-rest/services/emergency-care-episode-administrative-service.service';
import { PatientNameService } from "@core/services/patient-name.service";

const TRANSLATE_KEY_PREFIX = 'guardia.home.episodes.episode.actions';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

	getError = getError;
	hasError = hasError;

	constructor(
		private router: Router,
		private emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private imageDecoderService: ImageDecoderService,
		private snackBarService: SnackBarService,
		private readonly dialog: MatDialog,
		public readonly episodeStateService: EpisodeStateService,
		private readonly triageDefinitionsService: TriageDefinitionsService,
		private readonly patientService: PatientService,
		public readonly formBuilder: FormBuilder,
		public readonly triageMasterDataService: TriageMasterDataService,
		public readonly emergencyCareMasterDataService: EmergencyCareMasterDataService,
		private readonly emergencyCareEpisodeAdministrativeDischargeService: EmergencyCareEpisodeAdministrativeDischargeService,
		private readonly patientNameService: PatientNameService,) {
		this.filterService = new EpisodeFilterService(formBuilder, triageMasterDataService, emergencyCareMasterDataService);
	}

	filterService: EpisodeFilterService;

	readonly estadosEpisodio = EstadosEpisodio;
	readonly triages = Triages;
	readonly PACIENTE_TEMPORAL = 3;

	loading = true;
	episodes: Episode[];
	episodesOriginal: Episode[];
	patientsPhotos: PatientPhotoDto[];

	triageCategories$: Observable<TriageCategoryDto[]>;
	emergencyCareTypes$: Observable<MasterDataInterface<number>[]>;

	private static calculateWaitingTime(dateTime: DateTimeDto): number {
		const creationDate = dateTimeDtoToDate(dateTime);
		const now = new Date();
		return differenceInMinutes(now, creationDate);
	}

	ngOnInit(): void {
		this.loadEpisodes();
		this.triageCategories$ = this.filterService.getTriageCategories();
		this.emergencyCareTypes$ = this.filterService.getEmergencyCareTypes();
	}

	loadEpisodes(): void {
		this.emergencyCareEpisodeService.getAll()
			.pipe(
				map((episodes: EmergencyCareListDto[]) =>
					episodes.map(episode => this.setWaitingTime(episode))
				)
			)
			.subscribe((episodes: any[]) => {
				this.episodes = this.setPatientNames(episodes);
				this.episodesOriginal = episodes;
				this.loading = false;
				this.completePatientPhotos();
				this.episodes = this.episodesOriginal.filter(episode => this.filterService.filter(episode));
			}, _ => this.loading = false);
	}

	goToEpisode(id: number) {
		this.router.navigate([`${this.router.url}/episodio/${id}`]);
	}

	goToAdmisionAdministrativa(): void {
		this.router.navigate([`${this.router.url}/nuevo-episodio/administrativa`]);
	}

	atender(episodeId: number): void {

		const dialogRef = this.dialog.open(SelectConsultorioComponent, {
			width: '25%',
			data: {title : 'guardia.select_consultorio.ATENDER'}
		});

		dialogRef.afterClosed().subscribe(consultorio => {
			if (consultorio) {
				this.episodeStateService.atender(episodeId, consultorio.id).subscribe(changed => {
						if (changed) {
							this.snackBarService.showSuccess(`${TRANSLATE_KEY_PREFIX}.atender.SUCCESS`);
							this.goToEpisode(episodeId);
						} else {
							this.snackBarService.showError(`${TRANSLATE_KEY_PREFIX}.atender.ERROR`);
						}
					}, _ => this.snackBarService.showError(`${TRANSLATE_KEY_PREFIX}.atender.ERROR`)
				);
			}
		});
	}

	finalizar(episodeId: number): void {
		const dialogRef = this.dialog.open(ConfirmDialogComponent, {
			data: {
				title: 'guardia.home.episodes.episode.actions.finalizar_ausencia.TITLE',
				content: 'guardia.home.episodes.episode.actions.finalizar_ausencia.CONFIRM'
			}
		});

		dialogRef.afterClosed().subscribe(confirmed => {
			if (confirmed) {
				this.emergencyCareEpisodeAdministrativeDischargeService.newAdministrativeDischargeByAbsence(episodeId).subscribe(changed => {
						if (changed) {
							this.snackBarService
								.showSuccess(`${TRANSLATE_KEY_PREFIX}.finalizar_ausencia.SUCCESS`);
							this.loadEpisodes();
						} else {
							this.snackBarService
								.showError(`${TRANSLATE_KEY_PREFIX}.finalizar_ausencia.ERROR`);
						}
					}, _ => this.snackBarService
						.showError(`${TRANSLATE_KEY_PREFIX}.finalizar_ausencia.ERROR`)
				);
			}
		});
	}

	nuevoTriage(episode: EmergencyCareListDto): void {
		this.triageDefinitionsService.getTriagePath(episode.type?.id)
			.subscribe( ({component}) => {
				const dialogRef = this.dialog.open(component, {data: episode.id});
				dialogRef.afterClosed().subscribe(idReturned => {
					if (idReturned) {
						this.loadEpisodes();
					}
				});
			});
	}

	filter(): void {
		this.filterService.markAsFiltered();
		if (this.filterService.isValid()) {
			this.episodes = this.episodesOriginal
				.filter(episode => this.filterService.filter(episode));
		}
	}

	clear(control: string): void {
		this.filterService.clear(control);
		this.filter();
	}

	private completePatientPhotos() {
		if (this.patientsPhotos) {
			this.patientsPhotos.forEach(patientPhoto => {
				this.setEpisodePhoto(patientPhoto.patientId, patientPhoto.imageData);
			});
		} else {
			const patientsIds = getPatientsIds(this.episodes);
			if (patientsIds.length) {
				this.patientService.getPatientsPhotos(patientsIds).subscribe(patientsPhotos => {
					this.patientsPhotos = patientsPhotos;
					this.patientsPhotos.forEach(patientPhoto => {
						this.setEpisodePhoto(patientPhoto.patientId, patientPhoto.imageData);
					});
				});
			}
		}

		function getPatientsIds(episodes: any[]) {
			const ids = [];
			episodes.forEach(ep => {
				if (ep.patient?.id) {
					ids.push(ep.patient.id);
				}
			});
			return ids;
		}
	}

	private setEpisodePhoto(patientId: number, imageData: string) {
		const episode = this.episodes.find(ep => ep.patient?.id === patientId);
		if (episode) {
			episode.decodedPatientPhoto = this.imageDecoderService.decode(imageData);
		}
	}

	private setWaitingTime(episode: EmergencyCareListDto): Episode {
		return {
			...episode,
			waitingTime: episode.state.id === this.estadosEpisodio.EN_ESPERA ?
				HomeComponent.calculateWaitingTime(episode.creationDate) : undefined
		};
	}

	setPatientNames(episodes: any []) {
		return episodes.filter(e => {
			if(e.patient?.person)
				e.patient.person.firstName = this.patientNameService.getPatientName(e.patient.person.firstName, e.patient.person.nameSelfDetermination);
		})
	}
}

export interface Episode {
	waitingTime: number;
	decodedPatientPhoto?: Observable<string>;
	creationDate: DateTimeDto;
	doctorsOffice: DoctorsOfficeDto;
	id: number;
	patient: EmergencyCarePatientDto;
	state: MasterDataDto;
	triage: EmergencyCareEpisodeListTriageDto;
	type: MasterDataDto;
}
