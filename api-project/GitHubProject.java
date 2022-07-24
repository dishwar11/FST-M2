package LiveProject;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class GitHubProject {
    Map<String,String> reqHeaders = new HashMap<>();
    String baseURI ="https://api.github.com";
    RequestSpecification resquestSpec;
    ResponseSpecification responseSpec;
    int sshid;


    @BeforeClass
    public void setup() {
        resquestSpec = new RequestSpecBuilder()
                .setBaseUri(baseURI)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "token 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")
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectResponseTime(lessThan(3000l), TimeUnit.MILLISECONDS)
                .build();
    }

    @Test(priority = 1)
    public void postRequest() {

        String reqBody = "{\"title\": \"TestAPIKey\", \"key\": \"ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQChCU3T5o/zc/BDK+xZ/3ngK+runvd9edveF5Zh4K4anfJ6Nij/BhRTKFfr5wAjHg/RrJehvfVgqJ5ZgOQAsNkMTJcSINK+2qYo6R7XIRCZHQBT10vwV3i/m2/W8xfaT2JdUFFkam2dgCF67Q/vcwoEc6HP6gtjs8lcYpU7uRnGWduHoS99/Otfy0La9MqggohtdDjZpdeKYHPOSddznPZ54d4cyZnZNfhUNt46T7oMR+HYewwHPmapf6QteloaGslqpxiTsRakE54kKSZ1s/yJACVz20KvA/GD8WpJ6f2QbI37umUimthlKl27McSx1476mrKUxAqPqazmdO3+R4Rv\"}";


        Response res = given().spec(resquestSpec).body(reqBody).when().post("user/keys");

        System.out.println(res.getBody().asString());

        res.then().statusCode(201);

        sshid=res.then().extract().path("id");

        System.out.println(sshid);


    }


    @Test(priority = 2)
    public void getRequest() {

        //Response res = given().spec(resquestSpec).when().get("/user/keys");

        Response res = given().spec(resquestSpec).pathParam("keyId",sshid).when().get("/user/keys/{keyId}");


        System.out.println(res.getBody().asString());

        res.then().statusCode(200);

    }

    @Test(priority = 3)
    public void deleteRequest() {



        Response res = given().spec(resquestSpec).pathParam("keyId",sshid).when().delete("/user/keys/{keyId}");

        System.out.println(res.getBody().asString());

        res.then().statusCode(204);

    }
}
