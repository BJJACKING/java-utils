package com.znb.java.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class ThriftCallerFactory {

    private static class Handler implements InvocationHandler {
        private TServiceClient client;
        public Handler(TServiceClient client){
            this.client = client;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {
            try {
                return method.invoke(client, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }
    @SuppressWarnings("unchecked")
    public static synchronized <Iface> Iface getClient(Class<Iface> clazz, String host, int port) {
        try {
            String className = getClientClassNameByIfaceClass(clazz);
            Class<TServiceClient> clientClass = (Class<TServiceClient>) Class.forName(className);

            TServiceClient client = createClient(clientClass, host, port, 6000);
            Iface iface = (Iface) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, new Handler(client));
            return iface;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <Iface> String getClientClassNameByIfaceClass(Class<Iface> clazz) {
        return StringUtils.removeEnd(clazz.getName(), "$Iface").concat("$Client");
    }

    static TServiceClient createClient(Class<?> clientClass, String host, int port, int timeout) throws TException {
        try {
            TSocket socket = new TSocket(host, port, timeout);
            TTransport frameTransport = new TFramedTransport(socket);
            TProtocol protocol = new TBinaryProtocol(frameTransport);
            frameTransport.open();
            return (TServiceClient) clientClass.getConstructor(TProtocol.class).newInstance(protocol);
        } catch (TTransportException e) {
            throw e;
        } catch (Exception e) {
            throw new TException("can not create Service Client: " + clientClass.getName(), e);
        }
    }

    public static void main(String[] args) {

    }
}
