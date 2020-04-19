import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField,
} from 'react-admin';

const ClinicalSpecialtyShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="name" />
            <TextField source="description" />
            <TextField source="sctidCode" />
        </SimpleShowLayout>
    </Show>
);

export default ClinicalSpecialtyShow;
