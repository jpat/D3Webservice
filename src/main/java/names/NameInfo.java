package names;

public class NameInfo {

    private final String _name;
    private final String _state;
    private final int _year;
    private final int _frequency;

    public NameInfo(String name, String state, int year, int frequency) {
        _name = name;
        _state = state;
        _year = year;
        _frequency = frequency;
    }

    public String getName(){ return _name; }

    public String getState(){ return _state; }

    public Integer getYear(){ return _year; }

    public Integer getFrequency(){ return _frequency; }
}