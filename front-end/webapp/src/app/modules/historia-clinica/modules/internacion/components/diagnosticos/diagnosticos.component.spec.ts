import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DiagnosticosComponent } from './diagnosticos.component';

describe('DiagnosticosComponent', () => {
  let component: DiagnosticosComponent;
  let fixture: ComponentFixture<DiagnosticosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DiagnosticosComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiagnosticosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
