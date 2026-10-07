package com.familymenu.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import java.net.http.*;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FamilymenuBackendApplicationTests {
    @Autowired Environment environment;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper json = new ObjectMapper();

    private HttpResponse<String> request(String method, String path, String body) throws Exception {
        return client.send(HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + environment.getProperty("local.server.port") + "/api/v1" + path))
            .header("Content-Type", "application/json")
            .method(method, body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body))
            .build(), HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void dishesProductsAndShoppingWorkThroughHttpAndDatabase() throws Exception {
        String suffix = UUID.randomUUID().toString();
        HttpResponse<String> product = request("POST", "/products", "{\"name\":\"  Tomato " + suffix + "  \"}");
        assertThat(product.statusCode()).isEqualTo(201);
        String tomato = json.readTree(product.body()).get("id").asText();
        assertThat(request("POST", "/products", "{\"name\":\"tomato " + suffix + "\"}").statusCode()).isEqualTo(409);
        HttpResponse<String> soapResponse = request("POST", "/products", "{\"name\":\"Soap " + suffix + "\"}");
        String soap = json.readTree(soapResponse.body()).get("id").asText();
        String dishBody = "{\"name\":\"Salad\",\"category\":\"LUNCH\",\"ingredients\":[{\"productId\":\"" + tomato + "\",\"amount\":300,\"unit\":\"G\"}]}";
        HttpResponse<String> created = request("POST", "/dishes", dishBody);
        assertThat(created.statusCode()).isEqualTo(201);
        String dish = json.readTree(created.body()).get("id").asText();
        assertThat(json.readTree(created.body()).get("recipeSteps")).isEmpty();
        assertThat(request("GET", "/dishes/" + dish, null).statusCode()).isEqualTo(200);
        assertThat(request("GET", "/dishes", null).body()).contains(dish);
        assertThat(request("DELETE", "/products/" + tomato, null).statusCode()).isEqualTo(409);
        assertThat(request("PUT", "/products/" + tomato, "{\"name\":\"Tomatoes " + suffix + "\"}").statusCode()).isEqualTo(200);
        assertThat(request("GET", "/dishes/" + dish, null).body()).contains("Tomatoes");
        assertThat(request("PUT", "/dishes/" + dish, dishBody.replace("300", "500")).statusCode()).isEqualTo(200);
        assertThat(request("POST", "/dishes", dishBody.replace("G", "INVALID")).statusCode()).isEqualTo(400);
        assertThat(request("POST", "/dishes", dishBody.replace("300", "0")).statusCode()).isEqualTo(400);
        assertThat(request("GET", "/products?query=" + suffix, null).body()).contains(soap).contains(tomato);
        // Replace tomato completely with soap to prove removed database links disappear.
        assertThat(request("PUT", "/dishes/" + dish, dishBody.replace(tomato, soap)).statusCode()).isEqualTo(200);
        assertThat(request("DELETE", "/products/" + tomato, null).statusCode()).isEqualTo(204);
        assertThat(request("DELETE", "/dishes/" + dish, null).statusCode()).isEqualTo(204);
        assertThat(request("DELETE", "/products/" + soap, null).statusCode()).isEqualTo(204);
        assertThat(request("GET", "/dishes/" + dish, null).statusCode()).isEqualTo(404);
        assertThat(request("POST", "/dishes", dishBody.replace("LUNCH", "INVALID")).statusCode()).isEqualTo(400);
    }
    @Test
    void savedShoppingListsCombineMenusDishesAndManualProducts() throws Exception {
        String suffix = UUID.randomUUID().toString();
        HttpResponse<String> productResponse = request("POST", "/products", "{\"name\":\"Saved tomato " + suffix + "\"}");
        String productId = json.readTree(productResponse.body()).get("id").asText();
        String dishBody = "{\"name\":\"Saved salad\",\"category\":\"LUNCH\",\"ingredients\":[{\"productId\":\"" + productId + "\",\"amount\":300,\"unit\":\"G\"}]}";
        HttpResponse<String> dishResponse = request("POST", "/dishes", dishBody);
        String dishId = json.readTree(dishResponse.body()).get("id").asText();
        HttpResponse<String> menuResponse = request("POST", "/menus", "{\"dishIds\":[\"" + dishId + "\"]}");
        assertThat(menuResponse.statusCode()).isEqualTo(201);
        String menuId = json.readTree(menuResponse.body()).get("id").asText();

        HttpResponse<String> inboxResponse = request("GET", "/shopping-lists/inbox", null);
        assertThat(inboxResponse.statusCode()).isEqualTo(200);
        assertThat(inboxResponse.body()).contains(menuId);
        assertThat(request("DELETE", "/shopping-lists/inbox", null).statusCode()).isEqualTo(409);

        HttpResponse<String> listResponse = request("POST", "/shopping-lists", "{\"name\":\"Birthday " + suffix + "\"}");
        assertThat(listResponse.statusCode()).isEqualTo(201);
        String listId = json.readTree(listResponse.body()).get("id").asText();
        String listPath = "/shopping-lists/" + listId;
        assertThat(request("POST", listPath + "/menus/" + menuId, null).statusCode()).isEqualTo(200);
        // Re-adding the same menu is idempotent.
        assertThat(request("POST", listPath + "/menus/" + menuId, null).statusCode()).isEqualTo(200);
        assertThat(request("POST", listPath + "/dishes/" + dishId, null).statusCode()).isEqualTo(200);
        assertThat(request("DELETE", "/dishes/" + dishId, null).statusCode()).isEqualTo(409);
        assertThat(request("DELETE", "/menus/" + menuId, null).statusCode()).isEqualTo(409);
        String productBody = "{\"productId\":\"" + productId + "\",\"amount\":0.4,\"unit\":\"KG\"}";
        HttpResponse<String> manualResponse = request("POST", listPath + "/products", productBody);
        assertThat(manualResponse.statusCode()).isEqualTo(200);
        JsonNode shoppingList = json.readTree(manualResponse.body());
        String purchaseId = shoppingList.get("manualProducts").get(0).get("id").asText();
        assertThat(shoppingList.get("items").get(0).get("amount").decimalValue()).isEqualByComparingTo("1");
        assertThat(shoppingList.get("items").get(0).get("unit").asText()).isEqualTo("KG");
        assertThat(json.readTree(request("GET", listPath, null).body()).get("items").get(0).get("amount").decimalValue())
                .isEqualByComparingTo("1");

        HttpResponse<String> removedMenu = request("DELETE", listPath + "/menus/" + menuId, null);
        assertThat(json.readTree(removedMenu.body()).get("items").get(0).get("amount").decimalValue())
                .isEqualByComparingTo("700");
        assertThat(request("DELETE", "/products/" + productId, null).statusCode()).isEqualTo(409);
        assertThat(request("PUT", listPath + "/products/" + purchaseId, productBody.replace("0.4", "0.5")).statusCode())
                .isEqualTo(200);
        assertThat(request("DELETE", listPath + "/dishes/" + dishId, null).statusCode()).isEqualTo(200);
        JsonNode manualOnlyList = json.readTree(request("GET", listPath, null).body());
        assertThat(manualOnlyList.get("items").get(0).get("amount").decimalValue()).isEqualByComparingTo("500");
        HttpResponse<String> secondManualResponse = request("POST", listPath + "/products", productBody);
        assertThat(secondManualResponse.statusCode()).isEqualTo(200);
        assertThat(request("DELETE", listPath + "/products/" + purchaseId, null).statusCode()).isEqualTo(200);
        assertThat(json.readTree(request("GET", listPath, null).body()).get("items").get(0).get("amount").decimalValue())
                .isEqualByComparingTo("400");
        assertThat(request("DELETE", listPath + "/contents", null).statusCode()).isEqualTo(200);
        assertThat(json.readTree(request("GET", listPath, null).body()).get("items")).isEmpty();
        HttpResponse<String> householdProduct = request("POST", "/products", "{\"name\":\"Cleaning " + suffix + "\"}");
        String householdProductId = json.readTree(householdProduct.body()).get("id").asText();
        String householdPurchase = "{\"productId\":\"" + householdProductId + "\",\"amount\":1,\"unit\":\"PCS\"}";
        assertThat(request("POST", listPath + "/products", householdPurchase).statusCode()).isEqualTo(200);
        assertThat(request("DELETE", "/products/" + householdProductId, null).statusCode()).isEqualTo(409);
        assertThat(request("DELETE", listPath + "/contents", null).statusCode()).isEqualTo(200);
        assertThat(request("DELETE", "/products/" + householdProductId, null).statusCode()).isEqualTo(204);
        assertThat(request("PUT", listPath, "{\"name\":\"New Year\"}").statusCode()).isEqualTo(200);
        assertThat(request("GET", "/shopping-lists", null).body()).contains("New Year");
        assertThat(request("POST", listPath + "/menus/missing", null).statusCode()).isEqualTo(404);
        assertThat(request("POST", listPath + "/dishes/missing", null).statusCode()).isEqualTo(404);
        assertThat(json.readTree(request("GET", listPath, null).body()).get("dishIds")).isEmpty();
        assertThat(request("DELETE", listPath, null).statusCode()).isEqualTo(204);
        assertThat(request("DELETE", "/shopping-lists/inbox/menus/" + menuId, null).statusCode()).isEqualTo(200);
        assertThat(request("DELETE", "/dishes/" + dishId, null).statusCode()).isEqualTo(204);
        assertThat(request("DELETE", "/products/" + productId, null).statusCode()).isEqualTo(204);
    }

}
