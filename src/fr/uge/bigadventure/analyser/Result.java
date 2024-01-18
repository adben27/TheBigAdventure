package fr.uge.bigadventure.analyser;

import java.util.Objects;

public record Result(Token token, String content, int lineNo) {
  
	public Result {
    Objects.requireNonNull(token);
    Objects.requireNonNull(content);
  }
}
