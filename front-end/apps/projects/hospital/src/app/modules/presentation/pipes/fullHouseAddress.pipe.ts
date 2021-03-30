import { Pipe, PipeTransform } from '@angular/core';
@Pipe({
  name: 'fullHouseAddress'
})
export class FullHouseAddressPipe implements PipeTransform {

  transform(address: Address): string {
	if (!address) {
		return '';
	}

	const streetName = address.street ? address.street : '';
	const streetNumber = address.number ? ' ' + address.number : '';
	const floor = address.floor ? ' ' +  address.floor : '';
	const apartment = address.apartment ? ' ' +  address.apartment : '';
	return streetName + streetNumber + floor + apartment;
  }

}

export class Address {
	street?: string;
	number?: string;
	floor?: string;
	apartment?: string;
}
