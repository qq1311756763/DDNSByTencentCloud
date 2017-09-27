package cn.joinv;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        System.out.println("Please choose the work mod:");
        System.out.println("1.DDNSClient   2.ReturnIP");
        Scanner sc=new Scanner(System.in);
        String choose = sc.next();
        if(choose.equals("1"))//DDNSClient
        {
            String gitStr ="";
            gitStr+="POSTcns.api.qcloud.com/v2/index.php?Action=RecordModify&Nonce=";
            gitStr+=(int)(Math.random()*90000+10000);
            gitStr+="&Region=&SecretId=AKIDMelgUBR7JKBqW6OimgUOCpGVkrnXqE7o&SignatureMethod=HmacSHA256&Timestamp=";
            gitStr+=(long)System.currentTimeMillis()/1000;
            gitStr+="&domain=gitdraw.cn&recordId=321515145&recordLine=默认&recordType=A&subDomain=test&value="+getIPbyWeb();

            System.out.println(gitStr);

            String key = new sun.misc.BASE64Encoder().encode(HMACSHA256(gitStr.getBytes(),"uhJcWFn9OXVOCpIZebP6KOGyXzTbdZVW".getBytes()));


            try {
                key = URLEncoder.encode(key, "UTF-8");
                System.out.println(key);
            } catch (UnsupportedEncodingException e) {
                System.out.println(e.toString());
            }
        }
        else if (choose.equals("2"))//返回访问者的ip
        {
            ServerSocket ss;
            Socket socket;
            BufferedReader in;
            PrintWriter out;
            try
            {
                ss = new ServerSocket(10000);

                System.out.println("The server is waiting your input...");

                while(true)
                {
                    socket = ss.accept();

                    System.out.println(socket.getRemoteSocketAddress());

                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(socket.getOutputStream(), true);
                    String line = in.readLine();

                    if(line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
                        System.out.println("server exit");
                        break;
                    }
                    else if(line.equals("getIP")) {
                        String tmp = socket.getRemoteSocketAddress().toString();
                        String[] tmps = tmp.split("/");
                        tmp = tmps[1];
                        String[] tmps1 = tmp.split(":");
                        String usrIP = tmps1[0];
                        out.println(usrIP);
                        out.flush();
                    }


                    System.out.println("you input is : " + line);

                    out.close();
                    in.close();
                    socket.close();


                }

                ss.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
      }



    /*
    通过互联网IP查询网站获取本机公网IP 2017/9/21
    因为互联网免费ip查询网站地址经常改变，该方法不一定一直可用，可自行根据不同的网页尝试修改
    */
    public static String getIPbyWeb()
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
