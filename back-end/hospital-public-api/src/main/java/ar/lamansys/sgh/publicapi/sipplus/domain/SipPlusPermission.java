package ar.lamansys.sgh.publicapi.sipplus.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SipPlusPermission {

	public static final String EDIT_FORMS = "EditForms";
	public static final String ACCESS_ALL_INSTITUTIONS = "AccessAllInstitutions";
	public static final String NAVIGATE_PREGNANCIES = "NavigatePregnancies";
	public static final String PRINT_FORMS = "PrintForms";
	public static final String DELETE_FORMS = "DeleteForms";
	public static final String CLOSE_FORMS = "CloseForms";
	public static final String VERIFY_FORMS = "VerifyForms";
	public static final String FORM_SIPNM2 = "sipnm2";
	public static final String ACCESS_FORM_SECTION = "AccessFormSection";
	public static final List<Integer> SECTIONS = Arrays.asList(1,2,3,4,5,6,7,8,9);


	public static List<String> getUserPermissions() {
		List<String> permissions = new ArrayList<>();
		permissions.add(SipPlusPermission.EDIT_FORMS);
		permissions.add(SipPlusPermission.ACCESS_ALL_INSTITUTIONS);
		permissions.add(SipPlusPermission.NAVIGATE_PREGNANCIES);
		permissions.add(SipPlusPermission.PRINT_FORMS);
		permissions.add(SipPlusPermission.DELETE_FORMS);
		permissions.add(SipPlusPermission.CLOSE_FORMS);
		permissions.add(SipPlusPermission.VERIFY_FORMS);

		SipPlusPermission.SECTIONS.stream().forEach(section -> {
			String permission = SipPlusPermission.ACCESS_FORM_SECTION + "(" + SipPlusPermission.FORM_SIPNM2 + "," + section + ")";
			permissions.add(permission);
		});
		return permissions;
	}
}

