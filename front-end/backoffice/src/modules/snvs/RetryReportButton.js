import * as React from 'react';
import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { useNotify, useRedirect, fetchStart, fetchEnd, Button } from 'react-admin';
import { sgxFetchApiWithToken } from '../../libs/sgx/api/fetch';

const RetryReportButton = ({ record }) => {
    const dispatch = useDispatch();
    const redirect = useRedirect();
    const notify = useNotify();
    const [loading, setLoading] = useState(false);
    const handleClick = () => {
        setLoading(true);
        dispatch(fetchStart());
        sgxFetchApiWithToken(`backoffice/snvs/${record.id}/retry-report`, { method: 'PUT' })
            .then((response) => {
                notify('El reporte se ejecutó exitosamente', { type: 'success' })
                redirect('/snvs');
            })
            .catch((e) => {
                notify('Error: no se ha podido reportar', { type: 'warning' })
            })
            .finally(() => {
                setLoading(false);
                dispatch(fetchEnd());
            });
    };
    return <Button label="Reportar" onClick={handleClick} disabled={loading} />;
};

export default RetryReportButton;