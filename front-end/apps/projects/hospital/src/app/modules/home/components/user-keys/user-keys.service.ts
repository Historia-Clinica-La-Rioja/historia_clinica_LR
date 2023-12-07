import { Injectable } from '@angular/core';
import { processErrors } from "@core/utils/form.utils";
import { SnackBarService } from "@presentation/services/snack-bar.service";
import { BehaviorSubject, Observable, throwError } from 'rxjs';

import { ApiErrorMessageDto, GenerateApiKeyDto, GeneratedApiKeyDto, UserApiKeyDto } from '@api-rest/api-model';
import { ApiKeyService } from '@api-rest/services/api-key.service';
import { catchError, tap } from "rxjs/operators";

@Injectable({
    providedIn: 'root'
})
export class UserKeysService {
    list$ = new BehaviorSubject<UserApiKeyDto[]>(undefined);

    constructor(
        private apiKeyService: ApiKeyService,
        private readonly snackBarService: SnackBarService
    ) {
    }

	fetch() {
		this.apiKeyService.list().subscribe(
			list => this.list$.next(list)
		);
	}

	delete(apiKeyName: string) {
		this.apiKeyService.deleteApiKey(apiKeyName).subscribe(
			_ => this.fetch()
		);;
	}

	add(newApiKey: GenerateApiKeyDto): Observable<GeneratedApiKeyDto> {
		return this.apiKeyService.generateApiKey(newApiKey).pipe(
			tap(_ => this.fetch()),
			catchError((error: ApiErrorMessageDto) => {
                processErrors(error, (msg) => this.snackBarService.showError(msg));
                return throwError(() => new Error(error.text));
            })
		);;
	}

}
