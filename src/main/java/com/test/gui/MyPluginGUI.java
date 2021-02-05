package com.test.gui;

import com.test.sampler.MyPluginSampler;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.JSyntaxTextArea;
import org.apache.jmeter.gui.util.JTextScrollPane;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.protocol.java.config.JavaConfig;
import org.apache.jmeter.protocol.java.sampler.JavaSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.JLabeledChoice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MyPluginGUI extends AbstractSamplerGui implements ItemListener {
    private  static  final long serialVersionUID=20l;

    //IP
    private JTextField domain;
    //端口号
    private JTextField port;
    //encoding
    private JTextField contentEncoding;
    //路径
    private JTextField path;
    //keepalive
    private JCheckBox useKeepAlive;
    //method POST,GET
    private JLabeledChoice method;
    /** A panel allowing the user to set arguments for this test. */
    //动态可添加参数
    private ArgumentsPanel argsPanel;

    //IP页面设置
    private JPanel getDomainPanel(){
        domain=new JTextField(10);
        JLabel label=new JLabel("IP");
        label.setLabelFor(domain);

        JPanel panel=new HorizontalPanel();
        panel.add(label, BorderLayout.WEST);
        panel.add(domain,BorderLayout.CENTER);
        return panel;
    }

    //端口设置
    private JPanel getPortPanel(){
        port=new JTextField(10);
        JLabel label=new JLabel(JMeterUtils.getResString("web_server_port"));
        label.setLabelFor(port);

        JPanel panel=new HorizontalPanel();
        panel.add(label,BorderLayout.WEST);
        panel.add(port,BorderLayout.CENTER);
        return  panel;
    }

    //contentEncoding
    private JPanel getContentEncodingPanel(){
        contentEncoding=new JTextField(10);
        JLabel label=new JLabel("contentEncoding");
        label.setLabelFor(contentEncoding);

        JPanel panel=new HorizontalPanel();
        panel.add(label,BorderLayout.WEST);
        panel.add(contentEncoding,BorderLayout.CENTER);
        return panel;
    }

    //路径
    private JPanel getPathPanel(){
        path=new JTextField(10);
        JLabel label=new JLabel(JMeterUtils.getResString("path"));
        label.setLabelFor(path);

        JPanel panel=new HorizontalPanel();
        panel.add(label,BorderLayout.WEST);
        panel.add(path,BorderLayout.CENTER);
        return panel;
    }

    //keepalive 和 method
    private Component getMethodAndKeepAlive(){
        useKeepAlive =new JCheckBox(JMeterUtils.getResString("use_keepalive"));
        useKeepAlive.setFont(null);
        useKeepAlive.setSelected(true);

        JPanel optionPanel=new HorizontalPanel();
        optionPanel.setMinimumSize(optionPanel.getPreferredSize());
        optionPanel.add(useKeepAlive);

        String Marry[]={"GET","POST"};
        method=new JLabeledChoice(JMeterUtils.getResString("method"),Marry,true,false);
        JPanel methodPanel=new HorizontalPanel();
        methodPanel.setLayout(new BoxLayout(methodPanel,BoxLayout.Y_AXIS));
        methodPanel.add(optionPanel,BorderLayout.WEST);
        methodPanel.add(method,BorderLayout.WEST);
        return methodPanel;
    }


    /**
     * Create a panel containing components allowing the user to provide
     * arguments to be passed to the test class instance.
     *
     * @return a panel containing the relevant components
     */
    private JPanel createParameterPanel() {
        argsPanel = new ArgumentsPanel(JMeterUtils.getResString("paramtable")); // $NON-NLS-1$
        return argsPanel;
    }

    //页面构建
    public void createPanel(){
        JPanel settingPanel=new VerticalPanel();
        settingPanel.add(getDomainPanel());
        settingPanel.add(getPortPanel());
        settingPanel.add(getContentEncodingPanel());
        settingPanel.add(getPathPanel());
        settingPanel.add(getMethodAndKeepAlive());
//        settingPanel.add(getPostBodyContent());

        JPanel dataPanel=new JPanel(new BorderLayout(5,0));
        dataPanel.add(settingPanel,BorderLayout.NORTH);
        dataPanel.add(createParameterPanel(), BorderLayout.CENTER);

        setLayout(new BorderLayout(0,5));
        setBorder(makeBorder());
        add(makeTitlePanel(),BorderLayout.NORTH);
        add(dataPanel,BorderLayout.CENTER);
    }

    private void init(){
        createPanel();
    }

    public MyPluginGUI(){
        super();
        init();
    }

    @Override
    public String getLabelResource() {
        throw new IllegalStateException("this is should not  be called");
    }

    @Override
    public String getStaticLabel(){
        return "sample";
    }

    @Override
    public void clearGui(){
        super.clearGui();
        argsPanel.clearGui();

        domain.setText("");
        port.setText("");
        contentEncoding.setText("");
        path.setText("");
//        postBodyContent.setText("");
        method.setText("GET");
        useKeepAlive.setSelected(true);
    }

    /**
     * 创建一个sampler,然后将界面中的数据设置到这个新的sampler实例中
     * @return
     */
    @Override
    public TestElement createTestElement() {
       MyPluginSampler sampler=new MyPluginSampler();
       modifyTestElement(sampler);
       return sampler;
    }

    /**
     * 把界面上的数据移到sample中
     * @param testElement
     */
    @Override
    public void modifyTestElement(TestElement testElement) {
        testElement.clear();
        configureTestElement(testElement);
        //键值对
        testElement.setProperty(new TestElementProperty(MyPluginSampler.ARGUMENTS, (Arguments) argsPanel.createTestElement()));
        testElement.setProperty(MyPluginSampler.domain,domain.getText());
        testElement.setProperty(MyPluginSampler.port,port.getText());
        testElement.setProperty(MyPluginSampler.contentEncoding,contentEncoding.getText());
        testElement.setProperty(MyPluginSampler.path,path.getText());
        testElement.setProperty(MyPluginSampler.useKeepAlive,useKeepAlive.getText());
//        testElement.setProperty(MyPluginSampler.postBodyContent,postBodyContent.getText());
        testElement.setProperty(new BooleanProperty(MyPluginSampler.useKeepAlive,useKeepAlive.isSelected()));
        testElement.setProperty(MyPluginSampler.method,method.getText());
    }

    @Override
    public void configure(TestElement element){
        super.configure(element);
        //jmeter运行后，保存参数，不然执行后，输入框会清空

        argsPanel.configure((Arguments) element.getProperty(MyPluginSampler.ARGUMENTS).getObjectValue());
        domain.setText(element.getPropertyAsString(MyPluginSampler.domain));
        port.setText(element.getPropertyAsString(MyPluginSampler.port));
        contentEncoding.setText(element.getPropertyAsString(MyPluginSampler.contentEncoding));
        path.setText(element.getPropertyAsString(MyPluginSampler.path));
//        postBodyContent.setText(element.getPropertyAsString(MyPluginSampler.postBodyContent));
        method.setText("GET");
        useKeepAlive.setSelected(true);


    }

    /**
     * Invoked when an item has been selected or deselected by the user.
     * The code written for this method performs the operations
     * that need to occur when an item is selected (or deselected).
     *
     * @param e
     */
    @Override
    public void itemStateChanged(ItemEvent e) {

    }
}
