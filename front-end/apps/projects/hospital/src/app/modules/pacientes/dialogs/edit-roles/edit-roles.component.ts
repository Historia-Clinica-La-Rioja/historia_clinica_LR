import { Component, OnInit, Inject } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { RoleDto, UserRoleDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';

const ID_ROLES_DO_NOT_REQUIRE_PROFESSIONS
	= [
		5, 9
	];
@Component({
	selector: 'app-edit-roles',
	templateUrl: './edit-roles.component.html',
	styleUrls: ['./edit-roles.component.scss']
})
export class EditRolesComponent implements OnInit {
	public formParent: FormGroup = new FormGroup({});
	public roles: RoleDto[] = [];

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { personId: number; professionalId: number; roles: RoleDto[]; userId: number; rolesByUser: UserRoleDto[] },
		private contextService: ContextService,
		public dialog: MatDialogRef<EditRolesComponent>

	) {

	}

	ngOnInit(): void {
		this.roles = this.data.roles;
		this.initFormParent();
		this.initFormRole();
		this.addOwnRoles(this.data.rolesByUser);
	}

	initOwnFormRoles(elem: UserRoleDto): FormGroup {
		return new FormGroup({
			institutionId: new FormControl(elem.institutionId),
			roleDescription: new FormControl(elem.roleDescription),
			roleId: new FormControl(elem.roleId),
			userId: new FormControl(elem.userId)
		});
	}

	addOwnRoles(userRoles: UserRoleDto[]): void {
		const refRoles = this.formParent.get('roles') as FormArray;
		if (userRoles.length === 0)
			this.addNewRole();
		else
			userRoles.forEach((elem: UserRoleDto) => {
				refRoles.push(this.initOwnFormRoles(elem));
			});


	}

	initFormParent(): void {
		this.formParent = new FormGroup({
			roles: new FormArray([], [Validators.required])
		});
	}
	addNewRole(): void {
		const refRoles = this.formParent.get('roles') as FormArray;
		refRoles.push(this.initFormRole());
	}

	initFormRole(): FormGroup {
		return new FormGroup({
			institutionId: new FormControl(this.contextService.institutionId),
			roleDescription: new FormControl(null),
			roleId: new FormControl(null, [Validators.pattern(/^([5|9])+$/)]),
			userId: new FormControl(this.data.userId)
		});

	}


	removeValidation(key: string, index: number,): void {
		const refRoles = this.formParent.get('roles') as FormArray;
		const refSingle = refRoles.at(index).get(key) as FormGroup;

		refSingle.clearValidators();
		refSingle.updateValueAndValidity();
	}
	getCtrl(key: string, form: FormGroup): any {
		return form.get(key);
	}
	isDisableConfirmButton(): boolean {
		const refRoles = this.formParent.get('roles') as FormArray;
		const refArray = refRoles.controls;
		return (this.isDisabledAddRole() || refArray.length < 1);
	}
	isDisabledAddRole(): boolean {
		const refRoles = this.formParent.get('roles') as FormArray;
		const refArray = refRoles.controls;
		const i = refArray.length - 1;
		return (refArray[i]) ? !(refArray[i].value.roleId) : false;
	}
	clear(i: number): void {
		const refRoles = this.formParent.get('roles') as FormArray;
		refRoles.removeAt(i);
	}
	hadValue(i: number): boolean {
		const refRoles = this.formParent.get('roles') as FormArray;
		return (refRoles.at(i).value.roleId != null) ? true : false;
	}
	hasError(type: string, control: string, index: number): boolean {
		const refRoles = this.formParent.get('roles') as FormArray;
		const refSingle = refRoles.at(index).get(control) as FormGroup;
		return (refSingle.hasError(type));
	}
	isProfessional(control: string, index: number): boolean {
		if (this.data.professionalId != undefined) {
			this.removeValidation(control, index);
			return true;
		} else
			return false;
	}
	save(): void {
		const refRoles = this.formParent.get('roles') as FormArray;
		const userRoles: UserRoleDto[] = refRoles.value;
		this.dialog.close(userRoles);
	}

}
