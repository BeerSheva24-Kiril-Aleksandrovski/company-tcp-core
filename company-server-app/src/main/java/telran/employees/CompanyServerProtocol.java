package telran.employees;

import java.lang.reflect.Method;

import telran.net.*;

public class CompanyServerProtocol implements Protocol {

    Methods methods;

    public CompanyServerProtocol(Methods methods) {
        this.methods = methods;
    }

    @Override
    public Response getResponse(Request request) {
        String type = request.requestType();
        String data = request.requestData();
        Response response = null;
        try {
            Class<Methods> clazz = Methods.class;
            Method method = clazz.getDeclaredMethod(type, String.class);
            response = (Response)method.invoke(methods, data);
        } catch (Exception e) {
            response = new Response(ResponseCode.WRONG_DATA, e.getMessage());
        }
        return response;
    }
}
