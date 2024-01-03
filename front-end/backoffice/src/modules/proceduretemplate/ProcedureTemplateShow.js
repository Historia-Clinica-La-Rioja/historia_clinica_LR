import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField} from 'react-admin';
import { AssociatedParameters } from './AssociatedParameters';
import { AssociatedPractices } from './AssociatedPractices';

const ProcedureTemplateShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="description"/>
            <AssociatedPractices/>
            <AssociatedParameters/>
        </SimpleShowLayout>
    </Show>
);

export default ProcedureTemplateShow;