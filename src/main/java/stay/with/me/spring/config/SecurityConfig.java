package stay.with.me.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
//import stay.with.me.spring.jwt.JwtTokenProvider;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  {

//	 private final JwtTokenProvider jwtTokenProvider;


	@Bean
	public BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
				.csrf(AbstractHttpConfigurer::disable)  // token을 사용하는 방식이기 때문에 csrf를 disabl
				.httpBasic(AbstractHttpConfigurer::disable)
				//.formLogin(AbstractHttpConfigurer::disable) // 비동기 요청을 받기 위해, 기본 방식인 동기 요청 작업 비활성화
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))// CORS 에러 방지용
				.headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
				.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))// 세션을 사용하지 않기 때문(jwt사용)에 STATELESS로 설정
				//페이지 접근제한설정
				.authorizeHttpRequests(
						registry -> registry.requestMatchers("/**")
								.permitAll()
								.anyRequest()
								.authenticated()
				)



//           //JWT 토큰 예외처리부
//          .addFilterBefore(jwtAuthenticationFilter(jwtTokenProvider),UsernamePasswordAuthenticationFilter.class)

		//예외 처리
//          .exceptionHandling()
//          .authenticationEntryPoint(jwtAuthenticationEntryPoint) //401 에러 핸들링을 위한 설정
//          .accessDeniedHandler(jwtAccessDeniedHandler) // 403 에러 핸들링을 위한 설정

		;

		return http.build();
	}




//		private Filter jwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider2) {
//		// TODO Auto-generated method stub
//		return null;
//	}




	// CORS 허용 적용
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

//	        configuration.addAllowedOrigin("*");
//	        configuration.addAllowedHeader("*");
//	        configuration.addAllowedMethod("*");
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://15.165.166.251", "https://staywithme.kr"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}


}
