import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    FunctionField,
    TextField,
} from 'react-admin';
import SubReference from '../components/subreference';
import renderPerson from '../components/renderperson';

const HealthcareProfessionalHealthInsuranceShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <ReferenceField source="healthcareProfessionalId" reference="healthcareprofessionals" link={false}>
                <SubReference source="personId" reference="person" link={false}>
                    <FunctionField render={renderPerson} />
                </SubReference>
            </ReferenceField>

            <ReferenceField source="healthcareProfessionalId" reference="healthcareprofessionals" >
                <TextField source="licenseNumber" />
            </ReferenceField>

            <ReferenceField source="medicalCoverageId" reference="medicalcoverages" >
                <TextField source="acronym" />
            </ReferenceField>
        </SimpleShowLayout>
    </Show>
);

export default HealthcareProfessionalHealthInsuranceShow;
