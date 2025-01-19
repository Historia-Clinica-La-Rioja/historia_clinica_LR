import { Component, Input } from '@angular/core';
import { REGISTER_EDITOR_CASES, RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';
import { PresentationModule } from '@presentation/presentation.module';

@Component({
	selector: 'app-blocked-attention-place-details',
	templateUrl: './blocked-attention-place-details.component.html',
	styleUrls: ['./blocked-attention-place-details.component.scss'],
	standalone: true,
	imports: [PresentationModule]
})
export class BlockedAttentionPlaceDetailsComponent {

	readonly REGISTER_EDITOR_DATE_HOUR = REGISTER_EDITOR_CASES.DATE_HOUR;
	registerEditorInfo: RegisterEditor;
	details: BlockedAttentionPlaceDetails;

	@Input() set blockedAttentionPlaceDetails(blockedAttentionPlaceDetails: BlockedAttentionPlaceDetails) {
		if (blockedAttentionPlaceDetails) {
			this.details = blockedAttentionPlaceDetails;
			this.setRegisterEditorInfo(this.details);
		}
	};

	constructor() { }

	private setRegisterEditorInfo(details: BlockedAttentionPlaceDetails) {
		this.registerEditorInfo = {
			createdBy: details.createdBy,
			date: details.createdOn
		}
	}

}

export interface BlockedAttentionPlaceDetails {
	createdOn: Date;
	reason: string;
	observations?: string;
	createdBy: string;
}