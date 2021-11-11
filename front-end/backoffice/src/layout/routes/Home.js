import React from 'react';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import {
    Title,
    useTranslate,
    useAuthState,
    Loading,
 } from 'react-admin';

const ForbiddenMessage = () => (
    <span>No cuenta con los permisos necesarios</span>
);

const OkMessage = () => (
    <span>Bienvenido</span>
);

const Home = () => {
    const translate = useTranslate();
    const { loading, authenticated } = useAuthState();
    if (loading) {
        return <Loading />;
    }

    return <Card>
            <Title title={translate('sgh.dashboard.title')} />
            <CardContent>
                { authenticated? <OkMessage /> : <ForbiddenMessage /> }
            </CardContent>
        </Card>;

};

export default Home;
