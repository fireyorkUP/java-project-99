package hexlet.code.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.Util.ModelGen;
import hexlet.code.dto.label.CreateLabelDTO;
import hexlet.code.dto.label.UpdateLabelDTO;
import hexlet.code.Mapper.LabelMapper;
import hexlet.code.Model.Label;
import hexlet.code.Repository.LabelRepository;
import hexlet.code.Repository.TaskRepository;
import hexlet.code.Repository.TaskStatusRepository;
import hexlet.code.Repository.UserRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@SpringBootTest
@AutoConfigureMockMvc
class LabelsControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelGen modelGenerator;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LabelMapper labelMapper;

    private Label testLabel;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();

        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));

        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();

        labelRepository.save(testLabel);

    }

    @AfterEach
    public void clean() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        labelRepository.deleteAll();
        taskStatusRepository.deleteAll();
    }


    @Test
    public void testGetById() throws Exception {
        mockMvc.perform(get("/api/labels/" + testLabel.getId()).with(token))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreate() throws Exception {
        var data = new CreateLabelDTO();
        data.setName("test");

        var request = post("/api/labels")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isCreated());

        var label = labelRepository.findByName(data.getName())
                .orElseThrow(() -> new IllegalArgumentException("Label not found!"));

        assertNotNull(label);
        assertThat(label.getName()).isEqualTo(data.getName());
    }

    @Test
    public void testUpdate() throws Exception {
        var updatedData = new UpdateLabelDTO();
        updatedData.setName(JsonNullable.of("new"));

        var request = put("/api/labels/" + testLabel.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedData));

        mockMvc.perform(request).andExpect(status().isOk());

        var updatedLabel = labelRepository.findById(testLabel.getId())
                .orElseThrow(() -> new IllegalArgumentException("Label not found!"));

        assertNotNull(updatedLabel);
        assertThat(updatedLabel.getName()).isEqualTo(updatedData.getName().get());
    }

    @Test
    public void testCreateWithInvalidData() throws Exception {
        var data = new HashMap<String, String>(Map.of(
                "name", "ne"
        ));
        var request = put("/api/labels/" + testLabel.getId())
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));

        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/labels/" + testLabel.getId())
                        .with(token))
                .andExpect(status().isNoContent());
    }
}
