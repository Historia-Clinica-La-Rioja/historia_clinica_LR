import React, { useState } from 'react';
import { useDispatch } from "react-redux";
import {
    useRecordContext,
    Button,
    fetchStart,
    useNotify,
    useRefresh,
    fetchEnd
} from 'react-admin';
import { sgxFetchApiWithToken } from '../../../libs/sgx/api/fetch';
import { FORM_STATUS_INACTIVE } from './ParameterizedFormStatus';

export const UpdateFormEnablementInInstitution = (institutionId, props) => {
    const record = useRecordContext(props);
    const dispatch = useDispatch();
    const refresh = useRefresh();
    const notify = useNotify();
    const [loading, setLoading] = useState(false);
    const handleClick = (e) => {
        e.stopPropagation();
        setLoading(true);
        dispatch(fetchStart());
        const hasToEnabled = record.isEnabled ? false : true;
        sgxFetchApiWithToken(`backoffice/parameterizedform/${record.id}/update-institutional-enablement?institutionId=${institutionId.institutionId}&enablement=${hasToEnabled}`, 
            { method: 'PUT' })
            .then((response) => {
                refresh();
            })
            .catch((e) => {
                if (!e.body.text)
                    notify('Error al habilitar formulario', { type: 'warning' })
                else
                    notify(e.body.text, { type: 'warning' })
            })
            .finally(() => {
                setLoading(false);
                dispatch(fetchEnd());
            });
    };
    return (
        <>
            <Button
                onClick={handleClick}
                color="primary"
                size="small"
                disabled={record.statusId === FORM_STATUS_INACTIVE || loading }
                label={record.isEnabled ? 'Deshabilitar' : 'Habilitar'}
            />
        </>
    );

} 