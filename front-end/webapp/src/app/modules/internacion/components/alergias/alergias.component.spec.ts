import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AlergiasComponent } from './alergias.component';

describe('AlergiasComponent', () => {
  let component: AlergiasComponent;
  let fixture: ComponentFixture<AlergiasComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AlergiasComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AlergiasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
