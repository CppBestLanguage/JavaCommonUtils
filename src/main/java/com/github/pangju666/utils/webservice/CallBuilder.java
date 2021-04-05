package com.github.pangju666.utils.webservice;

import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.client.Call;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * webService 请求构建器，用于构建和发送webservice请求
 * <p>
 * <pre>
 * 如：
 * String result = CallBuilder.newInstance("http://www.pangju666.com/webservice/services?wsdl")
 *                 .operationName("testAction")
 *                 .addParameter("testParam", XMLType.XSD_STRING)
 *                 .invoke(XMLType.XSD_STRING, String.class, "test");
 * </pre>
 * 或发送SOAP请求
 * <pre>
 * String result = CallBuilder.newInstance("http://www.pangju666.com/webservice/services?wsdl")
 *                 .operationName("testAction")
 *                 .SOAPActionURI("http://www.pangju666.com/webservice/services/testAction")
 *                 .addParameter("testParam", XMLType.SOAP_STRING)
 *                 .invoke(XMLType.SOAP_STRING, String.class, "test");
 * </pre>
 * <p>
 *
 * @author pangju
 * @version 1.0 2020-4-4
 * @since 1.0
 * @see Call
 */
public class CallBuilder {
    /** webservice请求体 */
    private final Call call;

    /**
     * 构造方法
     *
     * @param endpointAddress webservice发布地址，如：{@code http://www.pangju666.com/webservice/services?wsdl}
     * @throws MalformedURLException webservice发布地址填写不合法时抛出
     */
    protected CallBuilder(String endpointAddress) throws ServiceException, MalformedURLException {
        this(new URL(endpointAddress));
    }

    /**
     * 构造方法
     *
     * @param endpointAddress webservice发布地址
     */
    protected CallBuilder(URL endpointAddress) throws ServiceException {
        Service service = new Service();
        this.call = (Call) service.createCall();
        this.call.setTargetEndpointAddress(endpointAddress);
    }

    /**
     * 超时时间，单位为毫秒
     *
     * @param timeout 超时时间
     */
    public CallBuilder timeout(Integer timeout) {
        this.call.setTimeout(timeout);
        return this;
    }

    /**
     * 调用方法名称
     *
     * @param operation 方法名称
     */
    public CallBuilder operationName(QName operation) {
        this.call.setOperationName(operation);
        return this;
    }

    /**
     * 调用方法名称
     *
     * @param operationName 方法名称, 如：{@code testAction}
     * @param namespace 命名空间, 如：{@code http://pangju666.com/}
     */
    public CallBuilder operationName(String operationName, String namespace) {
        QName operation = new QName(namespace, operationName);
        return operationName(operation);
    }

    /**
     * 调用方法名称
     *
     * @param operationName 方法名称
     */
    public CallBuilder operationName(String operationName) {
        return operationName(operationName, null);
    }

    /**
     * SOAP请求URI
     *
     * @param SOAPActionURI SOAP请求URI，如：{@code http://pangju666.com/testAction}
     */
    public CallBuilder SOAPActionURI(String SOAPActionURI) {
        this.call.setSOAPActionURI(SOAPActionURI);
        return this;
    }

    /**
     * 添加参数
     *
     * @param parameterName 参数名称
     * @param xmlType 参数类型，如：{@link XMLType#XSD_STRING}
     * @param parameterMode 参数模式
     */
    public CallBuilder addParameter(QName parameterName, QName xmlType, ParameterMode parameterMode) {
        this.call.addParameter(parameterName, xmlType, parameterMode);
        return this;
    }

    /**
     * 添加参数，参数模式取{@link ParameterMode#IN}
     *
     * @param parameterName 参数名称
     * @param xmlType 参数类型， 如：{@link XMLType#XSD_STRING}
     */
    public CallBuilder addParameter(QName parameterName, QName xmlType) {
        return addParameter(parameterName, xmlType, ParameterMode.IN);
    }

