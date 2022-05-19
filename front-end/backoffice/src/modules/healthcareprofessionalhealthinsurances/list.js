import React from 'react';
import {Datagrid, DeleteButton, FunctionField, List, ReferenceField, TextField} from 'react-admin';
import SubReference from '../components/subreference';
import renderPerson from "../components/renderperson";

const HealthcareProfessionalHealthInsuranceList = props => (
    <List {...props} >
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

