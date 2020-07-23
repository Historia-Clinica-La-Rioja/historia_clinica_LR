import { Component, ElementRef, OnInit } from '@angular/core';
import { SectorService } from "@api-rest/services/sector.service";
import { ClinicalSpecialtySectorService } from "@api-rest/services/clinical-specialty-sector.service";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { scrollIntoError } from "@core/utils/form.utils";
import { ConfirmDialogComponent } from "@core/dialogs/confirm-dialog/confirm-dialog.component";
import { TranslateService } from "@ngx-translate/core";
import { MatDialog } from "@angular/material/dialog";
import { DoctorsOfficeService } from "@api-rest/services/doctors-office.service";
import { HealthcareProfessionalService } from "@api-rest/services/healthcare-professional.service";
import { ContextService } from "@core/services/context.service";
import { Router } from "@angular/router";
import { APPOINTMENT_DURATIONS } from "../../constants/appointment";

const ROUTE_APPOINTMENT = 'turnos';

@Component({
	selector: 'app-new-agenda',
	templateUrl: './new-agenda.component.html',
	styleUrls: ['./new-agenda.component.scss']
})
export class NewAgendaComponent implements OnInit {

	public form: FormGroup;
	public sectors;
	public specialties;
	public doctorOffices;
	public professionals;
	public appointmentManagement: boolean = false;
	public autoRenew: boolean = false;
	public holidayWork: boolean = false;
	public appointmentDurations;
	private readonly routePrefix;

	constructor(private readonly formBuilder: FormBuilder,
	            private readonly el: ElementRef,
	            private readonly sectorService: SectorService,
	            private readonly clinicalSpecialtySectorService: ClinicalSpecialtySectorService,
	            private translator: TranslateService,
	            private dialog: MatDialog,
	            private doctorsOfficeService: DoctorsOfficeService,
	            private healthcareProfessionalService: HealthcareProfessionalService,
	            private readonly contextService: ContextService,
	            private readonly router: Router) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/`;
	}

	ngOnInit(): void {

		this.form = this.formBuilder.group({
			sectorId: [null, [Validators.required]],
			specialtyId: [{value: null, disabled: true}, [Validators.required]],
			doctorOffice: [{value: null, disabled: true}, [Validators.required]],
			professionalId: [{value: null, disabled: true}, [Validators.required]],
			initDate: [{value: null, disabled: true}, [Validators.required]],
			endDate: [{value: null, disabled: true}, [Validators.required]],
			appointmentDurationId: [{value: null, disabled: true}, [Validators.required]],
		});

		this.sectorService.getAll().subscribe(data => {
			this.sectors = data;
		});

		this.appointmentDurations = APPOINTMENT_DURATIONS;
	}

	setSpecialties(): void {
		let sectorId: number = this.form.controls.sectorId.value;
		this.clinicalSpecialtySectorService.getClinicalSpecialty(sectorId).subscribe(data => {
			this.specialties = data;
		});
		this.enableFormControl('specialtyId');
	}

	setDoctorOffices(): void {
		this.doctorsOfficeService.getDoctorsOffice().subscribe(data => this.doctorOffices = data);
		this.enableFormControl('doctorOffice');
	}

	setProfessionals(): void {
		//TODO para traer doctores por sector utilizar let sectorId: number = this.form.controls.sectorId.value;
		this.healthcareProfessionalService.getAllDoctors().subscribe(data => this.professionals = data);
		this.enableFormControl('professionalId');
	}

	enableAgendaDetails(): void {
		this.enableFormControl('initDate');
		this.enableFormControl('appointmentDurationId');
	}

	appointmentManagementChange(): void {
		this.appointmentManagement = !this.appointmentManagement;
	}

	initDateChange(event): void {
		this.form.controls.endDate.enable();
		//TODO set fecha minima del endDate a partir de initDate
	}

	private enableFormControl(controlName): void {
		this.form.get(controlName).reset();
		this.form.get(controlName).enable();
	}

	endDateChange(event): void {
		//TODO llamar servicio  para disponibilidad de consultorio a partir de las fechas
		//TODO validaciones de fechas
	}

	autoRenewChange(): void {
		this.autoRenew = !this.autoRenew;
	}

	holidayWorkChange(): void {
		this.holidayWork = !this.holidayWork;
	}

	save(): void {
		if (this.form.valid) {
			this.openDialog();
		} else {
			scrollIntoError(this.form, this.el);
		}
	}

	openDialog(): void {
		this.translator.get('turnos.new-agenda.CONFIRM').subscribe((res: string) => {
			const dialogRef = this.dialog.open(ConfirmDialogComponent, {
				width: '450px',
				data: {
					title: 'Nueva agenda',
					content: `${res}`,
					okButtonLabel: 'Confirmar'
				}
			});

			dialogRef.afterClosed().subscribe(result => {
				if (result) {
					//TODO llamar servicio para guardar agenda y redireccionar a turnos, confirmar con snackbar el resultado
					const url = `${this.routePrefix}${ROUTE_APPOINTMENT}`;
					this.router.navigate([url]);
				}
			});
		});
	}

}
