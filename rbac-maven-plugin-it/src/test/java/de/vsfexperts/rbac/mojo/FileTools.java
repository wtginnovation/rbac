package de.vsfexperts.rbac.mojo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.FileSystemResource;

public final class FileTools {

	public static String readFile(final String file) throws IOException {
		return readFile(new File(file));
	}

	public static String readFile(final File file) throws IOException {
		final FileSystemResource resource = new FileSystemResource(file);
		final Path path = Paths.get(resource.getURI());
		final byte[] content = Files.readAllBytes(path);

		return new String(content, Charset.forName("utf-8"));
	}

}
