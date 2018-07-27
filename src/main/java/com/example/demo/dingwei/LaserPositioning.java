package com.example.demo.dingwei;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author zhaowb
 * @Date 2018-6-11 15:16:39
 */
public class LaserPositioning {

    // 总长度，单位为米
    private final static double Y_TOTAL_LENGTH = 90.00;
    // 总长度差值
    private final static double Y_TOTAL_LENGTH_DIFFERENCE = 2.00;

    // 当一个Y为0，另一个Y不为0时的连续忽略次数
    private final static int CONTINUE_IGNORE_Y_COUNT = 5;
    // Y 值与上次的差值
    private final static double DIFFERENCE_FROM_LAST_Y = 1.00;
    // 有效Y值
    private static double pre_valid_value = 0.00;
    // Y 实际连续忽略次数
    private static int ignore_count_y = 0;

    // 第一个激光UDP Client 设置 start
    //远程IP
    private final static String HOST_X = "129.9.1.100";
    // 远程端口
    private final static int PORT_X = 11000;
    private static InetAddress inetAddress_x = null;
    // 数据报套接字
    private static DatagramSocket datagramSocket_x = null;
    // 用以发送数据报
    private static DatagramPacket datagramPacket_x = null;
    // 用以接受数据报
    private static DatagramPacket recePacket_x = null;
    // 第一个激光UDP Client 设置 end

    // 第二个激光UDP Client 设置 start
    // 远程端口
    private final static int PORT_Y1 = 11001;
    //远程IP
    private final static String HOST_Y1 = "129.9.1.101";

    private static InetAddress inetAddress_y1 = null;
    // 数据报套接字
    private static DatagramSocket datagramSocket_y1 = null;
    // 用以发送数据报
    private static DatagramPacket datagramPacket_y1 = null;
    // 用以接受数据报
    private static DatagramPacket recePacket_y1 = null;
    // 第二个激光UDP Client 设置 end

    // 第三个激光UDP Client 设置 start
    //远程IP
    private final static String HOST_Y2 = "129.9.1.102";
    // 远程端口
    private final static int PORT_Y2 = 11002;
    private static InetAddress inetAddress_y2 = null;
    // 数据报套接字
    private static DatagramSocket datagramSocket_y2 = null;
    // 用以发送数据报
    private static DatagramPacket datagramPacket_y2 = null;
    // 用以接受数据报
    private static DatagramPacket recePacket_y2 = null;
    //第三个激光UDP Client 设置 end

