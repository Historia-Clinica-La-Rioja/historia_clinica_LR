import React, { useState } from 'react';
import { useDispatch } from 'react-redux';

import {
    useNotify,
    fetchStart,
    fetchEnd,
    useRecordContext, useRedirect,
} from 'react-admin';

import Button from '@material-ui/core/Button';
import { sgxFetchApiWithToken } from '../api/fetch';

const MergeButton = ({ baseMedicalCoverage,
                         ...props
                     }) => {
    const [loading, setLoading] = useState(false);
    const notify = useNotify();
    const redirect = useRedirect();
    const record = useRecordContext(props);
    const dispatch = useDispatch();

    const handleClick = () => {
        setLoading(true);
        dispatch(fetchStart()); // start the global loading indicator

        sgxFetchApiWithToken(`backoffice/medicalcoveragesmerge/${record.id}/baseMedicalCoverage/${baseMedicalCoverage}`,{ method: 'PUT' })
            .then(() => {
                notify('mergeMedicalCoverage.merge_success', { type: 'info' })
                redirect('/medicalcoverages');
            })
            .catch(() => {
                notify('mergeMedicalCoverage.cant_merge', 'warning')
            })
            .finally(() => {
                setLoading(false);
                dispatch(fetchEnd()); // stop the global loading indicator
            });
    };

    return (
        <Button onClick={handleClick} color="primary" size="small" disabled={loading}>
            Unificar
        </Button>
    );
};

export default MergeButton;