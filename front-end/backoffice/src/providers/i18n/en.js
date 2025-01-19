import resources from '../../modules/resources.en';

const messages = {
    app: {
        menu: {
            staff: 'Staff',
            facilities: 'Facilities',
            debug: 'Inspect',
            masterData: 'Master Data',
            terminology: 'Terminology',
            booking: 'Online Booking',
            imageNetwork: 'Image Network',
            more: 'More',
        },
    },
    bo: {
        login: {
            redirect: {
                message: "It seems your session has ended."
            }
        }
    },
    sgh: {
        dashboard: {
            title: 'Integrated Health History',
            subtitle: 'Welcome',
        },
        components: {
            customtoolbar: {
                backButton: 'Back',
            }
        },
    },
    error: {
        "role-level": {
            institution: {
                required: 'The role requires an institution'
            }
        },
        "beds": {
            "existsHospitalization": "The bed cannot be edited because it has an associated hospitalization episode",
            "enabled-available": "Only enabled beds can be available",
            "available-free": "Only available beds can be free"
        },
        "doctorsoffices": {
            "closingBeforeOpening": "Opening time cannot be later than closing time",
            "matchingIds": "That Sector does not belong to that institution"
        },
        "healthcareprofessional": {
            "exists": "This person is already registered as a professional in the system",
        },
        "healthcare-professional": {
            "specialty-profession-exists": "The profession and specialty are already assigned",
            "only-one-specialty": "This specialty cannot be deleted as it is the only one the professional has",
            "specialty-profession-not-exists": "The specialty does not exist",
            "affected-to-diary-agenda": "This specialty and professional are assigned to an ongoing schedule"
        },
        "role": {
            "requiresprofessional": "Some of the assigned roles require the user to be a professional"
        },
        "PROFESSIONAL_REQUIRED": "Some of the assigned roles require the user to be a professional",
        "ROOT_LOST_PERMISSION": "The admin cannot lose the ROOT role",
        "USER_INVALID_ROLE": "The created user cannot have the following role: ROOT",
        "user": {
            "exists": "This person already has a user in the system",
            "hasrole": "The professional you want to delete has an associated role"
        },
        "sector": {
            "mandatoryCareType": "The care type is mandatory for that type of sector organization",
            "parentOfItself": "A sector cannot be its own parent"
        },
        forbidden: 'You do not have the necessary permissions',
        "sector-description-inst-unique": "A sector with the same name already exists in the institution",
        "care-line": {
            "clinical-specialty-exists": "The clinical specialty is already associated with the care line"
        },
        "medical-coverage": {
            "rnos-duplicated": "The Rnos is already associated with another medical coverage",
            "cuit-duplicated": "The CUIT is already associated with another medical coverage",
            "invalid-cuit": "The CUIT must be numeric",
            "plan-exists": "The plan is already associated with the medical coverage",
        },
        "loinc-code": {
            "create-disallowed" : "The creation of LOINC codes is disabled",
            "editable-fields-disallowed" : "Only the 'System Name' field can be edited",
            "delete-disallowed" : "The deletion of LOINC codes is disabled"
        }
    },
    files: {
        cant_download: 'The file could not be downloaded'
    },
    mergeMedicalCoverage: {
        merge_success: 'The coverage was successfully merged',
        cant_merge: 'The medical coverage could not be merged',
        dialog_title: 'Merge Medical Coverages',
    },
    resources,
};

export default messages;
