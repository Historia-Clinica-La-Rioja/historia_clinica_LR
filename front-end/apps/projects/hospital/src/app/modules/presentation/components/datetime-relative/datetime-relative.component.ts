import { Component, Input, OnInit } from '@angular/core';
import { dateTimeDtoToStringDate } from '@api-rest/mapper/date-dto.mapper';

import { formatDistanceToNow } from 'date-fns';
import esLocale from 'date-fns/locale/es';

@Component({
	selector: 'app-datetime-relative',
	templateUrl: './datetime-relative.component.html',
	styleUrls: ['./datetime-relative.component.scss']
})
export class DatetimeRelativeComponent implements OnInit {
	@Input() datetime: DateTimeDto;

	formattedRelativeTime: string;
	tooltipText: string;

	constructor() { }

	ngOnInit(): void {
		// const date = dateTimeDtoToDate(this.datetime);

		this.tooltipText = dateTimeDtoToStringDate(this.datetime);
		this.formattedRelativeTime = formatDistanceToNow(new Date(this.tooltipText), { addSuffix: true, locale: esLocale });
	}

}


export interface DateTimeDto{
    date: DateDto;
    time: TimeDto;
}

export interface TimeDto {
    hours: number;
    minutes: number;
    seconds?: number;
}

export interface DateDto {
    day: number;
    month: number;
    year: number;
}
