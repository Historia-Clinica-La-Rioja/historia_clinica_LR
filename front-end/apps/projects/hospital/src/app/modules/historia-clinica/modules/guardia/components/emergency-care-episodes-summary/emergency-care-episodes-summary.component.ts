import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder } from '@angular/forms';
import { DateTimeDto, PageDto, EmergencyCareListDto, ProfessionalPersonDto, DoctorsOfficeDto, EmergencyCareEpisodeListTriageDto, EmergencyCarePatientDto, MasterDataDto, BedDto, ShockroomDto, EmergencyCareEpisodeDischargeSummaryDto, SectorDto } from '@api-rest/api-model';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { TriageMasterDataService } from '@api-rest/services/triage-master-data.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { GuardiaRouterService } from '../../services/guardia-router.service';
import { PatientDescriptionUpdate } from '../emergency-care-dashboard-actions/emergency-care-dashboard-actions.component';
import { TriageDefinitionsService } from '../../services/triage-definitions.service';
import { EpisodeFilterService } from '../../services/episode-filter.service';

const NOT_FOUND = -1;

@Component({
	selector: 'app-emergency-care-episodes-summary',
	templateUrl: './emergency-care-episodes-summary.component.html',
	styleUrls: ['./emergency-care-episodes-summary.component.scss'],
	providers: [TriageDefinitionsService]
})
export class EmergencyCareEpisodesSummaryComponent implements OnInit {

	readonly PAGE_SIZE = 20;
	readonly FIRST_PAGE = 0;

	loading = true;
	episodes: Episode[];
	/* patientsPhotos: PatientPhotoDto[]; */
	elementsAmount: number;

	constructor(
		private emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private snackBarService: SnackBarService,
		public readonly formBuilder: UntypedFormBuilder,
		public readonly triageMasterDataService: TriageMasterDataService,
		public readonly emergencyCareMasterDataService: EmergencyCareMasterDataService,
		private readonly guardiaRouterService: GuardiaRouterService,
		readonly filterService: EpisodeFilterService,
	) { }

	ngOnInit(): void {
		this.loadEpisodes(this.FIRST_PAGE);
	}

	loadEpisodes(pageNumber: number): void {
		const filterData = this.filterService.filters;
		this.emergencyCareEpisodeService.getAll(this.PAGE_SIZE, pageNumber, filterData )
			.subscribe((result: PageDto<EmergencyCareListDto>) => {
				this.elementsAmount = result.totalElementsAmount;
				this.episodes = result.content.map(content => this.mapToEpisode(content));
				this.loading = false;
				/* this.completePatientPhotos(); */ // descomentar cuando se obtengan nuevamente las fotos del paciente
			}, _ => this.loading = false);
	}

	goToEpisode(episode: Episode, patient: { typeId: number, id: number }) {
		this.guardiaRouterService.goToEpisode(episode.state.id, { id: patient.id, typeId: patient.typeId });
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
		return {
			...episode,
			triage: {
				emergencyCareEpisodeListTriageDto: episode.triage,
				...(episode.triage.reasons && { reasons: episode.triage.reasons.map(reasons => reasons.snomed.pt) })
			}
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

}

export interface Episode {
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
