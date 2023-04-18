import { Component, OnInit } from '@angular/core';
import { PublicService } from '@api-rest/services/public.service';
import { environment } from '@environments/environment';

const shortHash = (fullHash: string) => fullHash ? fullHash.substring(0,8) : fullHash;

const buildCommitHashText = (apiHash: string, appHash: string) => {
	if (apiHash === appHash) {
		return `APP/API ${shortHash(apiHash)}`;
	}
	return `APP ${shortHash(appHash)} / API ${shortHash(apiHash)}`;
}

@Component({
	selector: 'app-footer',
	templateUrl: './footer.component.html',
	styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {
	applicationVersionNumber: string;
	commitHashText: string;

	constructor(
		private publicService: PublicService,
	) {

	}

	ngOnInit(): void {
		this.publicService.getApplicationCurrentVersion()
			.subscribe(versionDto => {
				this.commitHashText = buildCommitHashText(environment.commitHash, versionDto.commitId);
				this.applicationVersionNumber = versionDto.version;
			});

	}

}
