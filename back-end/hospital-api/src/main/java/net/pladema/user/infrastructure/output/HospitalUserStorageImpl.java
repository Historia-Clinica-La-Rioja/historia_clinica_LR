package net.pladema.user.infrastructure.output;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgx.auth.jwt.infrastructure.input.service.JwtExternalService;
import ar.lamansys.sgx.auth.user.infrastructure.input.service.UserExternalService;
import net.pladema.permissions.RoleUtils;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.person.controller.service.PersonExternalService;
import net.pladema.user.application.port.UserRoleStorage;
import net.pladema.user.application.port.exceptions.UserPersonStorageEnumException;
import net.pladema.user.application.port.exceptions.UserPersonStorageException;
import net.pladema.user.controller.service.domain.UserPersonInfoBo;
import net.pladema.user.domain.PersonDataBo;
import net.pladema.user.domain.UserDataBo;
import net.pladema.user.repository.UserPersonRepository;
import net.pladema.user.repository.VHospitalUserRepository;
import net.pladema.user.repository.entity.UserPerson;
import net.pladema.user.repository.entity.VHospitalUser;
import net.pladema.user.application.port.HospitalUserStorage;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HospitalUserStorageImpl implements HospitalUserStorage {

    private final UserPersonRepository userPersonRepository;

    private final PersonExternalService personExternalService;

    private final UserExternalService userExternalService;

    private final VHospitalUserRepository vHospitalUserRepository;

	private final JwtExternalService jwtExternalService;

	private final UserRoleStorage userRoleStorage;

    public HospitalUserStorageImpl(UserPersonRepository userPersonRepository,
								   PersonExternalService personExternalService,
								   UserExternalService userExternalService,
								   VHospitalUserRepository vHospitalUserRepository,
								   JwtExternalService jwtExternalService,
								   UserRoleStorage userRoleStorage) {
        this.userPersonRepository = userPersonRepository;
        this.personExternalService = personExternalService;
        this.userExternalService = userExternalService;
        this.vHospitalUserRepository = vHospitalUserRepository;
		this.jwtExternalService = jwtExternalService;
		this.userRoleStorage = userRoleStorage;
	}

    @Override
    public Optional<UserPersonInfoBo> getUserPersonInfo(Integer userId) {
        return userExternalService.getUser(userId)
                .map(userInfoDto -> {
                    UserPersonInfoBo result = new UserPersonInfoBo();
                    result.setId(userInfoDto.getId());
                    result.setEmail(userInfoDto.getUsername());
					result.setPreviousLogin(userInfoDto.getPreviousLogin());
                    return result;
                })
                .map(this::loadPersonInfo);
    }
    @Override
    public String getIdentificationNumber(Integer personId) {
        return personExternalService.getBasicDataPerson(personId).getIdentificationNumber();
    }

    @Override
    public void enableUser(Integer userId) {
        vHospitalUserRepository.findById(userId)
                .ifPresent(vHospitalUser ->
                        userExternalService.enableUser(vHospitalUser.getUsername()));
    }

    @Override
    public void disableUser(Integer userId) {
        vHospitalUserRepository.findById(userId)
                .ifPresent(vHospitalUser ->
                        userExternalService.disableUser(vHospitalUser.getUsername()));
    }


    @Override
    public void saveUserPerson(Integer userId, Integer personId) {
        userPersonRepository.save(new UserPerson(userId, personId));
    }

    @Override
    public void registerUser(String username, String email, String password){
        userExternalService.registerUser(username, email, password);
    }
    @Override
    public UserDataBo getUserByUsername(String username){
        return userExternalService.getUser(username)
                .map(userInfoDto -> {
                    return new UserDataBo(userInfoDto.getId(), userInfoDto.getUsername(), userInfoDto.isEnabled());
                }).orElseThrow(()-> new UserPersonStorageException(UserPersonStorageEnumException.UNEXISTED_USER,"El usuario %s no existe"));
    }
    @Override
    public Optional<UserDataBo> getUserDataByPersonId(Integer personId) {
        return userPersonRepository.getUserIdByPersonId(personId)
                .map(userId -> vHospitalUserRepository.findById(userId).orElseThrow(()-> new UserPersonStorageException(UserPersonStorageEnumException.UNEXISTED_USER_VIEW, "No se encuentran los datos del usuario")))
                .map(this::mapUserBo);
    }

    @Override
    public Boolean hasPassword(Integer userId){
        return userExternalService.getUser(userId)
                .map(userInfoDto -> userInfoDto.getPassword()!=null)
                .orElseThrow(()-> new UserPersonStorageException(UserPersonStorageEnumException.UNEXISTED_USER,"El usuario %s no existe"));
    }

    @Override
    public String createTokenPasswordReset(Integer userId) {
        return userExternalService.createTokenPasswordReset(userId);
    }

    @Override
    public Integer getUserIdByToken(String token) {
        return userExternalService.getUserIdByToken(token);
    }

    @Override
    public PersonDataBo getPersonDataBoByUserId(Integer userId) {
        return vHospitalUserRepository.findById(userId)
				.map(vHospitalUser ->
						userPersonRepository.getPersonIdByUserId(userId)
						.map(personExternalService::getBasicDataPerson)
						.map(basicDataPersonDto -> mapPersonDataBo(basicDataPersonDto, vHospitalUser))
								.orElseGet(() -> mapPersonDataBo(null, vHospitalUser)))
                .orElseThrow(()-> new UserPersonStorageException(UserPersonStorageEnumException.UNEXISTED_USER, String.format("El usuario %s no existe", userId)));
    }

	@Override
	public Optional<Integer> fetchUserIdFromNormalToken(String token) {
		return jwtExternalService.fetchUserIdFromNormalToken(token);
	}

    @Override
	public void resetTwoFactorAuthentication(Integer userId) {
		userExternalService.resetTwoFactorAuthentication(userId);

	}

	@Override
	public boolean hasProfessionalRole(Integer userId) {
		return RoleUtils.hasProfessionalRole(userRoleStorage.getRolesByUser(userId).stream()
				.map(r-> ERole.map(r.getRoleId()))
				.collect(Collectors.toList()));
	}

	private PersonDataBo mapPersonDataBo(BasicDataPersonDto person, VHospitalUser user) {
        return person != null ? new PersonDataBo(
                person.getFirstName(),
                person.getLastName(),
                person.getIdentificationType(),
                person.getIdentificationNumber(),
                user.getUserId(),
                user.getUsername()) : new PersonDataBo(user.getUserId(), user.getUsername());
    }

    private UserDataBo mapUserBo(VHospitalUser vHospitalUser) {
        return new UserDataBo(vHospitalUser.getUserId(), vHospitalUser.getUsername(), vHospitalUser.getEnable(), vHospitalUser.getLastLogin());
    }

    private UserPersonInfoBo loadPersonInfo(UserPersonInfoBo userPersonInfoBo) {
        userPersonRepository.getByUserId(userPersonInfoBo.getId())
                .map(userPerson -> personExternalService.getBasicDataPerson(userPerson.getPersonId()))
                .ifPresent(basicDataPersonDto -> {
                    userPersonInfoBo.setPersonId(basicDataPersonDto.getId());
                    userPersonInfoBo.setFirstName(basicDataPersonDto.getFirstName());
                    userPersonInfoBo.setLastName(basicDataPersonDto.getLastName());
					userPersonInfoBo.setNameSelfDetermination(basicDataPersonDto.getNameSelfDetermination());
                });
        return userPersonInfoBo;
    }
}

