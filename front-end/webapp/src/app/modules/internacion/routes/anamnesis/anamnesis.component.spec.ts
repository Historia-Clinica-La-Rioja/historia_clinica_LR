import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AnamnesisComponent } from './anamnesis.component';

describe('AnamnesisComponent', () => {
  let component: AnamnesisComponent;
  let fixture: ComponentFixture<AnamnesisComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AnamnesisComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AnamnesisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
