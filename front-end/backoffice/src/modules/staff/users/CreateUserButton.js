import React from 'react';
import {
    Button,
    useRedirect,
    useMutation,
    useNotify,
} from 'react-admin';
import {
    VisibilityOff,
 } from '@material-ui/icons';

 import PersonAddIcon from '@material-ui/icons/PersonAdd';

const CreateUserButton = ({ record }) => {

    const newUserPayload = record && {
        personId: record.id,
        username: record.email,
    };

    const notify = useNotify();
    const redirect = useRedirect();

    const [createPasswordReset, { loading }] = useMutation({
        type: 'create',
        resource: 'users',
        payload: { data: newUserPayload },
    }, {
        onSuccess: ({ data }) => {
            redirect("edit", "/users", data.id);
        },
        onFailure: error => notify('Error creando el usuario')
    });

    if (!newUserPayload) {
        return (<span>...</span>);
    }

    if (newUserPayload.username) {
        return (
            <Button
                style={{textTransform: "none"}}
                label={newUserPayload.username}
                onClick={createPasswordReset}
                disabled={loading}
            >
                <PersonAddIcon />
            </Button>
        );
    }

    return (
        <Button
                label="Falta definir Correo Electrónico"
                disabled={true}
            >
                <VisibilityOff />
            </Button>
    );
};

export default CreateUserButton;
