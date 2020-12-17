import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NuevaPrescripcionComponent } from './nueva-prescripcion.component';

describe('NuevaPrescripcionComponent', () => {
  let component: NuevaPrescripcionComponent;
  let fixture: ComponentFixture<NuevaPrescripcionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NuevaPrescripcionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NuevaPrescripcionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
