import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import {
	DateTimeDto,
	DoctorsOfficeDto, EmergencyCareEpisodeListTriageDto,
	EmergencyCareListDto,
	EmergencyCarePatientDto,
	MasterDataDto, MasterDataInterface,
	PatientPhotoDto,
	ProfessionalPersonDto,
	PageDto,
	EmergencyCareEpisodeFilterDto
} from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import { dateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { differenceInMinutes } from 'date-fns';
import { EstadosEpisodio, Triages } from '../../constants/masterdata';
import { ImageDecoderService } from '@presentation/services/image-decoder.service';
import { EpisodeStateService } from '../../services/episode-state.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { TriageDefinitionsService } from '../../services/triage-definitions.service';
import { PatientService } from '@api-rest/services/patient.service';
import { map, tap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { EpisodeFilterService } from '../../services/episode-filter.service';
import { TriageCategoryDto, TriageMasterDataService } from '@api-rest/services/triage-master-data.service';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { getError, hasError } from '@core/utils/form.utils';
import { EmergencyCareEpisodeAdministrativeDischargeService } from '@api-rest/services/emergency-care-episode-administrative-service.service';
import { PatientNameService } from "@core/services/patient-name.service";
import { anyMatch } from '@core/utils/array.utils';
import { PermissionsService } from '@core/services/permissions.service';
import { GuardiaRouterService } from '../../services/guardia-router.service';
import { AttentionPlace, PatientType } from '@historia-clinica/constants/summaries';
import { UntypedFormBuilder } from '@angular/forms';
import { EmergencyCareEpisodeAttendService } from '@historia-clinica/services/emergency-care-episode-attend.service';

const TRANSLATE_KEY_PREFIX = 'guardia.home.episodes.episode.actions';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss'],
	providers: [TriageDefinitionsService]
})
export class HomeComponent implements OnInit {

	getError = getError;
	hasError = hasError;

	filterService: EpisodeFilterService;

	readonly estadosEpisodio = EstadosEpisodio;
	readonly triages = Triages;
	readonly PACIENTE_TEMPORAL = 3;
	readonly EMERGENCY_CARE_TEMPORARY = PatientType.EMERGENCY_CARE_TEMPORARY;
	readonly PAGE_SIZE = 20;
	readonly FIRST_PAGE = 0;

	loading = true;
	episodes: Episode[];
	patientsPhotos: PatientPhotoDto[];
	elementsAmount: number;

	triageCategories$: Observable<TriageCategoryDto[]>;
	emergencyCareTypes$: Observable<MasterDataInterface<number>[]>;

	hasEmergencyCareRelatedRole: boolean;
	rolesToEpisodeAttend: ERole[] = [ERole.ENFERMERO, ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_MEDICO, ERole.ESPECIALISTA_EN_ODONTOLOGIA];

	private static calculateWaitingTime(dateTime: DateTimeDto): number {
		const creationDate = dateTimeDtoToDate(dateTime);
		const now = new Date();
		return differenceInMinutes(now, creationDate);
	}

	constructor(
		private router: Router,
		private emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private imageDecoderService: ImageDecoderService,
		private snackBarService: SnackBarService,
		private readonly dialog: MatDialog,
		public readonly episodeStateService: EpisodeStateService,
		private readonly triageDefinitionsService: TriageDefinitionsService,
		private readonly patientService: PatientService,
		public readonly formBuilder: UntypedFormBuilder,
		public readonly triageMasterDataService: TriageMasterDataService,
		public readonly emergencyCareMasterDataService: EmergencyCareMasterDataService,
		private readonly emergencyCareEpisodeAdministrativeDischargeService: EmergencyCareEpisodeAdministrativeDischargeService,
		private readonly patientNameService: PatientNameService,
		private readonly permissionsService: PermissionsService,
		private readonly guardiaRouterService: GuardiaRouterService,
		private readonly emergencyCareEpisodeAttend: EmergencyCareEpisodeAttendService
	) {
		this.filterService = new EpisodeFilterService(formBuilder, triageMasterDataService, emergencyCareMasterDataService);
	}

