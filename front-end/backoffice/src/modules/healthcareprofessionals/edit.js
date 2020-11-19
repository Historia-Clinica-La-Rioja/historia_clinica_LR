import React from 'react';
import {
    TextInput,
    Edit,
    SimpleForm,
    required,
    ReferenceManyField,
    Datagrid,
    TextField,
    DeleteButton, 
    ReferenceField,
} from 'react-admin';

import PersonReferenceInput from '../person/PersonReferenceInput';
import CreateRelatedButton from '../components/CreateRelatedButton';
import SectionTitle from '../components/SectionTitle';
import CustomToolbar from "../../modules/components/CustomToolbar";

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
            {/*TODO: Aislar esto en un componente. También se usa en show.js*/}
            <ReferenceManyField
                addLabel={false}
                reference="healthcareprofessionalspecialties"
                target="healthcareProfessionalId"
            >
                <Datagrid rowClick="show">
                    <ReferenceField source="professionalSpecialtyId" reference="professionalspecialties" >
                        <TextField source="description" />
                    </ReferenceField>
                    <ReferenceField source="clinicalSpecialtyId" reference="clinicalspecialties">
                        <TextField source="name" />
                    </ReferenceField>
                    <DeleteButton />
                </Datagrid>
            </ReferenceManyField>

        </SimpleForm>
    </Edit>
);

export default HealthcareProfessionalEdit;
