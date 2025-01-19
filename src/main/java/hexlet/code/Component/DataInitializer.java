package hexlet.code.Component;

import hexlet.code.Model.Label;
import hexlet.code.Model.TaskStatus;
import hexlet.code.Repository.LabelRepository;
import hexlet.code.Repository.TaskStatusRepository;
import hexlet.code.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import hexlet.code.Service.CustomUserDetailsService;
import hexlet.code.Model.User;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private final CustomUserDetailsService userService;

    @Autowired
    private final TaskStatusRepository statusRepository;

    @Autowired
    private final LabelRepository labelRepository;

    @Autowired
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
