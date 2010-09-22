package com.gavin.hotukdeals;

public class KVP {
	public String Key;
	public String Value;
	
	public KVP(String k, String v)
	{
		this.Key = k;
		this.Value = v;
	}
		
	@Override public String toString()
	{
		return this.Key;
	}
}
