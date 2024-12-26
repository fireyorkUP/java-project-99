package hexlet.code.app.Component;

import hexlet.code.app.Model.Label;
import hexlet.code.app.Model.TaskStatus;
import hexlet.code.app.Repository.LabelRepository;
import hexlet.code.app.Repository.TaskStatusRepository;
import hexlet.code.app.Repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import hexlet.code.app.Service.CustomUserDetailsService;
import hexlet.code.app.Model.User;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final CustomUserDetailsService userService;
    private final TaskStatusRepository statusRepository;
    private final LabelRepository labelRepository;
    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.findByEmail("hexlet@example.com").isEmpty()) {
            var email = "hexlet@example.com";
            var userData = new User();
            userData.setEmail(email);
            userData.setFirstName("Admin");
            userData.setLastName("Admin");
            userData.setPasswordDigest("qwerty");
            userService.createUser(userData);
        }


        var draftStatus = new TaskStatus();
        draftStatus.setName("Draft");
        draftStatus.setSlug("draft");
        statusRepository.save(draftStatus);

        var toReviewStatus = new TaskStatus();
        toReviewStatus.setName("Review");
        toReviewStatus.setSlug("to_review");
        statusRepository.save(toReviewStatus);

        var toBeFixedStatus = new TaskStatus();
        toBeFixedStatus.setName("ToBeFixed");
        toBeFixedStatus.setSlug("to_be_fixed");
        statusRepository.save(toBeFixedStatus);

        var toPublishStatus = new TaskStatus();
        toPublishStatus.setName("ToPublish");
        toPublishStatus.setSlug("to_publish");
        statusRepository.save(toPublishStatus);

        var publishedStatus = new TaskStatus();
        publishedStatus.setName("Published");
        publishedStatus.setSlug("published");
        statusRepository.save(publishedStatus);

        var featureLabel = new Label();
        featureLabel.setName("feature");
        labelRepository.save(featureLabel);

        var bugLabel = new Label();
        bugLabel.setName("bug");
        labelRepository.save(bugLabel);
    }
}
