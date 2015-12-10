package com.m2f.docstore.model;

public class BusinessRegistration {
	private String name;
	private String userName;
	private String email;
	private String password;
	private long contactNumber;
	private long alternateNumber;
	private double locationLat;
	private double locationLon;
	private String address;
	private int pincode;
	private String description;
	private String category;
	private String subCategory;
	private String photoUrl;
	//validations
	private boolean isPhoneValidated;
	private boolean isEmailValidated;
	private String typeOfVerification;
	private boolean isBusinessValidated;

	private SearchTags searchTags;
	private BusinessDetails businessDetails;
	
	public BusinessRegistration() {
		// TODO Auto-generated constructor stub
	}
	
	public BusinessRegistration(String name, String userName, String email, String password, long contactNumber,
			long alternateNumber, double locationLat, double locationLon, String address, int pincode,
			String description, String category, String subCategory, String photoUrl, boolean isPhoneValidated,
			boolean isEmailValidated, String typeOfVerification, boolean isBusinessValidated, SearchTags searchTags,
			BusinessDetails businessDetails) {
		super();
		this.name = name;
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.contactNumber = contactNumber;
		this.alternateNumber = alternateNumber;
		this.locationLat = locationLat;
		this.locationLon = locationLon;
		this.address = address;
		this.pincode = pincode;
		this.description = description;
		this.category = category;
		this.subCategory = subCategory;
		this.photoUrl = photoUrl;
		this.isPhoneValidated = isPhoneValidated;
		this.isEmailValidated = isEmailValidated;
		this.typeOfVerification = typeOfVerification;
		this.isBusinessValidated = isBusinessValidated;
		this.searchTags = searchTags;
		this.businessDetails = businessDetails;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(long contactNumber) {
		this.contactNumber = contactNumber;
	}

	public long getAlternateNumber() {
		return alternateNumber;
	}

	public void setAlternateNumber(long alternateNumber) {
		this.alternateNumber = alternateNumber;
	}

	public double getLocationLat() {
		return locationLat;
	}

	public void setLocationLat(double locationLat) {
		this.locationLat = locationLat;
	}

	public double getLocationLon() {
		return locationLon;
	}

	public void setLocationLon(double locationLon) {
		this.locationLon = locationLon;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPincode() {
		return pincode;
	}

	public void setPincode(int pincode) {
		this.pincode = pincode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public boolean isPhoneValidated() {
		return isPhoneValidated;
	}

	public void setPhoneValidated(boolean isPhoneValidated) {
		this.isPhoneValidated = isPhoneValidated;
	}

	public boolean isEmailValidated() {
		return isEmailValidated;
	}

	public void setEmailValidated(boolean isEmailValidated) {
		this.isEmailValidated = isEmailValidated;
	}

	public String getTypeOfVerification() {
		return typeOfVerification;
	}

	public void setTypeOfVerification(String typeOfVerification) {
		this.typeOfVerification = typeOfVerification;
	}

	public boolean isBusinessValidated() {
		return isBusinessValidated;
	}

	public void setBusinessValidated(boolean isBusinessValidated) {
		this.isBusinessValidated = isBusinessValidated;
	}

	public SearchTags getSearchTags() {
		return searchTags;
	}

	public void setSearchTags(SearchTags searchTags) {
		this.searchTags = searchTags;
	}

	public BusinessDetails getBusinessDetails() {
		return businessDetails;
	}

	public void setBusinessDetails(BusinessDetails businessDetails) {
		this.businessDetails = businessDetails;
	}

	
	@Override
	public String toString() {
		return "BusinessRegistration [name=" + name + ", userName=" + userName + ", email=" + email + ", password="
				+ password + ", contactNumber=" + contactNumber + ", alternateNumber=" + alternateNumber
				+ ", locationLat=" + locationLat + ", locationLon=" + locationLon + ", address=" + address
				+ ", pincode=" + pincode + ", description=" + description + ", category=" + category + ", subCategory="
				+ subCategory + ", photoUrl=" + photoUrl + ", isPhoneValidated=" + isPhoneValidated
				+ ", isEmailValidated=" + isEmailValidated + ", typeOfVerification=" + typeOfVerification
				+ ", isBusinessValidated=" + isBusinessValidated + ", searchTagsJson=" + searchTags
				+ ", businessDetailsJson=" + businessDetails + "]";
	}


	public static class SearchTags {
		//other details
		public String tags;
		public String certificates;
		public String qualification;
		public SearchTags(String tags, String certificates, String qualification) {
			super();
			this.tags = tags;
			this.certificates = certificates;
			this.qualification = qualification;
		}
		
		
	}

	public static class BusinessDetails {
		//business details
		public String panNumber;
		public String tinNumber;
		public String tanNumber;
		public String officeAddress;
		public BusinessDetails(String panNumber, String tinNumber, String tanNumber, String officeAddress) {
			super();
			this.panNumber = panNumber;
			this.tinNumber = tinNumber;
			this.tanNumber = tanNumber;
			this.officeAddress = officeAddress;
		}
		
		
	}


}
