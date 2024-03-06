package com.webclient1c.release02.appclasses;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.apache.tapestry5.beanmodel.PropertyConduit;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class MapPropertyConduit implements PropertyConduit {
	private final String key;
	private final Class<?> type;
	
	public MapPropertyConduit(String key, Class<?> type) {
		super();
		this.key = key;
		this.type = type;
	}
	
	public Object get(Object instance) {
		return ((Map) instance).get(key);
	}
	
	public void set(Object instance, Object value) {
		((Map) instance).put(key, value);
	}
	
	public Class getPropertyType() {
		return type;
	}
	
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return null;
	}
}
