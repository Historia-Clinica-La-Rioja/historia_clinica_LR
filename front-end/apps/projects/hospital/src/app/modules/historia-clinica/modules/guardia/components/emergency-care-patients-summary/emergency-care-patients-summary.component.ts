import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { MasterDataInterface, DateTimeDto, EmergencyCareEpisodeFilterDto, PageDto, EmergencyCareListDto, ProfessionalPersonDto, DoctorsOfficeDto, EmergencyCareEpisodeListTriageDto, EmergencyCarePatientDto, MasterDataDto, BedDto, ShockroomDto, EmergencyCareEpisodeDischargeSummaryDto, SectorDto } from '@api-rest/api-model';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { TriageMasterDataService } from '@api-rest/services/triage-master-data.service';
import { getError, hasError } from '@core/utils/form.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Observable } from 'rxjs';
import { EpisodeFilterService } from '../../services/episode-filter.service';
import { GuardiaRouterService } from '../../services/guardia-router.service';
import { PatientDescriptionUpdate } from '../emergency-care-dashboard-actions/emergency-care-dashboard-actions.component';
import { TriageCategory } from '../triage-chip/triage-chip.component';
import { TriageDefinitionsService } from '../../services/triage-definitions.service';
import { EstadosEpisodio } from '../../constants/masterdata';
import { dateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { differenceInMinutes } from 'date-fns';

const NOT_FOUND = -1;

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
			.subscribe((result: PageDto<EmergencyCareListDto>) => {
				this.elementsAmount = result.totalElementsAmount;
				this.episodes = result.content.map(content => this.mapToEpisode(content));
				this.loading = false;
				/* this.completePatientPhotos(); */ // descomentar cuando se obtengan nuevamente las fotos del paciente
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
		const timeSinceStateChange = episode?.stateUpdatedOn
			? this.calculateWaitingTime(episode.stateUpdatedOn)
			: undefined;
		return {
			...episode,
			triage: {
				emergencyCareEpisodeListTriageDto: episode.triage,
				...(episode.triage.reasons && { reasons: episode.triage.reasons.map(reasons => reasons.snomed.pt) })
			},
			waitingTime: timeSinceStateChange,
			waitingHours: ([EstadosEpisodio.AUSENTE, EstadosEpisodio.LLAMADO].includes(episode.state.id )) && timeSinceStateChange >= 60
			? Math.floor(timeSinceStateChange / 60)
			: undefined
		};
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
				const index = this.episodes.findIndex(episode => episode.id === patientDescriptionUpdate.episodeId);

				if (index !== NOT_FOUND) {
					this.episodes[index] = {
						...this.episodes[index],
						patient: {
							...this.episodes[index].patient,
							patientDescription: patientDescriptionUpdate.patientDescription
						}
					};
				}
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
	waitingTime?: number;
	waitingHours?: number;
	dischargeSummary?: EmergencyCareEpisodeDischargeSummaryDto;
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
	canBeAbsent?: boolean;
	calls?: number;
	bed?: BedDto;
	shockroom?: ShockroomDto;
	sector?: SectorDto;
}

export interface EpisodeListTriage {
	emergencyCareEpisodeListTriageDto: EmergencyCareEpisodeListTriageDto,
	reasons: string[],
}
