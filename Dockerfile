FROM gradle:8.7.0-jdk21

WORKDIR /

COPY / .

RUN chmod +x gradlew

RUN gradle installDist

CMD ./build/install/app/bin/app --spring.profiles.active=production
