import ClinicalServiceSectorCreate from "./ClinicalServiceSectorCreate";
import ClinicalServiceSectorList from "./ClinicalServiceSectorList";
import ClinicalServiceSectorShow from "./ClinicalServiceSectorShow";


const clinicalservicesectors = {
    show: ClinicalServiceSectorShow,
    list: ClinicalServiceSectorList,
    create: ClinicalServiceSectorCreate,
    options: {
        submenu: 'facilities'
    }
};

export default clinicalservicesectors;