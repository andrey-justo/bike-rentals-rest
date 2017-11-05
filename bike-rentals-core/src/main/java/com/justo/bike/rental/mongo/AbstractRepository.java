package com.justo.bike.rental.mongo;

import org.springframework.data.mongodb.core.MongoTemplate;

public abstract class AbstractRepository<T> {
	
	protected final MongoTemplate template;
	protected final Class<T> objClass;
	
	public AbstractRepository(MongoTemplate template, Class<T> clazz) {
		this.template = template;
		this.objClass = clazz;
	}
	
	public T insert(T obj) {
		template.insert(obj, collectionName());
		return obj;
	}
	
	public T update(T obj) {
		template.save(obj, collectionName());
		return obj;
	}
	
	public T find(String id) {
		return template.findById(id, objClass, collectionName());
	}
	
	public abstract String collectionName();

}
