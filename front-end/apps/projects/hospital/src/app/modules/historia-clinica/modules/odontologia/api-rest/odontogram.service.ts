import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { environment } from '@environments/environment';
import { OdontogramQuadrantDto } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class OdontogramService {

	constructor(
		private readonly http: HttpClient,
	) { }

	getOdontogram(): Observable<OdontogramQuadrantDto[]> {
		const url = `${environment.apiBase}/odontology/odontogram`;
		return this.http.get<OdontogramQuadrantDto[]>(url);

	}

	getSurfaces(): Observable<any> {
		return of(
			{
				// ejemplo de diente superior anterior derecho ej. 14
				right: {
					sctid: 64881354351,
					pt: "cara distal de pieza dentaria [como un todo] (estructura corporal)"
				},
				left: {
					sctid: 658813411,
					pt: "cara mesial de pieza dentaria [como un todo] (estructura corporal)"
				},
				external: {
					sctid: 116881435,
					pt: "cara vesticular de pieza dentaria [como un todo] (estructura corporal)"
				},
				internal: {
					sctid: 884613511,
					pt: "cara palatina de pieza dentaria [como un todo] (estructura corporal)"
				},
				central: {
					sctid: 135468811,
					pt: "borde incisal de pieza dentaria [como un todo] (estructura corporal)",
				}
			}

		);
	}
}
