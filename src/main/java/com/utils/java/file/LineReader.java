package com.utils.java.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LineReader {

	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("/Users/ktejkum/Downloads/testfile"));
		
		for(String line: lines) {
			if(line.contains("sample-text")) {
				System.out.println(line);
			}
		}
	}

}
