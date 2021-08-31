import React from 'react';
import {
    TextInput,
    Edit,
    SimpleForm,
    required,
} from 'react-admin';

import PersonReferenceInput from '../person/PersonReferenceInput';
import CreateRelatedButton from '../components/CreateRelatedButton';
import SectionTitle from '../components/SectionTitle';
import CustomToolbar from "../../modules/components/CustomToolbar";
import ProfessionalSpecialtiesSection from "./ProfessionalSpecialtiesSection";

const redirect = (basePath, id, data) => `/person/${data.personId}/show/2`;

const HealthcareProfessionalEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect={redirect} toolbar={<CustomToolbar isEdit={true}/>}>
            <PersonReferenceInput source="personId" validate={[required()]} />
            <TextInput source="licenseNumber" validate={[required()]} />

            <SectionTitle label="resources.healthcareprofessionals.fields.healthcareprofessionalspecialties"/>
            <CreateRelatedButton
                reference="healthcareprofessionalspecialties"
                refFieldName="healthcareProfessionalId"
                label="resources.healthcareprofessionalspecialties.createRelated"
            />
            <ProfessionalSpecialtiesSection/>

        </SimpleForm>
    </Edit>
);

export default HealthcareProfessionalEdit;
