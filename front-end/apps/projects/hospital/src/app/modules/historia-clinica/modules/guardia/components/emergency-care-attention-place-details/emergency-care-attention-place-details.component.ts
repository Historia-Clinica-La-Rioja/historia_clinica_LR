import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { SelectedSpace } from '../emergency-care-attention-place-space/emergency-care-attention-place-space.component';
import { EmergencyCareAttentionPlaceService } from '../../services/emergency-care-attention-place.service';
import { map, Observable } from 'rxjs';
import { SpaceType } from '../emergency-care-attention-place-sector/emergency-care-attention-place-sector.component';
import { EmergencyCareAttentionPlaceDetailDto, EmergencyCareBedDetailDto, EmergencyCareDoctorsOfficeDetailDto, EmergencyCareShockRoomDetailDto } from '@api-rest/api-model';
import { PlacePreview } from '../emergency-care-change-attention-place-preview-change/emergency-care-change-attention-place-preview-change.component';
import { AttentionPlaceUpdateService } from '../../services/attention-place-update.service';

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

	constructor(
		private emergencyCareAttentionPlaceService: EmergencyCareAttentionPlaceService,
		private attentionPlaceUpdateService: AttentionPlaceUpdateService
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
					bed: details.bed,
					moreDetails: details
				}))
			),
			[SpaceType.DoctorsOffices]: () => this.emergencyCareAttentionPlaceService.getDoctorOfficeDetails(this.selectedSpace.id).pipe(
				map((details: EmergencyCareDoctorsOfficeDetailDto) => ({
					doctorsOffice: details.doctorsOffice,
					moreDetails: details
				}))
			),
			[SpaceType.ShockRooms]: () => this.emergencyCareAttentionPlaceService.getShockRoomDetails(this.selectedSpace.id).pipe(
				map((details: EmergencyCareShockRoomDetailDto) => ({
					shockroom: details.shockroom,
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
}

interface EmergencyCareDetails {
	bed?: SpaceTypeDetails;
	doctorsOffice?: SpaceTypeDetails;
	shockroom?: SpaceTypeDetails;
	moreDetails: EmergencyCareAttentionPlaceDetailDto;
}

export interface SpaceTypeDetails {
	id: number;
	description: string;
	available: boolean;
	sectorDescription: string;
}
