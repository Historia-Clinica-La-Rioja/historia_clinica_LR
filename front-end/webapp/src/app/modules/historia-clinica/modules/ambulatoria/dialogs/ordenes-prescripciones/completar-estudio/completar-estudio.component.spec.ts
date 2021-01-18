import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompletarEstudioComponent } from './completar-estudio.component';

describe('CompletarEstudioComponent', () => {
  let component: CompletarEstudioComponent;
  let fixture: ComponentFixture<CompletarEstudioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CompletarEstudioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompletarEstudioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
