package com.utils.java.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LineReader2 {

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("/Users/ktejkum/sample-file"));
		
		for(String line: lines) {
			if(line.startsWith("{\"id\":")) {
				if(!line.contains("\"type\":\"ACTION\"")) {
				System.out.println(line);
				}
			}
		}
	}

}
