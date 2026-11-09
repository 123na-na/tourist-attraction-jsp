package util;

import java.security.MessageDigest;

public class Md5 {
	
	/**
	 * 获取str的MD5散列值，返回32位“加密”后的字符串。<br>
	 * 可在百度中搜索java md5，查找MD5的源代码。注意：不同的MD5类，生成的加密结果可能不相同
	 * @param  str为需“加密”的字符串
	 * @return 32位的MD5字符串
	 */
	public final static String getMd5(String str) {						//static类型，可直接调用此方法
       try {
            str += "cc856skdskgf";										//附加自定义的字符串。（放点盐）
            
        	byte[] btInput = str.getBytes();         					//输入的字符串转换成字节数组		
            MessageDigest mdInst = MessageDigest.getInstance("MD5");	//获得MD5摘要算法的 MessageDigest对象		
            mdInst.update(btInput);										//使用指定的字节更新摘要		
            byte[] md = mdInst.digest();								//获得密文
            				
            int  length = md.length;
            char result[] = new char[length * 2];
            char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
            byte b;
            int  k = 0;
            
            for (int i = 0; i < length; i++) {							//把密文转换成十六进制的字符数组
                b = md[i];
                result[k++] = hexDigits[b >>> 4 & 0xf];
                result[k++] = hexDigits[b & 0xf];
            }
            
            return (new String(result));								//将字符数组合成一个32位字符串并返回
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
