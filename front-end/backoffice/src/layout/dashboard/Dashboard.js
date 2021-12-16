import React from 'react';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import CardHeader from '@material-ui/core/CardHeader';


import {
    useTranslate,
    Title,
 } from 'react-admin';


const Dashboard = () => {
    const translate = useTranslate();
    return (
    <Card>
        <Title title={translate('sgh.dashboard.title')} />
        <CardHeader title={translate('sgh.dashboard.title')} />
        <CardContent>
        {translate('sgh.dashboard.subtitle')}
        </CardContent>
    </Card>
)};


export default Dashboard;
