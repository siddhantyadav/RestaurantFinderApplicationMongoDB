package com.adbms.mongo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.bson.Document;
import org.bson.types.ObjectId;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

@ManagedBean
@SessionScoped
public class Restaurant {
	private FacesContext context;
	private MongoDB mongoDB;
	private MongoDatabase db;
	private String name = "";
	private String borough = "";
	private String cuisine = "";
	private String no = "";
	private String street = "";
	private String zip = "";
	private String message;
	private boolean renderMessage;
	private String errorMessage;
	private boolean renderErrorMessage;
	private String nameEdit;
	private ObjectId _id;
	private MongoCollection<Document> collection;
	private String selectedBorough = "";
	private List<String> boroughList = new ArrayList<String>();
	private String selectedCuisine = "";
	private List<String> cuisineList = new ArrayList<String>();
	private String selectedZip = "";
	private List<String> zipList = new ArrayList<String>();
	private ArrayList<MongoDB> tableList;
	private boolean renderTable = false;

	public List<String> getCuisineList() {
		MongoCursor<String> cs = db.getCollection("restaurants").distinct("cuisine", String.class).iterator();
		cuisineList = new ArrayList<String>();
		cuisineList.add("");
		while (cs.hasNext()) {
			cuisineList.add(cs.next());
		}
		Collections.sort(cuisineList);
		return cuisineList;
	}

	public List<String> getZipList() {

		MongoCursor<String> cs = db.getCollection("restaurants").distinct("address.zipcode", String.class).iterator();
		zipList = new ArrayList<String>();

		while (cs.hasNext()) {
			zipList.add(cs.next());
		}
		Collections.sort(zipList);
		return zipList;
	}

	public List<String> getBoroughList() {

		MongoCursor<String> cs = db.getCollection("restaurants").distinct("borough", String.class).iterator();
		boroughList = new ArrayList<String>();
		boroughList.add("");
		while (cs.hasNext()) {
			boroughList.add(cs.next());
		}
		Collections.sort(boroughList);
		return boroughList;
	}

	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		MongoClient mongoClient = new MongoClient();
		db = mongoClient.getDatabase("test");
		collection = db.getCollection("restaurants");

	}

	public void reset() {
		name = "";
		no = "";
		cuisine = "";
		borough = "";
		street = "";
		zip = "";
		renderErrorMessage = false;
		errorMessage = "";
		renderMessage = false;
		message = "";
		renderTable = false;

	}

	public String addData() {
		if (!name.isEmpty()) {
			db.getCollection("restaurants").insertOne(
					new Document("name", name).append("cuisine", cuisine).append("borough", borough).append("address",
							new Document("building", no).append("street", street).append("zipcode", zip)));
			message = "Restaurant '" + name + "' inserted";
			renderMessage = true;
			return "SUCCESS";
		} else {
			renderErrorMessage = true;
			errorMessage = "Please enter Restaurant name";
			return "FAIL";
		}

	}

	public String findRest() {
		reset();
		FindIterable fI = db.getCollection("restaurants").find(new Document("name", nameEdit));
		System.out.println("in methos");
		if (fI.iterator().hasNext()) {
			Document doc = (Document) fI.iterator().next();
			System.out.println("in if" + doc);
			_id = doc.getObjectId("_id");
			name = doc.getString("name");
			cuisine = doc.getString("cuisine");
			borough = doc.getString("borough");
			Document address = (Document) doc.get("address");
			System.out.println("in if" + address);
			no = address.getString("building");
			street = address.getString("street");
			zip = address.getString("zipcode");

		} else {
			renderErrorMessage = true;
			errorMessage = "No Data Found";
			return "FAIL";
		}
		return "SUCCESS";
	}

	public String update() {
		if(!name.trim().isEmpty()){

		db.getCollection("restaurants").updateOne(new Document("_id", _id),
				new Document("$set",
						new Document("name", name).append("cuisine", cuisine).append("borough", borough).append(
								"address", new Document("building", no).append("street", street).append("zipcode",
										zip))));
		message = "Restaurant '" + name + "' updated";
		renderMessage = true;
		return "SUCCESS";
		} else {
			renderErrorMessage = true;
			errorMessage = "No Enter Data";
			return "FAIL";
		}
	}

	public String delete() {
		db.getCollection("restaurants").deleteOne(new Document("_id", _id));
		message = "Restaurant '" + name + "' deleted";
		renderMessage = true;
		return "SUCCESS";
	}

	public String findData() {

		Document main = new Document();
		main.append("name", java.util.regex.Pattern.compile(name));
		if (!selectedBorough.trim().isEmpty()) {
			main.append("borough", selectedBorough);
		}
		if (!selectedCuisine.trim().isEmpty()) {
			main.append("cuisine", selectedCuisine);
		}
		if (!no.trim().isEmpty()) {
			main.append("address.building", no);
		}
		if (!street.trim().isEmpty()) {
			main.append("address.street", street);
		}
		if (!selectedZip.trim().isEmpty()) {
			main.append("address.zipcode", selectedZip);
		}
		MongoCursor<Document> MC = db.getCollection("restaurants").find(main).iterator();
		tableList = new ArrayList<MongoDB>();
		while (MC.hasNext()) {
			Document doc = (Document) MC.next();
			tableList.add(new MongoDB(doc.getString("name"), doc.getString("borough"), doc.getString("cuisine"),
					doc.get("address").toString()));

		}
		if (tableList.size() > 0) {
			renderTable = true;
		}
		return "SUCCESS";
	}

	public String addPage() {
		reset();
		return "ADD";
	}

	public String updatePage() {
		reset();
		return "UPDATE";
	}

	public String findPage() {
		reset();
		return "FIND";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBorough() {
		return borough;
	}

	public void setBorough(String borough) {
		this.borough = borough;
	}

	public String getCuisine() {
		return cuisine;
	}

	public void setCuisine(String cuisine) {
		this.cuisine = cuisine;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public MongoDatabase getDb() {
		return db;
	}

	public boolean isRenderTable() {
		return renderTable;
	}

	public void setRenderTable(boolean renderTable) {
		this.renderTable = renderTable;
	}

	public ArrayList<MongoDB> getTableList() {
		return tableList;
	}

	public void setTableList(ArrayList<MongoDB> tableList) {
		this.tableList = tableList;
	}

	public String getSelectedCuisine() {
		return selectedCuisine;
	}

	public void setSelectedCuisine(String selectedCuisine) {
		this.selectedCuisine = selectedCuisine;
	}

	public void setCuisineList(List<String> cuisineList) {
		this.cuisineList = cuisineList;
	}

	public String getSelectedZip() {
		return selectedZip;
	}

	public void setSelectedZip(String selectedZip) {
		this.selectedZip = selectedZip;
	}

	public void setZipList(List<String> zipList) {
		this.zipList = zipList;
	}

	public void setBoroughList(List<String> boroughList) {
		this.boroughList = boroughList;
	}

	public String getSelectedBorough() {
		return selectedBorough;
	}

	public void setSelectedBorough(String selectedBorough) {
		this.selectedBorough = selectedBorough;
	}

	public String getNameEdit() {
		return nameEdit;
	}

	public void setNameEdit(String nameEdit) {
		this.nameEdit = nameEdit;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public boolean isRenderErrorMessage() {
		return renderErrorMessage;
	}

	public String getMessage() {
		return message;
	}

	public boolean isRenderMessage() {
		return renderMessage;
	}
}
