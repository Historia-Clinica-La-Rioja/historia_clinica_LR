import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { HierarchicalUnitDto, HierarchicalUnitStaffDto } from '@api-rest/api-model';
import { HierarchicalUnitStaffService } from '@api-rest/services/hierarchical-unit-staff.service';
import { HierarchicalUnitsService } from '@api-rest/services/hierarchical-units.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-edit-hierarchical-units',
	templateUrl: './edit-hierarchical-units.component.html',
	styleUrls: ['./edit-hierarchical-units.component.scss']
})
export class EditHierarchicalUnitsComponent implements OnInit {

	form: UntypedFormGroup;
	allHU: HierarchicalUnitDto[] = [];
	filteredHU: HierarchicalUnitDto[] = [];

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { userId: number, personHU: HierarchicalUnitStaffDto[], hasRoles: boolean },
		private readonly formBuilder: UntypedFormBuilder,
		private readonly hierarchicalUnitsService: HierarchicalUnitsService,
		private readonly hierarchicalUnitsStaffService: HierarchicalUnitStaffService,
		private readonly dialog: MatDialogRef<EditHierarchicalUnitsComponent>,
		private readonly snackBarService: SnackBarService
	) { }

	ngOnInit() {
		this.form = this.formBuilder.group({
			hierarchicalUnits: new UntypedFormArray([])
		});

		this.hierarchicalUnitsService.getByInstitution().subscribe(allHU => {
			this.allHU = allHU;
			if (this.data.personHU.length > 0)
				this.loadHU();
			else
				if (this.data.hasRoles)
					this.addEmpty();
		});
	}

	get huFormArray(): UntypedFormArray {
		return this.form.get('hierarchicalUnits') as UntypedFormArray;
	}

	createHU(hu: HierarchicalUnitStaffDto): UntypedFormGroup {
		return new UntypedFormGroup({
			hierarchicalUnit: new UntypedFormControl({ value: { id: hu.hierarchicalUnitId, name: hu.hierarchicalUnitAlias }, disabled: !this.data.hasRoles }, Validators.required),
			isResponsible: new UntypedFormControl({ value: hu.responsible, disabled: !this.data.hasRoles })
		});
	}

	addEmpty() {
		this.huFormArray.push(
			new UntypedFormGroup({
				hierarchicalUnit: new UntypedFormControl('', Validators.required),
				isResponsible: new UntypedFormControl(false)
			})
		);
	}

	compareHU(hu1: HierarchicalUnitDto, hu2: HierarchicalUnitDto) {
		return hu1.id === hu2.id;
	}

	save() {
		if (this.form.valid || this.huFormArray.length === 0) {
			const staff = this.mapToHUStaff();
			this.hierarchicalUnitsStaffService.update(this.data.userId, staff).subscribe({
				next: (success) => {
					if (success) {
						this.snackBarService.showSuccess('pacientes.edit_roles.messages.SUCCESS');
						this.dialog.close(success);
					}
				},
				error: () => this.snackBarService.showError('pacientes.edit_hierarchical_units.message.ERROR')
			});
		}
		else {
			this.form.markAllAsTouched();
		}
	}

	filterOptions(value: HierarchicalUnitDto) {
		this.setDisabledOption(value.id, true);
	}

	clear(index: number) {
		this.setDisabledOption(this.huFormArray.at(index).get('hierarchicalUnit').value.id, false)
		this.huFormArray.removeAt(index);
	}

	private loadHU() {
		this.data.personHU.forEach(hu => {
			this.huFormArray.push(this.createHU(hu));
			this.setDisabledOption(hu.hierarchicalUnitId, true);
		});
	}

	private mapToHUStaff(): HierarchicalUnitStaffDto[] {
		return this.huFormArray.controls.map(hu => {
			const alreadyStaff = this.data.personHU.find(personHU => personHU.hierarchicalUnitId === hu.get('hierarchicalUnit').value.id);
			if (alreadyStaff)
				return { id: alreadyStaff.id, responsible: hu.get('isResponsible').value, hierarchicalUnitId: hu.get('hierarchicalUnit').value.id, hierarchicalUnitAlias: hu.get('hierarchicalUnit').value.name }
			else
				return { id: null, responsible: hu.get('isResponsible').value, hierarchicalUnitId: hu.get('hierarchicalUnit').value.id, hierarchicalUnitAlias: hu.get('hierarchicalUnit').value.name }
		});
	}

	private setDisabledOption(id: number, disabled: boolean) {
		this.allHU = this.allHU.map(hu => hu.id === id ? { ...hu, disabled } : hu);
	}
}
