package net.thumbtack.hospital.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
public class GlobalControllerExceptionHandlerTest extends BaseControllerTest {
    @Test
    public void incorrectPostUrlTest() throws Exception {
        String url = "/incorrect_url";

        mvc.perform(MockMvcRequestBuilders.post(url))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void incorrectDeleteUrlTest() throws Exception {
        String url = "/incorrect_url";

        mvc.perform(MockMvcRequestBuilders.delete(url))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void incorrectGetUrlTest() throws Exception {
        String url = "/incorrect_url";

        mvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void incorrectPutUrlTest() throws Exception {
        String url = "/incorrect_url";

        mvc.perform(MockMvcRequestBuilders.put(url))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void incorrectPatchUrlTest() throws Exception {
        String url = "/incorrect_url";

        mvc.perform(MockMvcRequestBuilders.patch(url))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // TODO Добавить тетсы для метода handleFieldValidationExceptions

    // TODO Добавить тетсы для метода handlePermissionExceptions
}