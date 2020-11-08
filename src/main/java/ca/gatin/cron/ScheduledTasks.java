package ca.gatin.cron;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ca.gatin.api.response.ResponseStatus;
import ca.gatin.dao.service.UserPersistenceService;
import ca.gatin.model.security.User;

@Component
public class ScheduledTasks {
	
	@Autowired
	private UserPersistenceService userPersistenceService;
	
	private static final int TEN_MINUTES = 10 * 60 * 1000;
	private static final long THREE_HOURS = 3 * 60 * 60 * 1000;
	
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
	
	@Scheduled(fixedRate = TEN_MINUTES)
    public void purgePasswordResetKey() {
        
		List<User> users = userPersistenceService.getAll(); // change later to getAllWithPasswordResetKey();
		for (User user : users) {
			Date userResetTime = user.getDateCreatedResetPasswordKey();
			
			if (userResetTime != null) {
				long userResetTimeMs = userResetTime.getTime();
				long threeHoursAgo = System.currentTimeMillis() - THREE_HOURS;
				
				if (userResetTimeMs < threeHoursAgo) {
					user.setResetPasswordKey(null);
					user.setDateCreatedResetPasswordKey(null);
					user.setDateLastModified(new Date());
					try {
						userPersistenceService.save(user);
						logger.info("User's password reset key expired after 3 hours and purged successfully. Username: " + user.getUsername());
					} catch (Exception e) {
						logger.error("Error purging expired (after 3 hours) User password reset key", e);
					}
				}
			}
		}
    }

}
