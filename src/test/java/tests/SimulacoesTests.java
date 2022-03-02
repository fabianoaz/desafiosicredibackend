package tests;

import static io.restassured.RestAssured.given;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import io.restassured.response.ResponseBody;

public class SimulacoesTests {
	
	private String baseUrl = "http://localhost:8888";
	
	@Test
	public void RetornaTodasSimulacoes() {
		given()
			.log().all()
		.when()
			.get(baseUrl+"/api/v1/simulacoes")
		.then()
			.statusCode(200)
			.log().all();
	}
	
	@Test
	public void InsereNovaSimulacao() {
		
		given()
			.body("{ \"nome\": \"Fabiano Azevedo\","
					+ "  \"cpf\": 98765432100,\r\n"
					+ "  \"email\": \"fabianoazevedo@email.com\","
					+ "  \"valor\": 3000,"
					+ "  \"parcelas\": 6,"
					+ "  \"seguro\": true"
					+ "}")
			.contentType("application/json")
			.log().all()
		.when()
			.post(baseUrl+"/api/v1/simulacoes")
		.then()
			.statusCode(201)
			.body("cpf", Matchers.is("98765432100"))
			.log().everything();
	}
	
	@Test
	public void PostNovaSimulacaoFalha() {
		
		given()
			.body("{\r\n"
					+ "  \"nome\": \"Novo faltando item no payload\",\r\n"
					+ "  \"cpf\": 01006719008,\r\n"
					+ "  \"email\": \"fabianoazevedo@email.com\",\r\n"
					+ "  \"valor\": 3000\r\n"
					+ "}")
			.contentType("application/json")
			.log().all()
		.when()
			.post(baseUrl+"/api/v1/simulacoes")
		.then()
			.statusCode(400)
			.log().all();
	}
	
	@Test
	public void PostNovaSimulacaoCPFExistente() {

		given()
		.body("{ \"nome\": \"Mesmos dados do Deltrano\","
				+ "  \"cpf\": 17822386034,\r\n"
				+ "  \"email\": \"deltrano@gmail.com\","
				+ "  \"valor\": 3000,"
				+ "  \"parcelas\": 6,"
				+ "  \"seguro\": true"
				+ "}")
			.contentType("application/json")
			.log().all()
		.when()
			.post(baseUrl+"/api/v1/simulacoes")
		.then()
			.statusCode(400)
			.body("mensagem", Matchers.is("CPF duplicado"))
			.log().all();
	}
	
	@Test
	public void GetSimulacoesCPFNaoExiste() {
		String _cpf = "60094146012";
		given()
			.pathParam("cpf", _cpf)
			.log().all()
		.when()
			.get(baseUrl+"/api/v1/simulacoes/{cpf}",_cpf)
		.then()
			.statusCode(404)
			.body("mensagem", Matchers.is("CPF " + _cpf + " não encontrado"))
			.log().all();
	}
	
	@Test
	public void RetornaSimulacaoPorCPF() {
		String _cpf = "17822386034";
		given()
			.pathParam("cpf", _cpf)
			.log().all()
		.when()
			.get(baseUrl+"/api/v1/simulacoes/{cpf}",_cpf)
		.then()
			.statusCode(200)
			.body("cpf", Matchers.is(_cpf))
			.log().all();
	}
	
	@Test
	public void AtualizaSimulacaoPorCPF() {
		String _cpf = "17822386034";
		given()
			.pathParam("cpf", _cpf)
			.body("{\r\n"
					+ "  \"nome\": \"Deltrano EDITADO\",\r\n"
					+ "  \"cpf\": 17822386034,\r\n"
					+ "  \"email\": \"deltrano@gmail.com\",\r\n"
					+ "  \"valor\": 3000,\r\n"
					+ "  \"parcelas\": 6,\r\n"
					+ "  \"seguro\": true\r\n"
					+ "}")
			.contentType("application/json")
			.log().all()
		.when()
			.put(baseUrl+"/api/v1/simulacoes/{cpf}",_cpf)
		.then()
			.statusCode(200)
			.body("nome", Matchers.is("Deltrano EDITADO"))
			.body("cpf", Matchers.is(_cpf))
			.log().all();
	}
	
	@Test
	public void RemoveSimulacaoPorCPF() {
		String _cpf = "66414919004";
		ResponseBody _id;
		String _idRemove;
		_id = given()
			.pathParam("cpf", _cpf)
			.log().all()
		.when()
			.get(baseUrl+"/api/v1/simulacoes/{cpf}",_cpf)
			.body();
		
		_idRemove = _id.jsonPath().getString("id");
		
		given()
		.pathParam("id", _idRemove)
		.log().all()
	.when()
		.delete(baseUrl+"/api/v1/simulacoes/{id}",_idRemove)
	.then()
		.statusCode(200)
		.body(Matchers.is("OK"))
		.log().all();
	}

}
