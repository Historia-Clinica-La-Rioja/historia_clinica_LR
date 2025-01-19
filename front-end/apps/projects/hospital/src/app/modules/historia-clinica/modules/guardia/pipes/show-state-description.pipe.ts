import { Pipe, PipeTransform } from '@angular/core';
import { MasterDataDto } from '@api-rest/api-model';

@Pipe({
  name: 'showStateDescription'
})
export class ShowStateDescriptionPipe implements PipeTransform {

  transform(stateId: string, states: MasterDataDto[]): string {
		if (!stateId || !states.length)
			return "";
		const state = states.find(state => state.id === Number(stateId));
		return !!state ? state.description : "";
	}

}
