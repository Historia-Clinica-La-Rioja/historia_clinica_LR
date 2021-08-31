import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    DateField,
    NumberField
} from 'react-admin';

const ImmunizationSyncList = props => (
    <List {...props} hasCreate={false} hasEdit={true}>
        <Datagrid rowClick="show">
            <TextField source="id" label="Id vacuna" />
            <DateField source="synchronizedDate" label="Fecha de Sincronización" showTime />
            <NumberField source="priority" label="Prioridad" />
            <TextField source="externalId" label="Id Nomivac" />
            <TextField source="statusCode" label="Estado de sincronización" />
        </Datagrid>
    </List>
);

export default ImmunizationSyncList;
