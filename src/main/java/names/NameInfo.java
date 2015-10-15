package names;

public class NameInfo {

    private final String _state;
    private final int _year;
    private final int _frequency;

    public NameInfo(String state, int year, int frequency) {
        _state = state;
        _year = year;
        _frequency = frequency;
    }

    public String getState(){ return _state; }

    public int getYear(){ return _year; }

    public int getFrequency(){ return _frequency; }
}