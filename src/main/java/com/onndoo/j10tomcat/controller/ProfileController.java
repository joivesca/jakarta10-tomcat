package com.onndoo.j10tomcat.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import com.onndoo.j10tomcat.model.Profile;
import com.onndoo.j10tomcat.service.Service;
import com.onndoo.j10tomcat.util.FakeUserGenerator;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named(value = "ProfilesController")
@ViewScoped
public class ProfileController implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Inject
	private Service profileService;

	private List<Profile> profiles;
	private Profile selectedProfile;
	private Profile newProfile;
	private String selectedOption = "view";
	private String generatorOption;

	public ProfileController() {
    }

	@PostConstruct
	public void init() {		
		newProfile = new Profile();
		selectedProfile = new Profile();
	}

	public List<Profile> profilesList() {
		if (profiles == null) {
			profiles = profileService.listAll();
		}
		return profiles;
	}

	public long profileCount() {
		return profileService.count();
	}

	public void selectView() {
		selectedOption = "view";
	}

	public void selectEdit() {
		selectedOption = "edit";
	}

	public void selectCreate() {
		selectedOption = "create";
		openNewProfile();
	}

	public void openNewProfile() {
		newProfile = new Profile();
	}

	public void updateProfile() {
		profileService.update(selectedProfile);
		clearSelectedProfile();
	}

	public void createProfile() {
		profileService.create(newProfile);
		clearSelectedProfile();
	}

	public void deleteProfile() {
		if (selectedProfile != null) {
			profileService.delete(selectedProfile);
			clearSelectedProfile();
		}
	}

	public void clearSelectedProfile() {
		profiles = null;
		newProfile = null;
		selectedProfile = null;
		selectedOption = "view";
	}

	public void generateAndCreateRandomUsers() {
		FakeUserGenerator userGenerator = new FakeUserGenerator();
		// Use the selectedGenerator value to determine the generator strategy
		Optional<String> generatorInput = Optional.ofNullable(generatorOption);
		if (generatorInput.isPresent()) {
			userGenerator.setUsernameGenerator(generatorInput.get());
		}

		for (int i = 0; i < 10; i++) {
			Profile newUser = userGenerator.generateFakeUserProfile("user");
			profileService.create(newUser);
		}
		clearSelectedProfile();
	}

	public void generateAndCreateRandomAdmins() {
		FakeUserGenerator userGenerator = new FakeUserGenerator();

		Optional<String> generatorInput = Optional.ofNullable(generatorOption);
		if (generatorInput.isPresent()) {
			userGenerator.setUsernameGenerator(generatorInput.get());
		}

		for (int i = 0; i < 10; i++) {
			Profile newUser = userGenerator.generateFakeUserProfile("admin");
			profileService.create(newUser);
		}
		clearSelectedProfile();
	}

	public List<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}

	public Profile getSelectedProfile() {
		return selectedProfile;
	}

	public void setSelectedProfile(Profile selectedProfile) {
		this.selectedProfile = selectedProfile;
	}

	public Profile getNewProfile() {
		return newProfile;
	}

	public void setNewProfile(Profile newProfile) {
		this.newProfile = newProfile;
	}

	public String getSelectedOption() {
		return selectedOption;
	}

	public void setSelectedOption(String selectedOption) {
		this.selectedOption = selectedOption;
	}

	public String getGeneratorOption() {
		return generatorOption;
	}

	public void setGeneratorOption(String generatorOption) {
		this.generatorOption = generatorOption;
	}
}
