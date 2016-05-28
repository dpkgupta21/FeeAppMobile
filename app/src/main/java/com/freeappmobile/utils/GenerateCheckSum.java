package com.freeappmobile.utils;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class GenerateCheckSum {

    private String checksumKey;
    String id = "bl8spl";
    private String msg;
    private String CP_MSG = "CP1005!ABCD!12A6FE4478DD83BC927437FEE582A0B826C5439294E0333D6251E2A1E88A42E53E214C6F99CB31493683FF79FED2A2D9!NA!NA!NA";


    GenerateCheckSum(String msg) {
        this.msg = msg;
        this.checksumKey = Constant.checkSumId;

    }


    private void getCheckSum() {

        if (CP_MSG != null && CP_MSG.length() > 0)

        {
            // Token formation for HMAC
            // Input msg = <MSG>|<CP_MSG>
            // Output msg = <MSG>|<Checksum>|<CP_MSG>

            String strHMACMsg = msg + "|" + CP_MSG;


            System.out.println("token:-\t" + msg + "|" + HmacSHA256(strHMACMsg, checksumKey) + "|" + CP_MSG);
        }
    }


    public static String HmacSHA256(String message, String secret) {
        // MessageDigest md = null;
        try {

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(),
                    "HmacSHA256");
            sha256_HMAC.init(secret_key);

            byte raw[] = sha256_HMAC.doFinal(message.getBytes());

            StringBuffer ls_sb = new StringBuffer();
            for (int i = 0; i < raw.length; i++)
                ls_sb.append(char2hex(raw[i]));
            return ls_sb.toString(); // step 6
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String char2hex(byte x) {
        char arr[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F'};

        char c[] = {arr[(x & 0xF0) >> 4], arr[x & 0x0F]};
        return (new String(c));
    }


}
