package com.nullptr.utils.webservice;

import net.sf.json.JSONObject;
import org.apache.axis.client.Call;

import java.rmi.RemoteException;

/**
 * @author Administrator
 */
public final class WebServiceUtils {

    private WebServiceUtils() {
    }

    public static JSONObject handleAction(Call call, Object... parameters) throws RemoteException {
        Object result = call.invoke(parameters);
        return JSONObject.fromObject(result);
    }
}
