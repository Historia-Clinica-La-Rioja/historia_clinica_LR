package net.pladema.establishment.application.attentionplaces.exceptions;

import lombok.Getter;

@Getter
public class BlockAttentionPlaceException extends RuntimeException {

	private static String PREFIX = "block-attention-place-exception";
	public static final String NOT_FREE = String.format("%s.%s", PREFIX, "not-free");
	public static final String NOT_FOUND = String.format("%s.%s", PREFIX, "not-found");
	private static final String ALREADY_BLOCKED = String.format("%s.%s", PREFIX, "already-blocked");

	private String code;

	private BlockAttentionPlaceException(String message, String code) {
		super(message);
		this.code = code;
	}

	public static BlockAttentionPlaceException bedAlreadyBlocked(Integer institutionId, Integer bedId) {
		return new BlockAttentionPlaceException(
			String.format("La cama con id %s en la institución %s ya se encuentra bloqueada", bedId, institutionId),
			ALREADY_BLOCKED
		);
	}

	public static BlockAttentionPlaceException shockRoomAlreadyBlocked(Integer institutionId, Integer shockRoomId) {
		return new BlockAttentionPlaceException(
			String.format("El shockRoom con id %s en la institución %s ya se encuentra bloqueado", shockRoomId, institutionId),
			ALREADY_BLOCKED
		);
	}

	public static BlockAttentionPlaceException doctorsOfficeAlreadyBlocked(Integer institutionId, Integer doctorsOfficeId) {
		return new BlockAttentionPlaceException(
			String.format("La oficina con id %s en la institución %s ya se encuentra bloqueada", doctorsOfficeId, institutionId),
			ALREADY_BLOCKED
		);
	}

	public static BlockAttentionPlaceException bedNotFound(Integer institutionId, Integer bedId) {
		return new BlockAttentionPlaceException(
			String.format("No se encontró la cama con id %s en la institución %s", bedId, institutionId),
			NOT_FOUND
		);
	}

	public static BlockAttentionPlaceException shockRoomNotFound(Integer institutionId, Integer shockroomId) {
		return new BlockAttentionPlaceException(
			String.format("No se encontró el shockroom con id %s en la institución %s", shockroomId, institutionId),
			NOT_FOUND
		);
	}

	public static BlockAttentionPlaceException doctorsOfficeNotFound(Integer institutionId, Integer doctorsOfficeId) {
		return new BlockAttentionPlaceException(
			String.format("No se encontró la oficina con id %s en la institución %s", doctorsOfficeId, institutionId),
			NOT_FOUND
		);
	}

    public static BlockAttentionPlaceException bedNotFree(Integer institutionId, Integer id) {
		return new BlockAttentionPlaceException(
				String.format("La cama con id %s en la institución %s no se encuentra libre", id, institutionId),
				NOT_FREE
		);
    }

	public static BlockAttentionPlaceException shockRoomNotFree(Integer institutionId, Integer id) {
		return new BlockAttentionPlaceException(
				String.format("El shockroom con id %s en la institución %s no se encuentra libre", id, institutionId),
				NOT_FREE
		);
	}

	public static BlockAttentionPlaceException doctorsOfficeNotFree(Integer institutionId, Integer id) {
		return new BlockAttentionPlaceException(
				String.format("La oficina con id %s en la institución %s no se encuentra libre", id, institutionId),
				NOT_FREE
		);
	}
}
