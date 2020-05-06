import { TestBed } from '@angular/core/testing';

import { EvolutionNoteService } from './evolution-note.service';

describe('EvolutionNoteService', () => {
  let service: EvolutionNoteService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EvolutionNoteService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