    /**
     * 添加参数，参数类型取{@link XMLType#XSD_STRING}
     *
     * @param parameterName 参数名称
     * @param parameterMode 参数类型， 如：{@link XMLType#XSD_STRING}
     */
    public CallBuilder addParameter(QName parameterName, ParameterMode parameterMode) {
        return addParameter(parameterName, XMLType.XSD_STRING, parameterMode);
    }

    /**
     * 添加参数，参数类型取{@link XMLType#XSD_STRING}，参数模式取{@link ParameterMode#IN}
     *
     * @param parameterName 参数名称
     */
    public CallBuilder addParameter(QName parameterName) {
        return addParameter(parameterName, XMLType.XSD_STRING, ParameterMode.IN);
    }

    /**
     * 添加参数
     *
     * @param parameterName 参数名称
     * @param xmlType 参数类型，如：{@link XMLType#XSD_STRING}
     * @param parameterMode 参数模式
     */
    public CallBuilder addParameter(String parameterName, QName xmlType, ParameterMode parameterMode) {
        this.call.addParameter(parameterName, xmlType, parameterMode);
        return this;
    }

    /**
     * 添加参数，参数模式取{@link ParameterMode#IN}
     *
     * @param parameterName 参数名称
     * @param xmlType 参数类型， 如：{@link XMLType#XSD_STRING}
     */
    public CallBuilder addParameter(String parameterName, QName xmlType) {
        return addParameter(new QName(parameterName), xmlType);
    }

    /**
     * 添加参数，参数类型取{@link XMLType#XSD_STRING}
     *
     * @param parameterName 参数名称
     * @param parameterMode 参数类型， 如：{@link XMLType#XSD_STRING}
     */
    public CallBuilder addParameter(String parameterName, ParameterMode parameterMode) {
        return addParameter(new QName(parameterName), parameterMode);
    }

    /**
     * 添加参数，参数类型取{@link XMLType#XSD_STRING}，参数模式取{@link ParameterMode#IN}
     *
     * @param parameterName 参数名称
     */
    public CallBuilder addParameter(String parameterName) {
        return addParameter(new QName(parameterName));
    }

    /**
     * 生成请求实体
     */
    public Call build() {
        return this.call;
    }

    /**
     * 返回值类型
     *
     * @param returnType 返回值类型，如：{@link XMLType#XSD_STRING}
     * @param javaType 返回值对应JAVA类
     * @param parameters 请求参数
     */
    public <T> T invoke(QName returnType, Class<T> javaType, Object... parameters) throws RemoteException {
        this.call.setReturnType(returnType, javaType);
        return (T) call.invoke(parameters);
    }

    /**
     * 调用请求
     *
     * @param returnType 返回值类型，如：{@link XMLType#XSD_STRING}
     * @param parameters 请求参数
     * @return 请求结果
     * @throws RemoteException 请求错误时抛出
     */
    public Object invoke(QName returnType, Object... parameters) throws RemoteException {
        this.call.setReturnType(returnType);
        return call.invoke(parameters);
    }

    /**
     * 调用请求
     *
     * @param javaType 返回值对应JAVA类
     * @param parameters 请求参数
     * @return 请求结果
     * @throws RemoteException 请求错误时抛出
     */
    public <T> T invoke(Class<T> javaType, Object... parameters) throws RemoteException {
        this.call.setReturnClass(javaType);
        return (T) call.invoke(parameters);
    }

    /**
     * 调用请求
     *
     * @throws RemoteException 请求错误时抛出
     */
    public void invoke() throws RemoteException {
        call.invoke();
    }

    /**
     * 返回构建器实例
     *
     * @param endpointAddress webservice发布地址，如：{@code http://www.pangju666.com/webservice/services?wsdl}
     */
    public static CallBuilder newInstance(URL endpointAddress) throws ServiceException {
        return new CallBuilder(endpointAddress);
    }

    /**
     * 返回构建器实例
     *
     * @param endpointAddress webservice发布地址，如：{@code http://www.pangju666.com/webservice/services?wsdl}
     * @throws MalformedURLException webservice发布地址填写不合法时抛出
     */
    public static CallBuilder newInstance(String endpointAddress) throws ServiceException, MalformedURLException {
        return new CallBuilder(endpointAddress);
    }
}
