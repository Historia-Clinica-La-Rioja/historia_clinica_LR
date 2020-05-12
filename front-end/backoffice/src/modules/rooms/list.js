import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    ReferenceField,
    DateField,
} from 'react-admin';

const InstitutionList = props => (
    <List {...props} hasCreate={false} >
        <Datagrid rowClick="show">
            <TextField source="roomNumber"/>
            <TextField source="description" />
            <TextField source="type" />
            <DateField source="dischargeDate" />
            <ReferenceField source="clinicalSpecialtySectorId" reference="clinicalspecialtysectors">
                <TextField source="description"/>
            </ReferenceField>
        </Datagrid>
    </List>
);

export default InstitutionList;
