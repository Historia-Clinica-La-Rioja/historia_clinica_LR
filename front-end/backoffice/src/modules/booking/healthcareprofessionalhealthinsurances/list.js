import React from 'react';
import {Datagrid, DeleteButton, FunctionField, List, ReferenceField, TextField, Filter, TextInput} from 'react-admin';
import SubReference from '../../components/subreference';
import renderPerson from '../../components/renderperson';
import { SgxSelectInput } from '../../components';

const HealthcareProfessionalHealthInsuranceListFilter = (props) => (
    <Filter {...props}>
        <SgxSelectInput source="medicalCoverageId" element="medicalcoverages" optionText="name" allowEmpty={false} />
        <TextInput source="firstName" element="person" label="Nombre"></TextInput>
        <TextInput source="lastName" element="person" label="Apellido"></TextInput>
    </Filter>
);

const HealthcareProfessionalHealthInsuranceList = props => (
    <List {...props} filters={<HealthcareProfessionalHealthInsuranceListFilter />}>
        <Datagrid rowClick="show">
            <ReferenceField source="healthcareProfessionalId" reference="healthcareprofessionals" link={false} >
                <SubReference source="personId" reference="person" link={false}>
                    <FunctionField render={renderPerson} />
                </SubReference>
            </ReferenceField>
            <ReferenceField source="medicalCoverageId" reference="medicalcoverages">
                <TextField source="name" />
            </ReferenceField>
            <DeleteButton />
        </Datagrid>
    </List>
);

export default HealthcareProfessionalHealthInsuranceList;

