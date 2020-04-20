import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    DateField, TextInput
} from 'react-admin';

const InstitutionList = props => (
    <List {...props} bulkActionButtons={false}>
        <Datagrid rowClick="show">
            <TextField source="id" />
            <TextInput source="roomNumber" />
            <TextField source="description" />
            <TextField source="type" />
            <TextField source="sectorId" />
            <DateField source="dischargeDate" />
        </Datagrid>
    </List>
);

export default InstitutionList;