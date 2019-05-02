package com.museumsystem.museumserver.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.museumsystem.museumserver.service.ArtworkService;

@RunWith(SpringRunner.class)
@ContextConfiguration
@SpringBootTest
public class ArtworkServiceTest {

	@Autowired
	ArtworkService artworkService;
	
	@Test
	public void getArtworkTest() {		
		assertThat(artworkService.getArtwork(1L, "pl"), is(notNullValue()));
		assertThat(artworkService.getArtwork(1L, "en"), is(notNullValue()));
	}
}
