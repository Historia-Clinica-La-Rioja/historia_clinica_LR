import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField,
} from 'react-admin';

const ProcedureTemplateShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="description"/>
        </SimpleShowLayout>
    </Show>
);

export default ProcedureTemplateShow;