package fr.uge.bigadventure.analyser;

public enum Token {
  IDENTIFIER("[A-Za-z]+"),
  NUMBER("[0-9]+"),
  LEFT_PARENS("\\("),
  RIGHT_PARENS("\\)"),
  LEFT_BRACKET("\\["),
  RIGHT_BRACKET("\\]"),
  COMMA(","),
  COLON(":"),
  QUOTE("\"\"\"[^\"]+\"\"\""),
  NEWLINE("\n"),
  ;

  private final String regex;

  Token(String regex) {
    this.regex = regex;
  }
  
  public String regex() {
  	return regex;
  }
}

