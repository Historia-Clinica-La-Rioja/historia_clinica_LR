import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ApiErrorDto, DoctorInfoDto, DosageInfoDto, MedicationInfoDto } from '@api-rest/api-model';
import { dateDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { MedicationStatementInstitutionalSupplyService } from '@api-rest/services/medication-statement-institutional-supply.service';
import { PatientNameService } from '@core/services/patient-name.service';
import { PharmarcoDetail } from '@hsi-components/pharmarco-detail/pharmarco-detail.component';
import { ButtonType } from '@presentation/components/button/button.component';
import { RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';
import { MedicationToDispenseService } from '../../services/medication-to-dispense.service';
import { finalize } from 'rxjs';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { processErrors } from '@core/utils/form.utils';

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
	isLoading = false;

	constructor(@Inject(MAT_DIALOG_DATA) public medicationInfo: MedicationInfoDto,
				private readonly patientNameService: PatientNameService,
				private readonly medicationStatementInstitutionalSupplyService: MedicationStatementInstitutionalSupplyService,
				private readonly medicationToDispenseService: MedicationToDispenseService,
				private readonly snackBarService: SnackBarService) {}

	ngOnInit(): void {
		this.setPharmaco();
		this.setOtherDetails();
	}

	setIsValidToConfirm = (isValidToConfirm: boolean) => {
		this.isValid = isValidToConfirm;
	}

	confirm = () => {
		this.isLoading = true;
		this.medicationStatementInstitutionalSupplyService.save(this.medicationToDispenseService.saveMedicationStatement)
			.pipe(finalize(() => this.isLoading = false))
			.subscribe({
				next: (_) => this.snackBarService.showSuccess('ambulatoria.paciente.ordenes_prescripciones.menu_items.dispense.dialog.SUCCESS'),
				error: (error: ApiErrorDto) => processErrors(error, (msg) => this.snackBarService.showError(msg))
			})
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
