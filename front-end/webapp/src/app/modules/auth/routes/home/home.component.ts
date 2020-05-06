import { Component, OnInit } from '@angular/core';
import { LoggedUserService } from '../../services/logged-user.service';
import { map, filter, tap } from 'rxjs/operators';
import { RoleAssignment } from '@api-rest/api-model';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

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
	private router: Router,
  ) {
	this.institutions$ = loggedUserService.assignments$.pipe(
		map(
			(roleAssignments: RoleAssignment[]) => roleAssignments
				.filter(roleAssignment => roleAssignment.institutionId !== -1)
				.map(roleAssignment =>({
					id: roleAssignment.institutionId,
					name: `Institución #${roleAssignment.institutionId}`,
				}))
		),
		tap(algo => console.log('tap algo', algo)),
		filter(
			// con filter puedo realizar una acción y detener la notificación a los observadores
			// así evito que parpadee la pantalla (del cargando pasa a la otra pantalla)
			useConditionAndExecute(
				(institucion) => institucion.length === 0,
				() => this.router.navigate(['/auth/profile'])
			),
		),
	);
  }

  ngOnInit(): void {
  }

}
