import { Component, OnInit } from '@angular/core';
import { PROBLEMAS_ACTIVOS, PROBLEMAS_CRONICOS, PROBLEMAS_RESUELTOS } from '../../../../constants/summaries';

@Component({
	selector: 'app-problemas',
	templateUrl: './problemas.component.html',
	styleUrls: ['./problemas.component.scss']
})
export class ProblemasComponent implements OnInit {

	public readonly cronicos = PROBLEMAS_CRONICOS;
	public readonly activos = PROBLEMAS_ACTIVOS;
	public readonly resueltos = PROBLEMAS_RESUELTOS;

	constructor() {
	}

	ngOnInit(): void {
	}

}
