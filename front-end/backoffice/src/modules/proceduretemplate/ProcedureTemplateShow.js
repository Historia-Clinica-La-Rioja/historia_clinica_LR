import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField} from 'react-admin';
import { AssociatedParameters } from './ProcedureTemplateParameters';
import { AssociatedPractices } from './ProcedureTemplatePractices';

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