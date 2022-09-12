package ar.lamansys.sgx.auth.user.domain.userpassword;

import org.springframework.stereotype.Service;

@Service
public class PasswordValidator {

	private int mayus;
	private int min;
	private int num;

	public boolean passwordIsValid(String newPassword) {

		mayus = 0;
		min = 0;
		num = 0;

		for(int i = 0; i < newPassword.length(); i++)
		{
			char ch = newPassword.charAt(i);
			if (ch >= 'A' && ch <= 'Z')
				mayus++;
			if (ch >= 'a' && ch <= 'z')
				min++;
			if (ch >= '0' && ch <= '9')
				num++;
		}

		if(mayus == 0 || min == 0 || num == 0)
			return false;

		return true;
	}

}
