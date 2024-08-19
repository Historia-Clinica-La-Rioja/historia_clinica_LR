package net.pladema.procedure.domain;

import java.util.Objects;

public class ProcedureParameterTypeBo {
	public static final Short NUMERIC = 1;
	public static final Short FREE_TEXT = 2;
	public static final Short SNOMED_ECL = 3;
	public static final Short TEXT_OPTION = 4;


  	static public boolean isNumeric(Short typeId) {
  		return Objects.equals(typeId, ProcedureParameterTypeBo.NUMERIC);}
	static public boolean isFreeText(Short typeId) {
  		return Objects.equals(typeId, ProcedureParameterTypeBo.FREE_TEXT);
     }
	static public boolean isSnomedEcl(Short typeId) {
  		return Objects.equals(typeId, ProcedureParameterTypeBo.SNOMED_ECL);
     }
	static public boolean isTextOption(Short typeId) {
  		return Objects.equals(typeId, ProcedureParameterTypeBo.TEXT_OPTION);
     }

}
