open module de.vsfexperts.rbac.configuration {

	requires java.base;
	requires transitive org.apache.commons.lang3;
	requires transitive logback.classic;
	requires transitive logback.core;
	requires transitive slf4j.api;
	requires transitive spring.core;
	requires transitive spring.jcl;
	
	requires static transitive spring.aop;
	requires static transitive spring.beans;
	requires static transitive spring.context;
	requires static transitive spring.expression;
	requires static transitive snakeyaml;

	exports de.vsfexperts.rbac.configuration.domain;
	exports de.vsfexperts.rbac.configuration.domain.util;
	exports de.vsfexperts.rbac.configuration.io;
}
