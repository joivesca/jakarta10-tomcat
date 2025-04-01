package com.onndoo.j10tomcat.util;

import com.github.javafaker.Faker;
import com.onndoo.j10tomcat.model.Profile;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class FakeUserGenerator {
	private final Faker faker;
    private static Set<String> generatedUsernames = new HashSet<>();
    private Supplier<String> usernameGenerator;

    public FakeUserGenerator() {
        faker = new Faker();
        setUsernameGenerator("dragonBall");
    }
    
    public void setUsernameGenerator(String input) {
        switch (input) {
            case "dragonBall":
                usernameGenerator = () -> faker.dragonBall().character();
                break;
            case "harryPotter":
                usernameGenerator = () -> faker.harryPotter().character();
                break;
            case "rickandMorty":
                usernameGenerator = () -> faker.rickAndMorty().character();
                break;
            default:
                usernameGenerator = () -> faker.hobbit().character();
                break;
        }
    }
    
    private String generateUniqueUsername() {
        String username;
        do {
            username = usernameGenerator.get();
        } while (generatedUsernames.contains(username));

        generatedUsernames.add(username);
        return username;
    }
    
    public Profile generateFakeUserProfile(String groupName) {
        Profile profile = new Profile();
        profile.setUsername(generateUniqueUsername());
        profile.setPassword(faker.internet().password());
        profile.setGroupName(groupName);
        return profile;
    }
}