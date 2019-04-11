package com.example.proxy;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccessTests {

  @LocalServerPort
  private int port;

  private TestRestTemplate template = new TestRestTemplate();

  @Test
  public void protectedPage() {
    ResponseEntity<String> response = template.getForEntity("http://localhost:"
        + port + "/protected", String.class);
    Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }

  //This test should work
  @Ignore
  @Test
  @WithMockUser
  public void protectedPageWithUser() {
    ResponseEntity<String> response = template.getForEntity("http://localhost:"
        + port + "/protected", String.class);
    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
  }

}
