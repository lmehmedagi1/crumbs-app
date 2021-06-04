package com.crumbs.userservice.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {

//    @Value("${spring.mail.username}")
//    private String username;
//
//    @Value("${spring.mail.password}")
//    private String password;
//
//    @Value("${spring.mail.host}")
//    private String host;
//
//    @Value("${spring.mail.port}")
//    private Integer port;
//
//    @Bean
//    public JavaMailSenderImpl mailSender() {
//        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//
//        javaMailSender.setHost(host);
//        javaMailSender.setPort(port);
//        javaMailSender.setUsername(username);
//        javaMailSender.setPassword(password);
//
//        Properties props = javaMailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "true");
//
//        return javaMailSender;
//    }
}
