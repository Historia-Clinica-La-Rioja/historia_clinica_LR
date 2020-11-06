import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    ReferenceField,
    DateField
} from 'react-admin';

export const PersonList = props => (
    <List {...props}>
        <Datagrid rowClick="show">
            <TextField source="id" />
            <TextField source="firstName" />
            <TextField source="lastName" />
            <ReferenceField source="identificationTypeId" reference="identificationTypes" link={false}>
                <TextField source="description" />
            </ReferenceField>
            <TextField source="identificationNumber" />
            <ReferenceField source="genderId" reference="genders">
                <TextField source="id" />
            </ReferenceField>
            <DateField source="birthDate" />
        </Datagrid>
    </List>
);