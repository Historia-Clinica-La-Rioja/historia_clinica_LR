import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    Filter,
    TextInput
} from 'react-admin';

const InstitutionFilter = (props) => (
    <Filter {...props}>
        <TextInput source="name" />
        <TextInput source="sisaCode" />
    </Filter>
);

const InstitutionList = props => (
    <List {...props} filters={<InstitutionFilter />} bulkActionButtons={false}>
        <Datagrid rowClick="show">
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

