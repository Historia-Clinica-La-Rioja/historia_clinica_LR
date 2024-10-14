import React, {useEffect, useState }from 'react';
import {
    useRecordContext
} from 'react-admin';
import Typography from '@material-ui/core/Typography';

const ParameterizedFormScopes = props => {
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
        <div>
            <Typography variant="body2">
                {scopes.join(', ')}
            </Typography>
        </div>
    );
}

export default ParameterizedFormScopes;