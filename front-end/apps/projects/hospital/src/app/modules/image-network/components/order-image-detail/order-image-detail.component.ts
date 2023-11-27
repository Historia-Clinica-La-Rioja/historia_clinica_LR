import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { Color, ColoredLabel } from '@presentation/colored-label/colored-label.component';
import { Position } from '@presentation/components/identifier/identifier.component';
import { IDENTIFIER_CASES } from '../../../hsi-components/identifier-cases/identifier-cases.component';

@Component({
  selector: 'app-order-image-detail',
  templateUrl: './order-image-detail.component.html',
  styleUrls: ['./order-image-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class OrderImageDetailComponent implements OnInit {

  @Input() detailOrden: DetailOrdenImage;

  detail:DetailOrdenImage
  position = Position.ROW
  identifierCases= IDENTIFIER_CASES

  constructor() { }

  ngOnInit(): void {
    this.detail = MOCK_DETAIL_ORDER
  }

}

export interface DetailOrdenImage {
  title: string,
  statusOrder: ColoredLabel,
  orderNumber: number,
  problems: string,
  observations: string,
  professional: string,
  date: string
}

export const MOCK_DETAIL_ORDER:DetailOrdenImage = {
  title: 'Radiogradia de torax',
  statusOrder: {
    description: 'En Sala',
          color:  Color.YELLOW
  },
  orderNumber: null,
  problems: 'Dolor, Pinzamiento Lumbar, otro, buenas tardes',
  observations: 'Esto es un texto que representa el texto de la solicitud de la referencia. Esto es un texto que representa el texto de la solicitud de la referencia.',
  professional: 'ARIEL AREVALOS',
  date: '2023-10-11'
}