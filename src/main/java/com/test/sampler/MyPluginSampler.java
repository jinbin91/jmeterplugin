package com.test.sampler;

import net.minidev.json.JSONObject;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class MyPluginSampler extends AbstractSampler implements Serializable {
    private static  final  long   serialVersionUID=230l;

    private static  final Logger log= LoggingManager.getLoggerForClass();
    //页面属性数据存放
    public static final String domain="domain.text";
    public static final String port="port.text";
    public static final String contentEncoding="contentEncoding.text";
    public static final String path="path.text";
    public static final String method="method.text";
    public static final String useKeepAlive="useKeepAlive.text";
//    public static final String postBodyContent="postBodyContent.text";
    public static final String ARGUMENTS = "mypluginSampler.Arguments"; // $NON-NLS-1$


    private static AtomicInteger classCount=new AtomicInteger(0);//keep track of classes created

    private String getTitle(){
        return this.getName();
    }

    //从控制面板获取domain输入的数据
    public String getDomain(){
        return getPropertyAsString(domain);
    }
    //从控制面板获取端口的输入的数据
    public String getPort(){
        return getPropertyAsString(port);
    }
    //从控制面板获取contentEncoding输入的数据
    public String getContentEncoding(){
        return getPropertyAsString(contentEncoding);
    }
    //从控制面板获取path输入的数据
    public String getPath(){
        return  getPropertyAsString(path);
    }
    //从gui获取method输入的数据
    public String getMethod(){
        return getPropertyAsString(method);
    }
    //从gui获取useKeepAlive的值
    public  String getUseKeepAlive(){
        return getPropertyAsString(useKeepAlive);
    }
//    //从gui获取postBody输入的数据
//    public String getPostBodyContent(){
//        return getPropertyAsString(postBodyContent);
//    }
   public Arguments getArguments() {
     return (Arguments) getProperty(ARGUMENTS).getObjectValue();
   }



    public MyPluginSampler(){
        setName("sampler练习示例");
        classCount.incrementAndGet();
        trace("第一个sampler练习");
    }

    private void trace(String s){
        String t1=getTitle();
        String tn=Thread.currentThread().getName();
        String th=this.toString();

        log.debug(tn+" ("+classCount.get()+") "+t1+" "+s+" "+th);
    }

    @Override
    public SampleResult sample(Entry entry) {
        trace("sample()");
        SampleResult res=new SampleResult();
        boolean isOK=false;//did sample is succeed?

        String response=null;
        //sampler data
        String domain=getDomain();
        String port=getPort();
        String contentEncoding=getContentEncoding();
        String path=getPath();
        String useKeepAlive=getUseKeepAlive();
        String method=getMethod();
//        String postBodyContent=getPostBodyContent();
        Arguments arguments=getArguments();

        JSONObject jo=new JSONObject();
        jo.put("domain",domain);
        jo.put("port",port);
        jo.put("contentEncoding",contentEncoding);
        jo.put("path",path);
        jo.put("useKeepAlive",useKeepAlive);
        jo.put("method",method);
        jo.put("arguments",arguments.toString());

        res.setSampleLabel(getTitle());
        /**
         * perform sampler resultData
         */
        res.sampleStart();
        try{
            response=Thread.currentThread().getName();
            res.setSamplerData("setSamleData!!!");
            res.setResponseData(jo.toString(),null);
            log.debug("json:"+jo.toString());
            res.setDataType(SampleResult.TEXT);

            res.setResponseCodeOK();
            res.setResponseMessage("OK");
            isOK=true;
        }catch (Exception e){
            log.debug(" ",e);
            res.setResponseCode("500");
            res.setResponseMessage(e.toString());
        }
        res.sampleEnd();
        res.setSuccessful(isOK);
        return res;
    }
}
