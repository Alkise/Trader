package ru.alkise.trader.model.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Reflector {
	public static Map<String, String> reflect(Object o) {
		Class<? extends Object> c = o.getClass();

		Field[] publicFields = c.getFields();
		Map<Method, String> getterMethods = new LinkedHashMap<Method, String>();
		for (Field field : publicFields) {
			try {
				getterMethods.put(c.getMethod(
						"get" + String.valueOf(field.get(o)), (Class[]) null),
						field.getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Map<String, String> getterValues = new LinkedHashMap<String, String>();

		for (Method method : getterMethods.keySet()) {
			try {
				Object obj = method.invoke(o, (Object[]) null);
				Class<? extends Object> returnType = method.getReturnType();

				if ((returnType.equals(String.class))
						|| (returnType.equals(int.class))
						|| (returnType.equals(double.class))) {
					try {
						getterValues.put(getterMethods.get(method),
								String.valueOf(obj));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					if (returnType.equals(List.class)) {
						ArrayList<?> list = (ArrayList<?>) obj;
						for (Object object : list) {
							getterValues.putAll(reflect(object));
						}
					} else {
						getterValues.putAll(reflect(obj));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return getterValues;
	}
}
