import React from 'react';
import {
    List,
    Datagrid,
    ReferenceField,
    TextField,
    Filter,
    TextInput
} from 'react-admin';

import SgxSelectInput from '../../sgxSelectInput/SgxSelectInput';

const DoctorsOfficeFilter = props =>(
    <Filter {...props}>
        <TextInput source="description" />
        <SgxSelectInput source="institutionId" element="institutions" optionText="name" allowEmpty={false} />
        <SgxSelectInput label="Especialidad | Sector" source="clinicalSpecialtySectorId" element="clinicalspecialtysectors" optionText="description" allowEmpty={false} />
    </Filter>
);


const DoctorsOfficeList = props => (
    <List {...props} hasCreate={false} filters={<DoctorsOfficeFilter />} >
        <Datagrid rowClick="show">

            <TextField source="description" />

            <ReferenceField source="institutionId" reference="institutions">
                <TextField source="name" />
            </ReferenceField>

            <ReferenceField source="clinicalSpecialtySectorId" reference="clinicalspecialtysectors">
                <TextField source="description"/>
            </ReferenceField>

            <TextField source="topic" />
        </Datagrid>
    </List>
);

export default DoctorsOfficeList;

