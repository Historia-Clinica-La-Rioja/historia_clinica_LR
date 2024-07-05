import React, {useEffect, useState } from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField,
    useRecordContext,
} from 'react-admin';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';
import { SectionTitle } from '../../components';
import AssociatedParameters from './AssociatedParameters';

const FormScopes = props => {
    const record = useRecordContext(props);
    const [scopes, setScopes] = useState([]);

    useEffect(() => {
        if (record) {
            const FORM_SCOPES = [];
            if (record.outpatientEnabled) FORM_SCOPES.push('Ambulatorio');
            if (record.emergencyCareEnabled) FORM_SCOPES.push('Guardia');
            if (record.internmentEnabled) FORM_SCOPES.push('Internación');
            setScopes(FORM_SCOPES);
        }
    }, [record]);

    if (!record) return null;

    return (
        <Box mt={2}>
            <Typography variant="body1"  style={{ fontSize: '13px' }} color="textSecondary">
                Ámbito
            </Typography>
            <Typography variant="body1" style={{ fontSize: '14px' }}>
                {scopes.join(', ')}
            </Typography>
        </Box>
    );
}

const ParameterizedFormShow = props => (
    <Show {...props} hasEdit={false}>
        <SimpleShowLayout>
            <SectionTitle label="resources.parameterizedform.formName" />
            <TextField source="name" label=""></TextField>
            <FormScopes />
            <br />
            <AssociatedParameters />
        </SimpleShowLayout>
    </Show>
);

export default ParameterizedFormShow;