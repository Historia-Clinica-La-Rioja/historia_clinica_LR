
import { sgxFetchApiWithToken } from '../../../libs/sgx/api/fetch';

export const fetchHierarchicalUnits = (institutionId) => {
    const hierarchicalUnitsWithParents$ = switchMapPromise(fetchHierarchicalUnitsByInstitutionId(institutionId), addParents);
    const hierarchicalUnitsWithResposibles$ = switchMapPromise(hierarchicalUnitsWithParents$, addUsers);
    const hierarchicalUnitsWithResponsibleUserInfo$ = switchMapPromise(hierarchicalUnitsWithResposibles$, addUserInfo);
    return hierarchicalUnitsWithResponsibleUserInfo$;
}

export const fetchHierarchicalUnitTypes = () => {
    return sgxFetchApiWithToken(`backoffice/hierarchicalunittypes`, { method: 'GET' })
}

const fetchHierarchicalUnitsByInstitutionId = (id) => {
    return sgxFetchApiWithToken(`backoffice/hierarchicalunits?institutionId=${id}&size=1000`, { method: 'GET' })
}

const fetchUsersByHUId = (id) => {
    return sgxFetchApiWithToken(`backoffice/hierarchicalunitstaff?hierarchicalUnitId=${id}`, { method: 'GET' })
}

const fetchUserInfoById = (id) => {
    return sgxFetchApiWithToken(`backoffice/institutionuserpersons?ids=${id}`, { method: 'GET' })
}

const fetchHURelations = (id) => {
    return sgxFetchApiWithToken(`backoffice/hierarchicalunitrelationships?hierarchicalUnitChildId=${id}`, { method: 'GET' })
}


const switchMapPromise = (sourcePromise, mappingFunction) => {
    let lastPromise = null;

    return sourcePromise.then(sourceValue => {
        if (lastPromise) {
            // Si hay una promesa pendiente, se rechaza para cancelarla
            lastPromise.reject('Switched');
            lastPromise = null;
        }

        const mappedPromise = mappingFunction(sourceValue);
        lastPromise = mappedPromise;

        return mappedPromise;
    });
}


const addParents = objects => {
    const secondDataPromises = objects.content.map(obj =>
        fetchHURelations(obj.id).then(secondData => {
            return { ...obj, relations: secondData.content.map(r => r.hierarchicalUnitParentId) };
        })
    );
    return Promise.all(secondDataPromises);
}

const addUsers = hierarchicalUnits => {
    const secondDataPromises$ = hierarchicalUnits.map(hu => fetchUsersByHUId(hu.id).then(data => {
        return { ...hu, usersAmount: data.totalElements, responsibleUserIds: data.content.filter(u => u.responsible).map(u => u.userId) }
    }));
    return Promise.all(secondDataPromises$);
}

const addUserInfo = hierarchicalUnits => {
    const promises = [];
    hierarchicalUnits.forEach(hierarchicalUnit => {
        if (hierarchicalUnit.responsibleUserIds.length) {
            hierarchicalUnit.responsibleUserIds.forEach(id => {
                const promise = fetchUserInfoById(id)
                    .then(info => {
                        const toAdd = info.map(i => `${i.completeName}, ${i.completeLastName}`)
                        return { ...hierarchicalUnit, responsable: toAdd };
                    });

                promises.push(promise);
            });
        } else {
            promises.push(hierarchicalUnit)
        }
    });
    return Promise.all(promises);
}

