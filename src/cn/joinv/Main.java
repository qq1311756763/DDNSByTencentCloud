package cn.joinv;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        System.out.println("Please choose the work mod:");
        System.out.println("1.DDNSClient   2.ReturnIP");
        Scanner sc=new Scanner(System.in);
        String choose = sc.next();
        if(choose.equals("1"))//DDNSClient
        {
            String myIP = "";
            while(true) {
                String tmpStr = getIP();
                if(myIP.equals(tmpStr))
                {
                    System.out.println("IP未改变，30s后重新检测！");
                }
                else {
                    System.out.println("IP改变！");
                    myIP=tmpStr;
                    String gitStr = "";
                    gitStr += "POSTcns.api.qcloud.com/v2/index.php?";
                    String gitStr1 ="Action=RecordModify&Nonce="+ (int) (Math.random() * 90000 + 10000);

                    //String gitStr1 = "Action=RecordModify&Nonce="+"12345";

                    gitStr1 += "&Region=&SecretId=AKIDMelgUBR7JKBqW6OimgUOCpGVkrnXqE7o&SignatureMethod=HmacSHA256&Timestamp=";
                    gitStr1 += (long) System.currentTimeMillis() / 1000;

                    //gitStr1 += "1506502810";

                    gitStr1 += "&domain=gitdraw.cn&recordId=321515145&recordLine=默认&recordType=A&subDomain=test&value=" + getIP();

                    gitStr+=gitStr1;

                    System.out.println(gitStr);

                    //String key = new sun.misc.BASE64Encoder().encode(HMACSHA256(gitStr.getBytes(), "uhJcWFn9OXVOCpIZebP6KOGyXzTbdZVW".getBytes()));
                    String key = encodeBase64(HMACSHA256(gitStr.getBytes(), "uhJcWFn9OXVOCpIZebP6KOGyXzTbdZVW".getBytes()));
                    //String key = sha256_HMAC(gitStr,"uhJcWFn9OXVOCpIZebP6KOGyXzTbdZVW");
                    //String key = new sun.misc.BASE64Encoder().encode(HMACSHA256("POSTcns.api.qcloud.com/v2/index.php?Action=RecordModify&Nonce=27537&Region=&SecretId=AKIDMelgUBR7JKBqW6OimgUOCpGVkrnXqE7o&SignatureMethod=HmacSHA256&Timestamp=1506502371&domain=gitdraw.cn&recordId=321515145&recordLine=默认&recordType=A&subDomain=test&value=114.114.114.114".getBytes(), "uhJcWFn9OXVOCpIZebP6KOGyXzTbdZVW".getBytes()));

                    try {
                        key = URLEncoder.encode(key, "UTF-8");
                        System.out.println(key);
                        gitStr1+="&Signature="+key;
                        String rt = sendPost("https://cns.api.qcloud.com/v2/index.php",gitStr1);
                        System.out.println(rt);

                    } catch (UnsupportedEncodingException e) {
                        System.out.println(e.toString());
                    }
                }
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
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

    public static String getIP()
    {

        ServerSocket ss;
        Socket socket;
        BufferedReader in;
        PrintWriter out;
        while (true) {
            try {
                socket = new Socket("joinv.cn", 10000);

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println("getIP");
                out.flush();

                //BufferedReader line = new BufferedReader(new InputStreamReader(System.in));
                String line = in.readLine();
                String myIP = line;
                System.out.println("本机ip:"+myIP);

                out.close();
                in.close();

                socket.close();


                return myIP;

            } catch (IOException e) {
                System.out.println("fuck!connect failed!");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
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





    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    //=========================



    public static String encodeBase64(byte[] str)
    {
        byte[] res = Base64.encodeBase64(str);
        return new String(res);
    }
}



