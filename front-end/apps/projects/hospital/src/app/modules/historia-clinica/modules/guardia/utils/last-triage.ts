import { TriageListDto } from "@api-rest/api-model";
import { TriageService } from "@api-rest/services/triage.service";
import { getElementAtPosition } from "@core/utils/array.utils";
import { Observable, map } from "rxjs";

export class LastTriage {

    lastTriage$: Observable<TriageListDto>;

    constructor(
        protected triageService: TriageService,
        protected episodeId: number,
    ) {
        this.lastTriage$ = this.triageService.getAll(this.episodeId).pipe(map((triages: TriageListDto[]) => getElementAtPosition<TriageListDto>(triages, 0)))
    }

}