	ngOnInit(): void {
		this.loadEpisodes(this.FIRST_PAGE);
		this.triageCategories$ = this.filterService.getTriageCategories();
		this.emergencyCareTypes$ = this.filterService.getEmergencyCareTypes();
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.hasEmergencyCareRelatedRole = anyMatch<ERole>(userRoles, [ERole.ESPECIALISTA_MEDICO, ERole.ENFERMERO, ERole.PROFESIONAL_DE_SALUD]);
		});
	}

	loadEpisodes(pageNumber: number): void {
		const filterData: EmergencyCareEpisodeFilterDto = this.getFilterData();
		this.emergencyCareEpisodeService.getAll(this.PAGE_SIZE, pageNumber, filterData)
			.pipe(
				tap(result => this.elementsAmount = result.totalElementsAmount),
				map((episodes: PageDto<EmergencyCareListDto>) =>
					episodes.content.map(episode => this.setWaitingTime(episode))
				)
			)
			.subscribe((episodes: Episode[]) => {
				this.episodes = episodes;
				this.setPatientNames(this.episodes);
				this.loading = false;
				this.completePatientPhotos();
				this.episodes.forEach(episode => {
					if (episode.relatedProfessional)
						this.getFullProfessionalName(episode.relatedProfessional);
				});
			}, _ => this.loading = false);
	}

	private getFilterData(): EmergencyCareEpisodeFilterDto {
		return {
			mustBeEmergencyCareTemporal: this.filterService.getForm().value.emergencyCareTemporary,
			identificationNumber: this.filterService.getForm().value.identificationNumber,
			patientFirstName: this.filterService.getForm().value.firstName,
			patientId: this.filterService.getForm().value.patientId,
			patientLastName: this.filterService.getForm().value.lastName,
			mustBeTemporal: this.filterService.getForm().value.temporal,
			triageCategoryId: this.filterService.getForm().value.triage,
			typeId: this.filterService.getForm().value.emergencyCareType,
		};
	}

	goToEpisode(episode: Episode, patient: { typeId: number, id: number }) {
		this.guardiaRouterService.goToEpisode(episode.state.id, { id: patient.id, typeId: patient.typeId });
	}

	goToAdmisionAdministrativa(): void {
		this.router.navigate([`${this.router.url}/nuevo-episodio/administrativa`]);
	}

	atender(episodeId: number) {
		this.emergencyCareEpisodeAttend.attend(episodeId, true);
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
						this.loadEpisodes(this.FIRST_PAGE);
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
			.subscribe(({ component }) => {
				const dialogRef = this.dialog.open(component, { data: episode.id });
				dialogRef.afterClosed().subscribe(idReturned => {
					if (idReturned) {
						this.loadEpisodes(this.FIRST_PAGE);
					}
				});
			});
	}

	filter(): void {
		this.filterService.markAsFiltered();
		if (this.filterService.isValid()) {
			this.loadEpisodes(this.FIRST_PAGE);
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
		const minWaitingTime = episode.state.id === this.estadosEpisodio.EN_ESPERA ?
			HomeComponent.calculateWaitingTime(episode.creationDate) : undefined;
		return {
			...episode,
			waitingTime: minWaitingTime,
			waitingHours: minWaitingTime ? Math.round(minWaitingTime / 60) : undefined
		};
	}

	setPatientNames(episodes: Episode[]) {
		episodes.forEach(e => {
			if (e.patient?.person)
				e.patient.person.firstName = this.patientNameService.getPatientName(e.patient.person.firstName, e.patient.person.nameSelfDetermination);
		})
	}

	private getFullProfessionalName(professional: ProfessionalPersonDto) {
		const professionalName = `${this.patientNameService.getPatientName(professional.firstName, professional.nameSelfDetermination)}`;
		professional.fullName = `${professionalName} ${professional.middleNames == null ? "" : professional.middleNames} ${professional.lastName} ${professional.otherLastNames == null ? "" : professional.otherLastNames}`;
	}

	handlePageEvent(event) {
		this.loadEpisodes(event.pageIndex);
	}

}

export interface Episode {
	waitingTime: number;
	waitingHours: number;
	decodedPatientPhoto?: Observable<string>;
	creationDate: DateTimeDto;
	doctorsOffice: DoctorsOfficeDto;
	id: number;
	patient: EmergencyCarePatientDto;
	state: MasterDataDto;
	triage: EmergencyCareEpisodeListTriageDto;
	type: MasterDataDto;
	relatedProfessional: ProfessionalPersonDto;
	reason?: string;
}

export interface AttendPlace {
	id: number,
	attentionPlace: AttentionPlace
}
