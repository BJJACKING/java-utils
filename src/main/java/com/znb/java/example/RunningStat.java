package com.znb.java.example;

/**
 * @author zhangnaibin@xiaomi.com
 * @time 2016-01-27 下午3:45
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Signal;
import sun.misc.SignalHandler;

@SuppressWarnings("restriction")
public class RunningStat implements SignalHandler{
    static final Logger LOGGER = LoggerFactory.getLogger(RunningStat.class);
    private static boolean stopFlag = false;
    private static String hostip = "";

    static {
        hostip = "";
    }

    public static String getHostIp() {
        return hostip;
    }

    public static void setRunning() {
        stopFlag = false;
    }

    public static void setStoping() {
        stopFlag = true;
    }

    public static Boolean isStoping() {
        return stopFlag;
    }

    public static void signalHandle() {
        setRunning();
    }

    @Override //信号发生后执行的操作
    public void handle(Signal signalName) {
        LOGGER.info("recieve signal:" + signalName);
        setStoping();
    }

    //注册INT、TERM(god restart时发送此信号)，当进程接收到这些信号时，进程平滑退出
    public static void installSignal(){
        //注册信号
        RunningStat signalHandler = new RunningStat();
        Signal.handle(new Signal("TERM"), signalHandler);
        Signal.handle(new Signal("INT"), signalHandler);
    }
}
