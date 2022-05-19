import ClinicalSpecialtyMandatoryMedicalPracticeShow from "./show";
import ClinicalSpecialtyMandatoryMedicalPracticeList from "./list";
import ClinicalSpecialtyMandatoryMedicalPracticeCreate from "./create";
import ClinicalSpecialtyMandatoryMedicalPracticeEdit from "./edit";


const clinicalspecialtymandatorymedicalpractices = {
    edit: ClinicalSpecialtyMandatoryMedicalPracticeEdit,
    show: ClinicalSpecialtyMandatoryMedicalPracticeShow,
    list: ClinicalSpecialtyMandatoryMedicalPracticeList,
    create: ClinicalSpecialtyMandatoryMedicalPracticeCreate,
    options: {
        submenu: 'booking'
    }

};

export default clinicalspecialtymandatorymedicalpractices;
