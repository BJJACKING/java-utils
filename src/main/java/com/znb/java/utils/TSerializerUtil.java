package com.znb.java.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TList;
import org.apache.thrift.protocol.TSimpleJSONProtocol;
import org.apache.thrift.protocol.TType;
import org.apache.thrift.transport.TIOStreamTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangnaibin
 * @Date 2015-12-18 下午2:51
 */
public class TSerializerUtil {
    protected static final Logger LOG = LoggerFactory.getLogger(TSerializerUtil.class);

    // 需指定为byte转换string的格式为iso-8859-1, 否则数据逆向反转会有问题
    private static final String CHARSET = "ISO-8859-1";

    /**
     * 将一个thrift对象序列化为byte数组
     *
     * @param base thrift对象
     * @return
     */
    public static byte[] serialize(TBase<?, ?> base) {
        try {
            TSerializer serializer = new TSerializer();
            return serializer.serialize(base);
        } catch (TException e) {
            return new byte[0];
        }
    }

    /**
     * 将一个thrift对象序列化为String
     *
     * @param base thrift对象
     * @return
     */
    public static String toString(TBase<?, ?> base) {
        try {
            return new TSerializer().toString(base, CHARSET);
        } catch (TException e) {
            LOG.error("serialize data failed:" + base, e);
            return null;
        }
    }

    /**
     * 将byte数组反序列化为thrift对象
     *
     * @param base
     * @param data
     * @param <T>
     * @return
     */
    public static <T extends TBase<?, ?>> T deserialize(T base, byte[] data) {
        try {
            TDeserializer deserializer = new TDeserializer();
            deserializer.deserialize(base, data);
            return base;
        } catch (TException e) {
            return null;
        }
    }

    /**
     * 将byte数组反序列化为thrift对象
     *
     * @param base
     * @param data
     * @param <T>
     * @return
     */
    public static void deserialize(TBase<?, ?> base, String data) {
        try {
            new TDeserializer().deserialize(base, data, CHARSET);
        } catch (TException e) {
            LOG.error("deserialize data failed:" + data, e);
        }
    }

    /**
     * 把一个list集合序列化成byte[]
     *
     * @param objectList
     * @return
     */
    public static <T extends TBase<?, ?>> byte[] serializeList(List<T> objectList) {
        ByteArrayOutputStream fos = new ByteArrayOutputStream();

        TBinaryProtocol protocol = new TBinaryProtocol(new TIOStreamTransport(fos));
        try {
            protocol.writeListBegin(new TList(TType.STRUCT, objectList.size()));
            for (TBase<?, ?> object : objectList) {
                object.write(protocol);
            }
            protocol.writeListEnd();
        } catch (Exception e) {
            LOG.error("", e);
            return null;
        } finally {
            IOUtils.closeQuietly(fos);
        }
        return fos.toByteArray();
    }

    /**
     * 把byte[]反序列化成指定类型的对象list
     *
     * @param clazz
     * @param bytes
     * @return
     */
    public static <T extends TBase<?, ?>> List<T> dserializeList(Class<T> clazz, byte[] bytes) {
        TBinaryProtocol protocol = new TBinaryProtocol(new TIOStreamTransport(new ByteArrayInputStream(bytes)));
        List<T> objectList = new ArrayList<T>();

        try {
            TList listMetaInfo = protocol.readListBegin();
            for (int i = 0; i < listMetaInfo.size; i++) {
                try {
                    T object = clazz.newInstance();
                    object.read(protocol);
                    objectList.add((T) object);
                } catch (Exception e) {
                    LOG.error("", e);
                    break;
                }
            }
            protocol.readListEnd();
        } catch (Exception e) {
            LOG.error("", e);
        }
        return objectList;
    }

    /**
     * 将thrift对象序列化为json格式
     * @param obj thrift对象
     * @param removeKeys 需要移除的key
     * @return
     * @throws TException
     */
    public static JSONObject thriftToJson(TBase<?, ?> obj, String... removeKeys) {
        TSerializer serializer = new TSerializer(new TSimpleJSONProtocol.Factory());
        try {
            String json = serializer.toString(obj, "utf8");
            JSONObject result =  JSONObject.fromObject(json);
            if(removeKeys!=null){
                for(String k:removeKeys){
                    result.remove(k);
                }
            }
            return result;
        } catch (Exception e) {
            LOG.error("trans thrift to json excepiton! obj:{} exception:{}", obj, e);
            return new JSONObject();
        }

    }

    /**
     * 将List转换为JSONArray
     * @param objectList 需要转换的List,元素必须为thrift对象
     * @param remodeKeys thrift对象中需要移除的字段
     * @return
     */
    public static JSONArray thriftListToJSONArray(List<?> objectList, String... remodeKeys) {
        JSONArray array = new JSONArray();
        for (Object obj : objectList) {
            if (obj instanceof TBase<?, ?>) {
                array.add(thriftToJson((TBase<?,?>)obj, remodeKeys));
            } else {
                LOG.warn("this object is not thriftObject, Object:{}", obj);
            }
        }
        return array;
    }
}
