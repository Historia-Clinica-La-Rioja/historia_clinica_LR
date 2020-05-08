import { TestBed } from '@angular/core/testing';

import { DownloadService } from './download.service';

describe('DownloaderService', () => {
  let service: DownloadService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DownloadService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
