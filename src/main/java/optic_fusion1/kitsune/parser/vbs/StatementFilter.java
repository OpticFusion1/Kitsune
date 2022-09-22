package optic_fusion1.kitsune.parser.vbs;

@SuppressWarnings("rawtypes")
public class StatementFilter {

    private Class filterClass;

    public StatementFilter(Class clz) {
        this.filterClass = clz;
    }

    public Class getFilterClass() {
        return filterClass;
    }

}
