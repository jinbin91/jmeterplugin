package com.test.jna;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.BitSet;

public class JnaDemo {

    public interface  Library extends StdCallLibrary{
        //加载动态库文件
        Library A50Library=(Library) Native.loadLibrary("D:\\工作资料\\A50\\FixApi50\\fixapi50.dll",Library.class);
        //申明动态库文件中的方法

        /**
         * 5.1 Fix_Initialize 接口库初始化
         * @return 返回true表示初始化成功；False表示失败
         */
        public Boolean Fix_Initialize();

        /**
         * 5.2 Fix_Uninitialize 接口库卸载
         * @return 返回true表示初始化成功；False表示失败
         */
        public Boolean Fix_Uninitialize();
        /**
         * 5.3 Fix_SetAppInfo 设置第三方软件名称和版本
         * @param sAppName 第三方软件名称
         * @param sVer  第三方软件版本
         * @return 返回true表示初始化成功；False表示失败
         */
         public Boolean Fix_SetAppInfo(String sAppName, String sVer);
        /**
         * 5.5 Fix_Connect 连接顶点中间件服务器
         * @param sAddr 连接的服务器地址，格式为: "ip地址@端口/tcp"
         * @param sUser 通信用户名称
         * @param sPwd  通信用户的密码
         * @param nTimeOut 连接超时的时间
         * @return 系统返回类型为HANDLE_CONN的连接句柄 如果连接失败则返回0; 成功不为0;
         */
        public long Fix_Connect(String sAddr, String sUser , String sPwd , int nTimeOut);
        /**
         * 5.6 Fix_Close 关闭顶点中间件连接
         * @param conn 类型为HANDLE_CONN的连接句柄
         * @return 返回值为True表示成功; False表示失败。
         */
        public Boolean Fix_Close(Long conn);
        /**
         * 5.8 Fix_AllocateSession 申请会话句柄
         * @param conn [in] 连接句柄
         * @return 返回值类型为HANDLE_SESSION的会话对象；如果对象值为空表示对象分配失败。否则表示成功。
         */
        public long   Fix_AllocateSession(long conn);
        /**
         *5.9 Fix_ReleaseSession 释放会话句柄
         * @param sess [in] 会话句柄
         * @return 返回值为True表示成功; False表示失败。
         */
        public Boolean Fix_ReleaseSession(long sess);

        /**
         * 5.21 Fix_CreateHead 设置会话的业务功能号
         * @param sess 会话句柄
         * @param sFunc 系统提供的业务功能号
         * @return 返回值为True表示成功; False表示失败。
         */
        public Boolean Fix_CreateHead(long sess,String sFunc);

        /**
         * 5.22 Fix_SetString 设置字符类型请求数据
         * @param sess 会话句柄
         * @param id 请求域的tag值,参考【第三方接入业务接口文档】。
         * @param val 字符串类型，对应于id的业务数据
         * @return   返回值为True表示成功; False表示失败。
         */
        public Boolean Fix_SetString(long sess, int id,String val);

        /**
         * 5.23 Fix_SetLong 设置整型类型请求数据
         * @param sess 会话句柄
         * @param id 请求域的tag值，参考【第三方接入业务接口文档】
         * @param val 整型，对应于id的业务数据
         * @return
         */
        public Boolean Fix_SetLong(long sess , int id, Long  val);

        /**
         *5.24 Fix_SetDouble设置浮点类型请求数据
         * @param sess 会话句柄
         * @param id 请求域的tag值，参考【第三方接入业务接口文档】。
         * @param val 浮点类型，对应于id的业务数据
         * @param precise = 6 浮点数的精度
         * @return
         */
        public Boolean Fix_SetDouble(long sess, int id, double val, int precise);
        /**
         * 5.26 Fix_Run 业务提交
         * @param sess [in] 会话句柄
         * @return 返回值为True表示服务业务处理成功; False表示失败,这个失败是表示业务通信上的失败；失败后，可以通过Fix_GetCode取出错误码(必定是一个负数)。通过Fix_GetErrMsg取出错误的信息
         */
        public Boolean Fix_Run(long sess);

        /**
         * 5.30 Fix_GetCode 读取错误编码
         * @param sess [in] 会话句柄
         * @return 返回整数
         */
        public int Fix_GetCode(long sess);
        /**
         * 5.31 Fix_GetErrMsg 读取错误信息
         * @param sess [in] 会话句柄
         * @param out [in/out] 用于输出错误信息的字符串
         * @param outlen [in/out] 参数out缓冲区的大小，返回字符串长度
         * @return
         */
        public String Fix_GetErrMsg(long sess, byte[] out, int[] outlen);

        /**
         * 5.32 Fix_GetCount 读取应答总行数
         * @param sess [in] 会话句柄
         * @return 返回行数量
         */
        public int Fix_GetCount(long sess);
    }
    public static void main(String[] args) throws UnsupportedEncodingException {
        Library library=Library.A50Library;
        Boolean result=library.Fix_Initialize();
        System.out.println("fix_initialize result is "+result);

        Boolean result3=library.Fix_SetAppInfo("东吴证券测试","V1.0.0");
        System.out.println("fix_setAppInfo result is "+result3);

        long result4=library.Fix_Connect("10.26.7.20@1715/tcp","","",0);
        System.out.println("申请连接句柄 result is "+result4);

        long result6=library.Fix_AllocateSession(result4);
        System.out.println("申请会话句柄 result is "+result6);

        Boolean result8=library.Fix_CreateHead(result6,"100001");
        System.out.println("设置会话的业务功能号 result is "+result8);
        //设置参数
        Boolean result9=library.Fix_SetString(result6,605,"1000");
        Boolean result10=library.Fix_SetString(result6,781,"1000");
        Boolean result11=library.Fix_SetString(result6,598,"1000");
        //业务提交
        boolean result12=library.Fix_Run(result6);
        System.out.println("业务提交返回结果："+result12);
        //读取错误信息码
        int result13=library.Fix_GetCode(result6);
        System.out.println("读取错误信息码："+result13);
        //读取错误信息
        byte[] out=new byte[10000];
        int[] n=new int[1000];
        n[0]=1000;
        library.Fix_GetErrMsg(result6,out ,n);
        String result14 = new String(out,"GB2312");
        String encoded = Base64.getEncoder().encodeToString(out);
        System.out.println("读取错误信息："+result14);



        Boolean result7=library.Fix_ReleaseSession(result6);
        System.out.println("关闭会话句柄 result is"+result7);

        Boolean result5=library.Fix_Close(result4);
        System.out.println("关闭连接句柄 result is "+result5);

        Boolean result2=library.Fix_Uninitialize();
        System.out.println("fix_uninitialzie result is "+result2);
    }
}
