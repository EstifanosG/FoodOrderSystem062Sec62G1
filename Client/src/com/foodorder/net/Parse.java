package com.foodorder.net;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.util.Log;

import com.foodorder.beans.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class Parse {

	public CommonModel CommonPares(String str) {
		Gson gson = new Gson();
		Type type = new TypeToken<CommonModel>() {
		}.getType();
		CommonModel model = new CommonModel();
		model = gson.fromJson(str, type);
		return model;
	}

	public CommonModel GetBalance(String str) {
		Gson gson = new Gson();
		Type type = new TypeToken<CommonModel>() {
		}.getType();
		CommonModel model = new CommonModel();
		model = gson.fromJson(str, type);
		return model;
	}

	public CommonModel getRegistration(String str) {
		Gson gson = new Gson();
		Type type = new TypeToken<CommonModel>() {
		}.getType();
		CommonModel model = new CommonModel();
		model = gson.fromJson(str, type);
		return model;
	}

	public ArrayList<Rest> getRestList(String str) throws JsonSyntaxException {
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonArray Jarray = parser.parse(str).getAsJsonArray();
		ArrayList<Rest> lcs = new ArrayList<Rest>();
		for (JsonElement obj : Jarray) {
			Rest cse = gson.fromJson(obj, Rest.class);
			lcs.add(cse);
		}

		return lcs;
	}

	public ArrayList<MenuModel> GetMenuByRestId(String str)
			throws JsonSyntaxException {
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonArray Jarray = parser.parse(str).getAsJsonArray();
		ArrayList<MenuModel> lcs = new ArrayList<MenuModel>();
		for (JsonElement obj : Jarray) {
			MenuModel cse = gson.fromJson(obj, MenuModel.class);
			lcs.add(cse);
		}
		return lcs;
	}

	public UserInfo GetLoginInfo(String str) throws JsonSyntaxException {
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonArray Jarray = parser.parse(str).getAsJsonArray();
		ArrayList<UserInfo> lcs = new ArrayList<UserInfo>();
		for (JsonElement obj : Jarray) {
			UserInfo cse = gson.fromJson(obj, UserInfo.class);
			lcs.add(cse);
			Log.e("GetLoginInfo", "name:"+cse.getName()+", email:"+cse.getEmail()+", balance:"+cse.getBalance());
		}
		if (lcs.size() > 0) {
			return lcs.get(0);
		} else
			return null;
	}

	public ArrayList<OrderLine> GetOrderLine(String str)
			throws JsonSyntaxException {
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonArray Jarray = parser.parse(str).getAsJsonArray();
		ArrayList<OrderLine> lcs = new ArrayList<OrderLine>();
		for (JsonElement obj : Jarray) {
			OrderLine cse = gson.fromJson(obj, OrderLine.class);
			Log.w("GetOrderLine Parse:", cse.toString());
			lcs.add(cse);
		}
		return lcs;
	}
	
	public ArrayList<OrderLine> GetOrderLineDetail(String str) throws JsonSyntaxException {
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonArray Jarray = parser.parse(str).getAsJsonArray();
		ArrayList<OrderLine> lcs = new ArrayList<OrderLine>();
		for (JsonElement obj : Jarray) {
			OrderLine cse = gson.fromJson(obj, OrderLine.class);
			Log.w("GetOrderLine Parse:", cse.toString());
			lcs.add(cse);
		}
		return lcs;
	}
	
	public ArrayList<OrderLineDetail> GetOrderLines(String str) throws JsonSyntaxException {
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonArray Jarray = parser.parse(str).getAsJsonArray();
		ArrayList<OrderLineDetail> lcs = new ArrayList<OrderLineDetail>();
		for (JsonElement obj : Jarray) {
			OrderLineDetail cse = gson.fromJson(obj, OrderLineDetail.class);
			Log.w("GetOrderLines Parse:", cse.toString());
			lcs.add(cse);
		}
		return lcs;
	}

	public UserInfo GetUserInfo(String str) throws JsonSyntaxException {
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonArray jObj = parser.parse(str).getAsJsonArray();
		UserInfo user = gson.fromJson(jObj, UserInfo.class);
		return user;
	}
	
	public Giftcard GetGiftcard(String str) throws JsonSyntaxException{
		Gson gson = new Gson();
		Type type = new TypeToken<Giftcard>() {
		}.getType();
		Giftcard giftCard = new Giftcard();
		giftCard = gson.fromJson(str, type);
		return giftCard;
	}

}
