import { Pipe, PipeTransform } from '@angular/core';
@Pipe({
  name: 'fullHouseAddress'
})
export class FullHouseAddressPipe implements PipeTransform {

  transform(address: Address): string {
	if (!address) {
		return '';
	}

	const street = address.street ? address.street : '';
	const number = address.number ? ' Nro ' + address.number : '';
	const floor = address.floor ? ' ' +  address.floor : '';
	const apartment = address.apartment ? ' ' +  address.apartment : '';
	return street + number + floor + apartment;
  }

}

export class Address {
	street?: string;
	number?: string;
	floor?: string;
	apartment?: string;
}
