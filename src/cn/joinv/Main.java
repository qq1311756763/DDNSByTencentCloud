package cn.joinv;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class Main {
    public static void main(String[] args) {

        /*for(int i = 0; i < 4; i++){
            System.out.println("第" + (i + 1) + "位随机数为：" + );
        }
*/

        //(int)(Math.random()*90000+10000)
        //System.currentTimeMillis()

        /*String a =new sun.misc.BASE64Encoder().encode(HMACSHA256("GETcvm.api.qcloud.com/v2/index.php?Action=DescribeInstances&InstanceIds.0=ins-09dx96dg&Nonce=11886&Region=ap-guangzhou&SecretId=AKIDz8krbsJ5yKBZQpn74WFkmLPx3gnPhESA&SignatureMethod=HmacSHA256&Timestamp=1465185768".getBytes(),"Gu5t9xGARNpq86cd98joQYCN3Cozk1qA".getBytes()));
        System.out.println(a);*/

        //String gitStr = "POSTcns.api.qcloud.com/v2/index.php?Action=RecordModify&Nonce=279225849&Region=&SecretId=AKIDMelgUBR7JKBqW6OimgUOCpGVkrnXqE7o&SignatureMethod=HmacSHA256&Timestamp=1506409089";
        //String gitStr ="POSTcns.api.qcloud.com/v2/index.php?Action=RecordModify&Nonce=73697&Region=&SecretId=AKIDMelgUBR7JKBqW6OimgUOCpGVkrnXqE7o&SignatureMethod=HmacSHA256&Timestamp=1506416650&domain=gitdraw.cn&recordId=321515145&recordLine=默认&recordType=A&subDomain=test&value=114.114.114.114";
        /*gitStr+="POSTcns.api.qcloud.com/v2/index.php?Action=RecordModify&Nonce=";*/

        String gitStr ="";
        gitStr+="POSTcns.api.qcloud.com/v2/index.php?Action=RecordModify&Nonce=";
        gitStr+=(int)(Math.random()*90000+10000);
        gitStr+="&Region=&SecretId=AKIDMelgUBR7JKBqW6OimgUOCpGVkrnXqE7o&SignatureMethod=HmacSHA256&Timestamp=";
        gitStr+=(long)System.currentTimeMillis()/1000;
        gitStr+="&domain=gitdraw.cn&recordId=321515145&recordLine=默认&recordType=A&subDomain=test&value="+getIP();

        System.out.println(gitStr);

        String key = new sun.misc.BASE64Encoder().encode(HMACSHA256(gitStr.getBytes(),"uhJcWFn9OXVOCpIZebP6KOGyXzTbdZVW".getBytes()));


        try {
            key = URLEncoder.encode(key, "UTF-8");
            System.out.println(key);
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.toString());
        }
      }



    /*通过互联网IP查询网站获取本机公网IP 2017年9月21日*/
    public static String getIP()
    {
        String a = null;
        try {
            String url = "http://ip.chinaz.com/";
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new URL(url).openConnection().getInputStream(), "utf-8"));//GB2312可以根据需要替换成要读取网页的编码

            String target = "";
            while ((a = in.readLine()) != null) {
                System.out.println(a);
                target = a;
            }
            String[] aa = target.split("\\[");
            String b = aa[1];
            String[] bb = b.split("\\]");
            //System.out.println("IP:"+bb[0]);
            return bb[0];
        } catch (MalformedURLException e) {
        } catch (IOException e) {
            return "-1";
        }
        return "-1";
    }



/*
*
* 腾讯云鉴权字符串加密编码处理
* */
    public static byte[] HMACSHA256(byte[] data, byte[] key)
    {
        try  {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            return mac.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String byte2hex(byte[] b)
    {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b!=null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

}
