import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BlockAttentionPlaceCommandDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BlockAttentionPlaceService {

  private readonly BASE_BLOCK_URL = 'attention-places/block';
  private readonly BASE_UNBLOCK_URL = 'attention-places/unblock';

  constructor(
    private readonly http: HttpClient,
    private readonly contextService: ContextService,
  ) { }

  blockBed(id: number, blockReason: BlockAttentionPlaceCommandDto): Observable<void> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/${this.BASE_BLOCK_URL}/bed/${id}`;
    return this.http.post<void>(url, blockReason);
  }

  blockShockroom(id: number, blockReason: BlockAttentionPlaceCommandDto): Observable<void> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/${this.BASE_BLOCK_URL}/shockroom/${id}`;
    return this.http.post<void>(url, blockReason);
  }

  blockDoctorsOffice(id: number, blockReason: BlockAttentionPlaceCommandDto): Observable<void> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/${this.BASE_BLOCK_URL}/doctors-office/${id}`;
    return this.http.post<void>(url, blockReason);
  }

  unblockBed(id: number): Observable<void> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/${this.BASE_UNBLOCK_URL}/bed/${id}`;
    return this.http.put<void>(url, {});
  }

  unblockShockroom(id: number): Observable<void> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/${this.BASE_UNBLOCK_URL}/shockroom/${id}`;
    return this.http.put<void>(url, {});
  }

  unblockDoctorsOffice(id: number): Observable<void> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/${this.BASE_UNBLOCK_URL}/doctors-office/${id}`;
    return this.http.put<void>(url, {});
  }
}
