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
} from 'react-admin';

import PeopleReferenceInput from '../people/PeopleReferenceInput';
import CreateRelatedButton from '../components/CreateRelatedButton';
import SectionTitle from '../components/SectionTitle';

const HealthcareProfessionalEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" >
            <PeopleReferenceInput source="personId" validate={[required()]} />
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
                    <TextField source="description" />
                    <DeleteButton />
                </Datagrid>
            </ReferenceManyField>

        </SimpleForm>
    </Edit>
);

export default HealthcareProfessionalEdit;
