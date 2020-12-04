package com.techshard.batch.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Util {

//    private String encKey;
    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;

//    public Util(String encKey) {
//        this.encKey = encKey;
//    }
String encKey = "a0c74ee5987324d9ecd30ce4a728f559";


    public Util() {
    }

//
//    public static byte[] hexToBytes(String str) {
//        if (str == null) {
//            return null;
//        } else if (str.length() < 2) {
//            return null;
//        } else {
//            int len = str.length() / 2;
//            byte[] buffer = new byte[len];
//            for (int i = 0; i < len; i++) {
//                buffer[i] = (byte) Integer.parseInt(str.substring(i  2, i  2 + 2), 16);
//            }
//            return buffer;
//        }
//    }



    public String encryptData(String data)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String endata = "";
        if ("".equals(data) || null == data) {
            return "";
        }
        byte byteKey[] = (byte[]) hex2byte(encKey);
        SecretKeySpec skeySpec = new SecretKeySpec(byteKey, "AES");
        Cipher cip = Cipher.getInstance("AES");
        cip.init(1, skeySpec);
        byte bytedata[] = cip.doFinal(data.getBytes("UTF-8"));
        endata = asHex(bytedata);
        return endata;
    }

    public String decryptData(String encrypted)
            throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        if ("".equals(encrypted) || null == encrypted) {
            return "";
        }
        byte byteKey[] = (byte[]) hex2byte(encKey);
        byte byteEncData[] = hex2byte(encrypted);
        SecretKeySpec skeySpec = new SecretKeySpec(byteKey, "AES");
        Cipher cip = Cipher.getInstance("AES");
        cip.init(2, skeySpec);
        byte original[] = cip.doFinal(byteEncData);
        String originalString = new String(original, "UTF-8");
        return originalString;
    }

    public String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        for (int i = 0; i < buf.length; i++) {
            if ((buf[i] & 0xff) < 16) {
                strbuf.append("0");
            }
            strbuf.append(Long.toString(buf[i] & 0xff, 16));
        }

        return strbuf.toString();
    }

    public static byte[] hex2byte(String s) {
        return hex2byte(s.getBytes(), 0, s.length() >> 1);
    }

    public static byte[] hex2byte(byte b[], int offset, int len) {
        byte d[] = new byte[len];
        for (int i = 0; i < len * 2; i++) {
            int shift = i % 2 == 1 ? 0 : 4;
            d[i >> 1] |= Character.digit((char) b[offset + i], 16) << shift;
        }
        return d;
    }



    public static String sha256BytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String bytesToHex(byte[] data) {
        if (data == null) {
            return null;
        }
        int len = data.length;
        String str = "";
        for (int i = 0; i < len; i++) {
            if ((data[i] & 0xFF) < 16) {
                str = str + "0" + Integer.toHexString(data[i] & 0xFF);
            } else {
                str = str + Integer.toHexString(data[i] & 0xFF);
            }
        }
        return str;
    }




   // static Random rd = new Random();
    public static String doRandomPassword(int len) {
        String resp = "";
        try {
            Random rd = new Random();
            final String alphabet = "1234567890!@$%^&*()~ASQWERTYUOPDFGHJKZXVBNM?qwertyuopasdfghjkzxcvbnm";
            final int N = alphabet.length();
            int iLength = len;
            StringBuilder sb = new StringBuilder(iLength);
            for (int i = 0; i < iLength; i++) {
                sb.append(alphabet.charAt(rd.nextInt(N)));
            }
            resp = sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }


//    public static boolean isNumeric(String number) {
//        boolean isValid = false;
//
//        String expression = "^[-+]?[0-9]*\\.?[0-9]+$";
//        CharSequence inputStr = number;
//        //Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
//        Pattern pattern = Pattern.compile(expression);
//        Matcher matcher = pattern.matcher(inputStr);
//        if (matcher.matches()) {
//            isValid = true;
//        }
//        return isValid;
//    }


    public static boolean validateString(String name) {
        String pattern = "^[a-z A-Z 0-9 & @ $ # . : ; , - '-]*$";
        return name.matches(pattern);
    }

    public static boolean isNumeric(String number) {
        String pattern = "^[0-9-']*$";
        return number.matches(pattern);
    }


    public static boolean validateName(String name) {
        String pattern = "^[a-z A-Z & @ $ # . : ; , - ']*$";
        return name.matches(pattern);
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public static boolean validateDecimalCharacter(String decimalCharacter) {
        boolean validFormat = false;
        try {
            // String regex = "^\\d{1,10}\\.\\d{0,2}$"; this has a decimal places of upto 2 e.g. 0,1,2

            String regex = "^\\d{1,10}\\.\\d{2}$";//this has 2 decimal places
            validFormat = decimalCharacter.matches(regex);

        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        return validFormat;
    }





}
