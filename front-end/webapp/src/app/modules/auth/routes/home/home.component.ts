import { Component, OnInit } from '@angular/core';
import { LoggedUserService } from '../../services/logged-user.service';
import { map, filter, tap, switchMap } from 'rxjs/operators';
import { RoleAssignment, InstitutionDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { InstitutionService } from '../../../api-rest/services/institution.service';


const roleAssignmentsToIntitutionId = (roleAssignments: RoleAssignment[]) => roleAssignments
	.map(roleAssignment => roleAssignment.institutionId)
	.filter(institutionId => institutionId !== -1);

//	si se cumple la condicion, retorna false y ejecuta
const useConditionAndExecute = ( condition: (items) => boolean, command: (items) => void ) =>
	// el parametro del filter
	(eventData: any[]): boolean => {
		if ( condition(eventData) ) {
			command(eventData);
			return false;
		}
		return true;
	}


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
	institutions$: Observable<{ id: number, name: string }[]>;

  constructor(
	loggedUserService: LoggedUserService,
	institutionService: InstitutionService,
	private router: Router,
  ) {
	this.institutions$ = loggedUserService.assignments$.pipe(
		// roleAssignments => intitutionIds
		map(
			(roleAssignments: RoleAssignment[]) => roleAssignmentsToIntitutionId(roleAssignments)
		),
		// intitutionIds => institutionDtos
		switchMap(
			(institutionIds: number[]) => institutionService.getInstitutions(institutionIds)
		),
		// agrego filtros para poder redirigir al usuario si hay una institución sin resolver el observable

		// si institutionDtos.length == 0 => redirijo a su perfil (que vea sus roles)
		filter(
			useConditionAndExecute(
				(institutionDtos: InstitutionDto[]) => institutionDtos.length === 0,
				() => this.router.navigate(['/auth/profile'])
			),
		),
		// si institutionDtos.length == 1 => redirijo a la institucion
		filter(
			useConditionAndExecute(
				(institutionDtos: InstitutionDto[]) => institutionDtos.length === 1,
				(institutionDtos: InstitutionDto[]) => this.router.navigate(['/institucion', institutionDtos[0].id])
			),
		),
	);
  }

  ngOnInit(): void {
  }

  ingresar(institutionDto: InstitutionDto): void {
	this.router.navigate(['/institucion', institutionDto.id])
  }
}
