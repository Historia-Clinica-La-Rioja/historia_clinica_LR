import { Component, EventEmitter, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { BookingAppointmentDto, BookingDto, BookingPersonDto } from '@api-rest/api-model';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { ButtonType } from '@presentation/components/button/button.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-new-appointment-for-third-party-popup',
	templateUrl: './new-appointment-for-third-party-popup.component.html',
	styleUrls: ['./new-appointment-for-third-party-popup.component.scss']
})
export class NewAppointmentForThirdPartyPopupComponent implements OnInit {

	form: FormGroup<NewAppointmentForThirdPartyForm>;
	submitFormEvent: EventEmitter<void> = new EventEmitter<void>();
	isLoadingRequest = false;
	ButtonType = ButtonType.RAISED;
	constructor(
		@Inject(MAT_DIALOG_DATA) public data: NewAppointmentForThirdPartyPopupData,
		public dialogRef: MatDialogRef<NewAppointmentForThirdPartyPopupComponent>,
		private readonly appointmentsService: AppointmentsService,
		private readonly snackBarService: SnackBarService,
	) { }

	ngOnInit(): void {
		this.createForm();
	}

	save() {
		this.isLoadingRequest = true;
		this.submitFormEvent.emit();
		if (!this.form.valid) {
			this.isLoadingRequest = false;
			return;
		}
		const bookingDto = this.createBookingDto();
		this.appointmentsService.bookAppointment(bookingDto).subscribe( {
			next: () => {
				this.snackBarService.showSuccess("call-center.new-appointment.SUCCESS");
				this.dialogRef.close(true);
			},
			error: () => {
				this.snackBarService.showError("call-center.new-appointment.ERROR");
			}
		})
	}

	private createBookingDto(): BookingDto {
		return {
			appointmentDataEmail: this.form.value.email.email,
			bookingAppointmentDto: this.createBookingAppointmentDto(),
			bookingPersonDto: this.createBookingPersonDto()
		}
	}

	private createBookingAppointmentDto(): BookingAppointmentDto {
		return {
			coverageId: null,
			diaryId: this.data.diaryId,
			openingHoursId: this.data.openingHoursId,
			snomedId: this.data.practiceId,
			specialtyId: this.data.specialtyId,
			day: this.data.day,
			hour: this.data.hour,
			phoneNumber: this.form.value.phone.number,
			phonePrefix: this.form.value.phone.prefix,
		}
	}

	private createBookingPersonDto(): BookingPersonDto {
		return {
			firstName: this.form.value.fullName.name,
			genderId: this.form.value.genderId.genderId,
			idNumber: this.form.value.identificationNumber.identificationNumber,
			lastName: this.form.value.fullName.lastName,
			birthDate: null,
			email: this.form.value.email.email,
			phoneNumber: this.form.value.phone.number,
			phonePrefix: this.form.value.phone.prefix,
		}
	}

	private createForm() {
		this.form = new FormGroup<NewAppointmentForThirdPartyForm>({
			fullName: new FormControl(null),
			identificationNumber: new FormControl(null),
			genderId: new FormControl(null),
			phone: new FormControl(null),
			email: new FormControl(null),
		});
	}

}

export interface NewAppointmentForThirdPartyPopupData {
	diaryId: number;
	openingHoursId: number;
	specialtyId: number;
	day: string;
	hour: string;
	practiceId: number;
}

interface NewAppointmentForThirdPartyForm {
	fullName: FormControl<any>;
	identificationNumber: FormControl<any>;
	genderId: FormControl<any>;
	phone: FormControl<any>;
	email: FormControl<any>;
}