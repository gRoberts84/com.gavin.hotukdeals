package com.gavin.hotukdeals;

import org.json.JSONException;
import org.json.JSONObject;

public class Deal {
	public String Title;
	public String DealLink;
	public String MobileDealLink;
	public String Image;
	public String Description;
	public String Submitted;
	public String WentHot;
	public String User;
	public double Temprature;
	public double Price;
	public boolean Expired;
	public int Timestamp;
	
	public Deal(JSONObject obj)
	{
		try {
			this.Title = obj.getString("title");
			this.DealLink = obj.getString("deal_link");
			this.MobileDealLink = obj.getString("mobile_deal_link");
			this.Image = obj.getString("deal_image");
			this.Description = obj.getString("description");
			this.Submitted = obj.getString("submit_time");
			this.WentHot = obj.getString("hot_time");
			this.User = obj.getString("poster_name");
			this.Temprature = obj.getDouble("temprature");
			this.Price = obj.getDouble("price");
			this.Expired = obj.getBoolean("expired");
			this.Timestamp = obj.getInt("timestamp");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override public String toString()
	{
		return this.Title;
	}
	
	public Deal(){}
}
