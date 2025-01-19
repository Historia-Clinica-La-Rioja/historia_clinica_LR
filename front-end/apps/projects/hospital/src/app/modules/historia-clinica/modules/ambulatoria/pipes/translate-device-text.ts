import { Pipe, PipeTransform } from "@angular/core";
import { EMunicipalGovernmentDevice } from "@api-rest/api-model";
import { ValueOption } from "../constants/violence-masterdata";

@Pipe({
    name: 'translateDeviceText'
})
export class TranslateDeviceTextPipe implements PipeTransform {

    transform(device: EMunicipalGovernmentDevice, list: ValueOption[]): string {
		return list.find(mdev => mdev.value === device)?.text
	}
}