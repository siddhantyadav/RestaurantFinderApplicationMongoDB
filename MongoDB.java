package com.adbms.mongo;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.bson.Document;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static java.util.Arrays.asList;

@ManagedBean
@SessionScoped
public class MongoDB {

	private String name;
	private String borough;
	private String cuisine;
	private String address;

	MongoDB(String name, String borough, String cuisine, String address) {
		this.name = name;
		this.borough = borough;
		this.cuisine = cuisine;
		this.address = address.replace("Document{{", "").replaceAll("}}", "");

	}

	public String getName() {
		return name;
	}

	public String getBorough() {
		return borough;
	}

	public String getCuisine() {
		return cuisine;
	}

	public String getAddress() {
		return address;
	}

}
