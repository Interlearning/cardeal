package br.com.cardeal.globals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.text.MaskFormatter;
import javax.xml.bind.DatatypeConverter;

import br.com.cardeal.model.Company;

public class Utils {
	
	public static boolean isClear(String textField) {
		if(textField == null || textField.length() == 0 || textField.equals(" "))
			return true;
		return false;
	}
	
	public static int HexStrToInt(String s) {
        if (s.length() >= 2) {
            if (s.charAt(0) == 'H')
                s = s.substring(1);
            return Integer.parseInt(s, 16);            
        }
        return 0;
    }
	
	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static final String toHexString(byte[] array) {
	    return DatatypeConverter.printHexBinary(array);
	}

	public static final byte[] toByteArray(String s) {
		if(s != null && s.length() >= 2) {
			if(s.charAt(0) == 'H')
				s = s.substring(1);
			return DatatypeConverter.parseHexBinary(s);
		}
		return null;
	    
	}
	
	public static long byteArrayToLong(byte[] array) {
		int offset = 0;
		 return
	      ((long)(array[offset]   & 0xff) << 56) |
	      ((long)(array[offset+1] & 0xff) << 48) |
	      ((long)(array[offset+2] & 0xff) << 40) |
	      ((long)(array[offset+3] & 0xff) << 32) |
	      ((long)(array[offset+4] & 0xff) << 24) |
	      ((long)(array[offset+5] & 0xff) << 16) |
	      ((long)(array[offset+6] & 0xff) << 8) |
	      ((long)(array[offset+7] & 0xff));	  
	}
	
	
	public static final byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
		}	
	
	public static final byte[] longToByteArray(long value) {
        return new byte[] {
                (byte)(value >>> 56),
                (byte)(value >>> 48),
                (byte)(value >>> 40),
                (byte)(value >>> 32),
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
		}	
	
	public static final byte[] dwordToByteArray(long value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
		}	
	
	public static final byte[] wordToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 8),
                (byte)value};
		}	
	
	public static final long byteArrayToLong(byte[] data, int index) {
		int value = 0;
		int offset = index * 4;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (data[i + offset] & 0x000000FF) << shift;
        }
        return value;		
	}

	public static final int byteArrayToInt(byte[] data, int index) {
		int value = 0;
		int offset = index * 4;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (data[i + offset] & 0x000000FF) << shift;
        }
        return value;		
	}


	public static final int byteArrayToIntLsbFirst(byte[] data, int index) {
		int value = 0;
		int offset = index * 4;
        for (int i = 0; i < 4; i++) {
            int shift = i * 8;
            value += (data[i + offset] & 0x000000FF) << shift;
        }
        return value;		
	}

	public static final long byteArrayToLongLsbFirst(byte[] data, int index) {
		int value = 0;
		value += data[index++];
		value += (data[index++] << 8);
		value += (data[index++] << 8 * 2);
		value += (data[index++] << 8 * 3);
		value += (data[index++] << 8 * 4);
		value += (data[index++] << 8 * 5);
		value += (data[index++] << 8 * 6);
		value += (data[index++] << 8 * 7);
		
		return value;
	}
	
	public static final long strToLong(String source) {
		return new BigInteger(source, 16).longValue();
	}

	public static byte[] subbytes(byte[] source, int srcBegin, int size) {
	    byte destination[];
	
	    destination = new byte[size];
	    getBytes(source, srcBegin, size, destination, 0);
	
	    return destination;
	}
	
	
	public static void getBytes(byte[] source, int srcBegin, int size, byte[] destination,
	      int dstBegin) {
	    System.arraycopy(source, srcBegin, destination, dstBegin, size);
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	    return true;
	}

	public static boolean isLong(String s) {
	    try { 
	        Long.parseLong(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	    return true;
	}
	
	
	public static int generateGid() {
		int min = 0;
		int max = 16777216;
		int randomNumber = new Random().nextInt(max - min) + min;
		return randomNumber;
	}
	
	public static String bytesToHex(byte[] bytes) {
	    final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	    char[] hexChars = new char[bytes.length * 2];
	    int v;
	    for ( int j = 0; j < bytes.length; j++ ) {
	        v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	public static String toHex(int n) {
		return Integer.toHexString(n);
	}
	
	public static Date now() {
		return new Date();
	}
	
	public static String clearCnpj(String cnpj) {
		if(cnpj == null)
			return null;
		StringBuilder b = new StringBuilder();
		for(int i = 0; i < cnpj.length(); i++) {
			char c = cnpj.charAt(i);
			if(c >= '0' && c <= '9')
				b.append(c);
		}
		return b.toString();
	}

	public static String formatToCnpjOrCpf(String value) {
		if(value != null && value.length() == 11)
			return format("###.###.###-##", value);
		if(value != null && value.length() == 14)
			return format("##.###.###/####-##", value);
		
		return value;
	}
	
	public static String format(String pattern, Object value) {
        MaskFormatter mask;
        try {
            mask = new MaskFormatter(pattern);
            mask.setValueContainsLiteralCharacters(false);
            return mask.valueToString(value);
        } catch (Exception e) {
            return value.toString();
        }
    }
	
	public static List<String> buildList(String value) {
		if(value == null)
			return null;
		
		String[] values = value.split("\r\n");
		List<String> result = new ArrayList<String>();
		for(String s : values) {
			if ( !s.isEmpty() ) result.add(s);
		}
		return result;
	}
	
	public static String buildFromList(List<String> list) {
		if(list == null)
			return null;
		StringBuilder b = new StringBuilder();
		for(String s : list) {
			b.append(s);
			b.append("\r\n");
		}
		return b.toString();
	}
	
	public static String format1(double value) {
		return String.format("%.1f", value).replace(',', '.');
	}
	
	public static String formatWeight(double value) {
		return String.format("%.3f", value).replace(',', '.');
	}
	
	public static String formatProductId(int id) {
		return String.format("%04d", id);
	}
	
	public static String formatQty(int qty) {
		return String.format("%04d", qty);
	}
	
	public static String formatSerial(long id) {
		return String.format("%08d", id);
	}
	
	public static String formatFilial(long id) {
		return String.format("%0"+Company.getLengthFieldId()+"d", id);
	}	
	
	public static String formatPallet(long id) {
		return String.format("%06d", id);
	}	
	
	public static void copyFile(File origem, File destino){
		
		InputStream in = null;
		OutputStream out = null;
		
		try {
			in = new FileInputStream(origem);
			out = new FileOutputStream(destino);
			byte[] buf = new byte[1024];
			int len;
			
			while ( (len = in.read(buf) ) > 0 ){
				out.write(buf, 0, len);
			}		
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			
			try {
				
				if ( in != null){
					in.close();
				}
				
				if ( out != null){
					out.close();
				}
				
			} catch (IOException e) {				
				e.printStackTrace();
			}		
			
		}
		
	}
	
	public static void copyDirectory(File srcDir, File dstDir)  {
        
		if (srcDir.isDirectory()) {
            if (!dstDir.exists()) {
                dstDir.mkdir();
            } 
            String[] children = srcDir.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(srcDir, children[i]),
                                     new File(dstDir, children[i]));
            }
        } else {
            copyFile(srcDir, dstDir);
        }
    }
	
	public static boolean createDir(String dir) throws Exception
    {
        boolean dirCreated = false;
        File diretorio = new File(dir);
        
        if ( !diretorio.exists() ) 
        {
            diretorio.mkdirs();  
        }
        
        if ( diretorio.exists() ) 
        {
            dirCreated = true;
        }
        
        diretorio = null;
        
        return dirCreated;
    }
    
    public static String encrypt( String text ) 
    {   
                
        String returnEncrypt = null;
        
        if ( text != null && !text.isEmpty() )
        {
            try 
            {                              
               returnEncrypt = PswSecurity.encrypt(text);
            }  
            catch ( Exception e ) 
            {   
               e.printStackTrace();   
            }   
            
        }

        return returnEncrypt;   
    }
    
    public static String decrypt( String text ) 
    {   
                
        String returnEncrypt = null;
        
        if ( text != null && !text.isEmpty() )
        {
            try 
            {                              
               returnEncrypt = PswSecurity.decrypt(text);
            }  
            catch ( Exception e ) 
            {   
               e.printStackTrace();   
            }   
            
        }

        return returnEncrypt;   
    }
	
}
