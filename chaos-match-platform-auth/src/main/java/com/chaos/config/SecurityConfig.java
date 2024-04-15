package com.chaos.config;

import com.chaos.feign.UserFeignClient;
import com.chaos.filter.JwtAuthenticationTokenFilter;
import com.chaos.handler.provider.EmailPasswordAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    private final AccessDeniedHandler accessDeniedHandler;

    private final AuthenticationEntryPoint authenticationEntryPoint;


    @Bean
    public EmailPasswordAuthenticationProvider emailPasswordAuthenticationProvider(){
        return new EmailPasswordAuthenticationProvider();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .antMatchers("/auth/wxLogin").anonymous()
                .antMatchers("/auth/getKey").anonymous()
                .antMatchers("/auth/passwordLogin").anonymous()
                .antMatchers("/auth/manage/passwordLogin").anonymous()
                .antMatchers("/auth/manage/getKey").anonymous()
                .antMatchers("/auth/refresh").permitAll()
                .antMatchers("/auth/manage/refresh").permitAll()
                .antMatchers("/feign/*").anonymous()
                // 除上面外的所有请求全部需要认证即可访问
                .anyRequest().authenticated();

        //配置异常处理器
        http
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);


        http.logout().disable();

        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //允许跨域
        http.cors();

        http.authenticationProvider(emailPasswordAuthenticationProvider());

    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}