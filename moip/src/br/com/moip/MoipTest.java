package br.com.moip;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class MoipTest {
	private static final Pattern patternURL = Pattern.compile("request_to=\"[a-z].*\" response_headers");
	private static final Pattern patternStatusHttp = Pattern.compile("response_status=\"[0-9].*\"");

	public static void main(String[] args) throws IOException {
		HashMap<String, Integer> mapUrls = new HashMap<String, Integer>();
		HashMap<String, Integer> mapStatusHttp = new HashMap<String, Integer>();

		Path path = Paths.get(System.getProperty("user.home"), "\\Desktop\\log.txt");

		Stream<String> lines = Files.lines(path, StandardCharsets.ISO_8859_1);

		lines.forEach(linha -> {
			findByTemplate(linha, patternURL, mapUrls, "request_to=\"", "\" response_headers");
			findByTemplate(linha, patternStatusHttp, mapStatusHttp, "response_status=\"", "\"");
		});

		mapUrls.forEach((url, qtd) -> {
			System.out.println(url + " - " + qtd);
		});

		mapStatusHttp.forEach((statushttp, qtd) -> {
			System.out.println(statushttp + " - " + qtd);
		});

	}

	private static void findByTemplate(String line, Pattern pattern, HashMap<String, Integer> hashMap, String replace1,
			String replace2) {
		Matcher matcher = pattern.matcher(line);
		if (matcher.find()) {
			String result = matcher.group(0).replace(replace1, "").replace(replace2, "");
			if (hashMap.containsKey(result)) {
				hashMap.put(result, hashMap.get(result) + 1);
			} else {
				hashMap.put(result, 1);
			}
		}
	}

}