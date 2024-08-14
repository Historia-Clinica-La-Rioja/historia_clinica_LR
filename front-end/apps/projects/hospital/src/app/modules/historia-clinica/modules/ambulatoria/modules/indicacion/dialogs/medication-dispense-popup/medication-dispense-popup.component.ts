import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DoctorInfoDto, DosageInfoDto, MedicationInfoDto } from '@api-rest/api-model';
import { dateDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { PatientNameService } from '@core/services/patient-name.service';
import { PharmarcoDetail } from '@hsi-components/pharmarco-detail/pharmarco-detail.component';
import { ButtonType } from '@presentation/components/button/button.component';
import { RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';

@Component({
	selector: 'app-medication-dispense-popup',
	templateUrl: './medication-dispense-popup.component.html',
	styleUrls: ['./medication-dispense-popup.component.scss']
})
export class MedicationDispensePopupComponent implements OnInit {

	ButtonType = ButtonType;
	pharmaco: PharmarcoDetail;
	problem: string;
	observations: string;
	registerEditor: RegisterEditor;
	isValid = false;

	constructor(@Inject(MAT_DIALOG_DATA) private medicationInfo: MedicationInfoDto,
				private readonly patientNameService: PatientNameService) {}

	ngOnInit(): void {
		this.setPharmaco();
		this.setOtherDetails();
	}

	setIsValidToConfirm = (isValidToConfirm: boolean) => {
		this.isValid = isValidToConfirm;
	}

	private setPharmaco = () => {
		const dosage: DosageInfoDto =  this.medicationInfo.dosage;
		this.pharmaco = {
			pt: this.medicationInfo.snomed.pt,
			unitDose: dosage.dosesByUnit,
			dayDose: dosage.dosesByDay,
			treatmentDays: dosage.duration,
			quantity: dosage.quantityDto.value
		}
	}

	private setOtherDetails = () => {
		this.problem = this.medicationInfo.healthCondition.snomed.pt;
		this.observations = this.medicationInfo.observations;
		this.setRegisterEditor();
	}

	private setRegisterEditor = () => {
		const doctor: DoctorInfoDto = this.medicationInfo.doctor;
		this.registerEditor = {
			createdBy: this.patientNameService.getFullName(doctor.firstName, doctor.nameSelfDetermination, doctor.lastName),
			date: dateDtoToDate(this.medicationInfo.createdOn)
		}
	}
}
