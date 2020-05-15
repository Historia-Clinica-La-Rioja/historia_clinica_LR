import { Component, OnInit, Input } from '@angular/core';
import { newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';

@Component({
	selector: 'app-effective-time',
	templateUrl: './effective-time.component.html',
	styleUrls: ['./effective-time.component.scss']
})
export class EffectiveTimeComponent implements OnInit {

	@Input('model')
	dateObj: Moment = newMoment();

	constructor() { }

	ngOnInit(): void {}

}
