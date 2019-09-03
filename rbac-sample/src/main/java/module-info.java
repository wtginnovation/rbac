open module de.vsfexperts.rbac.sample {

	requires transitive de.vsfexperts.rbac.configuration;
	requires transitive de.vsfexperts.rbac.spring;

	requires jackson.annotations;
	requires tomcat.embed.core;
	requires tomcat.embed.el;
	requires tomcat.embed.websocket;

	requires com.fasterxml.classmate;
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.datatype.jdk8;
	requires com.fasterxml.jackson.datatype.jsr310;
	requires com.fasterxml.jackson.module.paramnames;
	requires java.annotation;
	requires java.instrument;
	requires java.sql;
	requires java.validation;
	requires org.hibernate.validator;
	requires org.jboss.logging;
	requires spring.boot.starter.json;
	requires spring.boot.starter.tomcat;
	requires spring.boot.starter.web;
	requires spring.core;
	requires spring.webmvc;
	
	requires spring.boot.starter.security;
	requires spring.security.config;
	requires spring.security.core;
	requires spring.security.web;
	requires spring.web;
	
}
