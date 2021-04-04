package com.github.pangju666.utils.webservice;

import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.client.Call;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import java.net.URL;

/**
 * webService 请求构建器
 *
 * @author pangju666
 * @version 1.0 2020-4-4
 * @since 1.2
 */
public class CallBuilder {
    private static final QName DEFAULT_XML_TYPE = XMLType.XSD_STRING;
    private static final ParameterMode DEFAULT_PARAMETER_MODE = ParameterMode.IN;
    public static final String NAMESPACE = "http://webService.wskqw.com/";

    private final Call call;

    private CallBuilder(URL endpointAddress) throws ServiceException {
        Service service = new Service();
        //this.call = new Call(service);
        this.call = (Call) service.createCall();
        this.call.setTargetEndpointAddress(endpointAddress);
    }

    public CallBuilder operationName(String operationName) {
        QName operation = new QName(NAMESPACE, operationName);
        this.call.setOperationName(operation);
        this.call.setUseSOAPAction(true);
        this.call.setSOAPActionURI(NAMESPACE + operationName);
        return this;
    }

    public CallBuilder addParameter(String parameterName, QName xmlType, ParameterMode parameterMode) {
        QName parameter = new QName(NAMESPACE, parameterName);
        this.call.addParameter(parameter, xmlType, parameterMode);
        return this;
    }

    public CallBuilder addParameter(String parameterName, QName xmlType) {
        QName parameter = new QName(NAMESPACE, parameterName);
        this.call.addParameter(parameter, xmlType, DEFAULT_PARAMETER_MODE);
        return this;
    }

    public CallBuilder addParameter(String parameterName) {
        return addParameter(parameterName, DEFAULT_XML_TYPE);
    }

    public <T> CallBuilder returnType(String returnName, Class<T> returnType) {
        QName returns = new QName(NAMESPACE, returnName);
        this.call.setReturnType(returns, returnType);
        return this;
    }

    public CallBuilder returnType(String returnName) {
        return returnType(returnName, String.class);
    }

    public Call build() {
        return this.call;
    }

    public static CallBuilder newInstance(URL endpointAddress) throws ServiceException {
        return new CallBuilder(endpointAddress);
    }
}
