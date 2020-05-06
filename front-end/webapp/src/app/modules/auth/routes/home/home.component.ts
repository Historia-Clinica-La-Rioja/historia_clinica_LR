import { Component, OnInit } from '@angular/core';
import { LoggedUserService } from '../../services/logged-user.service';
import { map, filter, tap, switchMap } from 'rxjs/operators';
import { RoleAssignment } from '@api-rest/api-model';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { InstitutionService } from '../../../api-rest/services/institution.service';

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
		map(
			(roleAssignments: RoleAssignment[]) => roleAssignments
				.map(roleAssignment => roleAssignment.institutionId)
				.filter(institutionId => institutionId !== -1)
		),
		switchMap(
			(institutionIds: number[]) => institutionService.getInstitutions(institutionIds)
		),
		filter(
			// con filter puedo realizar una acción y detener la notificación a los observadores
			// así evito que parpadee la pantalla (del cargando pasa a la otra pantalla)
			useConditionAndExecute(
				(instituciones) => instituciones.length === 0,
				() => this.router.navigate(['/auth/profile'])
			),
		),
	);
  }

  ngOnInit(): void {
  }

}
