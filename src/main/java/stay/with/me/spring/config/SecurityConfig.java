package stay.with.me.spring.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.function.client.WebClient;
import stay.with.me.spring.jwt.JwtAccessDeniedHandler;
import stay.with.me.spring.jwt.JwtAuthenticationEntryPoint;
import stay.with.me.spring.jwt.JwtAuthenticationFilter;
import stay.with.me.spring.jwt.JwtTokenProvider;
import stay.with.me.spring.oauth.CustomOAuth2UserService;
import stay.with.me.spring.oauth.OAuth2LoginFailureHandler;
import stay.with.me.spring.oauth.OAuth2LoginSuccessHandler;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtTokenProvider jwtTokenProvider;
	private final CustomOAuth2UserService oAuth2UserService;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

	@Bean
	public BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public WebClient webClient() {
		return WebClient.builder().build();
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(jwtTokenProvider);
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
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

				/*oauth2 설정*/
				.oauth2Login(oauth2 -> oauth2
								.authorizationEndpoint(authorization -> authorization
										.authorizationRequestRepository(authorizationRequestRepository()) // ✅ 세션 유지
								)
						.authorizationEndpoint(auth -> auth.baseUri("/oauth2/authorization"))// 로그인 요청 기본 경로
						.redirectionEndpoint(redirection -> redirection.baseUri("/login/oauth2/code/*"))// 리다이렉션 엔드포인트 설정
						.userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService)) // OAuth2UserService 설정
						.successHandler(oAuth2LoginSuccessHandler) //OAuth2 로그인 성공 시 JWT 발급
						.failureHandler(oAuth2LoginFailureHandler) // OAuth2 로그인 실패 핸들러
				)
				//cors 관련 추가
				.addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class)
				//JWT 토큰 예외처리
				.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(exception -> exception
						.authenticationEntryPoint(jwtAuthenticationEntryPoint) //401 에러 핸들링을 위한 설정
						.accessDeniedHandler(jwtAccessDeniedHandler) // 403 에러 핸들링을 위한 설정

				);

		return http.build();
	}
		// CORS 허용 적용
	    @Bean
	    public CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration configuration = new CorsConfiguration();

			configuration.setAllowedOrigins(Arrays.asList(
				"http://localhost:3000",
				"https://15.165.166.251",
				"https://www.staywithme.kr",
				"https://staywithme.kr",
				"wss://staywithme.kr",
				"ws://localhost"
	        ));
	        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
	        configuration.setAllowCredentials(true);

	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", configuration);
	        return source;
	    }
	@Bean
	public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
		return new HttpSessionOAuth2AuthorizationRequestRepository();
	}

//파비콘 추가
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/favicon.ico")
				.addResourceLocations("classpath:/static/");
	}

	//cors 관련 추가
	@Bean
	public CorsFilter corsFilter() {
		return new CorsFilter(corsConfigurationSource());
	}
	   
	}
