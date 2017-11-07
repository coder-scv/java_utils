
package cn.freedom.commonsutils;

public class ThreadUtil {

    public static void sleep(int time) {
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void sleepMsec(int time) {
    	try {
    		Thread.sleep(time);
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	}
    }
}
