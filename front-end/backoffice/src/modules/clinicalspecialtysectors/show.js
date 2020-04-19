import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    TextField,
} from 'react-admin';

const ClinicalSpecialtySectorShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="description"/>
            <ReferenceField source="sectorId" reference="sectors">
                <TextField source="description" />
            </ReferenceField>
            <ReferenceField source="clinicalSpecialtyId" reference="clinicalspecialties">
                <TextField source="name" />
            </ReferenceField>
        </SimpleShowLayout>
    </Show>
);

export default ClinicalSpecialtySectorShow;
