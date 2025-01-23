package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.utils.ModelGen;
import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.UpdateTaskDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.assertj.core.api.Assertions;
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
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TasksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ModelGen modelGenerator;

    private Task testTask;
    private TaskStatus testTaskStatus;
    private User testUser;
    private Label testLabel;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();

        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));

        testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();

        taskStatusRepository.save(testTaskStatus);
        userRepository.save(testUser);
        labelRepository.save(testLabel);

        testTask.setAssignee(testUser);
        testTask.setTaskStatus(testTaskStatus);
        testTask.setLabels(new HashSet<>(Set.of(testLabel)));

        taskRepository.save(testTask);
    }

    @AfterEach
    public void clean() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        labelRepository.deleteAll();
        taskStatusRepository.deleteAll();
    }

    @Test
    public void testIndex() throws Exception {
        var response = mockMvc.perform(get("/api/tasks").with(token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        var body = response.getContentAsString();

        List<TaskDTO> taskDTOS = objectMapper.readValue(body, new TypeReference<>() { });

        var actual = taskDTOS.stream().map(taskMapper::map).toList();
        var excepted = taskRepository.findAll();

        Assertions.assertThat(actual).containsExactlyInAnyOrderElementsOf(excepted);
    }

    @Test
    public void testFilteredIndex() throws Exception {
        var result = mockMvc.perform(get("/api/tasks?status=" + testTaskStatus.getSlug())
                        .with(token))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body)
                .isArray()
                .hasSize(1);
    }

    @Test
    public void testShow() throws Exception {
        var response = mockMvc.perform(get("/api/tasks/" + testTask.getId()).with(token))
                .andExpect(status().isOk())
                .andReturn();
        var body = response.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("title").isEqualTo(testTask.getName()),
                v -> v.node("index").isEqualTo(testTask.getIndex())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var data = new TaskCreateDTO();
        data.setTitle("New Task");
        data.setSlug(testTaskStatus.getSlug());

        var request = post("/api/tasks")
                .with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var task = taskRepository.findByName(data.getTitle()).get();

        assertNotNull(task);
        assertThat(task.getName()).isEqualTo(data.getTitle());
        assertThat(task.getTaskStatus().getSlug()).isEqualTo(data.getSlug());
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new UpdateTaskDTO();
        data.setTitle(JsonNullable.of("Update"));

        var request = put("/api/tasks/" + testTask.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isOk());

        var task = taskRepository.findById(testTask.getId()).get();
        assertThat(task.getName()).isEqualTo("Update");
    }

    @Test
    public void testDestroy() throws Exception {
        var request = delete("/api/tasks/" + testTask.getId()).with(jwt());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
        assertThat(taskRepository.existsById(testTask.getId())).isEqualTo(false);

    }
}
