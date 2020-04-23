import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConceptsSearchDialogComponent } from './concepts-search-dialog.component';

describe('ConceptsSearchDialogComponent', () => {
  let component: ConceptsSearchDialogComponent;
  let fixture: ComponentFixture<ConceptsSearchDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConceptsSearchDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConceptsSearchDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
