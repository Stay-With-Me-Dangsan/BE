package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {
	
	
//	@Bean
//    public BCryptPasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
	
	   @Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    	http
	    			.csrf(AbstractHttpConfigurer::disable) // token을 사용하는 방식이기 때문에 csrf를 disabl
	        		//.formLogin(AbstractHttpConfigurer::disable) // 비동기 요청을 받기 위해, 기본 방식인 동기 요청 작업 비활성화
	                .httpBasic(AbstractHttpConfigurer::disable)
	                .cors(cors -> cors.configurationSource(corsConfigurationSource()))// CORS 에러 방지용
	                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) 
	                 // 세션을 사용하지 않기 때문(jwt사용)에 STATELESS로 설정
	                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	                
	                 //페이지 접근제한설정
//	                .authorizeHttpRequests(
//	                        registry -> registry.requestMatchers("/**/*")
//	                                .permitAll()
//	                                .anyRequest()
//	                                .authenticated()
//	                )
	                // JWT 토큰 예외처리부
	                
	                return http.build();
	    }


	    // CORS 허용 적용
	    @Bean
	    public CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration configuration = new CorsConfiguration();

	        configuration.addAllowedOrigin("*");
	        configuration.addAllowedHeader("*");
	        configuration.addAllowedMethod("*");
	        configuration.setAllowCredentials(true);

	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", configuration);
	        return source;
	    }


	}
