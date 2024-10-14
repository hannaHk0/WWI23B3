package de.rinderle.dhbw.wwi23b3;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class KaWwi23B3ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createAndGetTodo() throws Exception {
        // Step 1: Create a new todo item
        String newTodoJson = """
                {
                    "task": "Write integration test",
                    "completed": false
                }
                """;

        // Perform POST request to create a todo
        MvcResult result = mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newTodoJson))
                .andExpect(status().isCreated())  // Status check
                .andReturn();  // Return the MvcResult for further checks

        // Get the response content as String (the entire response body in JSON format)
        String jsonResponse = result.getResponse().getContentAsString();

        String expectedResponse = """
                {
                  "id":1,
                  "task":"Write integration test",
                  "completed":false
                }
                """;

        JSONAssert.assertEquals(expectedResponse, jsonResponse, true);

        // Step 2: Retrieve the created todo item by ID and check the response again
        MvcResult getResult = mockMvc.perform(get("/todos/" + 1))
                .andExpect(status().isOk())
                .andReturn();

        String getResponse = getResult.getResponse().getContentAsString();

        MvcResult getAllResult = mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andReturn();

        String getAllResultBody = getAllResult.getResponse().getContentAsString();

        String expectedResponseGet = """
                {
                    "id": 1,
                    "task": "Write integration test",
                    "completed": false
                }
                """;


        JSONAssert.assertEquals(expectedResponseGet, getResponse, true);

        mockMvc.perform(delete("/todos" + 1)).andExpect(status().isOk());

        mockMvc.perform(get("/todos" + 1)).andExpect(status().isOk());


    }


}
