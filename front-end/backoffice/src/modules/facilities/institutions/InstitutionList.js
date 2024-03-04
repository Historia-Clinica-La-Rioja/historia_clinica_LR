import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    Filter,
    TextInput, 
    ReferenceField
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
            <ReferenceField source="dependencyId" reference="dependencies" link={false}>
                <TextField source="description" />
            </ReferenceField>
            <TextField source="provinceCode" />
        </Datagrid>
    </List>
);

export default InstitutionList;

