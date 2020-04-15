import React from 'react';
import {
    List,
    Datagrid,
    TextField,
} from 'react-admin';

const InstitutionList = props => (
    <List {...props} bulkActionButtons={false}>
        <Datagrid rowClick="show">
            <TextField source="id" />
            <TextField source="name" />
            <TextField source="website" />
            <TextField source="phone" />
            <TextField source="email" />
            <TextField source="cuit" />
            <TextField source="sisaCode" />
        </Datagrid>
    </List>
);

export default InstitutionList;

