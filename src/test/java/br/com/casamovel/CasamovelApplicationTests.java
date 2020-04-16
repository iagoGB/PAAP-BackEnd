package br.com.casamovel;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(value = "resources/application-test.properties")
public abstract class CasamovelApplicationTests {
	
	@Value("${local.server.port}")
	protected int porta;

	// @Test
	// public void contextLoads() {
		
	// }

}
