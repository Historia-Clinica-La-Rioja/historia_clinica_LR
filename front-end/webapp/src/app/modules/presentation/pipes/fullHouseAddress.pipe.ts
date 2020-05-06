import { Pipe, PipeTransform } from '@angular/core';
import { AddressDto } from '@api-rest/api-model';

@Pipe({
  name: 'fullHouseAddress'
})
export class FullHouseAddressPipe implements PipeTransform {

  transform(address: AddressDto): unknown {
	if(!address)
		return '';
	let street = address.street ? address.street : '';
	let number = address.number ? ' Nro ' + address.number : '';
	let floor = address.floor ? ' ' +  address.floor : '';
	let apartment = address.apartment ? ' ' +  address.apartment : '';
	return street + number + floor + apartment;
  }

}
