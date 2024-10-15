import { Component, Input } from '@angular/core';
import { EmergencyCareAttentionPlaceDto, EmergencyCareBedDto, EmergencyCareDoctorsOfficeDto, ShockroomDto } from '@api-rest/api-model';
import { SpaceTypeDetails } from '../emergency-care-attention-place-details/emergency-care-attention-place-details.component';
import { getSpaceState } from '../../utils/space-state';

const ORGANIZED_BY_SPECIALTY_ID = 1;
@Component({
	selector: 'app-emergency-care-attention-place-sector',
	templateUrl: './emergency-care-attention-place-sector.component.html',
	styleUrls: ['./emergency-care-attention-place-sector.component.scss']
})
export class EmergencyCareAttentionPlaceSectorComponent {

	_sector: EmergencyCareAttentionPlaceDto;
	specialtyDescriptions: string[];
	isOrganizedBySpecialty: boolean;
	spaces: SpaceItem[];

	@Input() set sector(sector: EmergencyCareAttentionPlaceDto) {
		if (sector) {
			this._sector = sector;
			this.isOrganizedBySpecialty = sector.sectorOrganizationId == ORGANIZED_BY_SPECIALTY_ID;
			this.spaces = this.getNonEmptySpaces(sector);
			this.specialtyDescriptions = this.getSpecialtyDescriptions(sector);
		}
	}

	constructor() { }

	private getNonEmptySpaces(sector: EmergencyCareAttentionPlaceDto): SpaceItem[] {
		return [
			{ type: SpaceType.DoctorsOffices, items: this.mapToSpaceTypeDetails(sector.doctorsOffices) },
			{ type: SpaceType.ShockRooms, items: this.mapToSpaceTypeDetails(sector.shockRooms) },
			{ type: SpaceType.Beds, items: this.mapToSpaceTypeDetails(sector.beds) }
		].filter(space => space.items?.length);
	}

	private getSpecialtyDescriptions(sector: EmergencyCareAttentionPlaceDto): string[] {
		return sector.clinicalSpecialtySectors?.length > 0
			? sector.clinicalSpecialtySectors.map(specialty => specialty.description)
			: [];
	}

	private mapToSpaceTypeDetails(details: EmergencyCareDoctorsOfficeDto[] | ShockroomDto[] | EmergencyCareBedDto[]): SpaceTypeDetails[] {
		return details.map(detail => {
			return {
				...detail,
				state: getSpaceState(detail)
			}
		});
	}
}

export enum SpaceType {
	DoctorsOffices = 'Consultorios',
	ShockRooms = 'Shockrooms',
	Beds = 'Habitaciones'
}

export interface SpaceItem {
	type: SpaceType;
	items: SpaceTypeDetails[];
}
