import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentsSummaryComponent } from './documents-summary.component';

describe('DocumentsSummaryComponent', () => {
  let component: DocumentsSummaryComponent;
  let fixture: ComponentFixture<DocumentsSummaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DocumentsSummaryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentsSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
