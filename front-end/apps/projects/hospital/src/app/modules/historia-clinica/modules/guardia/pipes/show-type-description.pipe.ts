import { Pipe, PipeTransform } from '@angular/core';
import { MasterDataInterface } from '@api-rest/api-model';

@Pipe({
	name: 'showTypeDescription'
})
export class ShowTypeDescriptionPipe implements PipeTransform {

	transform(typeId: string, types: MasterDataInterface<number>[]): string {
		if (!typeId || !types.length)
			return "";

		const type = types.find(type => type.id === Number(typeId));
		return !!type ? type.description : "";
	}

}
