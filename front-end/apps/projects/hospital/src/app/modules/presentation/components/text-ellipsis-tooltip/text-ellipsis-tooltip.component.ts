import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-text-ellipsis-tooltip',
	templateUrl: './text-ellipsis-tooltip.component.html',
	styleUrls: ['./text-ellipsis-tooltip.component.scss']
})
export class TextEllipsisTooltipComponent {

	private _limit: number;
	_text: string;
	truncateText: string;
	showTooltip = false;
	
	@Input()
	set text(text: string) {
		this._text = text;
		this.updateTruncateText();
	}
	
	@Input()
	set limit(limit: number) {
		this._limit = limit || 0;
		this.updateTruncateText();
	}
	
	private updateTruncateText() {
		if (this._text.length > this._limit && this._limit > 0) {
			this.truncateText = `${this._text.substring(0, this._limit)}...`;
			this.showTooltip = true;
		} else {
			this.truncateText = this._text;
			this.showTooltip = false;
		}
	}
}
