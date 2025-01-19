import { Pipe, PipeTransform } from '@angular/core';
import { SpaceType } from '@historia-clinica/modules/guardia/components/emergency-care-attention-place-sector/emergency-care-attention-place-sector.component';
import { TranslateService } from '@ngx-translate/core';

@Pipe({
  name: 'showSpaceDetailTitle'
})
export class ShowSpaceDetailTitlePipe implements PipeTransform {

	constructor(private readonly translateService: TranslateService) {}

	transform(space: SpaceType): string {
		const spaceTypeMapping = {
			[SpaceType.DoctorsOffices]: this.translateService.instant('guardia.home.attention_places.space.DOCTOR_OFFICE'),
			[SpaceType.ShockRooms]: this.translateService.instant('guardia.home.attention_places.space.SHOCKROOM'),
			[SpaceType.Beds]: this.translateService.instant('guardia.home.attention_places.space.BED')
		};

		return space ? spaceTypeMapping[space] : '';
	}
}
