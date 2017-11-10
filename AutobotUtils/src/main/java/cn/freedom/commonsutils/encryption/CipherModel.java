package cn.freedom.commonsutils.encryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import cn.freedom.commonsutils.encryption.util.HexStrUtils;
import cn.freedom.commonsutils.encryption.util.StringAppendUtil;


public enum CipherModel {
	DES_ECB("DES", "DES/ECB/NoPadding"), DESede_ECB("DESede", "DESede/ECB/NoPadding"), DESede_CBC("DESede", "DESede/CBC/NoPadding");

	private String SecretKeyType;
	private String CipherType;

	public Cipher getCipher(String keyValueHex, int type) {
		try {
			byte[] keyValue = HexStrUtils.hexStr2ByteArray(keyValueHex);
			return getCipher(keyValue, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Cipher getCipher(byte[] keyValue, int type) {
		try {
			SecretKey secretKey = new SecretKeySpec(keyValue, SecretKeyType);// 恢复密钥
			Cipher cipher = Cipher.getInstance(CipherType);// Cipher完成加密或解密工作类
			// iv 全0
			if (this == DESede_CBC) {
				byte[] keyiv =
				{ 0, 0, 0, 0, 0, 0, 0, 0 };
				IvParameterSpec ips = new IvParameterSpec(keyiv);
				cipher.init(type, secretKey, ips);// 对Cipher初始化，解密模式
			} else {
				cipher.init(type, secretKey);// 对Cipher初始化，解密模式
			}
			return cipher;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private CipherModel(String secretKeyType, String cipherType) {
		SecretKeyType = secretKeyType;
		CipherType = cipherType;
	}

	public String desJava(String key, String dataHex, int type, boolean hasAppend, String appendStr) {
		// java 3des key 重算 key = kl + kr + kl
		if (this == DESede_CBC || this == DESede_ECB) {
			key = key + key.substring(0, 16);
		}

		if (type == Cipher.ENCRYPT_MODE) {
			dataHex = StringAppendUtil.appendStr(dataHex, appendStr);
		}
		byte[] data = HexStrUtils.hexStr2ByteArray(dataHex);
		Cipher cipher = getCipher(key, type);
		byte[] cipherByte = null;
		try {
			cipherByte = cipher.doFinal(data);// 加密data
		} catch (Exception e) {
			e.printStackTrace();
		}
		String resultHex = HexStrUtils.byteArray2HexStr(cipherByte);

		if (type == Cipher.DECRYPT_MODE && hasAppend) {
			resultHex = StringAppendUtil.removeAppend(resultHex, appendStr);
		}
		return resultHex;
	}

	public String Dec(String key, String data) {
		return desJava(key, data, Cipher.DECRYPT_MODE, true, "FFFF");
	}

	public String Enc(String key, String data) {
		return desJava(key, data, Cipher.ENCRYPT_MODE, true, "FFFF");

	}

	public String Dec(String key, String data, String appendStr) {
		return desJava(key, data, Cipher.DECRYPT_MODE, true, appendStr);
	}

	public String Enc(String key, String data, String appendStr) {
		return desJava(key, data, Cipher.ENCRYPT_MODE, true, appendStr);

	}

}
