package tests;

import static io.restassured.RestAssured.given;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class RestricoesTests {
	
	private String baseUrl = "http://localhost:8888";
	
	@Test
	public void GetCPFComRestricao() {
		String _cpf = "60094146012";
		given()
			.pathParam("cpf", _cpf)
			.log().all()
		.when()
			.get(baseUrl+"/api/v1/restricoes/{cpf}",_cpf)
		.then()
			.statusCode(200)
			.body("mensagem", Matchers.is("O CPF "+ _cpf +" tem problema"))
			.log().all()
		;
	}
	
	@Test
	public void GetCPFSemRestricao() {
		String _cpf = "98765432100";
		given()
			.pathParam("cpf", _cpf)
			.log().all()
		.when()
			.get(baseUrl+"/api/v1/restricoes/{cpf}",_cpf)
		.then()
			.statusCode(204)
			.log().all()
		;
	}

}
