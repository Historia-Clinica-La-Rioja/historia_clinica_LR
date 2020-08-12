import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    TextField,
    FunctionField,
    ReferenceManyField,
    Datagrid,
    DeleteButton,
} from 'react-admin';

import CreateRelatedButton from '../components/CreateRelatedButton';
import SectionTitle from '../components/SectionTitle';

const renderPerson = (choice) => `${choice.identificationNumber} ${choice.lastName} ${choice.firstName}`;
const HealthcareProfessionalShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="licenseNumber" />
            <ReferenceField source="personId" reference="people">
                <FunctionField render={renderPerson}/>
            </ReferenceField>

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

        </SimpleShowLayout>
    </Show>
);

export default HealthcareProfessionalShow;
