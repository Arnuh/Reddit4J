package ca.arnah.reddit4j.objects.reddit;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubredditTest{
	
	@ParameterizedTest
	@ValueSource(strings = {
		// min length
		"a1_",
		// max length, has numbers
		"012345678901234567890",
		// upper- and lower-case letters
		"Java"})
	void isValidSubredditNameTrue(String name){
		assertTrue(Subreddit.isValidSubredditName(name));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
		// too short
		"a", "bb",
		// illegal characters
		"cc#", "ddé",
		// underscore in first position
		"_hi",
		// too long (22 chars)
		"0123456789012345678901"})
	void isValidSubredditNameFalse(String name){
		assertFalse(Subreddit.isValidSubredditName(name));
	}
}