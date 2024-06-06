import React, {useState} from 'react';
import Button from '@material-ui/core/Button';
import {
    useNotify,
    fetchStart,
    fetchEnd,
    useRefresh,
} from 'react-admin';
import { useDispatch } from 'react-redux';
import { sgxFetchApiWithToken } from '../../../libs/sgx/api/fetch';

const OutpatientConsultationForwardButton = ({ record }) => {
    const dispatch = useDispatch();
    const refresh = useRefresh();
    const notify = useNotify();
    const [loading, setLoading] = useState(false);
    const handleClick = () => {
        setLoading(true);
        dispatch(fetchStart());
        sgxFetchApiWithToken(`backoffice/cipresencounters/${record.id}/forward-outpatient-consultation`, { method: 'PUT' })
            .then((response) => {
                notify('Intento de reenvio exitoso', { type: 'success' })
                refresh();
            })
            .catch((e) => {
                if (!e.body.text)
                    notify('El reenvio de la consulta tuvo un error', { type: 'warning' })
                else
                    notify(e.body.text, { type: 'warning' })
            })
            .finally(() => {
                setLoading(false);
                dispatch(fetchEnd());
            });
    };
    return (
        <Button onClick={handleClick} color="primary" size="small" disabled={loading}>
            Reenviar
        </Button>
    );
};
export default OutpatientConsultationForwardButton;
