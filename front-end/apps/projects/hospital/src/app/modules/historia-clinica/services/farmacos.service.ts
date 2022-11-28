import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FarmacosService {

  constructor(
    private readonly http: HttpClient
  ) {}

  getPharmacos(params) {
    const url = `${environment.apiBase}/vademecum/concepts`;
    return this.http.get<any>(url, {params});
  }
}
