import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;




public class basics {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	// validate if add place API is working as expected
		 
		//given - all input details
		//when - submit the API--resource,http method
		//Then - validate the response
		
		RestAssured.baseURI="https://rahulshettyacademy.com";
		String response=given().log().all().queryParam("key","qaclick123").header("Content-Type","application/json")
		.body("{\r\n" + 
				"  \"location\": {\r\n" + 
				"    \"lat\": -38.383494,\r\n" + 
				"    \"lng\": 33.427362\r\n" + 
				"  },\r\n" +  
				"  \"accuracy\": 50,\r\n" + 
				"  \"name\": \"Priya house\",\r\n" + 
				"  \"phone_number\": \"(+91) 9960362796\",\r\n" + 
				"  \"address\": \"plot no.2,katol,dist.Nagpur\",\r\n" + 
				"  \"types\": [\r\n" + 
				"    \"shoe park\",\r\n" + 
				"    \"shop\"\r\n" + 
				"  ],\r\n" + 
				"  \"website\": \"http://rahulshettyacademy.com\",\r\n" + 
				"  \"language\": \"French-IN\"\r\n" + 
				"}").when().post("maps/api/place/add/json")
		.then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("Server","Apache/2.4.18 (Ubuntu)").extract().response().asString();
		
		System.out.println(response);
		
		
		//for above-check assertion by checking scope value as app and we will apply this to body in above code
		//for above-server response checking by header type given in postman
		
		//for below-add place--> update place with new address-->get place to validate if new address is represent in response
		 
		JsonPath js=new JsonPath(response);  // for passing json		
		String placeId=js.getString("place_id");
		System.out.println(placeId);
		
		//Update place
		String newAddress = "Summer walk, Africa";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type" , "application/json")
		.body("{\r\n" + 
				"\"place_id\":\""+placeId+"\",\r\n" + 
				"\"address\":\""+newAddress+"\",\r\n" + 
				"\"key\":\"qaclick123\"\r\n" + 
				"}").
		when().put("maps/api/place/update/json")
		.then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		//Get Place
		
		String getPlaceResponse= given().log().all().queryParam("key", "qaclick123")
		.queryParam("place_id", placeId)
		.when().get("maps/api/place/get/json")
		.then().assertThat().log().all().statusCode(200).extract().response().asString();
        
		JsonPath js1= new JsonPath(getPlaceResponse);
		String actualAddress =js1.getString("address");
		System.out.println(actualAddress);
		Assert.assertEquals(actualAddress,newAddress);
	}

}
