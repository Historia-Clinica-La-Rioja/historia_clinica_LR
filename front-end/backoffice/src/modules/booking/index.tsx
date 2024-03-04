import React from 'react';
import {
    Resource
} from 'react-admin';
import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';
import {
    ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE,
} from '../roles';
import healthcareprofessionalhealthinsurances from './healthcareprofessionalhealthinsurances';
import mandatorymedicalpractices from './mandatorymedicalpractices';
import clinicalspecialtymandatorymedicalpractices from './clinicalspecialtymandatorymedicalpractices';
import healthinsurancepractices from './healthinsurancepractices';
import mandatoryprofessionalpracticefreedays from './mandatoryprofessionalpracticefreedays';
import bookingInstitutions from './booking-institutions';

const resourcesReservaTurnos = (permissions: SGXPermissions) =>
    permissions.isOn('BACKOFFICE_MOSTRAR_ABM_RESERVA_TURNOS') && permissions.hasAnyAssignment(ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE) ?
        [
        <Resource name="booking-institution" {...bookingInstitutions(permissions)} />,
        <Resource name="healthcareprofessionalhealthinsurances"  {...healthcareprofessionalhealthinsurances} />,
        <Resource name="mandatorymedicalpractices"  {...mandatorymedicalpractices} />,
        <Resource name="clinicalspecialtymandatorymedicalpractices"  {...clinicalspecialtymandatorymedicalpractices} />,
        <Resource name="healthinsurancepractices"  {...healthinsurancepractices} />,
        <Resource name="mandatoryprofessionalpracticefreedays"  {...mandatoryprofessionalpracticefreedays} />,
        ] : []
;

export default resourcesReservaTurnos;