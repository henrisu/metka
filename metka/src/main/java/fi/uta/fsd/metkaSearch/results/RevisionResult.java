package fi.uta.fsd.metkaSearch.results;

public class RevisionResult implements SearchResult {
    private final ResultList.ResultType type = ResultList.ResultType.REVISION;
    private final Long id;
    private final Long no;

    public RevisionResult(Long id, Long no) {
        this.id = id;
        this.no = no;
    }

    @Override
    public ResultList.ResultType getType() {
        return type;
    }

    public Long getId() {
        return id;
    }

    public Long getNo() {
        return no;
    }

    @Override
    public String toString() {
        return "Result is "+"ID: "+id+" | NO: "+no;
    }
}
