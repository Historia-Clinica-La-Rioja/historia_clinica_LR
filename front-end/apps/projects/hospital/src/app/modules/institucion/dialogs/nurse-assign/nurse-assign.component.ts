import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ApiErrorMessageDto, BedInfoDto, PersonDataDto } from '@api-rest/api-model';
import { BedService } from '@api-rest/services/bed.service';
import { UserService } from '@api-rest/services/user.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Observable, map } from 'rxjs';

const NURSE_ROLE: number = 7;

@Component({
	selector: 'app-nurse-assign',
	templateUrl: './nurse-assign.component.html',
	styleUrls: ['./nurse-assign.component.scss']
})
export class NurseAssignComponent implements OnInit {

	nurses$: Observable<TypeaheadOption<PersonDataDto>[]>;
	selectedNurse: PersonDataDto;

	constructor(@Inject(MAT_DIALOG_DATA) public data: {bed: BedInfoDto},
				private readonly userService: UserService,
				private readonly bedService: BedService,
				private readonly snackBarService: SnackBarService,
				private dialogRef: MatDialogRef<NurseAssignComponent>) { }

	ngOnInit(): void {
		this.setNurses();
	}

	private setNurses() {
		this.nurses$ = this.userService.getUsersByInstitutionRoles([NURSE_ROLE])
		.pipe(
			map(toTypeaheadOptionList)
		)

		function toTypeaheadOptionList(nurses: PersonDataDto[]):
		TypeaheadOption<PersonDataDto>[] {
			return nurses.map(toTypeaheadOption);

			function toTypeaheadOption(person: PersonDataDto): TypeaheadOption<PersonDataDto> {
				return {
					compareValue: person.fullName,
					value: person
				};
			}
		}
	}

	setNurse(nurse: PersonDataDto) {
		this.selectedNurse = nurse;
	}

	saveAssignedNurse() {
		this.bedService.updateBedNurse(this.selectedNurse.userId, this.data.bed.bed.id)
		.subscribe({
			next: (_) => {
				this.dialogRef.close(true);
				this.snackBarService.showSuccess('gestion-camas.detail.more-actions.ASSIGN_SUCCESS');
			},
			error: (err: ApiErrorMessageDto) => {
				this.dialogRef.close();
				this.snackBarService.showError(err.text);
			}
		})
	}
}