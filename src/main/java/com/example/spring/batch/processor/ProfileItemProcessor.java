package com.example.spring.batch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import com.example.spring.batch.entity.Profile;
import com.example.spring.batch.entity.User;

public class ProfileItemProcessor implements ItemProcessor<User, Profile> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileItemProcessor.class);

    @Override
    public Profile process(User user) throws Exception {

        LOGGER.info("Processing user data.....{}", user);

        Profile profile = new Profile();
        profile.setUserId(user.getId());
        profile.setFullName(user.getFirstName() + " " + user.getLastName());
        return profile;
    }
}