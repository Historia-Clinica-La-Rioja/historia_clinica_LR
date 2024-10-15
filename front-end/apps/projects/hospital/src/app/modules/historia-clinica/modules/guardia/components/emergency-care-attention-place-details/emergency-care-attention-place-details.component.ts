import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { SelectedSpace } from '../emergency-care-attention-place-space/emergency-care-attention-place-space.component';
import { EmergencyCareAttentionPlaceService } from '../../services/emergency-care-attention-place.service';
import { map, Observable } from 'rxjs';
import { SpaceType } from '../emergency-care-attention-place-sector/emergency-care-attention-place-sector.component';
import { EmergencyCareAttentionPlaceDetailDto, EmergencyCareBedDetailDto, EmergencyCareBedDto, EmergencyCareDoctorsOfficeDetailDto, EmergencyCareDoctorsOfficeDto, EmergencyCareShockRoomDetailDto, ShockroomDto } from '@api-rest/api-model';
import { PlacePreview } from '../emergency-care-change-attention-place-preview-change/emergency-care-change-attention-place-preview-change.component';
import { AttentionPlaceUpdateService } from '../../services/attention-place-update.service';
import { getSpaceState } from '../../utils/space-state';
import { BlockedAttentionPlaceDetails } from '../../standalone/blocked-attention-place-details/blocked-attention-place-details.component';
import { PatientNameService } from '@core/services/patient-name.service';
import { dateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';

@Component({
	selector: 'app-emergency-care-attention-place-details',
	templateUrl: './emergency-care-attention-place-details.component.html',
	styleUrls: ['./emergency-care-attention-place-details.component.scss']
})
export class EmergencyCareAttentionPlaceDetailsComponent implements OnInit {

	@Input() selectedSpace: SelectedSpace;
	details$: Observable<EmergencyCareDetails>;
	spaceTypeDetails: SpaceTypeDetails;
	lastPlacePreview: PlacePreview = this.initializePlacePreview();
	blockedAttentionPlaceDetails: BlockedAttentionPlaceDetails;
	readonly SpaceState = SpaceState;

	constructor(
		private emergencyCareAttentionPlaceService: EmergencyCareAttentionPlaceService,
		private attentionPlaceUpdateService: AttentionPlaceUpdateService,
		private readonly patientNameService: PatientNameService,
	) { }

	ngOnInit() {
		this.loadDetails();
		this.listenToUpdates();
	}

	ngOnChanges(changes: SimpleChanges) {
		if (changes.selectedSpace && !changes.selectedSpace.isFirstChange()) {
			this.loadDetails();
		}
	}

	private initializePlacePreview(): PlacePreview {
		return {
			placeType: null,
			placeTypeDescription: null,
			sectorDescription: null
		};
	}

	loadDetails() {
		const serviceMethodMap = {
			[SpaceType.Beds]: () => this.emergencyCareAttentionPlaceService.getBedDetails(this.selectedSpace.id).pipe(
				map((details: EmergencyCareBedDetailDto) => ({
					bed: this.mapToSpaceTypeDetails(details.bed),
					bedDetailDto: details.bed,
					type: SpaceType.Beds,
					moreDetails: details
				}))
			),
			[SpaceType.DoctorsOffices]: () => this.emergencyCareAttentionPlaceService.getDoctorOfficeDetails(this.selectedSpace.id).pipe(
				map((details: EmergencyCareDoctorsOfficeDetailDto) => ({
					doctorsOffice: this.mapToSpaceTypeDetails(details.doctorsOffice),
					type: SpaceType.DoctorsOffices,
					moreDetails: details
				}))
			),
			[SpaceType.ShockRooms]: () => this.emergencyCareAttentionPlaceService.getShockRoomDetails(this.selectedSpace.id).pipe(
				map((details: EmergencyCareShockRoomDetailDto) => ({
					shockroom: this.mapToSpaceTypeDetails(details.shockroom),
					type: SpaceType.ShockRooms,
					moreDetails: details
				}))
			)
		};
		const loadDetailsMethod = serviceMethodMap[this.selectedSpace.spaceType];
		if (loadDetailsMethod) {
			this.details$ = loadDetailsMethod();
			this.details$.subscribe(details => {
				this.loadSpaceTypeDetails(details);
				this.loadLastPlacePreview(details);
				if (this.spaceTypeDetails.state === SpaceState.BLOCKED)
					this.blockedAttentionPlaceDetails = this.getBlockedAttentionPlaceDetails(details);
			});
		}
	}

	loadSpaceTypeDetails(details: EmergencyCareDetails) {
		this.spaceTypeDetails = details.bed || details.doctorsOffice || details.shockroom;
	}

	loadLastPlacePreview(details: EmergencyCareDetails) {
		this.lastPlacePreview.placeType = this.selectedSpace.spaceType;
		this.lastPlacePreview.placeTypeDescription = details.bed?.description || details.doctorsOffice?.description || details.shockroom?.description;
		this.lastPlacePreview.sectorDescription = details.bed?.sectorDescription || details.doctorsOffice?.sectorDescription || details.shockroom?.sectorDescription;
	}

	private listenToUpdates(){
		this.attentionPlaceUpdateService.update$.subscribe(() => {
			this.loadDetails();
		});
	}

	private mapToSpaceTypeDetails(details: EmergencyCareDoctorsOfficeDto | ShockroomDto | EmergencyCareBedDto): SpaceTypeDetails {
		return {
			...details,
			state: getSpaceState(details),
		}
	}

	private getBlockedAttentionPlaceDetails(details: EmergencyCareDetails): BlockedAttentionPlaceDetails {

		const attentionPlaceStatusInfo = details.moreDetails.status;
		const { firstName, nameSelfDetermination, lastName } = attentionPlaceStatusInfo.blockedBy;
		
		return {
			createdOn: dateTimeDtoToDate(attentionPlaceStatusInfo.createdOn),
			reason: attentionPlaceStatusInfo.reasonEnumDescription,
			observations: attentionPlaceStatusInfo.reason,
			createdBy: this.patientNameService.completeName(firstName, nameSelfDetermination, lastName)
		}
	}
}

export interface EmergencyCareDetails {
	bed?: SpaceTypeDetails;
	doctorsOffice?: SpaceTypeDetails;
	shockroom?: SpaceTypeDetails;
	bedDetailDto?: EmergencyCareBedDto;
	type: SpaceType;
	moreDetails: EmergencyCareAttentionPlaceDetailDto;
}

export interface SpaceTypeDetails {
	id: number;
	description: string;
	state: SpaceState;
	sectorDescription: string;
}

export enum SpaceState {
	AVAILABLE, NOT_AVAILABLE, BLOCKED
}
