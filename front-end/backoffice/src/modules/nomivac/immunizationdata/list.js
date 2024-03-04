import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    ReferenceField,
    NumberField,
} from 'react-admin';

import {
    SgxDateField,
} from '../../components';

const ImmunizationDataList = props => (
    <List {...props} hasCreate={false} hasEdit={true}>
        <Datagrid rowClick="show">
            <NumberField source="id" label="Identificador vacuna" />
            <TextField source="vaccineSctid" label="Sctid vacuna" />
            <TextField source="vaccinePt" label="Descripción vacuna" />
            <SgxDateField source="administrationDate" label="Fecha de administración" showTime />
            <SgxDateField source="expirationDate" label="Fecha de vencimiento" showTime />
            <TextField source="conditionDescription" label="Condición" />
            <TextField source="schemeDescription" label="Esquema" />
            <TextField source="dose" label="Dosis" />
            <TextField source="doseOrder" label="Número de dosis" />
            <TextField source="lotNumber" label="Lote" />
            <ReferenceField source="institutionId" reference="institutions">
                <TextField source="name" />
            </ReferenceField>
        </Datagrid>
    </List>
);

export default ImmunizationDataList;
