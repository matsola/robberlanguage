package se.arcella.springboot.functions.robberlanguage;

import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

@SpringBootApplication
public class RobberLanguageApplication {

	public static void main(String[] args) {
		SpringApplication.run(RobberLanguageApplication.class, args);
	}

	@Bean
	public Function<String, String> encode() {
		String vocals = "aeiouyåäö";
		return message -> message.chars()
				.mapToObj(c -> (char) c)
				.flatMap(c -> !Character.isLetter(c) && vocals.indexOf(c) < 0 ? Stream.of(c) : Stream.of(c, 'o', c))
				.collect(Collector.of(StringBuilder::new, StringBuilder::append, StringBuilder::append,
						StringBuilder::toString));
	}

	@Bean
	public Function<Message<Request>, Message<Response>> encode2() {
		return req -> ok(encode().apply(req.getPayload().getMessage()));
	}

	private static Message<Response> ok(String res) {
		return new GenericMessage<>(new Response(res), Collections.singletonMap("statusCode", HttpStatus.OK.value()));
	}
}
