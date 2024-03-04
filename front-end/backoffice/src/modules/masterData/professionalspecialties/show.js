import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    TextField,
} from 'react-admin';

const ProfessionalSpecialtyShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="description" />
            <TextField source="descriptionProfessionRef" />
            <TextField source="sctidCode" />
            <ReferenceField source="educationTypeId" reference="educationtypes">
                <TextField source="description"/>
            </ReferenceField>
        </SimpleShowLayout>
    </Show>
);

export default ProfessionalSpecialtyShow;
