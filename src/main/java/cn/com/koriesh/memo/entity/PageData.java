package cn.com.koriesh.memo.entity;


import cn.com.koriesh.memo.common.DateUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 参数封装
 * @CreateDate: 2019/12/30 2019/12/30
 * @UpdateAuthor:
 * @UpdateDate: 2019/12/30 2019/12/30
 */
@Slf4j
public class PageData extends HashMap implements Map {

    private static final long serialVersionUID = 1L;

    transient Map map = null;
    transient HttpServletRequest request;

    public PageData(HttpServletRequest request) {
        this.request = request;
        Map properties = request.getParameterMap();
        Map returnMap = new HashMap();
        Iterator entries = properties.entrySet().iterator();
        Entry entry;
        while (entries.hasNext()) {
            String name = "";
            String value = "";
            StringBuilder valueSb = new StringBuilder();
            entry = (Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            // 当前值为空时 直接赋空值
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    valueSb.append(values[i]).append(",");
                }
                value = valueSb.substring(0, valueSb.length() - 1);
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        map = returnMap;
    }

    public PageData() {
        map = new HashMap();
    }

    @Override
    public Object get(Object key) {
        Object obj = null;
        if (map.get(key) instanceof Object[]) {
            Object[] arr = (Object[]) map.get(key);
            obj = request == null ? arr : (request.getParameter((String) key) == null ? arr : arr[0]);
        } else {
            obj = map.get(key);
        }
        return obj;
    }

    public String getString(Object key, String value) {
        Object obj = get(key);
        if (obj == null) {
            return value;
        } else {
            return obj.toString().trim();
        }
    }

    public Integer getInt(Object key, int value) {
        Object obj = get(key);
        if (obj == null) {
            return value;
        } else {
            return Integer.parseInt(obj.toString());
        }
    }

    public Long getLong(Object key, long value) {
        Object obj = get(key);
        if (obj == null) {
            return value;
        } else {
            return Long.parseLong(obj.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object put(Object key, Object value) {
        return map.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    public void clear() {
        map.clear();
    }

    public boolean containsKey(Object key) {
        // TODO Auto-generated method stub
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        // TODO Auto-generated method stub
        return map.containsValue(value);
    }

    public Set entrySet() {
        // TODO Auto-generated method stub
        return map.entrySet();
    }

    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return map.isEmpty();
    }

    public Set keySet() {
        // TODO Auto-generated method stub
        return map.keySet();
    }

    @SuppressWarnings("unchecked")
    public void putAll(Map t) {
        // TODO Auto-generated method stub
        map.putAll(t);
    }

    public int size() {
        // TODO Auto-generated method stub
        return map.size();
    }

    public Collection values() {
        // TODO Auto-generated method stub
        return map.values();
    }

    public Object toBean(Object bean) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                Type type = property.getPropertyType();
                if (map.containsKey(key)) {
                    Object value = map.get(key);
                    if (value == null || value.toString().length() < 1) {
                        continue;
                    } else {
                        value = this.getValue(value, type);
                    }
                    Method setter = property.getWriteMethod();
                    setter.invoke(bean, value);
                }
            }
            return bean;
        } catch (Exception ex) {
            log.error("PageData.toBean->Exception: {}" ,ex);
            return null;
        }
    }

    private Object getValue(Object object, Type type) {
        if ("class java.lang.Long".equals(type.toString())) {
            object = Long.parseLong(object.toString());
        }
        if ("class java.lang.Integer".equals(type.toString())) {
            object = Integer.parseInt(object.toString());
        }
        if ("class java.lang.Double".equals(type.toString())) {
            object = Double.parseDouble(object.toString());
        }
        if ("class java.lang.Float".equals(type.toString())) {
            object = Float.parseFloat(object.toString());
        }
        if ("class java.math.BigDecimal".equals(type.toString())) {
            BigDecimal setValue = new BigDecimal(object.toString());
            object = setValue.setScale(4, BigDecimal.ROUND_HALF_DOWN);
        }
        if ("class java.util.List".equals(type.toString())) {
            object = this.getPageDataListString(object);
        }
        if ("class java.util.Date".equals(type.toString())) {
            object = this.getPageDataDate(object);
        }
        return object;
    }

    private Object getPageDataDate(Object object) {
        if (object.toString().length() < 11) {
            object = DateUtil.datetimeStrToDate(object.toString() + " 00:00:00");
        } else {
            int a = object.toString().split(":").length;
            StringBuilder b = new StringBuilder();
            for (int i = 3 - a; i > 0; i--) {
                b.append(":00");
            }
            object = DateUtil.datetimeStrToDate(object.toString() + b);
        }
        return object;
    }

    private Object getPageDataListString(Object object) {
        List<String> stringList = (List<String>) object;
        if (stringList.size() > 0) {
            StringBuilder setValue = new StringBuilder();
            for (String str : stringList) {
                setValue.append(str).append(",");
            }
            object = setValue.substring(0, setValue.length() - 1);
        } else {
            object = "";
        }
        return object;
    }

}
