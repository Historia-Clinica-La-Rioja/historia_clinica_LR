import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    TextField,
    FunctionField,
} from 'react-admin';

import CreateRelatedButton from '../components/CreateRelatedButton';
import SectionTitle from '../components/SectionTitle';
import ProfessionalSpecialtiesSection from "./ProfessionalSpecialtiesSection";

const redirect = (basePath, id, data) => `/person/${data.personId}/show/2`;

const renderPerson = (choice) => `${choice.identificationNumber} ${choice.lastName} ${choice.firstName}`;
const HealthcareProfessionalShow = props => (
    <Show {...props}>
        <SimpleShowLayout redirect={redirect}>
            <TextField source="licenseNumber" />
            <ReferenceField source="personId" reference="person">
                <FunctionField render={renderPerson}/>
            </ReferenceField>

            <SectionTitle label="resources.healthcareprofessionals.fields.healthcareprofessionalspecialties"/>
            <CreateRelatedButton
                reference="healthcareprofessionalspecialties"
                refFieldName="healthcareProfessionalId"
                label="resources.healthcareprofessionalspecialties.createRelated"
            />
            {/*TODO: Aislar esto en un componente. También se usa en show.js*/}
            <ProfessionalSpecialtiesSection/>
        </SimpleShowLayout>
    </Show>
);

export default HealthcareProfessionalShow;
