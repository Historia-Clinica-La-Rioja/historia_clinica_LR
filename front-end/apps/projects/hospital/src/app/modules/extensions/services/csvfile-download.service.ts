import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CSVFileDownloadService {
  private _enabled = false;

  constructor() { }

  set enabledDownload(value: boolean) {
    this._enabled = value;
  }

  get enabledDownload() {
    return this._enabled;
  }

  donwload(): void {
    // To do
  }

}
