import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    ReferenceField,
    DeleteButton, 
    Filter,
    TextInput
} from 'react-admin';
import SubReference from '../components/subreference';


import SgxSelectInput from '../../sgxSelectInput/SgxSelectInput';

const ClinicalServiceSectorFilter = props =>(
    <Filter {...props}>
        <TextInput source="description" />
        <SgxSelectInput  source="sectorId" element="sectors" optionText="description" allowEmpty={false} />
        <SgxSelectInput  source="clinicalSpecialtyId" element="clinicalservices" optionText="name" allowEmpty={false} />
    </Filter>
);

const ClinicalServiceSectorList = props => (
    <List {...props} hasCreate={false} filters={<ClinicalServiceSectorFilter />}>
        <Datagrid rowClick="show">
            <TextField source="description"/>
            <ReferenceField source="sectorId" reference="sectors">
                <TextField source="description" />
            </ReferenceField>
            {/*Referencia a la institución del sector*/}
            <ReferenceField source="sectorId" reference="sectors" link={false} label="resources.sectors.fields.institutionId">
                <SubReference source="institutionId" reference="institutions" link={false}>
                    <TextField source="name"/>
                </SubReference>
            </ReferenceField>
            <ReferenceField source="clinicalSpecialtyId" reference="clinicalservices">
                <TextField source="name" />
            </ReferenceField>
            <DeleteButton />
        </Datagrid>
    </List>
);

export default ClinicalServiceSectorList;
