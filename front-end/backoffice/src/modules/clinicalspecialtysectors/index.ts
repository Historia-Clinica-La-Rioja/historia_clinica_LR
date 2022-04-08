import ClinicalSpecialtySectorShow from './ClinicalSpecialtySectorShow';
import ClinicalSpecialtySectorList from './ClinicalSpecialtySectorList';
import ClinicalSpecialtySectorCreate from './ClinicalSpecialtySectorCreate';

const clinicalspecialtysectors = {
    show: ClinicalSpecialtySectorShow,
    list: ClinicalSpecialtySectorList,
    create: ClinicalSpecialtySectorCreate,
    options: {
        submenu: 'facilities'
    }
};

export default clinicalspecialtysectors;
