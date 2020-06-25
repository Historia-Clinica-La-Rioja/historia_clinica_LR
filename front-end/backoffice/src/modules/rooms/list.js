import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    ReferenceField,
    DateField, Filter, ReferenceInput, SelectInput,
} from 'react-admin';

const RoomFilter = props =>(
    <Filter {...props}>
        <ReferenceInput label="Especialidad | Sector" source="id" reference="clinicalspecialtysectors" alwaysOn allowEmpty={false}>
            <SelectInput optionText="description" />
        </ReferenceInput>
    </Filter>
);

const InstitutionList = props => (
    <List {...props} hasCreate={false} filters={<RoomFilter/>}>
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
