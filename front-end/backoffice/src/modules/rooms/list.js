import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    ReferenceField,
    DateField, Filter
} from 'react-admin';

import SgxSelectInput from '../../sgxSelectInput/SgxSelectInput';

const RoomFilter = props =>(

    <Filter {...props}>
        <SgxSelectInput label="Especialidad | Sector" source="clinicalSpecialtySectorId" element="clinicalspecialtysectors" optionText="description" allowEmpty={false} />
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
