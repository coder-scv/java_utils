package cn.freedom.commonsutils.encryption;
//package cn.freedom.hy;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.security.InvalidKeyException;
//import java.security.KeyFactory;
//import java.security.NoSuchAlgorithmException;
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//import java.security.spec.InvalidKeySpecException;
//import java.security.spec.RSAPrivateKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.Cipher;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.NoSuchPaddingException;
//
//import org.bouncycastle.asn1.ASN1Sequence;
//import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//
//import cn.freedom.commonsutils.FileUtils;
//import cn.freedom.commonsutils.encryption.util.HexStrUtils;
//import sun.misc.BASE64Decoder;
//
//public class RSAEncrypt {
//
//	public RSAEncrypt() {
//		super();
//		String pubKeyStr = FileUtils.read(Config.getRSAPublicKeyPath() , '-' , "\r");
//		String priKeyStr = FileUtils.read(Config.getRSAPrivateKeyPath() , '-' , "\r");
//
//		// 加载公钥
//		try {
//			System.out.println(pubKeyStr);
//			loadPublicKey(pubKeyStr);
//			System.out.println("加载公钥成功");
//		} catch (Exception e) {
//			System.err.println(e.getMessage());
//			System.err.println("加载公钥失败");
//		}
//
//		// 加载私钥
//		try {
//			System.out.println(priKeyStr);
//			loadPrivateKey(priKeyStr);
//			System.out.println("加载私钥成功");
//		} catch (Exception e) {
//			System.err.println(e.getMessage());
//			System.err.println("加载私钥失败");
//		}
//	}
//
//	/**
//	 * 私钥
//	 */
//	private RSAPrivateKey privateKey;
//
//	/**
//	 * 公钥
//	 */
//	private RSAPublicKey publicKey;
//
//	/**
//	 * 从字符串中加载公钥
//	 * @param publicKeyStr 公钥数据字符串
//	 * @throws Exception 加载公钥时产生的异常
//	 */
//	public void loadPublicKey(String publicKeyStr) throws Exception {
//		try {
//			BASE64Decoder base64Decoder = new BASE64Decoder();
//			byte[] buffer = base64Decoder.decodeBuffer(publicKeyStr);
//			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
//			this.publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
//		} catch (NoSuchAlgorithmException e) {
//			throw new Exception("无此算法");
//		} catch (InvalidKeySpecException e) {
//			throw new Exception("公钥非法");
//		} catch (IOException e) {
//			throw new Exception("公钥数据内容读取错误");
//		} catch (NullPointerException e) {
//			throw new Exception("公钥数据为空");
//		}
//	}
//
//	public void loadPrivateKey(String privateKeyStr) throws Exception {
//		try {
//			BASE64Decoder base64Decoder = new BASE64Decoder();
//			byte[] buffer = base64Decoder.decodeBuffer(privateKeyStr);
//
//			// pkcs8形式密钥
//			// PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);
//			// KeyFactory keyFactory= KeyFactory.getInstance("RSA");
//			// this.privateKey= (RSAPrivateKey)
//			// keyFactory.generatePrivate(keySpec);
//
//			// openssl默认生产密钥为pkcs1形式
//			RSAPrivateKeyStructure asn1PrivKey = new RSAPrivateKeyStructure((ASN1Sequence) ASN1Sequence.fromByteArray(buffer));
//			RSAPrivateKeySpec rsaPrivKeySpec = new RSAPrivateKeySpec(asn1PrivKey.getModulus(), asn1PrivKey.getPrivateExponent());
//			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//			this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(rsaPrivKeySpec);
//
//		} catch (NoSuchAlgorithmException e) {
//			throw new Exception("无此算法");
//		} catch (InvalidKeySpecException e) {
//			throw new Exception("私钥非法");
//		} catch (IOException e) {
//			throw new Exception("私钥数据内容读取错误");
//		} catch (NullPointerException e) {
//			throw new Exception("私钥数据为空");
//		}
//	}
//
//	/**
//	 * 加密过程
//	 * @param publicKey 公钥
//	 * @param plainTextData 明文数据
//	 * @return
//	 * @throws Exception 加密过程中的异常信息
//	 */
//	public byte[] encrypt(byte[] plainTextData) throws Exception {
//		if (publicKey == null) {
//			throw new Exception("加密公钥为空, 请设置");
//		}
//		Cipher cipher = null;
//		try {
//			cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
//			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//			byte[] output = cipher.doFinal(plainTextData);
//			return output;
//		} catch (NoSuchAlgorithmException e) {
//			throw new Exception("无此加密算法");
//		} catch (NoSuchPaddingException e) {
//			e.printStackTrace();
//			return null;
//		} catch (InvalidKeyException e) {
//			throw new Exception("加密公钥非法,请检查");
//		} catch (IllegalBlockSizeException e) {
//			throw new Exception("明文长度非法");
//		} catch (BadPaddingException e) {
//			throw new Exception("明文数据已损坏");
//		}
//	}
//
//	/**
//	 * 解密过程
//	 * @param privateKey 私钥
//	 * @param cipherData 密文数据
//	 * @return 明文
//	 * @throws Exception 解密过程中的异常信息
//	 */
//	public byte[] decrypt(byte[] cipherData) throws Exception {
//		if (privateKey == null) {
//			throw new Exception("解密私钥为空, 请设置");
//		}
//		Cipher cipher = null;
//		try {
//			cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
//			cipher.init(Cipher.DECRYPT_MODE, privateKey);
//			byte[] output = cipher.doFinal(cipherData);
//			return output;
//		} catch (NoSuchAlgorithmException e) {
//			throw new Exception("无此解密算法");
//		} catch (NoSuchPaddingException e) {
//			e.printStackTrace();
//			return null;
//		} catch (InvalidKeyException e) {
//			throw new Exception("解密私钥非法,请检查");
//		} catch (IllegalBlockSizeException e) {
//			throw new Exception("密文长度非法");
//		} catch (BadPaddingException e) {
//			throw new Exception("密文数据已损坏");
//		}
//	}
//
//	public static void main(String[] args) throws Exception {
//		RSAEncrypt rsaEncrypt = new RSAEncrypt();
//		// rsaEncrypt.genKeyPair();
//
//		// 测试字符串
//		String encryptStr = "Test String Hello RSA!";
//
//		try {
//			// 加密
//			byte[] cipher = rsaEncrypt.encrypt(encryptStr.getBytes());
//			// 解密
//			byte[] plainText = rsaEncrypt.decrypt(cipher);
//			System.out.println("密文长度:" + cipher.length);
//			System.out.println(HexStrUtils.byteArray2HexStr(cipher));
//			System.out.println("明文长度:" + plainText.length);
//			System.out.println(HexStrUtils.byteArray2HexStr(plainText));
//			System.out.println(new String(plainText));
//		} catch (Exception e) {
//			System.err.println(e.getMessage());
//		}
//	}
//
////	private static String read(String path) {
////		try {
////			File file = new File(path);
////			BufferedReader br = new BufferedReader(new FileReader(file));
////			String line = "";
////			StringBuffer sb = new StringBuffer();
////			while ((line = br.readLine()) != null) {
////				if (line.charAt(0) == '-') {
////					continue;
////				} else {
////					sb.append(line);
////					sb.append('\r');
////				}
////			}
////			br.close();
////			String result = sb.toString();
////
////			return result;
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
////		return null;
////
////	}
//}
