import { Injectable } from '@angular/core';
import { OdontologyConceptDto } from '@api-rest/api-model';
import { Observable, ReplaySubject } from 'rxjs';
import { ConceptsService } from '../api-rest/concepts.service';

@Injectable()
export class ConceptsFacadeService {

	constructor(
		private readonly conceptsService: ConceptsService
	) {
		this.loadDiagnostics();
		this.loadProcedures();
	}


	private diagnosticsSubject: ReplaySubject<OdontologyConceptDto[]> = new ReplaySubject(null);
	private diagnostics$: Observable<OdontologyConceptDto[]> = this.diagnosticsSubject.asObservable();

	private proceduresSubject: ReplaySubject<OdontologyConceptDto[]> = new ReplaySubject(null);
	private procedures$: Observable<OdontologyConceptDto[]> = this.proceduresSubject.asObservable();

	getDiagnostics$(): Observable<OdontologyConceptDto[]> {
		return this.diagnostics$;
	}

	getProcedures$(): Observable<OdontologyConceptDto[]> {
		return this.procedures$;
	}


	private loadDiagnostics() {
		this.conceptsService.getDiagnostics().subscribe(
			diagnostics => this.diagnosticsSubject.next(diagnostics)
		);
	}

	private loadProcedures() {
		this.conceptsService.getProcedures().subscribe(
			procedures => this.proceduresSubject.next(procedures)
		);
	}
}
