import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    ReferenceField,
} from 'react-admin';

const ClinicalSpecialtySectorList = props => (
    <List {...props} >
        <Datagrid rowClick="show">
            <TextField source="description"/>
            <ReferenceField source="sectorId" reference="sectors">
                <TextField source="description" />
            </ReferenceField>
            <ReferenceField source="clinicalSpecialtyId" reference="clinicalspecialties">
                <TextField source="name" />
            </ReferenceField>
        </Datagrid>
    </List>
);

export default ClinicalSpecialtySectorList;

