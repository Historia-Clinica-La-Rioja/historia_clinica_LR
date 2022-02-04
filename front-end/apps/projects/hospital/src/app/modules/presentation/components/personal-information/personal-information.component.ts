import { Component, EventEmitter, Input, OnChanges, Output } from '@angular/core';
import { Router } from '@angular/router';
import { AddressDto, IdentificationTypeDto, PatientMedicalCoverageDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { Address } from '@presentation/pipes/fullHouseAddress.pipe';

@Component({
	selector: 'app-personal-information',
	templateUrl: './personal-information.component.html',
	styleUrls: ['./personal-information.component.scss']
})
export class PersonalInformationComponent implements OnChanges {

	@Input() personalInformation: PersonalInformation;
	@Input() patientMedicalCoverage: PatientMedicalCoverageDto[];
	@Output() goToMedicalHistory = new EventEmitter<boolean>();
	public addressPresent = false;
	public address: Address;
	private readonly routePrefix;

	constructor(
		private readonly contextService: ContextService,
		private readonly router: Router

	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}`;
	}

	ngOnChanges() {
		if (this.personalInformation?.address) {
			this.addressPresent = Object.values(this.personalInformation?.address).find(o => o && typeof (o) !== 'object') ? true : false;
			if (this.addressPresent) {
				this.address = this.mapToAddress(this.personalInformation.address);
			}
		}
	}

	formatPhonePrefixAndNumber() : string{
		return this.personalInformation.phonePrefix
			? this.personalInformation.phonePrefix + "-" + this.personalInformation.phoneNumber
			: this.personalInformation.phoneNumber;
	}
	mapToAddress(addressDto: AddressDto) {
		return {
			street: addressDto.street,
			number: addressDto.number,
			floor: addressDto.floor,
			apartment: addressDto.apartment
		};
	}

	buttonIsPressedGoToMedicalHistory() {
		this.goToMedicalHistory.emit(true);
	}

}

export class PersonalInformation {
	identificationNumber: string;
	identificationType: IdentificationTypeDto;
	cuil: string;
	address: AddressDto;
	birthDate: string;
	email: string;
	phonePrefix: string;
	phoneNumber: string;
	medicalCoverageName: string;
	medicalCoverageAffiliateNumber: string;
}
