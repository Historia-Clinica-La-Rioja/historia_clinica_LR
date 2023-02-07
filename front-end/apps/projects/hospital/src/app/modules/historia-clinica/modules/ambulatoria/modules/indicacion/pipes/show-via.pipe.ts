import { Pipe, PipeTransform } from '@angular/core';
import { MasterDataDto } from '@api-rest/api-model';

@Pipe({
	name: 'showVia'
})
export class ShowViaPipe implements PipeTransform {

	transform(idVia: number, vias: MasterDataDto[]): string {
		return vias.find(v => v.id === idVia)?.description;
	}

}
