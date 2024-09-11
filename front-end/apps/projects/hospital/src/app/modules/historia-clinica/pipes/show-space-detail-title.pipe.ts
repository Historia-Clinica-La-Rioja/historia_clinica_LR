import { Pipe, PipeTransform } from '@angular/core';
import { SpaceType } from '@historia-clinica/modules/guardia/components/emergency-care-attention-place-sector/emergency-care-attention-place-sector.component';

@Pipe({
  name: 'showSpaceDetailTitle'
})
export class ShowSpaceDetailTitlePipe implements PipeTransform {

	transform(space: SpaceType): string {
		const spaceTypeMapping = {
			[SpaceType.DoctorsOffices]: 'Consultorio',
			[SpaceType.ShockRooms]: 'Shockroom',
			[SpaceType.Beds]: 'Habitación'
		};

		return space ? spaceTypeMapping[space] : '';
	}
}
