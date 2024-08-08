import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { MasterDataInterface, DateTimeDto, EmergencyCareEpisodeFilterDto, PageDto, EmergencyCareListDto, ProfessionalPersonDto, DoctorsOfficeDto, EmergencyCareEpisodeListTriageDto, EmergencyCarePatientDto, MasterDataDto } from '@api-rest/api-model';
import { dateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { TriageMasterDataService } from '@api-rest/services/triage-master-data.service';
import { PatientNameService } from '@core/services/patient-name.service';
import { getError, hasError } from '@core/utils/form.utils';
import { PatientType } from '@historia-clinica/constants/summaries';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { differenceInMinutes } from 'date-fns';
import { Observable, tap, map } from 'rxjs';
import { EstadosEpisodio, Triages } from '../../constants/masterdata';
import { EpisodeFilterService } from '../../services/episode-filter.service';
import { GuardiaRouterService } from '../../services/guardia-router.service';
import { PatientDescriptionUpdate } from '../emergency-care-dashboard-actions/emergency-care-dashboard-actions.component';
import { TriageCategory } from '../triage-chip/triage-chip.component';
import { TriageDefinitionsService } from '../../services/triage-definitions.service';

@Component({
	selector: 'app-emergency-care-patients-summary',
	templateUrl: './emergency-care-patients-summary.component.html',
	styleUrls: ['./emergency-care-patients-summary.component.scss'],
	providers: [TriageDefinitionsService]
})
export class EmergencyCarePatientsSummaryComponent implements OnInit {

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
	/* patientsPhotos: PatientPhotoDto[]; */
	elementsAmount: number;

	triageCategories$: Observable<TriageCategory[]>;
	emergencyCareTypes$: Observable<MasterDataInterface<number>[]>;

	constructor(
		private router: Router,
		private emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private snackBarService: SnackBarService,
		public readonly formBuilder: UntypedFormBuilder,
		public readonly triageMasterDataService: TriageMasterDataService,
		public readonly emergencyCareMasterDataService: EmergencyCareMasterDataService,
		private readonly patientNameService: PatientNameService,
		private readonly guardiaRouterService: GuardiaRouterService,
	) {
		this.filterService = new EpisodeFilterService(formBuilder, triageMasterDataService, emergencyCareMasterDataService);
	}

	ngOnInit(): void {
		this.loadEpisodes(this.FIRST_PAGE);
		this.triageCategories$ = this.filterService.getTriageCategories();
		this.emergencyCareTypes$ = this.filterService.getEmergencyCareTypes();
	}

	loadEpisodes(pageNumber: number): void {
		const filterData: EmergencyCareEpisodeFilterDto = this.getFilterData();
		this.emergencyCareEpisodeService.getAll(this.PAGE_SIZE, pageNumber, filterData)
			.pipe(
				tap(result => this.elementsAmount = result.totalElementsAmount),
				map((episodes: PageDto<EmergencyCareListDto>) =>
					episodes.content.map(episode => this.mapToEpisode(episode))
				)
			)
			.subscribe((episodes: Episode[]) => {
				this.episodes = episodes;
				this.setPatientNames(this.episodes);
				this.loading = false;
				/* this.completePatientPhotos(); */ // descomentar cuando se obtengan nuevamente las fotos del paciente
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

	//Descomentar y refactorizar cuando se obtengan nuevamente las fotos del paciente
	/* private completePatientPhotos() {
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
	} */

	private mapToEpisode(episode: EmergencyCareListDto): Episode {
		const minWaitingTime = episode.state.id === this.estadosEpisodio.EN_ESPERA ?
			this.calculateWaitingTime(episode.creationDate) : undefined;
		const timeSinceStateChange = episode.state.id === this.estadosEpisodio.AUSENTE ?
			this.calculateWaitingTime(episode.stateUpdatedOn) : undefined;
		return {
			...episode,
			triage: {
				emergencyCareEpisodeListTriageDto: episode.triage,
				...(episode.triage.reasons && { reasons: episode.triage.reasons.map(reasons => reasons.snomed.pt) })
			},
			waitingTime: episode.state.id === this.estadosEpisodio.AUSENTE ? timeSinceStateChange : minWaitingTime,
			waitingHours: episode.state.id === this.estadosEpisodio.AUSENTE ?
				(timeSinceStateChange ? Math.round(timeSinceStateChange / 60) : undefined) :
				(minWaitingTime ? Math.round(minWaitingTime / 60) : undefined),
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

	handleTriageDialogClosed(idReturned: number) {
		idReturned && this.loadEpisodes(this.FIRST_PAGE);
	}

	handlePatientDescriptionUpdate(patientDescriptionUpdate: PatientDescriptionUpdate) {
		this.emergencyCareEpisodeService.updatePatientDescription(patientDescriptionUpdate.episodeId, patientDescriptionUpdate.patientDescription).subscribe({
			next: () => {
				this.snackBarService.showSuccess('guardia.home.episodes.episode.actions.edit_patient_description.SUCCESS');
				const updatedEpisode = this.episodes.find(episode => episode.id === patientDescriptionUpdate.episodeId);
				updatedEpisode.patient.patientDescription = patientDescriptionUpdate.patientDescription;
			},
			error: () => this.snackBarService.showError('guardia.home.episodes.episode.actions.edit_patient_description.ERROR')
		});
	}

	handleAbsentState(stateChanged: boolean) {
		if (stateChanged) {
			this.loadEpisodes(this.FIRST_PAGE);
		}
	}

	private calculateWaitingTime(dateTime: DateTimeDto): number {
		const creationDate = dateTimeDtoToDate(dateTime);
		const now = new Date();
		return differenceInMinutes(now, creationDate);
	}

}

export interface Episode {
	waitingTime: number;
	waitingHours: number;
	decodedPatientPhoto?: Observable<string>;
	creationDate: DateTimeDto;
	stateUpdatedOn?: DateTimeDto;
	doctorsOffice: DoctorsOfficeDto;
	id: number;
	patient: EmergencyCarePatientDto;
	state: MasterDataDto;
	triage: EpisodeListTriage;
	type: MasterDataDto;
	relatedProfessional: ProfessionalPersonDto;
	reason?: string;
	canBeAbsent?: boolean
}

export interface EpisodeListTriage {
	emergencyCareEpisodeListTriageDto: EmergencyCareEpisodeListTriageDto,
	reasons: string[],
}