package com.example.proxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {

  @Test
  public void contextLoads() throws Exception {
    Application.main(getArgs());
  }

  private String[] getArgs(String... args) {
    List<String> list = new ArrayList<>(Arrays.asList(
        "--spring.main.showBanner=OFF",
        "--spring.main.registerShutdownHook=false"));
    if (args.length > 0) {
      list.add("--spring.main.sources="
          + StringUtils.arrayToCommaDelimitedString(args));
    }
    return list.toArray(new String[list.size()]);
  }

}
