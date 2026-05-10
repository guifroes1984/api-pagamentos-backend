package br.com.guifroes1984.api.pagamentos.security.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
	
	public static String getEmailUsuarioLogado() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null) {
			return null;
		}
		
		return authentication.getName();
	}

}
