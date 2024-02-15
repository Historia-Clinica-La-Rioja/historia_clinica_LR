import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField
} from 'react-admin';

const ProcedureTemplateParameterShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="description"/>
        </SimpleShowLayout>
    </Show>
);

export default ProcedureTemplateParameterShow;