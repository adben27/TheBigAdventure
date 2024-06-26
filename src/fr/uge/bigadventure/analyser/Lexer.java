package fr.uge.bigadventure.analyser;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Lexer {
  private static final List<Token> TOKENS = List.of(Token.values());
  private static final Pattern PATTERN = Pattern.compile(
      TOKENS.stream()
      .map(token -> "(" + token.regex() + ")")
      .collect(Collectors.joining("|")));

  private final String text;
  private final Matcher matcher;
  private int lineNo;

  public Lexer(String text) {
    this.text = Objects.requireNonNull(text);
    this.matcher = PATTERN.matcher(text);
    this.lineNo = 1;
  }

  public Result nextResult() {
    var matches = matcher.find();
    if (!matches) {
      return null;
    }
    for (var group = 1; group <= matcher.groupCount(); group++) {
      var start = matcher.start(group);
      if (start != -1) {
        var end = matcher.end(group);
        var content = text.substring(start, end);
        if(TOKENS.get(group - 1) == Token.QUOTE) {
        	lineNo += content.split("\n", -1).length - 1;
        }
        if(TOKENS.get(group - 1) == Token.NEWLINE) {
        	lineNo++;
        	return nextResult();
        }
        return new Result(TOKENS.get(group - 1), content, lineNo);
      }
    }
    throw new AssertionError();
  }

  
}
