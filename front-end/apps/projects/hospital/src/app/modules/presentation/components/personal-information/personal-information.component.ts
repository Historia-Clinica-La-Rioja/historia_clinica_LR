import { Component, EventEmitter, Input, OnChanges, Output } from '@angular/core';
import { AddressDto, HealthInsuranceDto, IdentificationTypeDto, PatientMedicalCoverageDto, PersonFileDto } from '@api-rest/api-model';
import { PersonFileService } from '@api-rest/services/person-file.service';
import { EMedicalCoverageType } from "@pacientes/dialogs/medical-coverage/medical-coverage.component";
import { Address } from '@presentation/pipes/fullHouseAddress.pipe';
@Component({
	selector: 'app-personal-information',
	templateUrl: './personal-information.component.html',
	styleUrls: ['./personal-information.component.scss']
})
export class PersonalInformationComponent implements OnChanges {

	@Input() personalInformation: PersonalInformation;
	@Input() patientMedicalCoverage: PatientMedicalCoverageDto[];
	@Input() showButtonGoToMedicalHistory: boolean;
	@Output() goToMedicalHistory = new EventEmitter<boolean>();
	typeART = EMedicalCoverageType.ART;
	typeObraSocial = EMedicalCoverageType.OBRASOCIAL;
	public addressPresent = false;
	public address: Address;
	files= [];

	constructor(
		private personFileService: PersonFileService
	) {
	}

	ngOnChanges() {
		if (this.personalInformation?.address) {
			this.addressPresent = Object.values(this.personalInformation?.address).find(o => o && typeof (o) !== 'object') ? true : false;
			if (this.addressPresent) {
				this.address = this.mapToAddress(this.personalInformation.address);
			}
		}
	}

	getAcronym(pmc : PatientMedicalCoverageDto){
		const mc = <HealthInsuranceDto> pmc.medicalCoverage;
		return (mc.acronym)? mc.acronym : mc.name;
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

	getMedicalCoveragePlanText(patientMedicalCoverage: PatientMedicalCoverageDto): string {
		return [patientMedicalCoverage?.planName?.toLowerCase(), patientMedicalCoverage?.condition?.toLowerCase()].filter(Boolean).join(' | ');
	}

	downloadFile(fileId: number, fileName: string): void {
		this.personFileService.downloadFile(fileId, this.personalInformation.personId, fileName);
	}
}

export class PersonalInformation {
	identificationNumber: string;
	identificationType: IdentificationTypeDto;
	cuil: string;
	address: AddressDto;
	birthDate: Date;
	email: string;
	phonePrefix: string;
	phoneNumber: string;
	medicalCoverageName: string;
	medicalCoverageAffiliateNumber: string;
	files?: PersonFileDto[];
	personId:number;
	educationLevel: string;
    ethnicity: string;
	occupation: string;
	religion: string;
}
