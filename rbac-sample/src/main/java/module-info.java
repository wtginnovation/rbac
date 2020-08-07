open module de.vsfexperts.rbac.sample {

	requires transitive de.vsfexperts.rbac.configuration;
	requires transitive de.vsfexperts.rbac.spring;

	requires org.apache.tomcat.embed.core;
	requires org.apache.tomcat.embed.websocket;

	requires com.fasterxml.jackson.annotation;
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.datatype.jdk8;
	requires com.fasterxml.jackson.datatype.jsr310;
	requires com.fasterxml.jackson.module.paramnames;
	requires java.annotation;
	requires java.instrument;
	requires java.sql;
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
