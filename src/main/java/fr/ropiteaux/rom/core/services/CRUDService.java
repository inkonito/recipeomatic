package fr.ropiteaux.rom.core.services;

import java.util.List;

import javax.persistence.TypedQuery;

public interface CRUDService<T> {

	public T create(T t);

	public T update(T t);

	public void delete(T t);

	public void delete(List<T> list);

	public T find(TypedQuery<T> query);

	public T findById(long id);

	public List<T> findAll();
}
