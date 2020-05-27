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

const HealthcareProfessionalSpecialtyShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="description"/>
            <ReferenceField source="healthcareProfessionalId" reference="healthcareprofessionals" link={false} label="Persona">
                <SubReference source="personId" reference="people" link={false}>
                    <FunctionField render={renderPerson} />
                </SubReference>
            </ReferenceField>

            <ReferenceField source="healthcareProfessionalId" reference="healthcareprofessionals" >
                <TextField source="licenseNumber" />
            </ReferenceField>
            <ReferenceField source="professionalSpecialtyId" reference="professionalspecialties" >
                <TextField source="description" />
            </ReferenceField>
        </SimpleShowLayout>
    </Show>
);

export default HealthcareProfessionalSpecialtyShow;
