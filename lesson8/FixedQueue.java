package lesson8;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Iterator;

public class FixedQueue<T> extends AbstractQueue<T>{

	ArrayList<T> elements = new ArrayList<T>();
	int maxSize = 0;

	public FixedQueue(int maxSize)
	{
		this.maxSize = maxSize;
	}

	@Override
	public boolean remove(Object o)
	{
		return false;
	}

	public T peak()
	{
		return elements.get(0);
	}

	public T take()
	{
		T element = elements.get(0);
		elements.remove(0);
		return (T) elements;
	}

	@Override
	public boolean add(Object o)
	{
		if(elements.size() >= maxSize) elements.remove(0); //trim log

		return elements.add((T) o);
	}

	@Override
	public boolean contains(Object o)
	{
		return elements.contains(o);
	}

	@Override
	public int size()
	{
		return elements.size();
	}

	@Override
	public Iterator<T> iterator()
	{
		return elements.iterator();
	}

	public boolean hasReachedMaxSize()
	{
		return elements.size() == maxSize;
	}
}
