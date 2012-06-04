package com.cloudtech.ebusi.service.security;

import java.io.UnsupportedEncodingException;

import org.springframework.security.authentication.encoding.BaseDigestPasswordEncoder;
import org.springframework.security.core.token.Sha512DigestUtils;

public class SHAPasswordEncoder extends BaseDigestPasswordEncoder {

	@Override
	public String encodePassword(String rawPass, Object salt) {
		try {
			return Sha512DigestUtils.shaHex((rawPass + (salt == null ? "" : salt)).getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// 如果失败，原文返回！
			return rawPass + salt;
		}
	}

	@Override
	public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
		return encPass.equals(encodePassword(rawPass, salt));
	}

}
