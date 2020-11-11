package com.andr.common.tool.encode;

/**
 * RC4加解密算法
 * @author
 */
public class RC4
{
	public static final String RC4_KEY = "swXs78Gsh&b$90Smx";//RC4加密算法对应密钥
	
    public static String decry_RC4(byte[] data, String key)
    {
		if (data == null || key == null) 
		{
		    return null;
		}
		return asString(RC4Base(data, key));
    }

    public static String decry_RC4(String data, String key)
    {
		if (data == null || key == null) 
		{
		    return null;
		}
		return new String(RC4Base(HexString2Bytes(data), key));
    }

    public static byte[] encry_RC4_byte(String data, String key)
    {
		if (data == null || key == null) 
		{
		    return null;
		}
		byte b_data[] = data.getBytes();
		return RC4Base(b_data, key);
    }

    public static String encry_RC4_string(String data, String key)
    {
		if (data == null || key == null) 
		{
		    return null;
		}
		return toHexString(asString(encry_RC4_byte(data, key)));
    }

    private static String asString(byte[] buf)
    {
		StringBuffer strbuf = new StringBuffer(buf.length);
		for(byte i : buf)
		{
			strbuf.append((char) i);
		}
		return strbuf.toString();
    }

    private static byte[] initKey(String aKey)
    {
		byte[] b_key = aKey.getBytes();
		byte state[] = new byte[256];
	
		for (int i = 0; i < 256; i++) 
		{
		    state[i] = (byte) i;
		}
		int index1 = 0;
		int index2 = 0;
		if (b_key == null || b_key.length == 0) 
		{
		    return null;
		}
		for (int i = 0; i < 256; i++) 
		{
		    index2 = ((b_key[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
		    byte tmp = state[i];
		    state[i] = state[index2];
		    state[index2] = tmp;
		    index1 = (index1 + 1) % b_key.length;
		}
		return state;
    }

    private static String toHexString(String s)
    {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < s.length(); i++) 
		{
		    int ch = (int) s.charAt(i);
		    String s4 = Integer.toHexString(ch & 0xFF);
		    if (s4.length() == 1)
		    {
			s4 = '0' + s4;
		    }
		    str.append(s4);
		}
		return str.toString();// 0x表示十六进制
    }

    private static byte[] HexString2Bytes(String src)
    {
		int size = src.length();
		byte[] ret = new byte[size / 2];
		byte[] tmp = src.getBytes();
		
		try 
		{
			for (int i = 0; i < size / 2; i++) 
			{
			    ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return ret;
    }

    private static byte uniteBytes(byte src0, byte src1)
    {
    	byte ret = 0;
    	
    	try 
    	{
    		char _b0 = (char) Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
    		_b0 = (char) (_b0 << 4);
    		char _b1 = (char) Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
    		ret = (byte) (_b0 ^ _b1);
		} 
    	catch (Exception e)
    	{
			e.printStackTrace();
		}
    	
		return ret;
    }

    private static byte[] RC4Base(byte[] input, String mKkey)
    {
		int x = 0;
		int y = 0;
		byte key[] = initKey(mKkey);
		int xorIndex;
		byte[] result = new byte[input.length];
		try 
		{
			for (int i = 0; i < input.length; i++) 
			{
			    x = (x + 1) & 0xff;
			    y = ((key[x] & 0xff) + y) & 0xff;
			    byte tmp = key[x];
			    key[x] = key[y];
			    key[y] = tmp;
			    xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
			    result[i] = (byte) (input[i] ^ key[xorIndex]);
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
    }
}