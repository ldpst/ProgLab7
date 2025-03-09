package server.commands;

public interface Commands {
    String SHOW = "show";
    String ADD = "add";
    String UPDATE = "update";
    String REMOVEBYID = "remove_by_id";
    String CLEAR = "clear";
    String HEAD = "head";
    String ADDIFMAX = "add_if_max";
    String REMOVEGREATER = "remove_greater";
    String MAXBYOPERATOR = "max_by_operator";
    String COUNTBYOPERATOR = "count_by_operator";
    String COUNTLESSTHANGENRE = "count_less_than_genre";
}
