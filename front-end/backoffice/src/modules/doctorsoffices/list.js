import React from 'react';
import {
    List,
    Datagrid,
    ReferenceField,
    TextField,
    Filter,
} from 'react-admin';

import SgxSelectInput from '../../sgxSelectInput/SgxSelectInput';

const DoctorsOfficeFilter = props =>(
    <Filter {...props}>
        <SgxSelectInput source="institutionId" element="institutions" optionText="name" allowEmpty={false} />
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

            <TextField
                source="openingTime"
                label="Horario de apertura"
                type="time"
            />
            <TextField
                source="closingTime"
                label="Horario de cierre"
                type="time"
            />
        </Datagrid>
    </List>
);

export default DoctorsOfficeList;

