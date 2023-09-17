import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemSummaryComponent } from './item-summary.component';

describe('PatientAvatarSummaryComponent', () => {
  let component: ItemSummaryComponent;
  let fixture: ComponentFixture<ItemSummaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ItemSummaryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ItemSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
