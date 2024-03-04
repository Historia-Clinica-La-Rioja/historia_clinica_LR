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

const FileFixButton = ({ record }) => {
    const dispatch = useDispatch();
    const refresh = useRefresh();
    const notify = useNotify();
    const [loading, setLoading] = useState(false);
    const handleClick = () => {
        setLoading(true);
        dispatch(fetchStart());
        sgxFetchApiWithToken(`backoffice/files/${record.id}/fix-metadata`, { method: 'PUT' })
            .then((response) => {
                notify('Se corrigieron metadatos del archivo exitosamente', { type: 'success' })
                refresh();
            })
            .catch((e) => {
                notify('La correción de metadatos tuvo un error', { type: 'warning' })
            })
            .finally(() => {
                setLoading(false);
                dispatch(fetchEnd());
            });
    };
    return (
        <Button onClick={handleClick} color="primary" size="small" disabled={loading}>
        Corregir metadata
        </Button>
    );
};
export default FileFixButton;
