package cn.freedom.commonsutils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CmdUtils {
	public static String exec(String cmd) {
        Runtime run = Runtime.getRuntime();
        
        StringBuffer sb = new StringBuffer();
        
        try {
            Process process = run.exec(cmd);
            InputStream in = process.getInputStream();
           
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
            String line = "";
            while ((line = br.readLine()) != null) {
            	sb.append(line);
            	sb.append("\r\n");
            }
            in.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
