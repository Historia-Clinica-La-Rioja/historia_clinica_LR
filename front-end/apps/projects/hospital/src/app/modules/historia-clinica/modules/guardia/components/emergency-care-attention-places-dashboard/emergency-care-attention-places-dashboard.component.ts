import { Component, Input } from '@angular/core';
import { EmergencyCareAttentionPlaceDto } from '@api-rest/api-model';

@Component({
	selector: 'app-emergency-care-attention-places-dashboard',
	templateUrl: './emergency-care-attention-places-dashboard.component.html',
	styleUrls: ['./emergency-care-attention-places-dashboard.component.scss']
})
export class EmergencyCareAttentionPlacesDashboardComponent {

	_sectors: EmergencyCareAttentionPlaceDto[];

	@Input() set sectors(sectors: EmergencyCareAttentionPlaceDto[]) {
		if (sectors) {
			this._sectors = this.mapSectorAvailability(sectors);
		}
	}

	constructor() { }

	private mapSectorAvailability(sectors: EmergencyCareAttentionPlaceDto[]): EmergencyCareAttentionPlaceDto[] {
		return sectors.map(sector => ({
			...sector,
			hasSpacesCreated:
				sector.doctorsOffices.length > 0 ||
				sector.shockRooms.length > 0 ||
				sector.beds.length > 0
		}));
	}

}
