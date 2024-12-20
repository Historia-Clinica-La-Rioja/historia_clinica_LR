import doctorsoffices from './doctorsoffices/es';
import rooms from './rooms/es';
import sectors from './sectors/es';
import shockroom from './shockroom/es';

const resourcesFacilities = {
    doctorsoffices,
    rooms,
    rootsectors: sectors,
    sectors,
    shockroom,
};

export default resourcesFacilities;