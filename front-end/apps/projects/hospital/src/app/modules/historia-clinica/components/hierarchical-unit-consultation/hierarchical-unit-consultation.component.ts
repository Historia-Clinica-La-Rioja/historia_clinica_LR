import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { HierarchicalUnitDto } from '@api-rest/api-model';
import { AccountService } from '@api-rest/services/account.service';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { HierarchicalUnitsService } from '@api-rest/services/hierarchical-units.service';
import { HierarchicalUnitService } from '@historia-clinica/services/hierarchical-unit.service';
import { forkJoin, switchMap, tap } from 'rxjs';

@Component({
	selector: 'app-hierarchical-unit-consultation',
	templateUrl: './hierarchical-unit-consultation.component.html',
	styleUrls: ['./hierarchical-unit-consultation.component.scss'],
})
export class HierarchicalUnitConsultationComponent implements OnInit {

	form: FormGroup;
	huToShow: HierarchicalUnitDto[] = [];

	private userId: number;
	private huByInstitution: HierarchicalUnitDto[] = [];
	private huByUser: HierarchicalUnitDto[] = [];

	@Input() patientId: number;
	@Output() response: EventEmitter<number> = new EventEmitter<number>();

	constructor(
		private readonly hierarchicalUnitService: HierarchicalUnitsService,
		private readonly accountService: AccountService,
		private readonly appointmentService: AppointmentsService,
		readonly hierarchicalUnitformService: HierarchicalUnitService
	) { }

	ngOnInit(): void {

		this.form = this.hierarchicalUnitformService.getForm();
		this.response.emit(this.form.value.hierarchicalUnitId);

		this.accountService.getInfo().pipe(
			tap(userInfo => this.userId = userInfo.id),
			switchMap(() =>
				this.appointmentService.hasCurrentAppointment(this.patientId).pipe(
					switchMap(hasAppointment => {
						if (hasAppointment)
							return this.appointmentService.getCurrentAppointmentHierarchicalUnit(this.patientId);
						else {
							const huByInstitution$ = this.hierarchicalUnitService.getByInstitution();
							const huByUser$ = this.hierarchicalUnitService.fetchAllByUserIdAndInstitutionId(this.userId);
							return forkJoin([huByInstitution$, huByUser$]);
						}
					})
				)
			)
		).subscribe((huInformation: HierarchicalUnitDto | [HierarchicalUnitDto[], HierarchicalUnitDto[]]) => {
			if (Array.isArray(huInformation))
				this.setHierarchicalUnits(huInformation);
			else
				this.setHierarchicalAppointment(huInformation);
		});
	}

	changeHUToShow() {
		this.form.controls.hierarchicalUnitId.setValue(null);
		if (this.form.value.isAReplacement)
			this.huToShow = this.huByInstitution;
		else
			this.huToShow = this.huByUser;
	}

	setHierarchicalUnitId() {
		this.response.emit(this.form.value.hierarchicalUnitId);
	}

	private setHierarchicalAppointment(huDto: HierarchicalUnitDto) {
		this.huToShow = [huDto];
		this.form.get('hierarchicalUnitId').patchValue(huDto?.id);
		this.form.get('hierarchicalUnitId').disable();
		this.form.get('isAReplacement').disable();
		this.setHierarchicalUnitId();
	}

	private setHierarchicalUnits(hus: [HierarchicalUnitDto[], HierarchicalUnitDto[]]) {
		this.huByInstitution = hus[0];
		this.huByUser = hus[1];
		this.huToShow = this.huByUser;
	}

}
