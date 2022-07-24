package LiveProject;


import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
    //
 Map<String,String> reqHeaders = new HashMap<>();

 String resourcePath ="/api/users";

 // Creating the pact

    @Pact(consumer = "UserConsumer", provider="UserProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder)
    {
        //Set the headers
        reqHeaders.put("Content-Type", "application/json");

        //create request and response body

        DslPart reqResBody = new PactDslJsonBody()
                .numberType("id")
                .stringType("firstName")
                .stringType("lastName")
                .stringType("email");

        return builder.given("Request to create user")
                .uponReceiving("Request to create user")
                    .method("POST")
                    .path(resourcePath)
                    .headers(reqHeaders)
                    .body(reqResBody)
                .willRespondWith()
                    .status(201)
                    .body(reqResBody)
                .toPact();

    }
 @Test
 @PactTestFor(providerName = "UserProvider", port="8282")
    public void consumerTest()
 {
     //Base URI
     String baseURI="http://localhost:8282";
     // Define request body
     Map<String, Object> reqbody = new HashMap<>();
     reqbody.put("id",123);
     reqbody.put("firstName","Ishwar");
     reqbody.put("lastName","Dharmadhikari");
     reqbody.put("email", "test@test.com");

     Response res = given().headers(reqHeaders).body(reqbody)
             .when().post(baseURI + resourcePath);

            System.out.println(res.getBody().asPrettyString());

            res.then().statusCode(201);
 }
}