    static {
        try {
            datagramSocket_x = new DatagramSocket(PORT_X);
            inetAddress_x = InetAddress.getByName(HOST_X);

            datagramSocket_y1 = new DatagramSocket(PORT_Y1);
            inetAddress_y1 = InetAddress.getByName(HOST_Y1);

            datagramSocket_y2 = new DatagramSocket(PORT_Y2);
            inetAddress_y2 = InetAddress.getByName(HOST_Y2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取数据
     * <p>
     *
     * @return 读取到的数据 解析成JSON {X:100,Y:55} 发送到云端
     */
    public static void read() {
//        try {
//            //第一个激光收发数据
//            byte[] str = "MS:SINGLE".getBytes();
//            datagramPacket_x = new DatagramPacket(str, str.length, inetAddress_x, PORT_X);
//            datagramSocket_x.send(datagramPacket_x);
//
//            byte[] buf = new byte[1024];
//            recePacket_x = new DatagramPacket(buf, buf.length);
//            datagramSocket_x.receive(recePacket_x);
//
//            Thread.sleep(10);
//            //第二个激光收发数据
//            datagramPacket_y1 = new DatagramPacket(str, str.length, inetAddress_y1, PORT_Y1);
//            datagramSocket_y1.send(datagramPacket_y1);
//
//            byte[] buf2 = new byte[1024];
//            recePacket_y1 = new DatagramPacket(buf2, buf2.length);
//            datagramSocket_y1.receive(recePacket_y1);
//
//            //第三个激光收发数据
//            datagramPacket_y2 = new DatagramPacket(str, str.length, inetAddress_y2, PORT_Y2);
//            datagramSocket_y2.send(datagramPacket_y2);
//
//            byte[] buf3 = new byte[1024];
//            recePacket_y2 = new DatagramPacket(buf3, buf3.length);
//            datagramSocket_y2.receive(recePacket_y2);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        String in_x = new String(recePacket_x.getData(), 0, recePacket_x.getLength());
//        String in_y1 = new String(recePacket_y1.getData(), 0, recePacket_y1.getLength());
//        String in_y2 = new String(recePacket_y2.getData(), 0, recePacket_y2.getLength());

        String in_x = "D:1.00m";
        String in_y1 = "D:9.6m";
        String in_y2 = "D:89.00m"; // 测试用的数据

        double y_result = 0.0;
        double y1 = 0.0;
        double y2 = 0.0;

        boolean is_x_valid = judgString(in_x);
        boolean is_y1_valid = judgString(in_y1);
        boolean is_y2_valid = judgString(in_y2);

        if (!is_x_valid || (!is_y1_valid && !is_y2_valid)) {
            return; // 丢弃全部数据，等待下一次执行read()（200ms之后）方法收发数据
        } else if (is_y1_valid && is_y2_valid) {

            y1 = Double.parseDouble(in_y1.substring(in_y1.indexOf("D:") + 2, in_y1.indexOf("m")));
            y2 = Double.parseDouble(in_y2.substring(in_y2.indexOf("D:") + 2, in_y2.indexOf("m")));

            //两个y如何相互校验：
            if (Math.abs(y1 + y2 - Y_TOTAL_LENGTH) < Y_TOTAL_LENGTH_DIFFERENCE) {
                y_result = Math.min(y1, y2);
            } else {
                double min_y = Math.min(y1, y2);
                if (judegY(min_y)) {
                    y_result = min_y;
                } else {
                    return; // 丢弃全部数据，等待下一次执行read()（200ms之后）方法收发数据
                }
            }
        } else { //只有一个Y有效，做下面的处理
            double temp_y = 0.0;

            if (judgString(in_y1)) {
                temp_y = Double.parseDouble(in_y1.substring(in_y1.indexOf("D:") + 2, in_y1.indexOf("m")));
            } else {
                temp_y = Double.parseDouble(in_y2.substring(in_y2.indexOf("D:") + 2, in_y2.indexOf("m")));
            }

            if (judegY(temp_y)) {
                y_result = temp_y;
            } else {
                return; // 丢弃全部数据，等待下一次执行read()（200ms之后）方法收发数据
            }
        }

        double x = Double.parseDouble(in_x.substring(in_x.indexOf("D:") + 2, in_x.indexOf("m")));
        String json = "{\"X\":" + x + ",\"Y\":" + y_result + "}";

        sendToCloud(json);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断String 是够为null "", 0,0.0,0.00 若是返回false ,否则返回true
     *
     * @param str 需要判断的 String
     * @return
     */
    public static boolean judgString(String str) {

        if ((str == null || "".equals(str))) {
            return false;
        } else {
            str = str.substring(str.indexOf("D:") + 2, str.indexOf("m"));
            if (("0.00".equals(str) || "0".equals(str) || "0.0".equals(str))) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * 判断 Y 值是否有效
     *
     * @param y_coordnate Y值
     * @return
     */
    public static boolean judegY(double y_coordnate) {

        if (pre_valid_value == 0.00) {
            pre_valid_value = y_coordnate;
        } else {
            // 一次移动超过1m 假定 超过 范围，丢弃数据
            if (Math.abs(y_coordnate - pre_valid_value) > DIFFERENCE_FROM_LAST_Y) {

                if (ignore_count_y < CONTINUE_IGNORE_Y_COUNT) {
                    ignore_count_y++;
                    return false;
                } else {
                    ignore_count_y = 0;
                    pre_valid_value = y_coordnate;
                    return true;
                }
            } else {
                ignore_count_y = 0;
                pre_valid_value = y_coordnate;
                return true;
            }
        }
        return true;
    }

    /**
     * 发送数据到云端
     *
     * @param str
     */
    public static void sendToCloud(String str) {

        System.out.println(str);
    }

    public static void main(String[] args) {
        boolean flag = true;
        while (flag) {
            read();
        }
    }
}