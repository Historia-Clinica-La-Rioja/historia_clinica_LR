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
const ProfessionalProfessionShow = props => (
    <Show {...props}>
        <SimpleShowLayout redirect={redirect}>
            <ReferenceField source="personId" reference="person">
                <FunctionField render={renderPerson}/>
            </ReferenceField>
            <ReferenceField source="professionalSpecialtyId" reference="professionalspecialties" link={false}>
                <TextField source="description" />
            </ReferenceField>

            <SectionTitle label="resources.professionalprofessions.fields.healthcareprofessionalspecialties"/>
            <CreateRelatedButton
                reference="healthcareprofessionalspecialties"
                refFieldName="professionProfessionalId"
                label="resources.healthcareprofessionals.buttons.linkSpecialities"
            />
            <ProfessionalSpecialtiesSection/>
        </SimpleShowLayout>
    </Show>
);

export default ProfessionalProfessionShow;
