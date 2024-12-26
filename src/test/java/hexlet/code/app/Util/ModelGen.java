package hexlet.code.app.Util;

import hexlet.code.app.dto.user.UserCreateDTO;
import hexlet.code.app.dto.user.UserUpdateDTO;
import hexlet.code.app.Model.Label;
import hexlet.code.app.Model.Task;
import hexlet.code.app.Model.TaskStatus;
import hexlet.code.app.Model.User;
import hexlet.code.app.Repository.TaskStatusRepository;
import hexlet.code.app.Repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor
public class ModelGen {
    Faker faker = new Faker();
    private Model<User> user;
    private Model<UserCreateDTO> createUserDTO;
    private Model<UserUpdateDTO> updateUserDTO;
    private Model<TaskStatus> taskStatus;
    private Model<Label> label;
    private Model<Task> task;

    @NonNull
    private UserRepository userRepository;

    @NonNull
    private TaskStatusRepository taskStatusRepository;

    @PostConstruct
    private void init() {
        createUserModel();
        createTaskStatusModel();
        createLabelModel();
        createTaskModel();
    }

    public void createUserModel() {
        user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getLastName), () -> faker.name().lastName())
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(User::getPasswordDigest), () -> faker.internet().password())
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .toModel();

        createUserDTO = Instancio.of(UserCreateDTO.class)
                .supply(Select.field(UserCreateDTO::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(UserCreateDTO::getFirstName), () -> JsonNullable.of(faker.name().firstName()))
                .supply(Select.field(UserCreateDTO::getLastName), () -> JsonNullable.of(faker.name().lastName()))
                .supply(Select.field(UserCreateDTO::getPassword), () -> faker.internet().password())
                .toModel();

        updateUserDTO = Instancio.of(UserUpdateDTO.class)
                .supply(Select.field(UserUpdateDTO::getEmail), () -> JsonNullable.of(faker.internet().emailAddress()))
                .supply(Select.field(UserUpdateDTO::getPassword), () -> JsonNullable.of(faker.internet().password()))
                .supply(Select.field(UserUpdateDTO::getFirstName), () -> JsonNullable.of(faker.name().firstName()))
                .supply(Select.field(UserUpdateDTO::getLastName), () -> JsonNullable.of(faker.name().lastName()))
                .toModel();
    }

    public void createTaskStatusModel() {
        taskStatus = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus::getId))
                .supply(Select.field(TaskStatus::getName), () -> faker.lorem().characters(3, 100))
                .supply(Select.field(TaskStatus::getSlug), () -> faker.internet().slug())
                .ignore(Select.field(TaskStatus::getCreatedAt))
                .toModel();
    }

    public void createLabelModel() {
        label = Instancio.of(Label.class)
                .ignore(Select.field(Label::getId))
                .ignore(Select.field(Label::getCreatedAt))
                .supply(Select.field(Label::getName), () -> faker.lorem().characters(3, 100))
                .toModel();
    }

    public void createTaskModel() {
        task = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .ignore(Select.field(Task::getLabels))
                .ignore(Select.field(Task::getAssignee))
                .ignore(Select.field(Task::getTaskStatus))
                .ignore(Select.field(Task::getCreatedAt))
                .supply(Select.field(Task::getName), () -> faker.lorem().characters(3, 100))
                .supply(Select.field(Task::getIndex), () -> faker.number().numberBetween(1, 1000))
                .supply(Select.field(Task::getDescription), () -> faker.lorem().word())
                .toModel();
    }
}